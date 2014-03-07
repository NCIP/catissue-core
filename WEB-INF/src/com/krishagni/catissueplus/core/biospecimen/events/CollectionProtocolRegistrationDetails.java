
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

public class CollectionProtocolRegistrationDetails {

	private ParticipantDetail participantDetails;

	private Long id;

	private Long cpId;

	private String ppid;

	private String barcode;

	private Date registrationDate;

	private ConsentResponseDetails consentResponseDetails;

	public ConsentResponseDetails getConsentResponseDetails() {
		return consentResponseDetails;
	}

	public void setConsentResponseDetails(ConsentResponseDetails consentResponseDetails) {
		this.consentResponseDetails = consentResponseDetails;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public ParticipantDetail getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(ParticipantDetail participantDetails) {
		this.participantDetails = participantDetails;
	}

	public static CollectionProtocolRegistrationDetails fromDomain(CollectionProtocolRegistration cpr) {
		CollectionProtocolRegistrationDetails details = new CollectionProtocolRegistrationDetails();
		details.setId(cpr.getId());
		details.setCpId(cpr.getCollectionProtocol().getId());
		return details;
	}

}
