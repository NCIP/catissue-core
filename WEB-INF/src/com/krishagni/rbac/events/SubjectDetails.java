package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectRole;

public class SubjectDetails {
	private Long id;
	
	private List<SubjectRoleDetails> roles = new ArrayList<SubjectRoleDetails>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<SubjectRoleDetails> getRoles() {
		return roles;
	}

	public void setRoles(List<SubjectRoleDetails> roles) {
		this.roles = roles;
	}
	
	public static SubjectDetails fromSubject(Subject subject) {
		if (subject == null) {
			subject = new Subject();
		}
		
		SubjectDetails ss = new SubjectDetails();
		
		ss.setId(subject.getId());
		
		for (SubjectRole sr : subject.getSubjectRoles()) {
			ss.getRoles().add(SubjectRoleDetails.fromSubjectRole(sr));
		}
		
		return ss;
	}
}
