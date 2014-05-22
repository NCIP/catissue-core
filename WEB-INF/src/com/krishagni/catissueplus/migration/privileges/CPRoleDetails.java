
package com.krishagni.catissueplus.migration.privileges;

import com.krishagni.catissueplus.core.privileges.events.RoleDetails;

public class CPRoleDetails {

	private Long id;

	private RoleDetails roleDetails;

	private String cpTitle;

	public Long getId() {
		return id;
	}

	public RoleDetails getRoleDetails() {
		return roleDetails;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRoleDetails(RoleDetails roleDetails) {
		this.roleDetails = roleDetails;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpTitle == null) ? 0 : cpTitle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CPRoleDetails other = (CPRoleDetails) obj;
		if (cpTitle == null) {
			if (other.cpTitle != null)
				return false;
		}
		else if (!cpTitle.equals(other.cpTitle))
			return false;
		return true;
	}

}
