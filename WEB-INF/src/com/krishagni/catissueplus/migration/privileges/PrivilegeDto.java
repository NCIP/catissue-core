package com.krishagni.catissueplus.migration.privileges;

import java.util.List;


public class PrivilegeDto {
 
	List<String> privileges;

	
	public List<String> getPrivileges() {
		return privileges;
	}

	
	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((privileges == null) ? 0 : privileges.hashCode());
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
		PrivilegeDto other = (PrivilegeDto) obj;
		if (privileges == null) {
			if (other.privileges != null)
				return false;
		}
		else if (!privileges.equals(other.privileges))
			return false;
		return true;
	}
	

	
}
