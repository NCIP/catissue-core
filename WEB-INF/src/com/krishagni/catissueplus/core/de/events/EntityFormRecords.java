package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class EntityFormRecords {
	private Long formId;
	
	private Long formCtxtId;
	
	private String formCaption;
	
	private List<FormRecordSummary> records;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getFormCtxtId() {
		return formCtxtId;
	}

	public void setFormCtxtId(Long formCtxtId) {
		this.formCtxtId = formCtxtId;
	}

	public String getFormCaption() {
		return formCaption;
	}

	public void setFormCaption(String formCaption) {
		this.formCaption = formCaption;
	}

	public List<FormRecordSummary> getRecords() {
		return records;
	}

	public void setRecords(List<FormRecordSummary> records) {
		this.records = records;
	}
}
