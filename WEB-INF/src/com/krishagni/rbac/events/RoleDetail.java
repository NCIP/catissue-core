package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.domain.RoleAccessControl;

public class RoleDetail {
	private Long id;
	
	private String name;
	
	private String description;
	
	private String parentRoleName;
	
	private Set<String> childRoles = new HashSet<String>();
	
	private List<RoleAccessControlDetails> acl = new ArrayList<RoleAccessControlDetails>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getParentRoleName() {
		return parentRoleName;
	}

	public void setParentRoleName(String parentRoleName) {
		this.parentRoleName = parentRoleName;
	}

	public Set<String> getChildRoles() {
		return childRoles;
	}

	public void setChildRoles(Set<String> childRoles) {
		this.childRoles = childRoles;
	}

	public List<RoleAccessControlDetails> getAcl() {
		return acl;
	}

	public void setAcl(List<RoleAccessControlDetails> acl) {
		this.acl = acl;
	}


	public static RoleDetail from(Role role) {
		RoleDetail rs = new RoleDetail();
		rs.setId(role.getId());
		rs.setName(role.getName());
		rs.setDescription(role.getDescription());
		
		for (RoleAccessControl rac : role.getAcl()) {
			rs.getAcl().add(RoleAccessControlDetails.fromRoleAccessControl(rac));
		}
		
		if (role.getParentRole() != null) {
			rs.setParentRoleName(role.getParentRole().getName());
		}
		
		for (Role childRole : role.getChildRoles()) {
			rs.getChildRoles().add(childRole.getName());
		}
		
		return rs;
	}
	
	public static List<RoleDetail> from(List<Role> roles) {
		List<RoleDetail> rs = new ArrayList<RoleDetail>();
		
		for (Role r : roles) {
			rs.add(RoleDetail.from(r));
		}
		
		return rs;
	}
}
