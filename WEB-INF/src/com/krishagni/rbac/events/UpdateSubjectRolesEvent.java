package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateSubjectRolesEvent extends RequestEvent {
	private Long subjectId;
	
	private List<SubjectRoleSummary> roles = new ArrayList<SubjectRoleSummary>();

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public List<SubjectRoleSummary> getRoles() {
		return roles;
	}

	public void setRoles(List<SubjectRoleSummary> roles) {
		this.roles = roles;
	}
}
