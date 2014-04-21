
package com.krishagni.catissueplus.core.biospecimen.events;

public class ConsentTierDetail {

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

	public void setParticipantResponse(String participantResponse) {
		this.participantResponse = participantResponse;
	}

	private String participantResponse;
}
