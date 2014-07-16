package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class RemoveFormContextEvent extends RequestEvent {
	public static enum FormType {
		DATA_ENTRY_FORMS,
		QUERY_FORMS
	}
	
	private Long formId;
	
	private FormType formType;
	
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public FormType getFormType() {
		return formType;
	}

	public void setFormType(FormType formType) {
		this.formType = formType;
	}
}
