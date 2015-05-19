package com.krishagni.openspecimen.custom.sgh.events;



public class BulkParticipantRegSummary {
	private Long cpId;
	private Integer participantCount;
	
	public BulkParticipantRegSummary() {
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
