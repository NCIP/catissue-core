package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;

public class NewSpecimenViewForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String idValue;
	
	private String participantId;
	
	private String cpId;
	
	private String specimenCollectionGroup;
	
	private String lineage;
	
	private String collectionStatus;
	
	private String createdDate;
	
	private String classValue;
	
	private String typeValue;
	
	private String tissueSite;
	
	private String tissueSide;
	
	private String pathologicalStatus;
	
	private String initialQuantity;
	
	private String availableQuantity;
	
	private String concentrationValue;
	
	private boolean isAvailableValue;
	
	private String storagePosition;
	
	private String label;
	
	private String barcode;
	
	private String specimenEvents;
	
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

	public String getSpecimenCollectionGroup() {
		return specimenCollectionGroup;
	}

	public void setSpecimenCollectionGroup(String specimenCollectionGroup) {
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public String getClassValue() {
		return classValue;
	}

	public void setClassValue(String classValue) {
		this.classValue = classValue;
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
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

	public String getPathologicalStatus() {
		return pathologicalStatus;
	}

	public void setPathologicalStatus(String pathologicalStatus) {
		this.pathologicalStatus = pathologicalStatus;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(String initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public String getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(String availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getConcentrationValue() {
		return concentrationValue;
	}

	public void setConcentrationValue(String concentrationValue) {
		this.concentrationValue = concentrationValue;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public String getStoragePosition() {
		return storagePosition;
	}

	public void setStoragePosition(String storagePosition) {
		this.storagePosition = storagePosition;
	}

	public boolean getIsAvailableValue() {
		return isAvailableValue;
	}

	public void setIsAvailableValue(boolean isAvailableValue) {
		this.isAvailableValue = isAvailableValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSpecimenEvents() {
		return specimenEvents;
	}

	public void setSpecimenEvents(String specimenEvents) {
		this.specimenEvents = specimenEvents;
	}
	
}
