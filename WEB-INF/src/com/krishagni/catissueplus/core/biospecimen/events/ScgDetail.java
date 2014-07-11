
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;

public class ScgDetail {

	private Long cprId;

	private Long cpeId;

	private Long id;

	private String name;

	
	private String clinicalDiagnosis;
	private String clinicalStatus;

	private String activityStatus;

	private String collectionSiteName;

	private String collectionStatus;

	private String barcode;

	private String comment;

	private String surgicalPathologyNumber;

	private String collectorName;

	private Date collectionTimestamp;

	private String collectionComments;

	private String collectionProcedure;

	private String collectionContainer;

	private String receiverName;

	private Date receivedTimestamp;

	private String receivedComments;

	private String receivedQuality;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getCpeId() {
		return cpeId;
	}

	public void setCpeId(Long cpeId) {
		this.cpeId = cpeId;
	}

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

	public String getCollectionSiteName() {
		return collectionSiteName;
	}

	public void setCollectionSiteName(String collectionSiteName) {
		this.collectionSiteName = collectionSiteName;
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

	public String getSurgicalPathologyNumber() {
		return surgicalPathologyNumber;
	}

	public void setSurgicalPathologyNumber(String surgicalPathologyNumber) {
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	public String getCollectorName() {
		return collectorName;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
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

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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

	public static ScgDetail fromDomain(SpecimenCollectionGroup scg) {
		ScgDetail detail = new ScgDetail();
		detail.setActivityStatus(scg.getActivityStatus());
		detail.setBarcode(scg.getBarcode());
		detail.setClinicalDiagnosis(scg.getClinicalDiagnosis());
		detail.setClinicalStatus(scg.getClinicalStatus());
		detail.setCollectionComments(scg.getCollectionComments());
		detail.setCollectionContainer(scg.getCollectionContainer());
		detail.setCollectionProcedure(scg.getCollectionProcedure());
		detail.setCollectionSiteName(scg.getCollectionSite().getName());
		detail.setCollectionStatus(scg.getCollectionStatus());
		detail.setCollectionTimestamp(scg.getCollectionTimestamp());
		detail.setCollectorName(scg.getCollector().getLastName() + ", " + scg.getCollector().getFirstName());
		detail.setComment(scg.getComment());
		detail.setCpeId(scg.getCollectionProtocolEvent().getId());
		detail.setCprId(scg.getCollectionProtocolRegistration().getId());
		detail.setId(scg.getId());
		detail.setName(scg.getName());
		detail.setReceivedComments(scg.getReceivedComments());
		detail.setReceivedQuality(scg.getReceivedQuality());
		detail.setReceivedTimestamp(scg.getReceivedTimestamp());
		detail.setReceiverName(scg.getReceiver().getLastName() + ", " + scg.getReceiver().getFirstName());
		detail.setSurgicalPathologyNumber(scg.getSurgicalPathologyNumber());
		return detail;
	}

}
