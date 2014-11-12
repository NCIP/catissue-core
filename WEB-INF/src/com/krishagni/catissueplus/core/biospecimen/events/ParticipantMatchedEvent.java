package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class ParticipantMatchedEvent extends ResponseEvent{	
	private String matchedAttr;

	private List<ParticipantDetail> participants;
	
	
	public String getMatchedAttr() {
		return matchedAttr;
	}


	public void setMatchedAttr(String matchedAttr) {
		this.matchedAttr = matchedAttr;
	}


	public List<ParticipantDetail> getParticipants() {
		return participants;
	}

	
	public void setParticipants(List<ParticipantDetail> participants) {
		this.participants = participants;
	}

	public static ParticipantMatchedEvent ok(String matchedAttr, List<ParticipantDetail> participants) {
		ParticipantMatchedEvent event = new ParticipantMatchedEvent();
		event.setMatchedAttr(matchedAttr);
		event.setParticipants(participants);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static ParticipantMatchedEvent invalidRequest(String message, Long... id) {
		ParticipantMatchedEvent resp = new ParticipantMatchedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static ParticipantMatchedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ParticipantMatchedEvent resp = new ParticipantMatchedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
