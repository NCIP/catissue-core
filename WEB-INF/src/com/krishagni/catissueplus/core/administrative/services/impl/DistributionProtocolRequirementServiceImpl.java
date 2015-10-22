package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocolRequirement;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolRequirementErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolRequirementFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolRequirementDao;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolRequirementService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolRequirementServiceImpl implements DistributionProtocolRequirementService {

	private DaoFactory daoFactory;
	
	private DistributionProtocolRequirementFactory dprFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setDprFactory(DistributionProtocolRequirementFactory dprFactory) {
		this.dprFactory = dprFactory;
	}
	
	private DistributionProtocolRequirementDao getDprDao() {
		return daoFactory.getDistributionProtocolRequirementDao();
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionProtocolRequirementDetail>> getRequirements(
			RequestEvent<DistributionProtocolRequirementListCriteria> req) {
		
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			List<DistributionProtocolRequirementDetail> result = getDprDao().getRequirements(req.getPayload());
			
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolRequirementDetail> getRequirement(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			DistributionProtocolRequirement existing = getDprDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolRequirementErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(DistributionProtocolRequirementDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolRequirementDetail> createRequirement(
			RequestEvent<DistributionProtocolRequirementDetail> req) {
		
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			DistributionProtocolRequirement dpr = dprFactory.createDistributionProtocolRequirement(req.getPayload());
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(null, dpr, ose);
			ose.checkAndThrow();
			
			getDprDao().saveOrUpdate(dpr);
			return ResponseEvent.response(DistributionProtocolRequirementDetail.from(dpr));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolRequirementDetail> updateRequirement(
			RequestEvent<DistributionProtocolRequirementDetail> req) {
		
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			Long dpReqId = req.getPayload().getId();
			DistributionProtocolRequirement existing = getDprDao().getById(dpReqId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolRequirementErrorCode.NOT_FOUND);
			}
			
			DistributionProtocolRequirement newDpr = dprFactory.createDistributionProtocolRequirement(req.getPayload());
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(existing, newDpr, ose);
			ose.checkAndThrow();
			
			existing.update(newDpr);
			getDprDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolRequirementDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolRequirementDetail> deleteRequirement(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			DistributionProtocolRequirement existing = getDprDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolRequirementErrorCode.NOT_FOUND);
			}
			
			existing.delete();
			getDprDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolRequirementDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConstraint(DistributionProtocolRequirement oldDpr,
			DistributionProtocolRequirement newDpr, OpenSpecimenException ose) {
		
		if (oldDpr == null || !oldDpr.equalsSpecimenGroup(newDpr)) {
			DistributionProtocolRequirementListCriteria crit = new DistributionProtocolRequirementListCriteria()
					.dpId(newDpr.getDp().getId())
					.specimenType(newDpr.getSpecimenType())
					.anatomicSite(newDpr.getAnatomicSite())
					.pathologyStatus(newDpr.getPathologyStatus())
					.includeDistQty(false);
	
			List<DistributionProtocolRequirementDetail> existing = getDprDao().getRequirements(crit);
			if (!CollectionUtils.isEmpty(existing)) {
				ose.addError(DistributionProtocolRequirementErrorCode.SPECIMEN_ALREADY_EXISTS);
			}
		}
	}
	
}
