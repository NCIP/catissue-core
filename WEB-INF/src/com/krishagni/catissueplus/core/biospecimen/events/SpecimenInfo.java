
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.SpecimenRequirement;

public class SpecimenInfo  implements Comparable<SpecimenInfo>{

	private Long id;

	private String label;

	private String requirementLabel;

	private String specimenType;

	private String specimenClass;

	private String collectionContainer;

	private String collectionStatus;

	private String instanceType;

	private Long requirementId;

	private Long scgId;

	private Long parentId;

	private List<SpecimenInfo> children = new ArrayList<SpecimenInfo>();

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String type) {
		this.instanceType = type;
	}

	public Long getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}

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

	public static SpecimenInfo fromSpecimen(Specimen specimen) {
		SpecimenInfo specimenInfo = new SpecimenInfo();
		specimenInfo.setId(specimen.getId());
		specimenInfo.setLabel(specimen.getLabel());
		specimenInfo.setSpecimenClass(specimen.getSpecimenClass());
		specimenInfo.setSpecimenType(specimen.getSpecimenType());
		specimenInfo.setCollectionStatus(specimen.getCollectionStatus());
		specimenInfo.setInstanceType("specimen");
		specimenInfo.setScgId(specimen.getSpecimenCollectionGroup().getId());
		if (specimen.getParentSpecimen() != null) {
			specimenInfo.setParentId(specimen.getParentSpecimen().getId());
		}

		if (specimen.getSpecimenRequirement() != null) {
			specimenInfo.setRequirementLabel(specimen.getSpecimenRequirement().getSpecimenRequirementLabel());
			specimenInfo.setCollectionContainer(specimen.getSpecimenRequirement().getCollectionContainer());
			specimenInfo.setRequirementId(specimen.getSpecimenRequirement().getId());
		}
		return specimenInfo;
	}

	public Long getScgId() {
		return scgId;
	}

	public void setScgId(Long scgId) {
		this.scgId = scgId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public static SpecimenInfo fromRequirement(SpecimenRequirement requirement) {
		SpecimenInfo specimenInfo = new SpecimenInfo();
		//		specimenInfo.setId(requirement.getId());
		specimenInfo.setRequirementId(requirement.getId());
		specimenInfo.setSpecimenClass(requirement.getSpecimenClass());
		specimenInfo.setSpecimenType(requirement.getSpecimenType());
		specimenInfo.setCollectionStatus(Status.SPECIMEN_COLLECTION_STATUS_PENDING.getStatus());
		specimenInfo.setRequirementLabel(requirement.getSpecimenRequirementLabel());
		specimenInfo.setCollectionContainer(requirement.getCollectionContainer());
		specimenInfo.setInstanceType("requirement");

		return specimenInfo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof SpecimenInfo) {
			SpecimenInfo specimenInfo = (SpecimenInfo) obj;
			if (specimenInfo.getId() == null && specimenInfo.getRequirementId() == null) {
				return false;
			}
			else if(this.requirementId == null && specimenInfo.getRequirementId() == null)
			{
				return false;
			}
			else if(this.requirementId !=null && specimenInfo.getRequirementId() != null)
			{
				return this.requirementId.equals(specimenInfo.getRequirementId());
			}
//			else if ((this.id + "_" + this.requirementId)
//					.equals(specimenInfo.getId() + "_" + specimenInfo.getRequirementId())) {
//				return true;
//			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requirementId == null) ? 0 : requirementId.hashCode());
		return result;
	}
	
	@Override
	public int compareTo(SpecimenInfo specimenInfo) {
		if (requirementId != null && specimenInfo.getRequirementId() == null) {
			return -1;
		} else if (requirementId == null && specimenInfo.getRequirementId() != null) {
			return 1;
		} else if (requirementId != null && specimenInfo.getRequirementId() != null) {
			return requirementId.compareTo(specimenInfo.getRequirementId());
		} else {
			return id.compareTo(specimenInfo.getId());
		}		
	}
}
