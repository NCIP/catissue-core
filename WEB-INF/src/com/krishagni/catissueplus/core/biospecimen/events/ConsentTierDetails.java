
package com.krishagni.catissueplus.core.biospecimen.events;

public class ConsentTierDetails {

	private String consentStatment;

	public String getConsentStatment() {
		return consentStatment;
	}

	public void setConsentStatment(String consentStatment) {
		this.consentStatment = consentStatment;
	}

	public String getParticipantResponse() {
		return participantResponse;
	}

	public void setParticipantResponse(String participantResponses) {
		this.participantResponse = participantResponses;
	}

	private String participantResponse;
}
