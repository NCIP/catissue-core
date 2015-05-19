package com.krishagni.openspecimen.custom.sgh.events;



public class BulkParticipantRegSummary {
	private Long cpId;
	private Integer participantCount;
	private Boolean printLabels; 
	
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

	public Boolean isPrintLabels() {
		return printLabels;
	}
	
	public void setPrintLabels(Boolean printLabels) {
		this.printLabels = printLabels;
	}

}
