package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.rbac.domain.SubjectRole;

public class SubjectRoleDetail {
	private Long id; 
	
	private RoleDetail role;
	
	private CollectionProtocolSummary collectionProtocol;
	
	private SiteDetail site;
	
	private boolean systemRole;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleDetail getRole() {
		return role;
	}

	public void setRole(RoleDetail role) {
		this.role = role;
	}

	public CollectionProtocolSummary getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocolSummary collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public SiteDetail getSite() {
		return site;
	}

	public void setSite(SiteDetail site) {
		this.site = site;
	}
	
	public boolean getSystemRole() {
		return systemRole;
	}

	public void setSystemRole(boolean systemRole) {
		this.systemRole = systemRole;
	}

	public static SubjectRoleDetail from(SubjectRole subjectRole) {
		SubjectRoleDetail sr = new SubjectRoleDetail();
		sr.setId(subjectRole.getId());
		sr.setRole(RoleDetail.from(subjectRole.getRole()));
		sr.setSystemRole(subjectRole.isSystemRole());
		
		if (subjectRole.getCollectionProtocol() != null) {
			sr.setCollectionProtocol(CollectionProtocolSummary.from(subjectRole.getCollectionProtocol()));
		}
		
		if (subjectRole.getSite() != null) {
			sr.setSite(SiteDetail.from(subjectRole.getSite()));
		}
		return sr;
	}
	
	public static List<SubjectRoleDetail> from(Collection<SubjectRole> subjectRoles) {
		List<SubjectRoleDetail> roles = new ArrayList<SubjectRoleDetail>();
		for (SubjectRole role : subjectRoles) {
			roles.add(from(role));
		}
		
		return roles;
	}
}
