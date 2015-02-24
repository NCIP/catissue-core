package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.rbac.domain.SubjectRole;

public class SubjectRoleDetail {
	private Long dsoId;
	
	private RoleDetail roleDetails;

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}

	public RoleDetail getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetail roleDetails) {
		this.roleDetails = roleDetails;
	}
	
	public static SubjectRoleDetail from(SubjectRole subjectRole) {
		SubjectRoleDetail srs = new SubjectRoleDetail();
		srs.setDsoId(subjectRole.getDsoId());
		
		if (subjectRole.getRole() != null) {
			srs.setRoleDetails(RoleDetail.from(subjectRole.getRole()));
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
