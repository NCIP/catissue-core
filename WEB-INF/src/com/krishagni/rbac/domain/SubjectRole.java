package com.krishagni.rbac.domain;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class SubjectRole {
	private Long id;
	
	private Subject subject;

	private Role role;
	
	private CollectionProtocol collectionProtocol;
	
	private Site site;
	
	private boolean systemRole;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;

	}
	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
	public boolean isSystemRole() {
		return systemRole;
	}

	public void setSystemRole(boolean systemRole) {
		this.systemRole = systemRole;
	}

	@Override
	public int hashCode() {
		return 31 * 1 
				+ ((role == null) ? 0 : role.hashCode())
				+ ((collectionProtocol == null) ? 0 : collectionProtocol.hashCode()) 
				+ ((site == null) ? 0 : site.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		SubjectRole other = (SubjectRole)obj;
		boolean roleEquals = false;
		
		if (getRole().equals(other.getRole())) {
			roleEquals = true;
		} 

		boolean cpEquals = false;
		if (getCollectionProtocol() == null && other.getCollectionProtocol() == null) {
			cpEquals = true;
		} else if (getCollectionProtocol() != null && getCollectionProtocol().equals(other.getCollectionProtocol())) {
			cpEquals = true;
		}
		
		boolean siteEquals = false;
		if (getSite() == null && other.getSite() == null) {
			siteEquals = true;
		} else if (getSite() != null && getSite().equals(other.getSite())) {
			siteEquals = true;
		}
		
		return roleEquals && cpEquals && siteEquals;
	}
}
