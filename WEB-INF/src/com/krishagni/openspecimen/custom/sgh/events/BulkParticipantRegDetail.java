package com.krishagni.openspecimen.custom.sgh.events;

import java.util.List;

import com.krishagni.openspecimen.custom.sgh.events.ParticipantRegDetail;


public class BulkParticipantRegDetail {
	private Long cpId;
	private Integer participantCount;
	
	public BulkParticipantRegDetail() {
	}
	
	public BulkParticipantRegDetail(Long cpId, List<ParticipantRegDetail> registrations) {
		this.cpId = cpId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Integer getParticipantCount() {
		return participantCount;
	}

	public void setParticipantCount(Integer participantCount) {
		this.participantCount = participantCount;
	}

}
