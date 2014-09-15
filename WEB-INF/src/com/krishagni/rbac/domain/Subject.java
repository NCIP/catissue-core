package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

public class Subject {
	private Long id;
	
	private Set<SubjectRole> subjectRoles = new HashSet<SubjectRole>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<SubjectRole> getSubjectRoles() {
		return subjectRoles;
	}

	public void setSubjectRoles(Set<SubjectRole> subjectRoles) {
		this.subjectRoles = subjectRoles;
	}
	
	public void assignRole(SubjectRole subjectRole) {
		boolean found = false;		
		for (SubjectRole sr : subjectRoles) {
			if (sr.getDsoId().equals(subjectRole.getDsoId())) {
				sr.setRole(subjectRole.getRole());
				found = true;
				break;
			}
		}
		
		if (!found) {
			subjectRole.setSubject(this);
			subjectRoles.add(subjectRole);
		}
	}
	
	public void updateRoles(List<SubjectRole> subjectRoles, Session session) {
		Set<Long> dsoIds = new HashSet<Long>();
		session.flush();
		
		for (SubjectRole role : subjectRoles) {
			if (!dsoIds.add(role.getDsoId())) {
				throw new IllegalArgumentException("Multiple roles defined for same DSO");
			}
			
			role.setSubject(this);
		}
		
		this.subjectRoles.clear();
		this.subjectRoles.addAll(subjectRoles);
	}		
}
