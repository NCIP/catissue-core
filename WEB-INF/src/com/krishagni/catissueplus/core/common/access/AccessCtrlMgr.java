package com.krishagni.catissueplus.core.common.access;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.AccessDetail;
import com.krishagni.catissueplus.core.common.events.Operation;
import com.krishagni.catissueplus.core.common.events.Resource;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;
import com.krishagni.rbac.events.CpSiteInfo;
import com.krishagni.rbac.service.RbacService;

@Configurable
public class AccessCtrlMgr {

	@Autowired
	private RbacService rbacService;
	
	@Autowired
	private DaoFactory daoFactory;
	
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
	
	public void ensureReadPermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		User user = AuthUtil.getCurrentUser();
		if (user.isAdmin()) {
			return;
		}
		
		if (!rbacService.canUserPerformOp( 
				user.getId(), 
				resource.getName(), 
				Operation.READ.getName(), 
				cp.getId(), 
				getSiteIds(sites))) {
			throwAccessDenied();
		}
	}
	
	public void ensureCreatePermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		User user = AuthUtil.getCurrentUser();
		if (user.isAdmin()) {
			return;
		}
		
		if(!rbacService.canUserPerformOp(
				user.getId(), 
				resource.getName(), 
				Operation.CREATE.getName(), 
				cp.getId(), 
				getSiteIds(sites))) {
			throwAccessDenied();
		}
	}
	
	public void ensureUpdatePermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		User user = AuthUtil.getCurrentUser();
		if (user.isAdmin()) {
			return;
		}
		
		if(!rbacService.canUserPerformOp(
				user.getId(), 
				resource.getName(), 
				Operation.UPDATE.getName(), 
				cp.getId(), 
				getSiteIds(sites))) {
			throwAccessDenied();
		}
	}
	
	public void ensureDeletePermission(Resource resource, CollectionProtocol cp, Set<Site> sites) {
		User user = AuthUtil.getCurrentUser();
		if (user.isAdmin()) {
			return;
		}
		
		if(!rbacService.canUserPerformOp(
				user.getId(), 
				resource.getName(), 
				Operation.DELETE.getName(), 
				cp.getId(), 
				getSiteIds(sites))) {
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
		throw OpenSpecimenException.userError(RbacErrorCode.ADMIN_PRIVILEGES_REQUIRED);
	}
	
	private void throwAccessDenied() {
		throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
	}
}
