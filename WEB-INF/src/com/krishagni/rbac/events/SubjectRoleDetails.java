package com.krishagni.rbac.events;

import com.krishagni.rbac.domain.SubjectRole;

public class SubjectRoleDetails {
	private Long dsoId;
	
	private RoleDetails roleDetails;

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}

	public RoleDetails getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetails roleDetails) {
		this.roleDetails = roleDetails;
	}
	
	public static SubjectRoleDetails fromSubjectRole(SubjectRole subjectRole) {
		SubjectRoleDetails srs = new SubjectRoleDetails();
		srs.setDsoId(subjectRole.getDsoId());
		
		if (subjectRole.getRole() != null) {
			srs.setRoleDetails(RoleDetails.fromRole(subjectRole.getRole()));
		}
		
		return srs;
	}
}
