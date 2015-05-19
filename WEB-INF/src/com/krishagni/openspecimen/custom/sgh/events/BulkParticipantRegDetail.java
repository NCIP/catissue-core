package com.krishagni.openspecimen.custom.sgh.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;


public class BulkParticipantRegDetail extends BulkParticipantRegSummary {

	private List<CollectionProtocolRegistrationDetail> cprDetails;

	public BulkParticipantRegDetail(Long cpId, Integer participantCount, List<CollectionProtocolRegistrationDetail> result) {
		this.cprDetails = result;
		this.setCpId(cpId);
		this.setParticipantCount(participantCount);
	}

	public List<CollectionProtocolRegistrationDetail> getCprDetails() {
		return cprDetails;
	}

	public void setCprDetails(List<CollectionProtocolRegistrationDetail> cprDetails) {
		this.cprDetails = cprDetails;
	}
	
}
