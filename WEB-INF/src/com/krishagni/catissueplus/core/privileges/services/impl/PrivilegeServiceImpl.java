
package com.krishagni.catissueplus.core.privileges.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.privileges.events.UserPrivDetail;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import gov.nih.nci.security.authorization.domainobjects.Role;

public class PrivilegeServiceImpl implements PrivilegeService {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public List<Long> getCpList(Long userId, String privilegeConst) {
		Set<Long> cpList = new HashSet<Long>();
		UserPrivDetail privDetail = daoFactory.getCPUserRoleDao().getUserPrivDetail(userId);
		String roleId = getRole(privDetail.getCsmUserId());
		if (roleId.equalsIgnoreCase(Constants.ADMIN_USER))
    {
			cpList.addAll(daoFactory.getCPUserRoleDao().getAllCpIds());
			return new ArrayList<Long>(cpList);
    }
		List<Long> cp_Ids = daoFactory.getCPUserRoleDao().getCpIdsByUserId(userId);
		for (Long long1 : cp_Ids) {
			cpList.add(long1);
		}
		cpList.addAll(cp_Ids);
		List<Long> siteIds = daoFactory.getCPUserRoleDao().getSiteIdsByUserId(userId);
		PrivilegeManager privilegeManager;
		try {
			privilegeManager = PrivilegeManager.getInstance();
		
		final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(privDetail.getLoginName());
			if (siteIds != null && !siteIds.isEmpty())
			{
				boolean hasViewPrivilege = false; // This checks if user has Registration and/or Specimen_Processing privilege
				
				for (final Long siteId : siteIds)
				{
					final String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
					edu.wustl.common.util.global.Variables.privilegeDetailsMap.get(Constants.EDIT_PROFILE_PRIVILEGE);
					if (privilegeCache.hasPrivilege(peName,privilegeConst))
//							edu.wustl.common.util.global.Variables.privilegeDetailsMap
//									.get(privilegeConst)))
					{
						hasViewPrivilege = true;
					}
					else if(privilegeCache.hasPrivilege(peName,Permissions.SPECIMEN_PROCESSING))
					{
						hasViewPrivilege = true;
					}
					if(hasViewPrivilege)
					{
						List<Long> cpIds = daoFactory.getCPUserRoleDao().getCpIdBySiteId(siteId);
						cpList.addAll(cpIds);
				}
			}
	}
//		for (Long siteId : siteIds) {
//			if(privilegeCache.hasPrivilege("SITE_" + siteId + "_All_CP", privilegeConst)){
//				
//			}
//		}
		
		
		}
		catch (SMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<Long>(cpList);
	}
	
	private String getRole(Long csmUserId){
		try
		{
				final Role role = SecurityManagerFactory.getSecurityManager()
						.getUserRole(csmUserId);
				if (role != null && role.getId() != null)
				{
					return role.getId().toString();
				}
		}
		catch (final SMException e)
		{
		}
		return "";
	}
	

	@Override
	public Map<Long, Boolean> getCPPrivileges(Long userId, List<Long> cpIds, String privilegeConst) {
		Map<Long, Boolean> cpPrivilegeMap = new HashMap<Long, Boolean>();
		List<Long> authrizedCPs = getCpList(userId, privilegeConst);
		for (Long cpId : cpIds) {
			if (authrizedCPs.contains(cpId)) {
				cpPrivilegeMap.put(cpId, true);
				continue;
			}
			cpPrivilegeMap.put(cpId, false);
		}
		return cpPrivilegeMap;
	}

	@Override
	public boolean hasPhiAccess(Long userId, Long cpId){
		UserPrivDetail privDetail = daoFactory.getCPUserRoleDao().getUserPrivDetail(userId);
		String roleId = getRole(privDetail.getCsmUserId());
		boolean isPhiView = false;
		if (roleId.equalsIgnoreCase(Constants.ADMIN_USER))
    {
			return true;
    }
		PrivilegeManager privilegeManager;
		try {
			privilegeManager = PrivilegeManager.getInstance();
		final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(privDetail.getLoginName());
		if(privilegeCache.hasPrivilege(CollectionProtocol.class.getName()+"_"+cpId, Permissions.REGISTRATION)){
			isPhiView = true;
		}
		else{
			List<Long> siteIds = daoFactory.getCPUserRoleDao().getSiteIdsByCpId(cpId);
			for (final Long siteId : siteIds)
			{
				final String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
				edu.wustl.common.util.global.Variables.privilegeDetailsMap.get(Constants.EDIT_PROFILE_PRIVILEGE);
				if (privilegeCache.hasPrivilege(peName,Permissions.REGISTRATION))
				{
					isPhiView = true;
				}
			}
		}
		}
		catch(SMException e){
			e.printStackTrace();
		}
		return isPhiView;
	}

}
