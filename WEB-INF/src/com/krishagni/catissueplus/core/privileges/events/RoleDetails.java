
package com.krishagni.catissueplus.core.privileges.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.privileges.domain.Privilege;
import com.krishagni.catissueplus.core.privileges.domain.Role;

public class RoleDetails {

	private Long id;

	private String name;

	private String description;

	private List<String> privilegeNames = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPrivilegeNames() {
		return privilegeNames;
	}

	public void setPrivilegeNames(List<String> privilegeNames) {
		this.privilegeNames = privilegeNames;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static RoleDetails fromDomain(Role role) {
		RoleDetails roleDetails = new RoleDetails();
		roleDetails.setId(role.getId());
		roleDetails.setName(role.getName());
		roleDetails.setDescription(role.getDescription());
		setPrivileges(roleDetails, role.getPrivileges());
		return roleDetails;
	}

	private static void setPrivileges(RoleDetails roleDetails, Set<Privilege> privileges) {
		List<String> privilegeNames = new ArrayList<String>();
		for (Privilege privilege : privileges) {
			privilegeNames.add(privilege.getName());
		}
		roleDetails.setPrivilegeNames(privilegeNames);
	}

}
