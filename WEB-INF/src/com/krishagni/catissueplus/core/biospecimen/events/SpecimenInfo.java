
package com.krishagni.catissueplus.core.biospecimen.events;

public class SpecimenInfo {

	private Long id;

	private String label;

	private String requirementLabel;

	private String specimenType;

	private String specimenClass;

	private String collectionContainer;

	private String collectionStatus;

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRequirementLabel() {
		return requirementLabel;
	}

	public void setRequirementLabel(String requirementLabel) {
		this.requirementLabel = requirementLabel;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getCollectionContainer() {
		return collectionContainer;
	}

	public void setCollectionContainer(String collectionContainer) {
		this.collectionContainer = collectionContainer;
	}

}
