package com.krishagni.rbac.events;

public class SubjectRoleOp {
	public enum OP {
		ADD, UPDATE, REMOVE
	}

	private Long subjectId;
	
	private SubjectRoleDetail subjectRole;
	
	private OP op;

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public SubjectRoleDetail getSubjectRole() {
		return subjectRole;
	}

	public void setSubjectRole(SubjectRoleDetail subjectRole) {
		this.subjectRole = subjectRole;
	}

	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}
	
}
