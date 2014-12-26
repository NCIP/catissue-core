
package com.krishagni.catissueplus.core.biospecimen.events;

public class ConsentTierResponseDetail {

	private String consentStatment;
	
	private String participantResponse;

	public String getConsentStatment() {
		return consentStatment;
	}

	public void setConsentStatment(String consentStatment) {
		this.consentStatment = consentStatment;
	}

	public String getParticipantResponse() {
		return participantResponse;
	}

	public void setParticipantResponse(String participantResponse) {
		this.participantResponse = participantResponse;
	}
}
