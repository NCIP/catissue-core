package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;

public class SpecimenCollectionGroupViewForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idValue;
	
	private String participantId;
	
	private String cpId;
	
	private String clickedNodeId;
	
	private String evtDate;
	
	private String studyCalendarEventPoint;
	
	private String collectionSite;
	
	private String clinicalDignosis;
	
	private String clinicalStatus;
	
	private String collectionStatus;
	
	private String activityStatus;
	
	private String surgicalPathologyNumber;
	
	private String collectedDate;
	
	private String receivedDate;
	
	private String collectedProcedure;
	
	private String receivedQuality;
	
	private String collectedContainer;
	
	private String specimenGroupName;
	
	private String barcode;
	
	public String getCollectedDate() {
		return collectedDate;
	}

	public void setCollectedDate(String collectedDate) {
		this.collectedDate = collectedDate;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getCollectedProcedure() {
		return collectedProcedure;
	}

	public void setCollectedProcedure(String collectedProcedure) {
		this.collectedProcedure = collectedProcedure;
	}

	public String getCollectedContainer() {
		return collectedContainer;
	}

	public void setCollectedContainer(String collectedContainer) {
		this.collectedContainer = collectedContainer;
	}

	public String getReceivedQuality() {
		return receivedQuality;
	}

	public void setReceivedQuality(String receivedQuality) {
		this.receivedQuality = receivedQuality;
	}

	
	public String getSpecimenGroupName() {
		return specimenGroupName;
	}

	public void setSpecimenGroupName(String specimenGroupName) {
		this.specimenGroupName = specimenGroupName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getStudyCalendarEventPoint() {
		return studyCalendarEventPoint;
	}

	public void setStudyCalendarEventPoint(String studyCalendarEventPoint) {
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	public String getCollectionSite() {
		return collectionSite;
	}

	public void setCollectionSite(String collectionSite) {
		this.collectionSite = collectionSite;
	}

	public String getClinicalDignosis() {
		return clinicalDignosis;
	}

	public void setClinicalDignosis(String clinicalDignosis) {
		this.clinicalDignosis = clinicalDignosis;
	}

	public String getSurgicalPathologyNumber() {
		return surgicalPathologyNumber;
	}

	public void setSurgicalPathologyNumber(String surgicalPathologyNumber) {
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getClickedNodeId() {
		return clickedNodeId;
	}

	public void setClickedNodeId(String clickedNodeId) {
		this.clickedNodeId = clickedNodeId;
	}

	public String getEvtDate() {
		return evtDate;
	}

	public void setEvtDate(String evtDate) {
		this.evtDate = evtDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
}
