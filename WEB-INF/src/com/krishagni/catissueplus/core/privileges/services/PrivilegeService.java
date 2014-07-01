
package com.krishagni.catissueplus.core.privileges.services;

import java.util.List;
import java.util.Map;

public interface PrivilegeService {
	
	public List<Long> getCpList(Long userId, String privilege);
	
	public Map<Long, Boolean> getCPPrivileges(Long userId, List<Long> cpIds, String privlege);
	
	public boolean hasPhiAccess(Long userId, Long cpId);
	
}
