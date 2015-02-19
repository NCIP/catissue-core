
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DpListCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

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
	public ResponseEvent<List<DistributionProtocolDetail>> getDistributionProtocols(RequestEvent<DpListCriteria> req) {
		try {
			return ResponseEvent.response(
					DistributionProtocolDetail.from(daoFactory.getDistributionProtocolDao()
							.getDistributionProtocols(req.getPayload())));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> getDistributionProtocol(RequestEvent<Long> req) {
		try {
			Long protocolId = req.getPayload();
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			DistributionProtocol distributionProtocol = distributionProtocolFactory.createDistributionProtocol(req.getPayload());
			ensureUniqueConstraints(distributionProtocol);
			
			daoFactory.getDistributionProtocolDao().saveOrUpdate(distributionProtocol);
			return ResponseEvent.response(DistributionProtocolDetail.from(distributionProtocol));
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
			Long protocolId = req.getPayload().getId();
			String title = req.getPayload().getTitle();
			
			DistributionProtocol existing = null;
			if (protocolId != null) {
				existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
			} else {
				existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(title);
			}
			
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
		
			DistributionProtocol distributionProtocol = distributionProtocolFactory.createDistributionProtocol(req.getPayload());
			ensureUniqueConstraints(distributionProtocol);
			
			existing.update(distributionProtocol);
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> deleteDistributionProtocol(RequestEvent<Long> req) {
		try {
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}

			existing.delete();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		}
		catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConstraints(DistributionProtocol distributionProtocol) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if (!isUniqueTitle(distributionProtocol)) {
			ose.addError(DistributionProtocolErrorCode.DUP_TITLE);
		}
		
		if (!isUniqueShortTitle(distributionProtocol)) {
			ose.addError(DistributionProtocolErrorCode.DUP_SHORT_TITLE);
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueTitle(DistributionProtocol distributionProtocol) {
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(distributionProtocol.getTitle());
		if (existing == null) {
			return true; // no dp by this name
		} else if (distributionProtocol.getId() == null) {
			return false; // a different dp by this name exists 
		} else if (existing.getId().equals(distributionProtocol.getId())) {
			return true; // same dp
		} else {
			return false;
		}
	}

	private boolean isUniqueShortTitle(DistributionProtocol distributionProtocol) {
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getByShortTitle(distributionProtocol.getShortTitle());
		if (existing == null) {
			return true; // no dp by this short title
		} else if (distributionProtocol.getId() == null) {
			return false; // a different dp by this short-title exists 
		} else if (existing.getId().equals(distributionProtocol.getId())) {
			return true; // same dp
		} else {
			return false;
		}
	}
}
