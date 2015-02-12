
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.util.CommonUtil;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ListDpCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionProtocolServiceImpl implements DistributionProtocolService {
	private DaoFactory daoFactory;

	private DistributionProtocolFactory distributionProtocolFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDistributionProtocolFactory(DistributionProtocolFactory distributionProtocolFactory) {
		this.distributionProtocolFactory = distributionProtocolFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionProtocolDetail>> getDistributionProtocols(RequestEvent<ListDpCriteria> req) {
		List<DistributionProtocol> distributionProtocols = daoFactory.getDistributionProtocolDao()
				.getAllDistributionProtocol(req.getPayload().maxResults());
		
		List<DistributionProtocolDetail> result = new ArrayList<DistributionProtocolDetail>();

		for (DistributionProtocol distributionProtocol : distributionProtocols) {
			result.add(DistributionProtocolDetail.fromDomain(distributionProtocol));
		}

		return ResponseEvent.response(result);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> getDistributionProtocol(RequestEvent<DistributionProtocolQueryCriteria> req) {
		DistributionProtocolQueryCriteria crit = req.getPayload();
		
		DistributionProtocol dp = null;
		if (crit.getId() != null) {
			dp = daoFactory.getDistributionProtocolDao().getDistributionProtocol(crit.getId());
		} else if (crit.getTitle() != null) {
			dp = daoFactory.getDistributionProtocolDao().getDistributionProtocol(crit.getTitle());
		}
		
		if (dp == null) {
			return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(DistributionProtocolDetail.fromDomain(dp));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			DistributionProtocol distributionProtocol = distributionProtocolFactory.create(req.getPayload());

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueTitle(distributionProtocol.getTitle(), ose);
			ensureUniqueShortTitle(distributionProtocol.getShortTitle(), ose);
			ose.checkAndThrow();
			
			daoFactory.getDistributionProtocolDao().saveOrUpdate(distributionProtocol);
			return ResponseEvent.response(DistributionProtocolDetail.fromDomain(distributionProtocol));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> updateDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			DistributionProtocolDetail detail = req.getPayload();
			
			DistributionProtocol oldDp = null;
			if (detail.getId() != null) {
				oldDp = daoFactory.getDistributionProtocolDao().getDistributionProtocol(detail.getId());
			} else {
				oldDp = daoFactory.getDistributionProtocolDao().getDistributionProtocol(detail.getTitle());
			}
			
			if (oldDp == null) {
				throw OpenSpecimenException.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}

			
			DistributionProtocol distributionProtocol = distributionProtocolFactory.create(detail);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			checkShortTitle(oldDp.getShortTitle(), distributionProtocol.getShortTitle(), ose);
			checkTitle(oldDp.getTitle(), distributionProtocol.getTitle(), ose);
			ose.checkAndThrow();
			
			oldDp.update(distributionProtocol);
			daoFactory.getDistributionProtocolDao().saveOrUpdate(oldDp);

			return ResponseEvent.response(DistributionProtocolDetail.fromDomain(oldDp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> deleteDistributionProtocol(RequestEvent<DistributionProtocolQueryCriteria> req) {
		try {
			DistributionProtocolQueryCriteria crit = req.getPayload();
			DistributionProtocol oldDp = null;

			if (crit.getId() != null) {
				oldDp = daoFactory.getDistributionProtocolDao().getDistributionProtocol(crit.getId());
			} else {
				oldDp = daoFactory.getDistributionProtocolDao().getDistributionProtocol(crit.getTitle());
			}
			
			if (oldDp == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}

			oldDp.delete();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(oldDp);
			
			return ResponseEvent.response(DistributionProtocolDetail.fromDomain(oldDp));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void checkTitle(String oldTitle, String newTitle, OpenSpecimenException ose) {
		if (!oldTitle.equals(newTitle)) {
			ensureUniqueTitle(newTitle, ose);
		}
	}

	private void checkShortTitle(String oldShortTitle, String newShortTitle, OpenSpecimenException ose) {
		if (!oldShortTitle.equals(newShortTitle)) {
			ensureUniqueShortTitle(newShortTitle, ose);
		}
	}

	private void ensureUniqueShortTitle(String shortTitle, OpenSpecimenException ose) {
		if (!daoFactory.getDistributionProtocolDao().isUniqueShortTitle(shortTitle)) {
			ose.addError(DistributionProtocolErrorCode.DUP_SHORT_TITLE);
		}

	}

	private void ensureUniqueTitle(String title, OpenSpecimenException ose) {
		if (!daoFactory.getDistributionProtocolDao().isUniqueTitle(title)) {
			ose.addError(DistributionProtocolErrorCode.DUP_TITLE);
		}
	}
}
