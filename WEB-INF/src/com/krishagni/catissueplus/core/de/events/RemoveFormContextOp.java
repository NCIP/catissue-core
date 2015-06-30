package com.krishagni.catissueplus.core.de.events;


public class RemoveFormContextOp {
	public enum RemoveType { SOFT_REMOVE, HARD_REMOVE };
	
	private Long formId;
	
	private FormType formType;
	
	private Long cpId;
	
	private RemoveType removeType; 
	
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

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public RemoveType getRemoveType() {
		return removeType;
	}

	public void setRemoveType(RemoveType removeType) {
		this.removeType = removeType;
	}
}
