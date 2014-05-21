
package com.krishagni.catissueplus.core.privileges.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.common.SetUpdater;

public class Role {

	private Long id;

	private String name;

	private String description;

	private Set<Privilege> privileges = new HashSet<Privilege>();

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

	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	public void update(Role role) {
		this.setName(role.getName());
		setAllPrivileges(role.getPrivileges());
	}

	private void setAllPrivileges(Set<Privilege> privCollection) {
		SetUpdater.<Privilege> newInstance().update(this.getPrivileges(), privCollection);
	}
}
