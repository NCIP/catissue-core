package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.rbac.common.errors.RbacErrorCode;

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
	
	public SubjectRole addRole(SubjectRole sr) {
		sr.setSubject(this);
		getRoles().add(sr);
		return sr;
	}
	
	public SubjectRole updateRole(SubjectRole sr) {
		SubjectRole existingRole = getExistingRole(sr.getId());
		
		if (existingRole == null) {
			OpenSpecimenException.userError(RbacErrorCode.SUBJECT_ROLE_NOT_FOUND);
		}
		
		existingRole.setSite(sr.getSite());
		existingRole.setCollectionProtocol(sr.getCollectionProtocol());
		existingRole.setRole(sr.getRole());
		existingRole.setSubject(this);
		return existingRole;
	}
	
	public SubjectRole removeSubjectRole(Long srId) {
		SubjectRole existingRole = getExistingRole(srId);
		
		if (existingRole == null) {
			OpenSpecimenException.userError(RbacErrorCode.SUBJECT_ROLE_NOT_FOUND);
		}
		
		getRoles().remove(existingRole);
		return existingRole;
	}

	private SubjectRole getExistingRole(Long srId) {
		for (SubjectRole sr : getRoles()) {
			if (sr.getId().equals(srId)) {
				return sr;
			}
		}
		
		return null;
	}
	
}
