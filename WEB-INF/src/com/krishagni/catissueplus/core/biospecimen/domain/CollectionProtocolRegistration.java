
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collection;
import java.util.Date;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;

public class CollectionProtocolRegistration {

	private final String ACTIVITY_STATUS_ACTIVE = "Active";
	private Long id;

	private String protocolParticipantIdentifier;

	private Date registrationDate;

	private Participant participant;

	private CollectionProtocol collectionProtocol;

	private Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection;

	private String activityStatus;

	private String signedConsentDocumentURL;

	private Date consentSignatureDate;

	private User consentWitness;

	private Collection<ConsentTierResponse> consentTierResponseCollection;

	private Integer offset;

	private String barcode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProtocolParticipantIdentifier() {
		return protocolParticipantIdentifier;
	}

	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier) {
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public Collection<SpecimenCollectionGroup> getSpecimenCollectionGroupCollection() {
		return specimenCollectionGroupCollection;
	}

	public void setSpecimenCollectionGroupCollection(Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection) {
		this.specimenCollectionGroupCollection = specimenCollectionGroupCollection;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getSignedConsentDocumentURL() {
		return signedConsentDocumentURL;
	}

	public void setSignedConsentDocumentURL(String signedConsentDocumentURL) {
		this.signedConsentDocumentURL = signedConsentDocumentURL;
	}

	public Date getConsentSignatureDate() {
		return consentSignatureDate;
	}

	public void setConsentSignatureDate(Date consentSignatureDate) {
		this.consentSignatureDate = consentSignatureDate;
	}

	public User getConsentWitness() {
		return consentWitness;
	}

	public void setConsentWitness(User consentWitness) {
		this.consentWitness = consentWitness;
	}

	public Collection<ConsentTierResponse> getConsentTierResponseCollection() {
		return consentTierResponseCollection;
	}

	public void setConsentTierResponseCollection(Collection<ConsentTierResponse> consentTierResponseCollection) {
		this.consentTierResponseCollection = consentTierResponseCollection;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public void update(CollectionProtocolRegistration cpr) {
		// TODO: Auto-generated method stub
		
	}
	
	public void setActive()
	{
		this.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
	}

}
