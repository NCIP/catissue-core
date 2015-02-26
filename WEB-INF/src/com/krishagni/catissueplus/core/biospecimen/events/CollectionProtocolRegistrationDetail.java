
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;

public class CollectionProtocolRegistrationDetail {
	private Long id;
	
	private ParticipantDetail participant;

	private Long cpId;
	
	private String cpTitle;

	private String ppid;

	private String barcode;

	private String activityStatus;

	private Date registrationDate;

	private ConsentDetail consentDetails;
	
	private List<String> modifiedAttributes = new ArrayList<String>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ParticipantDetail getParticipant() {
		return participant;
	}

	public void setParticipant(ParticipantDetail participant) {
		this.participant = participant;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public ConsentDetail getConsentDetails() {
		return consentDetails;
	}

	public void setConsentDetails(ConsentDetail consentDetails) {
		this.consentDetails = consentDetails;
	}

	public Boolean isPpidModified() {
		return modifiedAttributes.contains("ppid");
	}

	public Boolean isBarcodeModified() {
		return modifiedAttributes.contains("barcode");
	}

	public Boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}

	public Boolean isRegistrationDateModified() {
		return modifiedAttributes.contains("registrationDate");
	}

	public Boolean isParticipantModified() {
		return modifiedAttributes.contains("participantDetail");
	}
	
	public void modifyParticipant() {
		modifiedAttributes.add("participantDetail");
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public static CollectionProtocolRegistrationDetail from(CollectionProtocolRegistration cpr) {		 
		CollectionProtocolRegistrationDetail detail = new CollectionProtocolRegistrationDetail();
		
		detail.setParticipant(ParticipantDetail.from(cpr.getParticipant()));
		detail.setId(cpr.getId());
		detail.setCpId(cpr.getCollectionProtocol().getId());
		detail.setActivityStatus(cpr.getActivityStatus());
		detail.setCpTitle(cpr.getCollectionProtocol().getTitle());
		detail.setBarcode(cpr.getBarcode());
		detail.setPpid(cpr.getPpid());
		detail.setRegistrationDate(cpr.getRegistrationDate());
		
		ConsentDetail consent = new ConsentDetail();
		consent.setConsentDocumentUrl(cpr.getSignedConsentDocumentUrl());
		consent.setConsentSignatureDate(cpr.getConsentSignDate());
		if (cpr.getConsentWitness() != null) {
			consent.setWitnessName(cpr.getConsentWitness().getEmailAddress());
		}
		
		if (cpr.getConsentResponses() != null) {
			for (ConsentTierResponse response : cpr.getConsentResponses()) {
				ConsentTierResponseDetail stmt = new ConsentTierResponseDetail();
				if (response.getConsentTier() != null) {
					stmt.setConsentStatment(response.getConsentTier().getStatement());
					stmt.setParticipantResponse(response.getResponse());
					consent.getConsentTierResponses().add(stmt);
				}
				
			}
		}
		
		detail.setConsentDetails(consent);
		return detail;
	}
	
	public static List<CollectionProtocolRegistrationDetail> fromCprs(List<CollectionProtocolRegistration> cprList) {
		List<CollectionProtocolRegistrationDetail> cprs = new ArrayList<CollectionProtocolRegistrationDetail>();
		
		for (CollectionProtocolRegistration cpr : cprList) {
			cprs.add(from(cpr));
		}
		
		return cprs;
	}

}
