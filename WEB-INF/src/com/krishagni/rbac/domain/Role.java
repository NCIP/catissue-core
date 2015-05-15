package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.proxy.HibernateProxyHelper;

import com.krishagni.catissueplus.core.common.CollectionUpdater;

public class Role {
	private Long id;

	private String name;

	private String description;
	
	private Role parentRole;
	
	private Set<RoleAccessControl> acl = new HashSet<RoleAccessControl>();
	
	private Set<Role> childRoles = new HashSet<Role>();
	
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

	public Role getParentRole() {
		return parentRole;
	}

	public void setParentRole(Role parentRole) {
		this.parentRole = parentRole;
	}

	public Set<RoleAccessControl> getAcl() {
		return acl;
	}

	public void setAcl(Set<RoleAccessControl> acl) {
		this.acl = acl;
	}
	
	public Set<Role> getChildRoles() {
		return childRoles;
	}

	public void setChildRoles(Set<Role> childRoles) {
		this.childRoles = childRoles;
	}

	public void updateRole(Role other) {
		setName(other.getName());
		setDescription(other.getDescription());
		setParentRole(other.getParentRole());
		updateChildRoles(other);
		updateAcl(other);
	}
	
	public boolean isDescendentOf(Role other) {
		if (id == null || other.getId() == null) {
			return false;
		}

		Role role = this;
		while (role != null) {
			if (other.getId().equals(role.getId())) {
				return true;
			}

			role = role.getParentRole();
		}

		return false;
	}

	
	@Override
	public int hashCode() {
		return 31 * 1 + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != HibernateProxyHelper.getClassWithoutInitializingProxy(obj)) {
			return false;
		}
		
		Role other = (Role) obj;
		if (name != null && name.equals(other.getName())) {
			return true;
		} 
		
		return false;
	}
	
	private void updateChildRoles(Role other) {
		for (Role child : this.getChildRoles()) {
			if (!other.getChildRoles().contains(child)) {
				child.setParentRole(null);
			}
		}

		CollectionUpdater.update(getChildRoles(), other.getChildRoles());
		for (Role childRole: getChildRoles()) {
			childRole.setParentRole(this);
		}
	}
	
	private void updateAcl(Role other) {
		CollectionUpdater.update(getAcl(), other.getAcl());
		for (RoleAccessControl rac : getAcl()) {
			rac.setRole(this);
		}
	}
}
