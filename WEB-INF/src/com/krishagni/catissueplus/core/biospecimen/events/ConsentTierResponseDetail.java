
package com.krishagni.catissueplus.core.biospecimen.events;

public class ConsentTierResponseDetail {

	private String consentStatement;
	
	private String participantResponse;

	public String getConsentStatement() {
		return consentStatement;
	}

	public void setConsentStatement(String consentStatement) {
		this.consentStatement = consentStatement;
	}

	public String getParticipantResponse() {
		return participantResponse;
	}

	public void setParticipantResponse(String participantResponse) {
		this.participantResponse = participantResponse;
	}
}
