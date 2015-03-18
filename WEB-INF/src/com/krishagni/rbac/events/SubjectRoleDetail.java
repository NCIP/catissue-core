package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.rbac.domain.SubjectRole;

public class SubjectRoleDetail {
	private RoleDetail roleDetails;
	
	private CollectionProtocolSummary collectionProtocol;
	
	private SiteDetail site;
	
	public RoleDetail getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetail roleDetails) {
		this.roleDetails = roleDetails;
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

	public static SubjectRoleDetail from(SubjectRole subjectRole) {
		SubjectRoleDetail srs = new SubjectRoleDetail();
		srs.setRoleDetails(RoleDetail.from(subjectRole.getRole()));
		
		if (subjectRole.getCollectionProtocol() != null) {
			srs.setCollectionProtocol(CollectionProtocolSummary.from(subjectRole.getCollectionProtocol()));
		}
		
		if (subjectRole.getSite() != null) {
			srs.setSite(SiteDetail.from(subjectRole.getSite()));
		}
		return srs;
	}
	
	public static List<SubjectRoleDetail> from(Collection<SubjectRole> subjectRoles) {
		List<SubjectRoleDetail> roles = new ArrayList<SubjectRoleDetail>();
		for (SubjectRole role : subjectRoles) {
			roles.add(from(role));
		}
		
		return roles;
	}
}
