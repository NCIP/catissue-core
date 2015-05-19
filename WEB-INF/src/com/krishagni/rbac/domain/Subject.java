package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class Subject {
	private Long id;
	
	private String activityStatus;
	
	private Set<SubjectRole> roles = new HashSet<SubjectRole>();
	
	//
	// read only
	//
	private Set<SubjectAccess> accessList = new HashSet<SubjectAccess>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Set<SubjectRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<SubjectRole> subjectRoles) {
		this.roles = subjectRoles;
	}
	
	public Set<SubjectAccess> getAccessList() {
		return accessList;
	}

	public void setAccessList(Set<SubjectAccess> accessList) {
		this.accessList = accessList;
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
		SubjectRole existingRole = getRole(sr.getId());
		
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
		SubjectRole existingRole = getRole(srId);
		if (existingRole == null) {
			OpenSpecimenException.userError(RbacErrorCode.SUBJECT_ROLE_NOT_FOUND);
		}
		
		getRoles().remove(existingRole);
		return existingRole;
	}
	
	public SubjectRole removeSubjectRole(SubjectRole sr) {
		SubjectRole existingRole = getRole(sr);
		if (existingRole == null) {
			OpenSpecimenException.userError(RbacErrorCode.SUBJECT_ROLE_NOT_FOUND);
		}
		
		getRoles().remove(existingRole);
		return existingRole;
	}
	
	public void removeAllSubjectRoles() {
		getRoles().clear();
	}
	
	public SubjectRole getRole(Long srId) {
		for (SubjectRole sr : getRoles()) {
			if (sr.getId().equals(srId)) {
				return sr;
			}
		}
		
		return null;
	}
	
	private SubjectRole getRole(SubjectRole input) {
		for (SubjectRole sr : getRoles()) {
			if (!sr.getRole().equals(input.getRole())) {
				continue;
			}

			if (!isCpEquals(sr.getCollectionProtocol(), input.getCollectionProtocol())) {
				continue;
			}
			
			if (!isSiteEquals(sr.getSite(), input.getSite())) {
				continue;
			}
					
			return sr;
		}
		
		return null;
	}
	
	private boolean isCpEquals(CollectionProtocol cp1, CollectionProtocol cp2) {
		boolean equals = false;
		if (cp1 == null && cp2 == null) {
			equals = true;
		} else if (cp1 != null && cp1.equals(cp2)) {
			equals = true;
		}

		return equals;
	}

	private boolean isSiteEquals(Site site1, Site site2) {
		boolean equals = false;
		if (site1 == null && site2 == null) {
			equals = true;
		} else if (site1 != null && site1.equals(site2)) {
			equals = true;
		}

		return equals;
	}
}
