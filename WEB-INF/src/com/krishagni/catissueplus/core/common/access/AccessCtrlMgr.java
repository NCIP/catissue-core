package com.krishagni.catissueplus.core.common.access;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.AccessDetail;
import com.krishagni.catissueplus.core.common.events.Operation;
import com.krishagni.catissueplus.core.common.events.Resource;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectRole;
import com.krishagni.rbac.events.CpSiteInfo;
import com.krishagni.rbac.repository.DaoFactory;
import com.krishagni.rbac.service.RbacService;

@Configurable
public class AccessCtrlMgr {

	@Autowired
	private RbacService rbacService;
	
	@Autowired
	private DaoFactory daoFactory;
	
	@Autowired
	private UserDao userDao;
	
	private static AccessCtrlMgr instance;
	
	private AccessCtrlMgr() {
		
	}
	
	public static AccessCtrlMgr getInstance() {
		if (instance == null) {
			instance = new AccessCtrlMgr();
		}
		
		return instance;
	}
	
	public void ensureUserIsAdmin() {
		User user = AuthUtil.getCurrentUser();
		
		if (!user.isAdmin()) {
			throwAdminAccessRequired();
		}
	}
	
	public boolean canUserPerformOp(Long userId, Resource resource, Operation[] operations) {
		List<String> ops = new ArrayList<String>();
		for (Operation operation : operations) {
			ops.add(operation.getName());
		}
		
		return rbacService.canUserPerformOp(userId, resource.getName(), ops.toArray(new String[0]));
	}
	
	public Set<Site> getAccessibleSites() {
		User user = AuthUtil.getCurrentUser();		
		Subject subject = daoFactory.getSubjectDao().getById(user.getId());
				
		Set<Site> results = new HashSet<Site>();
		boolean accessAllSites = false;
		for (SubjectRole role : subject.getRoles()) {
			if (role.getSite() == null) {
				accessAllSites = true;
				break;
			}
			
			results.add(role.getSite());
		}
		
		if (accessAllSites) {
			results.clear();

			//
			// AuthUtil user is from different session
			// we need to fetch a fresh copy from DB
			//
			user = userDao.getById(user.getId()); 
			results.addAll(user.getDepartment().getInstitute().getSites());
		}

		return results;
	}
	
	public void ensureReadPermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		ensureUserHasPermission(resource, cp, sites, Operation.READ);
	}
	
	public void ensureCreatePermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		ensureUserHasPermission(resource, cp, sites, Operation.CREATE);
	}
	
	public void ensureUpdatePermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		ensureUserHasPermission(resource, cp, sites, Operation.UPDATE);
	}
	
	public void ensureDeletePermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		ensureUserHasPermission(resource, cp, sites, Operation.DELETE);
	}
	
	private void ensureUserHasPermission(Resource resource, CollectionProtocol cp, Set<Site> sites, Operation operation) {
		User user = AuthUtil.getCurrentUser();
		if (user.isAdmin()) {
			return;
		}
		
		boolean canPerformOp = rbacService.canUserPerformOp( 
				user.getId(), 
				resource.getName(), 
				operation.getName(), 
				cp.getId(), 
				getSiteIds(sites));
		
		if (!canPerformOp) {
			throwAccessDenied();
		}
	}
	
	public AccessDetail getReadableCpIds() {
		User user = AuthUtil.getCurrentUser();
		if (user.isAdmin()) {
			return AccessDetail.ACCESS_TO_ALL;
		}
		
		List<CpSiteInfo> cpSites = rbacService.getAccessibleCpSites(
				user.getId(), 
				Resource.CP.getName(), 
				Operation.READ.getName());
		
		Set<Long> readableCps = new HashSet<Long>();
		Set<Long> sitesToBeFetched = new HashSet<Long>();
		
		AccessDetail accessDetail = new AccessDetail(false);		
		for (CpSiteInfo cpSite : cpSites) {
			if (cpSite.getCpId() == null && cpSite.getSiteId() == null) {
				accessDetail.setAccessAll(true);
				break;
			} else if (cpSite.getCpId() != null) {
				readableCps.add(cpSite.getCpId());
			} else if (cpSite.getSiteId() != null) { 
				sitesToBeFetched.add(cpSite.getSiteId());
			}
		}
		
		if (!sitesToBeFetched.isEmpty() && !accessDetail.canAccessAll()) {
			List<Long> ids = new ArrayList<Long>(sitesToBeFetched);
			readableCps.addAll(daoFactory.getCollectionProtocolDao().getCpIdsBySiteIds(ids));
		}
		
		accessDetail.setIds(readableCps);
		if (!accessDetail.isAccessAllowed()) {
			throwAccessDenied();
		}
		
		return accessDetail;
	}
	
	private Set<Long> getSiteIds(Set<Site> sites) {
		Set<Long> siteIds = new HashSet<Long>();		
		for (Site site : sites) {
			siteIds.add(site.getId());
		}
		
		return siteIds;
	}

	private void throwAdminAccessRequired() {
		throw OpenSpecimenException.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
	}
	
	private void throwAccessDenied() {
		throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
	}
}
