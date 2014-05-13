package com.krishagni.catissueplus.core.privileges.domain.factory;

import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;


public interface RoleFactory {
	
	public Role createRole(RoleDetails details);

}
