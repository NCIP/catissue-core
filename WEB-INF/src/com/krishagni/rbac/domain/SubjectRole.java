package com.krishagni.rbac.domain;

public class SubjectRole {
	private Long id;
	
	private Subject subject;

	private Role role;
	
	private Long dsoId;

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

	public Long getDsoId() {
		return dsoId;
	}

	public void setDsoId(Long dsoId) {
		this.dsoId = dsoId;
	}	
}
