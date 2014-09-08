
package com.krishagni.catissueplus.core.privileges.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.privileges.PrivilegeType;
import com.krishagni.catissueplus.core.privileges.events.UserPrivDetail;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.security.exception.SMException;
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
	public List<Long> getCpList(Long userId, String privilegeConst,boolean isChkPrivilege) {
		Set<Long> cpList = new HashSet<Long>();
		Set<Long> readDeniedList = new HashSet<Long>();
		UserPrivDetail privDetail = daoFactory.getCPUserRoleDao().getUserPrivDetail(userId);
		String roleId = getRole(privDetail.getCsmUserId());
		if (Constants.ADMIN_USER.equalsIgnoreCase(roleId))
    {
			cpList.addAll(daoFactory.getCPUserRoleDao().getAllCpIds());
			return new ArrayList<Long>(cpList);
    }
		if (!isChkPrivilege)
    {
			cpList.addAll(daoFactory.getCPUserRoleDao().getAllCpIds());
    }
		
		PrivilegeManager privilegeManager;
		try {
			privilegeManager = PrivilegeManager.getInstance();
		
		final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(privDetail.getLoginName());
		List<Long> cp_Ids = daoFactory.getCPUserRoleDao().getCpIdsByUserId(userId);
		for (Long cpId : cp_Ids) {
			if(privilegeCache.hasPrivilege(CollectionProtocol.class.getName()+"_"+cpId, PrivilegeType.READ_DENIED.name())){
				readDeniedList.add(cpId);
			}
			cpList.add(cpId);
		}
//		cpList.addAll(cp_Ids);
		List<Long> siteIds = daoFactory.getCPUserRoleDao().getSiteIdsByUserId(userId);
			if (siteIds != null && !siteIds.isEmpty())
			{
				boolean hasViewPrivilege = false; // This checks if user has Registration and/or Specimen_Processing privilege
				
				for (final Long siteId : siteIds)
				{
					final String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
					edu.wustl.common.util.global.Variables.privilegeDetailsMap.get(Constants.EDIT_PROFILE_PRIVILEGE);
					
					if(privilegeCache.hasPrivilege(peName,PrivilegeType.READ_DENIED.name()))
					{
						hasViewPrivilege = false;
						List<Long> cpIds = daoFactory.getCPUserRoleDao().getCpIdBySiteId(siteId);
						readDeniedList.addAll(cpIds);
					}
					else if(privilegeCache.hasPrivilege(peName,privilegeConst))
					{
						hasViewPrivilege = true;
					}
					else if(privilegeCache.hasPrivilege(peName,PrivilegeType.SPECIMEN_PROCESSING.name()))
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
		
			cpList.removeAll(readDeniedList);
		}
		catch (SMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<Long>(cpList);
	}
	
//	private void removeReadDenied(Set<Long> cpList,PrivilegeCache privilegeCache){
//		for (Long long1 : cpList) {
//			if(privilegeCache.hasPrivilege(peName,privilegeConst))
//			{
//				hasViewPrivilege = true;
//			}
//		}
//	}
	
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
	public boolean hasPrivilege(Long userId, Long cpId,String privilegeConstant){
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
		if(hasPrivilege(privilegeCache, CollectionProtocol.class.getName()+"_"+cpId, privilegeConstant)){
			isPhiView = true;
		}
		else{
			List<Long> siteIds = daoFactory.getCPUserRoleDao().getSiteIdsByCpId(cpId);
			for (final Long siteId : siteIds)
			{
				final String peName = Constants.getCurrentAndFuturePGAndPEName(siteId);
				if (privilegeCache.hasPrivilege(peName,privilegeConstant))
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

	@Override
	public List<Long> getCpList(Long userId, String privilege) {

		return getCpList(userId, privilege, false);
	}
	
	private boolean hasPrivilege(PrivilegeCache privilegeCache, String key, String privilegeConstant) throws SMException {
		if(privilegeCache.hasPrivilege(key, privilegeConstant)){
			return true;
		}else if(PrivilegeType.PHI_ACCESS.value().equals(privilegeConstant)){
			return privilegeCache.hasPrivilege(key, PrivilegeType.REGISTRATION.value());
		}
		
		return false;
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

}
