package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectRole;

public class SubjectDetail {
	private Long id;
	
	private List<SubjectRoleDetail> roles = new ArrayList<SubjectRoleDetail>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<SubjectRoleDetail> getRoles() {
		return roles;
	}

	public void setRoles(List<SubjectRoleDetail> roles) {
		this.roles = roles;
	}
	
	public static SubjectDetail from(Subject subject) {
		if (subject == null) {
			subject = new Subject();
		}
		
		SubjectDetail ss = new SubjectDetail();
		ss.setId(subject.getId());
		for (SubjectRole sr : subject.getRoles()) {
			ss.getRoles().add(SubjectRoleDetail.from(sr));
		}
		
		return ss;
	}
}
