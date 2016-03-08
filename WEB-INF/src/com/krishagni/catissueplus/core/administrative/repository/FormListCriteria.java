package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class FormListCriteria extends AbstractListCriteria<FormListCriteria> {
	private String formType;
	
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

}
