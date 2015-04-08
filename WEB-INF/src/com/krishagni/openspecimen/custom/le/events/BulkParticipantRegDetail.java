package com.krishagni.openspecimen.custom.le.events;

import java.util.List;

public class BulkParticipantRegDetail {
	private Long cpId;
	
	private List<ParticipantRegDetail> registrations;
	
	public BulkParticipantRegDetail() {
		
	}
	
	public BulkParticipantRegDetail(Long cpId, List<ParticipantRegDetail> registrations) {
		this.cpId = cpId;
		this.registrations = registrations;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public List<ParticipantRegDetail> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<ParticipantRegDetail> registrations) {
		this.registrations = registrations;
	}
}
