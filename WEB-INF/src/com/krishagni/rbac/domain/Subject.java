package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.krishagni.catissueplus.core.common.CollectionUpdater;

public class Subject {
	private Long id;
	
	private Set<SubjectRole> roles = new HashSet<SubjectRole>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<SubjectRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<SubjectRole> subjectRoles) {
		this.roles = subjectRoles;
	}
	
	public void updateRoles(List<SubjectRole> subjectRoles, Session session) {
		CollectionUpdater.update(getRoles(), subjectRoles);
	}		
}
