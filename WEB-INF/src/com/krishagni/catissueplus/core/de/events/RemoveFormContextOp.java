package com.krishagni.catissueplus.core.de.events;


public class RemoveFormContextOp {
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
