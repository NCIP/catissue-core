package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class FormListCriteria extends AbstractListCriteria<FormListCriteria> {
	private String formType;

	private Long userId;

	private Set<Long> cpIds;
	
	@Override
	public FormListCriteria self() {
		return this;
	}

	public String getFormType() {
		return formType;
	}

	public FormListCriteria formType(String formType) {
		this.formType = formType;
		return self();
	}

	public Long userId() {
		return userId;
	}

	public FormListCriteria userId(Long userId) {
		this.userId = userId;
		return self();
	}

	public Set<Long> cpIds() {
		return cpIds;
	}

	public FormListCriteria cpIds(Set<Long> cpIds) {
		this.cpIds = cpIds;
		return self();
	}
}
