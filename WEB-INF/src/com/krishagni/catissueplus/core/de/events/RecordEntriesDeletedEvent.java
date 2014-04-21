package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RecordEntriesDeletedEvent extends ResponseEvent {
	
	private List<Long> deletedRecIds;

	public List<Long> getDeletedRecIds() {
		return deletedRecIds;
	}

	public void setDeletedRecIds(List<Long> deletedIds) {
		this.deletedRecIds = deletedIds;
	}
	
	public static RecordEntriesDeletedEvent ok(List<Long> deletedRecIds) {
		RecordEntriesDeletedEvent resp = new RecordEntriesDeletedEvent();
		resp.setDeletedRecIds(deletedRecIds);
		resp.setStatus(EventStatus.OK);
		return resp;		
	}
	
	public static RecordEntriesDeletedEvent notFound() {
		RecordEntriesDeletedEvent resp = new RecordEntriesDeletedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}

	
}
