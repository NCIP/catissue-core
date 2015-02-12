package com.krishagni.catissueplus.core.de.events;


public class GetFileDetailOp {
	
	private Long formId;
	
	private Long recordId;
	
	private String ctrlName;
	
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getCtrlName() {
		return ctrlName;
	}

	public void setCtrlName(String ctrlName) {
		this.ctrlName = ctrlName;
	}
}
