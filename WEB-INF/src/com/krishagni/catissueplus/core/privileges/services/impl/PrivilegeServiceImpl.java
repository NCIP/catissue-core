
package com.krishagni.catissueplus.core.privileges.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;

public class PrivilegeServiceImpl implements PrivilegeService {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public List<Long> getCpList(Long userId, String privilegeConst) {
		Set<Long> cpList = new HashSet<Long>();

//		List<Long> cpIds = daoFactory.getCPUserRoleDao().getCpIdsByUserAndPrivilege(userId, privilegeConst);
//		cpList.addAll(cpIds);
//
//		cpIds = daoFactory.getUserSiteRoleDao().getCpIdsByUserAndPrivilege(userId, privilegeConst);
//		cpList.addAll(cpIds);

		return new ArrayList<Long>(cpList);
	}

	@Override
	public Map<Long, Boolean> getCPPrivileges(Long userId, List<Long> cpIds, String privilegeConst) {
		Map<Long, Boolean> cpPrivilegeMap = new HashMap<Long, Boolean>();
//		List<Long> authrizedCPs = getCpList(userId, privilegeConst);
//		for (Long cpId : cpIds) {
//			if (authrizedCPs.contains(cpId)) {
//				cpPrivilegeMap.put(cpId, true);
//				continue;
//			}
//			cpPrivilegeMap.put(cpId, false);
//		}
		return cpPrivilegeMap;
	}

}
