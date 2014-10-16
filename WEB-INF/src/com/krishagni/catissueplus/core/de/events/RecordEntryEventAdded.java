package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RecordEntryEventAdded extends ResponseEvent {

	private Long recordEntryId;
	
	public Long getRecordEntryId() {
		return recordEntryId;
	}

	public void setRecordEntryId(Long recordEntryId) {
		this.recordEntryId = recordEntryId;
	}

	public static RecordEntryEventAdded ok(Long recordEntryId) {
		RecordEntryEventAdded resp = new RecordEntryEventAdded();
		resp.setStatus(EventStatus.OK);
		resp.setRecordEntryId(recordEntryId);
		return resp;		
	}
	
	public static RecordEntryEventAdded notFound() {
		RecordEntryEventAdded resp = new RecordEntryEventAdded();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
}
