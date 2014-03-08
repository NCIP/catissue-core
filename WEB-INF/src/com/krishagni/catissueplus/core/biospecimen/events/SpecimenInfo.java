
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;

public class SpecimenInfo {

	private Long id;

	private String label;

	private String requirementLabel;

	private String specimenType;

	private String specimenClass;

	private String collectionContainer;

	private String collectionStatus;
	
	private List<SpecimenInfo> children = new ArrayList<SpecimenInfo>();

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

	
	public List<SpecimenInfo> getChildren() {
		return children;
	}
	
	public void addChildren(SpecimenInfo specimenInfo) {
		this.children.add(specimenInfo);
	}

	public void setChildren(List<SpecimenInfo> specimenInfoList) {
		this.children = specimenInfoList;
	}
	
	public static SpecimenInfo from(Specimen specimen) {
		SpecimenInfo specimenInfo = new SpecimenInfo();
		specimenInfo.setId(specimen.getId());
		specimenInfo.setLabel(specimen.getLabel());
		specimenInfo.setSpecimenClass(specimen.getSpecimenClass());
		specimenInfo.setSpecimenType(specimen.getSpecimenType());
		specimenInfo.setCollectionStatus(specimen.getCollectionStatus());
		
		specimenInfo.setRequirementLabel(specimen.getSpecimenRequirement().getLabel());
		specimenInfo.setCollectionContainer(specimen.getSpecimenRequirement().getCollectionContainer());
		return specimenInfo;
	}
}
