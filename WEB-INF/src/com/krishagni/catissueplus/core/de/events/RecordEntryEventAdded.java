package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RecordEntryEventAdded extends ResponseEvent {

	public static RecordEntryEventAdded ok() {
		RecordEntryEventAdded resp = new RecordEntryEventAdded();
		return resp;		
	}
	
	public static RecordEntryEventAdded notFound() {
		RecordEntryEventAdded resp = new RecordEntryEventAdded();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
}
