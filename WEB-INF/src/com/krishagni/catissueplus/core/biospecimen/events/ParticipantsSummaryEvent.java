
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ParticipantsSummaryEvent extends ResponseEvent {

	private List<ParticipantInfo> participantsInfo;

	public List<ParticipantInfo> getParticipantsInfo() {
		return participantsInfo;
	}

	public void setParticipantsInfo(List<ParticipantInfo> participantsInfo) {
		this.participantsInfo = participantsInfo;
	}

	public static ParticipantsSummaryEvent ok(List<ParticipantInfo> participantsInfo) {
		ParticipantsSummaryEvent participantsSummaryEvent = new ParticipantsSummaryEvent();
		participantsSummaryEvent.setParticipantsInfo(participantsInfo);
		participantsSummaryEvent.setStatus(EventStatus.OK);
		return participantsSummaryEvent;
	}

	public static ParticipantsSummaryEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ParticipantsSummaryEvent resp = new ParticipantsSummaryEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
