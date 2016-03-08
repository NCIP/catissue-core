
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteFactory;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.SiteSummary;
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
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.service.RbacService;


public class SiteServiceImpl implements SiteService, ObjectStateParamsResolver {
	private SiteFactory siteFactory;

	private DaoFactory daoFactory;
	
	private RbacService rbacSvc;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSiteFactory(SiteFactory siteFactory) {
		this.siteFactory = siteFactory;
	}
	
	public void setRbacSvc(RbacService rbacSvc) {
		this.rbacSvc = rbacSvc;
	}
	
	@Override
	@PlusTransactional	
	public ResponseEvent<List<SiteSummary>> getSites(RequestEvent<SiteListCriteria> req) {
		try {
			SiteListCriteria listCrit = req.getPayload();			
			List<Site> sites = null;
			
			if (AuthUtil.isAdmin()) {
				sites = daoFactory.getSiteDao().getSites(listCrit);
			} else if (listCrit.listAll() && AccessCtrlMgr.getInstance().canCreateUpdateDistributionOrder()) {
				sites = daoFactory.getSiteDao().getSites(listCrit);
			} else {
				sites = getAccessibleSites(listCrit);
			}
			
			List<SiteSummary> result = SiteSummary.from(sites);
			if (listCrit.includeStat()) {
				addSiteStats(result);
			}
			
			return ResponseEvent.response(result);
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
			site.addOrUpdateExtension();
			addDefaultCoordinatorRoles(site, site.getCoordinators());
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
			
			removeDefaultCoordinatorRoles(existing, existing.getCoordinators());
			existing.delete(deleteOp.isClose());
			return ResponseEvent.response(SiteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public String getObjectName() {
		return "site";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getSiteDao().getSiteIds(key, value);
	}

	private void addSiteStats(List<SiteSummary> sites) {
		if (CollectionUtils.isEmpty(sites)) {
			return;
		}
		
		Map<Long, SiteSummary> sitesMap = new HashMap<Long, SiteSummary>();
		for (SiteSummary site : sites) {
			sitesMap.put(site.getId(), site);
		}
		
		Map<Long, Integer> cpCountMap = daoFactory.getSiteDao().getCpCountBySite(sitesMap.keySet());
		for (Map.Entry<Long, Integer> cpCount : cpCountMap.entrySet()) {
			sitesMap.get(cpCount.getKey()).setCpCount(cpCount.getValue());
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
			
			Collection<User> addedCoordinators = 
				CollectionUtils.subtract(site.getCoordinators(), existing.getCoordinators());
			Collection<User> removedCoordinators = 
				CollectionUtils.subtract(existing.getCoordinators(), site.getCoordinators());
			
			existing.update(site);			
			daoFactory.getSiteDao().saveOrUpdate(existing);
			existing.addOrUpdateExtension();
			
			removeDefaultCoordinatorRoles(existing, removedCoordinators);
			addDefaultCoordinatorRoles(existing, addedCoordinators);
			
			return ResponseEvent.response(SiteDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	private void addDefaultCoordinatorRoles(Site site, Collection<User> users) {
		for (User user: users) {
			rbacSvc.addSubjectRole(site, null, user, getDefaultCoordinatorRoles());
		}
	}
	
	private void removeDefaultCoordinatorRoles(Site site, Collection<User> users) {
		for (User user: users) {
			rbacSvc.removeSubjectRole(site, null, user, getDefaultCoordinatorRoles());
		}
	}
	
	private String[] getDefaultCoordinatorRoles() {
		return new String[] {"Administrator"};
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

		if (result == null) {
			try {
				AccessCtrlMgr.getInstance().ensureCreateShipmentRights();
				result = getFromDb(crit);
			} catch (OpenSpecimenException ose) {
				
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
