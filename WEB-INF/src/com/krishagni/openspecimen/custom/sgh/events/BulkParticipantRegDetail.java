package com.krishagni.openspecimen.custom.sgh.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;


public class BulkParticipantRegDetail extends BulkParticipantRegSummary {

	private List<CollectionProtocolRegistrationDetail> cprDetails;

	public List<CollectionProtocolRegistrationDetail> getCprDetails() {
		return cprDetails;
	}

	public void setCprDetails(List<CollectionProtocolRegistrationDetail> cprDetails) {
		this.cprDetails = cprDetails;
	}
	
	public static BulkParticipantRegDetail from(BulkParticipantRegSummary summary, 
			List<CollectionProtocolRegistrationDetail> registrations){
		
		BulkParticipantRegDetail detail = new BulkParticipantRegDetail();
		detail.setCpId(summary.getCpId());
		detail.setParticipantCount(summary.getParticipantCount());
		detail.cprDetails = registrations;
		detail.setPrintLabels(summary.isPrintLabels());
		return detail;
	}
	
}
