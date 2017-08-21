package com.krishagni.rbac.events;

public class SubjectRoleSummary {
	private String role;
	
	private Long dsoId;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}
}
