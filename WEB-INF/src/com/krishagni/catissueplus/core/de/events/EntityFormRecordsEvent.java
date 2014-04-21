package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class EntityFormRecordsEvent extends ResponseEvent {
	private Long entityId; 
	
	private List<FormRecordSummary> formRecords;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long cprId) {
		this.entityId = cprId;
	}

	public List<FormRecordSummary> getFormRecords() {
		return formRecords;
	}

	public void setFormRecords(List<FormRecordSummary> formRecords) {
		this.formRecords = formRecords;
	}
	
	public static EntityFormRecordsEvent ok(List<FormRecordSummary> formRecords) {
		EntityFormRecordsEvent resp = new EntityFormRecordsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormRecords(formRecords);
		return resp;
	}
}
