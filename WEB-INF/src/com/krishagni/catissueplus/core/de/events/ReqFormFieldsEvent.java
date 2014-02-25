package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqFormFieldsEvent extends RequestEvent {
	private Long formId;
	
	private boolean prefixParentFormCaption = false;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public boolean isPrefixParentFormCaption() {
		return prefixParentFormCaption;
	}

	public void setPrefixParentFormCaption(boolean prefixParentFormCaption) {
		this.prefixParentFormCaption = prefixParentFormCaption;
	}
}
