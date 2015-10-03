
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStat;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStatListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.rbac.common.errors.RbacErrorCode;

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
			DpListCriteria crit = req.getPayload();
			Set<Long> siteIds = AccessCtrlMgr.getInstance().getCreateUpdateAccessDistributionOrderSites();
			if (siteIds != null && CollectionUtils.isEmpty(siteIds)) {
				return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
			}
			
			if (siteIds != null) {
				crit.siteIds(siteIds);
			}
			
			List<DistributionProtocol> dps = daoFactory.getDistributionProtocolDao().getDistributionProtocols(crit);
			List<DistributionProtocolDetail> result = DistributionProtocolDetail.from(dps);
			
			if (crit.includeStat()) {
				addDpStats(result);
			}
						
			return ResponseEvent.response(result);
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
			
			AccessCtrlMgr.getInstance().ensureReadDPRights(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DistributionProtocol dp = distributionProtocolFactory.createDistributionProtocol(req.getPayload());
			ensureUniqueConstraints(dp, null);
			
			daoFactory.getDistributionProtocolDao().saveOrUpdate(dp);
			return ResponseEvent.response(DistributionProtocolDetail.from(dp));
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
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
						
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
			ensureUniqueConstraints(distributionProtocol, existing);
			
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
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> deleteDistributionProtocol(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}

			existing.delete();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> updateActivityStatus(RequestEvent<DistributionProtocolDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			Long dpId = req.getPayload().getId();
			String status = req.getPayload().getActivityStatus();
			if (StringUtils.isBlank(status) || !Status.isValidActivityStatus(status)) {
				return ResponseEvent.userError(ActivityStatusErrorCode.INVALID);
			}
			
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(dpId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			if (existing.getActivityStatus().equals(status)) {
				return ResponseEvent.response(DistributionProtocolDetail.from(existing));
			}
			
			if (status.equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
				existing.delete();
			} else {
				existing.setActivityStatus(status);
			}

			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
		
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionOrderStat>> getOrderStats(
			RequestEvent<DistributionOrderStatListCriteria> req) {
		try {
			DistributionOrderStatListCriteria crit = req.getPayload();
			
			if (crit.dpId() != null) {
				DistributionProtocol dp = daoFactory.getDistributionProtocolDao().getById(crit.dpId());
				if (dp == null) {
					return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
				}
				
				AccessCtrlMgr.getInstance().ensureReadDPRights(dp);
			} else {
				Set<Long> siteIds = AccessCtrlMgr.getInstance().getCreateUpdateAccessDistributionOrderSites();
				if (siteIds != null && CollectionUtils.isEmpty(siteIds)) {
					return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
				}
				
				if (siteIds != null) {
					crit.siteIds(siteIds);
				}
			}
			
			List<DistributionOrderStat> details = daoFactory.getDistributionProtocolDao().getOrderStats(crit);
			return ResponseEvent.response(details);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void addDpStats(List<DistributionProtocolDetail> dps) {
		if (CollectionUtils.isEmpty(dps)) {
			return;
		}
		
		Map<Long, DistributionProtocolDetail> dpMap = new HashMap<Long, DistributionProtocolDetail>();
		for (DistributionProtocolDetail dp : dps) {
			dpMap.put(dp.getId(), dp);
		}
				
		Map<Long, Integer> countMap = daoFactory.getDistributionProtocolDao()
				.getSpecimensCountByDpIds(dpMap.keySet());
		
		for (Map.Entry<Long, Integer> count : countMap.entrySet()) {
			dpMap.get(count.getKey()).setDistributedSpecimensCount(count.getValue());
		}		
	}
	
	private void ensureUniqueConstraints(DistributionProtocol newDp, DistributionProtocol existingDp) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if (!isUniqueTitle(newDp, existingDp)) {
			ose.addError(DistributionProtocolErrorCode.DUP_TITLE, newDp.getTitle());
		}
		
		if (!isUniqueShortTitle(newDp, existingDp)) {
			ose.addError(DistributionProtocolErrorCode.DUP_SHORT_TITLE, newDp.getShortTitle());
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueTitle(DistributionProtocol newDp, DistributionProtocol existingDp) {
		if (existingDp != null && newDp.getTitle().equals(existingDp.getTitle())) {
			return true;
		}
		
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(newDp.getTitle());
		if (existing != null) {
			return false;
		}
		
		return true;
	}

	private boolean isUniqueShortTitle(DistributionProtocol newDp, DistributionProtocol existingDp) {
		if (existingDp != null && newDp.getShortTitle().equals(existingDp.getShortTitle())) {
			return true;
		}
		
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getByShortTitle(newDp.getShortTitle());
		if (existing != null) {
			return false;
		}
		
		return true;
	}
	
}