
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collection;
import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.util.Utility;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;

public class CollectionProtocolRegistration {

	private final String ACTIVITY_STATUS_ACTIVE = "Active";

	private final String ACTIVITY_STATUS_DISABLED = "Disabled";

	private Long id;

	private String protocolParticipantIdentifier;

	private Date registrationDate;

	private Participant participant;

	private CollectionProtocol collectionProtocol;

	private Collection<SpecimenCollectionGroup> scgCollection;

	private String activityStatus;

	private String signedConsentDocumentURL;

	private Date consentSignatureDate;

	private User consentWitness;

	private Collection<ConsentTierResponse> consentResponseCollection;

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

	public Collection<SpecimenCollectionGroup> getScgCollection() {
		return scgCollection;
	}

	public void setScgCollection(Collection<SpecimenCollectionGroup> scgCollection) {
		this.scgCollection = scgCollection;
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

	public Collection<ConsentTierResponse> getConsentResponseCollection() {
		return consentResponseCollection;
	}

	public void setConsentResponseCollection(Collection<ConsentTierResponse> consentTierResponseCollection) {
		this.consentResponseCollection = consentTierResponseCollection;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public boolean isActive() {
		return ACTIVITY_STATUS_ACTIVE.equals(this.getActivityStatus());
	}

	public void setActive() {
		this.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
	}

	public void delete(boolean isIncludeChildren) {
		if (isIncludeChildren) {
			for (SpecimenCollectionGroup scg : this.getScgCollection()) {
				scg.delete(isIncludeChildren);
			}
		}
		else {
			checkActiveDependents();
		}
		this.setBarcode(Utility.getDisabledValue(this.getBarcode()));
		this.setProtocolParticipantIdentifier(Utility.getDisabledValue(protocolParticipantIdentifier));
		this.setActivityStatus(ACTIVITY_STATUS_DISABLED);
	}

	private void checkActiveDependents() {
		for (SpecimenCollectionGroup scg : this.getScgCollection()) {
			if (scg.isActive()) {
				throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
			}
		}
	}

}
