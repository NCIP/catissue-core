package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeleteRecordEntriesEvent extends RequestEvent {

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
