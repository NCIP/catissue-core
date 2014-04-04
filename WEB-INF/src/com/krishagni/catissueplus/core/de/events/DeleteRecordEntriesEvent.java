package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class DeleteRecordEntriesEvent {

	private Long formId;
	
	private List<Long> recordIds;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public List<Long> getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(List<Long> recordIds) {
		this.recordIds = recordIds;
	}
}
