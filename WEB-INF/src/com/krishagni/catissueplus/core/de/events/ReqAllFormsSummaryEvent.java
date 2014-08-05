package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqAllFormsSummaryEvent extends RequestEvent {
	public static enum FormType {
		DATA_ENTRY_FORMS,
		QUERY_FORMS,
		SPECIMEN_EVENT_FORMS
	}

	private FormType formType;
	
	public FormType getFormType() {
		return formType;
	}

	public void setFormType(FormType formType) {
		this.formType = formType;
	}
}
