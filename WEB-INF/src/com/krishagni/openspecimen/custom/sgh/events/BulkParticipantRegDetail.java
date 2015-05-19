package com.krishagni.openspecimen.custom.sgh.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;


public class BulkParticipantRegDetail extends BulkParticipantRegSummary {

	private List<CollectionProtocolRegistrationDetail> cprDetails;

	public BulkParticipantRegDetail(Boolean isPrintLabels, Long cpId, Integer participantCount, List<CollectionProtocolRegistrationDetail> registrations) {
		this.cprDetails = registrations;
		this.setCpId(cpId);
		this.setParticipantCount(participantCount);
		this.setPrintLabels(isPrintLabels);
	}

	public List<CollectionProtocolRegistrationDetail> getCprDetails() {
		return cprDetails;
	}

	public void setCprDetails(List<CollectionProtocolRegistrationDetail> cprDetails) {
		this.cprDetails = cprDetails;
	}
	
}
