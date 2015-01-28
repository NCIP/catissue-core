
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantDetailEvent extends ResponseEvent {
	private Long participantId;
	
	private ParticipantDetail participantDetail;

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public ParticipantDetail getParticipantDetail() {
		return participantDetail;
	}

	public void setParticipantDetail(ParticipantDetail participantDetail) {
		this.participantDetail = participantDetail;
	}

	public static ParticipantDetailEvent ok(ParticipantDetail participantDetail) {
		ParticipantDetailEvent event = new ParticipantDetailEvent();
		event.setParticipantDetail(participantDetail);
		event.setStatus(EventStatus.OK);
		return event;
	}
	
	public static ParticipantDetailEvent notFound(Long participantId) {
		ParticipantDetailEvent resp = new ParticipantDetailEvent();
		resp.setParticipantId(participantId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static ParticipantDetailEvent invalidRequest(CatissueErrorCode error, String field) {
		ParticipantDetailEvent resp = new ParticipantDetailEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setErroneousFields(new ErroneousField[] {new ErroneousField(error, field)});
		return resp;
	}
}
