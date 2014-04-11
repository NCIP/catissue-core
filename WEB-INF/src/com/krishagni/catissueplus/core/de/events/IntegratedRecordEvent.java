package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class IntegratedRecordEvent extends ResponseEvent {

	public static IntegratedRecordEvent ok() {
		IntegratedRecordEvent resp = new IntegratedRecordEvent();
		return resp;		
	}
	
	public static IntegratedRecordEvent notFound() {
		IntegratedRecordEvent resp = new IntegratedRecordEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
}
