package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BulkRegistrationCreatedEvent extends ResponseEvent {
	private ParticipantRegistrationDetails participantDetails;

	public ParticipantRegistrationDetails getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantRegistrationDetails participantDetails) {
		this.participantDetails = participantDetails;
	}
	
	public static BulkRegistrationCreatedEvent ok(ParticipantRegistrationDetails participantDetails) {
		BulkRegistrationCreatedEvent event = new BulkRegistrationCreatedEvent();
		event.setParticipantDetails(participantDetails);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static BulkRegistrationCreatedEvent invalidRequest(String message, ErroneousField... fields) {
		BulkRegistrationCreatedEvent resp = new BulkRegistrationCreatedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static BulkRegistrationCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		BulkRegistrationCreatedEvent resp = new BulkRegistrationCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static BulkRegistrationCreatedEvent accessDenied(String registration, Long cpId) {
		BulkRegistrationCreatedEvent resp = new BulkRegistrationCreatedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setMessage("User doens't have "+registration+" privileges.");
		return resp;
	}
}
