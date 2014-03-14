
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;

public class SpecimenCollectionGroup {

	private final String ACTIVITY_STATUS_ACTIVE = "Active";

	private final String ACTIVITY_STATUS_DISABLED = "Disabled";

	private final String COLLECTION_STATUS_PENDING = "Pending";

	private Long id;

	private String name;

	private String clinicalDiagnosis;

	private String clinicalStatus;

	private String activityStatus;

	private Site collectionSite;

	private String collectionStatus;

	private String barcode;

	private String comment;

	private Date encounterTimestamp;

	private String surgicalPathologyNumber;

	private User collector;

	private Date collectionTimestamp;

	private String collectionComments;

	private String collectionProcedure;

	private String collectionContainer;

	private User receiver;

	private Date receivedTimestamp;

	private String receivedComments;

	private String receivedQuality;

	private CollectionProtocolEvent collectionProtocolEvent;

	private Set<Specimen> specimenCollection = new HashSet<Specimen>();

	private CollectionProtocolRegistration collectionProtocolRegistration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Site getCollectionSite() {
		return collectionSite;
	}

	public void setCollectionSite(Site collectionSite) {
		this.collectionSite = collectionSite;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getEncounterTimestamp() {
		return encounterTimestamp;
	}

	public void setEncounterTimestamp(Date encounterTimestamp) {
		this.encounterTimestamp = encounterTimestamp;
	}

	public String getSurgicalPathologyNumber() {
		return surgicalPathologyNumber;
	}

	public void setSurgicalPathologyNumber(String surgicalPathologyNumber) {
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	public User getCollector() {
		return collector;
	}

	public void setCollector(User collector) {
		this.collector = collector;
	}

	public Date getCollectionTimestamp() {
		return collectionTimestamp;
	}

	public void setCollectionTimestamp(Date collectionTimestamp) {
		this.collectionTimestamp = collectionTimestamp;
	}

	public String getCollectionComments() {
		return collectionComments;
	}

	public void setCollectionComments(String collectionComments) {
		this.collectionComments = collectionComments;
	}

	public String getCollectionProcedure() {
		return collectionProcedure;
	}

	public void setCollectionProcedure(String collectionProcedure) {
		this.collectionProcedure = collectionProcedure;
	}

	public String getCollectionContainer() {
		return collectionContainer;
	}

	public void setCollectionContainer(String collectionContainer) {
		this.collectionContainer = collectionContainer;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Date getReceivedTimestamp() {
		return receivedTimestamp;
	}

	public void setReceivedTimestamp(Date receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
	}

	public String getReceivedComments() {
		return receivedComments;
	}

	public void setReceivedComments(String receivedComments) {
		this.receivedComments = receivedComments;
	}

	public String getReceivedQuality() {
		return receivedQuality;
	}

	public void setReceivedQuality(String receivedQuality) {
		this.receivedQuality = receivedQuality;
	}

	public CollectionProtocolEvent getCollectionProtocolEvent() {
		return collectionProtocolEvent;
	}

	public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent) {
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	public Set<Specimen> getSpecimenCollection() {
		return specimenCollection;
	}

	public void setSpecimenCollection(Set<Specimen> specimenCollection) {
		this.specimenCollection = specimenCollection;
	}

	public CollectionProtocolRegistration getCollectionProtocolRegistration() {
		return collectionProtocolRegistration;
	}

	public void setCollectionProtocolRegistration(CollectionProtocolRegistration collectionProtocolRegistration) {
		this.collectionProtocolRegistration = collectionProtocolRegistration;
	}

	public void setActive() {
		this.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
	}

	public void setCollectionStatusPending() {
		this.setCollectionStatus(COLLECTION_STATUS_PENDING);
	}

	public boolean isActive() {
		return ACTIVITY_STATUS_ACTIVE.equals(this.getActivityStatus());
	}

	public void delete(boolean isIncludeChildren) {
		if (isIncludeChildren) {
			for (Specimen specimen : this.getSpecimenCollection()) {
				specimen.delete(isIncludeChildren);
			}
		}
		else {
			checkActiveDependents();
		}
		this.setActivityStatus(ACTIVITY_STATUS_DISABLED);
	}

	private void checkActiveDependents() {
		for (Specimen specimen : this.getSpecimenCollection()) {
			if (specimen.isActive()) {
				throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
			}
		}
	}

}
