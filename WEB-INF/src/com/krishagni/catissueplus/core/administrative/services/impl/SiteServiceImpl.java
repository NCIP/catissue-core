
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.SiteListCriteria;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.Operation;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.Resource;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;


public class SiteServiceImpl implements SiteService {
	private SiteFactory siteFactory;

	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSiteFactory(SiteFactory siteFactory) {
		this.siteFactory = siteFactory;
	}
	
	@Override
	@PlusTransactional	
	public ResponseEvent<List<SiteDetail>> getSites(RequestEvent<SiteListCriteria> req) {
		try {
			List<Site> sites = null;
			if (AuthUtil.isAdmin()) {
				sites = daoFactory.getSiteDao().getSites(req.getPayload());
			} else {
				sites = getAccessibleSites(req.getPayload());
			} 
			
			return ResponseEvent.response(SiteDetail.from(sites));
		} catch(OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch(Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional		
	public ResponseEvent<SiteDetail> getSite(RequestEvent<SiteQueryCriteria> req) {
		SiteQueryCriteria crit = req.getPayload();
		Site site = null;
		
		if (AuthUtil.isAdmin()) {
			site = getFromDb(crit);
		} else {
			site = getFromAccessibleSite(crit);
		}
		
		if (site == null) {
			return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
		}
		
		return ResponseEvent.response(SiteDetail.from(site));
	}

	@Override
	@PlusTransactional	
	public ResponseEvent<SiteDetail> createSite(RequestEvent<SiteDetail> req) {
		try {	
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			Site site = siteFactory.createSite(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(site, null, ose);
			ose.checkAndThrow();
			
			daoFactory.getSiteDao().saveOrUpdate(site, true);			
			return ResponseEvent.response(SiteDetail.from(site));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional		
	public ResponseEvent<SiteDetail> updateSite(RequestEvent<SiteDetail> req) {
		return updateSite(req, false);
	}

	@Override
	@PlusTransactional		
	public ResponseEvent<SiteDetail> patchSite(RequestEvent<SiteDetail> req) {
		return updateSite(req, true);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			Site existing = daoFactory.getSiteDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional	
	public ResponseEvent<SiteDetail> deleteSite(RequestEvent<DeleteEntityOp> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DeleteEntityOp deleteOp = req.getPayload();
			Site existing = daoFactory.getSiteDao().getById(deleteOp.getId());
			if (existing == null) {
				return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
			}
			
			existing.delete(deleteOp.isClose());
			return ResponseEvent.response(SiteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private ResponseEvent<SiteDetail> updateSite(RequestEvent<SiteDetail> req, boolean partial) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();			
			SiteDetail detail = req.getPayload();
			
			Site existing = null;			
			if (detail.getId() != null) {
				existing = daoFactory.getSiteDao().getById(detail.getId()); 
			} else if (StringUtils.isNotBlank(detail.getName())) {
				existing = daoFactory.getSiteDao().getSiteByName(detail.getName());
			}
			
			if (existing == null) {
				return ResponseEvent.userError(SiteErrorCode.NOT_FOUND);
			}

			Site site = null;
			if (partial) {
				site = siteFactory.createSite(existing, detail);
			} else {
				site = siteFactory.createSite(detail); 
			}
						
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraint(site, existing, ose);
			ose.checkAndThrow();
			
			existing.update(site);			
			daoFactory.getSiteDao().saveOrUpdate(existing);			
			return ResponseEvent.response(SiteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	private void ensureUniqueConstraint(Site newSite, Site existingSite, OpenSpecimenException ose) {
		if (!isUniqueName(newSite, existingSite)) {
			ose.addError(SiteErrorCode.DUP_NAME);
		}
		
		if(!isUniqueCode(newSite, existingSite)) {
			ose.addError(SiteErrorCode.DUP_CODE);
		}
	}

	private boolean isUniqueName(Site newSite, Site existingSite) {
		if (existingSite != null && newSite.getName().equals(existingSite.getName())) {
			return true;
		}
		
		Site site = daoFactory.getSiteDao().getSiteByName(newSite.getName());
		if (site != null) {
			return false; 
		} 
		
		return true;
	}
	
	private boolean isUniqueCode(Site newSite, Site existingSite) {
		if (StringUtils.isBlank(newSite.getCode()) || 
			existingSite != null && newSite.getCode().equals(existingSite.getCode())) {
			return true;
		}
		
		Site site = daoFactory.getSiteDao().getSiteByCode(newSite.getCode());
		if (site != null) {
			return false; 
		}
		
		return true;
	}
	
	private List<Site> getAccessibleSites(SiteListCriteria criteria) {
		List<Site> results = new ArrayList<Site>();
		
		Set<Site> accessibleSites = null;
		if (StringUtils.isNotBlank(criteria.resource()) && StringUtils.isNotBlank(criteria.operation())) {
			Resource resource = Resource.fromName(criteria.resource());
			if (resource == null) {
				throw OpenSpecimenException.userError(RbacErrorCode.RESOURCE_NOT_FOUND);
			}
			
			Operation operation = Operation.fromName(criteria.operation());
			if (operation == null) {
				throw OpenSpecimenException.userError(RbacErrorCode.OPERATION_NOT_FOUND);
			}
			
			accessibleSites = AccessCtrlMgr.getInstance().getSites(resource, operation);
		} else {
			accessibleSites = AccessCtrlMgr.getInstance().getRoleAssignedSites();
		}
		
		String searchTerm = criteria.query();
		if (StringUtils.isNotBlank(searchTerm)) {
			for (Site site : accessibleSites) {
				if (StringUtils.containsIgnoreCase(site.getName(), searchTerm)) {
					results.add(site);
				}
			}
		} else {
			results.addAll(accessibleSites);
		}
		
		Collections.sort(results, new Comparator<Site>() {
			@Override
			public int compare(Site site1, Site site2) {
				return site1.getName().compareTo(site2.getName());
			}			
		});
		
		return results;
	}
	
	private Site getFromAccessibleSite(SiteQueryCriteria crit) {
		Set<Site> accessibleSites = AccessCtrlMgr.getInstance().getRoleAssignedSites();
		
		Long siteId = crit.getId();
		String siteName = crit.getName();
		Site result = null;		
		for (Site site : accessibleSites) {
			if (siteId != null && siteId.equals(site.getId())) {
				result = site;
			} else if (StringUtils.isNotBlank(siteName) && siteName.equals(site.getName())) {
				result = site;
			}
			
			if (result != null) {
				break;
			}
		}
		
		return result;
	}
	
	private Site getFromDb(SiteQueryCriteria crit) {
		Site result = null;
		
		if (crit.getId() != null) {
			result = daoFactory.getSiteDao().getById(crit.getId());
		} else if (crit.getName() != null) {
			result = daoFactory.getSiteDao().getSiteByName(crit.getName());
		}		
		
		return result;
	}
}
