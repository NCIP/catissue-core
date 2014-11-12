package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class SpecimenRequirement {
	private Long id;
	
	private Date collectionTimestamp;

	private User collector;

	private String collectionComments;

	private String collectionProcedure;

	private String collectionContainer;

	private String receivedQuality;

	private Date receivedTimestamp;
 
	private User receiver;

	private String receivedComments;

	private String storageType;

	private CollectionProtocolEvent collectionProtocolEvent;

	private String labelFormat;

	private String specimenRequirementLabel;

	private String activityStatus;
	
	private Double initialQuantity;
	
	private String lineage;
	
	private String pathologicalStatus;
	
	private String tissueSite;

	private String tissueSide;
	
	private String specimenClass;

	private String specimenType;

	private Set<Specimen> specimens = new HashSet<Specimen>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCollectionTimestamp() {
		return collectionTimestamp;
	}

	public void setCollectionTimestamp(Date collectionTimestamp) {
		this.collectionTimestamp = collectionTimestamp;
	}

	public User getCollector() {
		return collector;
	}

	public void setCollector(User collector) {
		this.collector = collector;
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

	public String getReceivedQuality() {
		return receivedQuality;
	}

	public void setReceivedQuality(String receivedQuality) {
		this.receivedQuality = receivedQuality;
	}

	public Date getReceivedTimestamp() {
		return receivedTimestamp;
	}

	public void setReceivedTimestamp(Date receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getReceivedComments() {
		return receivedComments;
	}

	public void setReceivedComments(String receivedComments) {
		this.receivedComments = receivedComments;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public CollectionProtocolEvent getCollectionProtocolEvent() {
		return collectionProtocolEvent;
	}

	public void setCollectionProtocolEvent(
			CollectionProtocolEvent collectionProtocolEvent) {
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	public String getLabelFormat() {
		return labelFormat;
	}

	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}

	public String getSpecimenRequirementLabel() {
		return specimenRequirementLabel;
	}

	public void setSpecimenRequirementLabel(String specimenRequirementLabel) {
		this.specimenRequirementLabel = specimenRequirementLabel;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Double getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(Double initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public String getPathologicalStatus() {
		return pathologicalStatus;
	}

	public void setPathologicalStatus(String pathologicalStatus) {
		this.pathologicalStatus = pathologicalStatus;
	}

	public String getTissueSite() {
		return tissueSite;
	}

	public void setTissueSite(String tissueSite) {
		this.tissueSite = tissueSite;
	}

	public String getTissueSide() {
		return tissueSide;
	}

	public void setTissueSide(String tissueSide) {
		this.tissueSide = tissueSide;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
	}
	
	
}
