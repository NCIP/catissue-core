
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantDeletedEvent extends ResponseEvent {


	
	public static ParticipantDeletedEvent ok()
	{
		ParticipantDeletedEvent event = new ParticipantDeletedEvent();
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static ParticipantDeletedEvent invalidRequest(String message, Long ... id) {
		ParticipantDeletedEvent resp = new ParticipantDeletedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}
	
	public static ParticipantDeletedEvent serverError(Throwable ... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ParticipantDeletedEvent resp = new ParticipantDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;		
	}
}
