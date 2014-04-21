
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

import edu.wustl.catissuecore.domain.CollectionProtocol;

public class CollectionProtocolRegistration {

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

	private Set<ConsentTierResponse> consentResponseCollection;

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
		if (Status.ACTIVITY_STATUS_DISABLED.equals(activityStatus)) {
			delete(false);
		}
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

	public Set<ConsentTierResponse> getConsentResponseCollection() {
		return consentResponseCollection;
	}

	public void setConsentResponseCollection(Set<ConsentTierResponse> consentTierResponseCollection) {
		this.consentResponseCollection = consentTierResponseCollection;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public boolean isActive() {
		return Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(this.getActivityStatus());
	}

	public void setActive() {
		setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public void delete(boolean isIncludeChildren) {
		if (isIncludeChildren) {
			for (SpecimenCollectionGroup scg : this.scgCollection) {
				scg.delete(isIncludeChildren);
			}
		}
		else {
			checkActiveDependents();
		}
		this.barcode = Utility.getDisabledValue(this.barcode);
		this.protocolParticipantIdentifier = Utility.getDisabledValue(this.protocolParticipantIdentifier);
		this.activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
	}

	private void checkActiveDependents() {
		for (SpecimenCollectionGroup scg : this.getScgCollection()) {
			if (scg.isActive()) {
				throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
			}
		}
	}

	public void update(CollectionProtocolRegistration cpr) {
		setProtocolParticipantIdentifier(cpr.getProtocolParticipantIdentifier());
		setRegistrationDate(cpr.getRegistrationDate());
		setActivityStatus(cpr.getActivityStatus());
		setSignedConsentDocumentURL(cpr.getSignedConsentDocumentURL());
		setConsentSignatureDate(cpr.getConsentSignatureDate());
		setConsentWitness(cpr.getConsentWitness());
		setBarcode(cpr.getBarcode());
		setconsents(cpr.getConsentResponseCollection());
	}

	private void setconsents(Set<ConsentTierResponse> consentResponseCollection) {
		SetUpdater.<ConsentTierResponse> newInstance().update(this.consentResponseCollection, consentResponseCollection);
	}

}
