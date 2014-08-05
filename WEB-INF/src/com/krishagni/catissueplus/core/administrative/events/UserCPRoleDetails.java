
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

public class UserCPRoleDetails {

	private Long id;

	private String roleName;

	private String cpTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}

	/*public static UserCPRoleDetails fromDomain(UserCPRole userCpRole) {
		UserCPRoleDetails userCpRoleDetails = new UserCPRoleDetails();
		userCpRoleDetails.setCpTitle(userCpRole.getCollectionProtocol().getTitle());
		userCpRoleDetails.setId(userCpRole.getId());
		userCpRoleDetails.setRoleName(userCpRole.getRole().getName());
		return userCpRoleDetails;
	}*/

}
