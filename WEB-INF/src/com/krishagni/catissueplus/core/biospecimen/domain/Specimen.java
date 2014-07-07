
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

public class Specimen {

	private Long id;

	private String tissueSite;

	private String tissueSide;

	private String pathologicalStatus;

	private String lineage;

	private Double initialQuantity;

	private String specimenClass;

	private String specimenType;

	private Double concentrationInMicrogramPerMicroliter;

	private String label;

	private String activityStatus;

	private Boolean isAvailable;

	private String barcode;

	private String comment;

	private Date createdOn;

	private Double availableQuantity;

	private String collectionStatus;

	private SpecimenCollectionGroup specimenCollectionGroup;

	private SpecimenRequirement specimenRequirement;

	private SpecimenPosition specimenPosition;

	private Specimen parentSpecimen;

	private Set<SpecimenEventParameters> eventCollection = new HashSet<SpecimenEventParameters>();

	private Set<Specimen> childCollection = new HashSet<Specimen>();

	private Set<Biohazard> biohazardCollection = new HashSet<Biohazard>();

	private Set<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public Double getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(Double initialQuantity) {
		this.initialQuantity = initialQuantity;
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

	public Double getConcentrationInMicrogramPerMicroliter() {
		return concentrationInMicrogramPerMicroliter;
	}

	public void setConcentrationInMicrogramPerMicroliter(Double concentrationInMicrogramPerMicroliter) {
		this.concentrationInMicrogramPerMicroliter = concentrationInMicrogramPerMicroliter;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus)) {
			delete(false);
		}
		this.activityStatus = activityStatus;
	}

	//	public void setActivityStatus(String activityStatus,boolean isToIncludeChildren) {
	//		if(Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus))
	//		{
	//			delete(isToIncludeChildren);
	//		}
	//		this.activityStatus = activityStatus;
	//	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Double getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Double availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public SpecimenCollectionGroup getSpecimenCollectionGroup() {
		return specimenCollectionGroup;
	}

	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup) {
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	public SpecimenRequirement getSpecimenRequirement() {
		return specimenRequirement;
	}

	public void setSpecimenRequirement(SpecimenRequirement specimenRequirement) {
		this.specimenRequirement = specimenRequirement;
	}

	public SpecimenPosition getSpecimenPosition() {
		return specimenPosition;
	}

	public void setSpecimenPosition(SpecimenPosition specimenPosition) {
		this.specimenPosition = specimenPosition;
	}

	public Specimen getParentSpecimen() {
		return parentSpecimen;
	}

	public void setParentSpecimen(Specimen parentSpecimen) {
		this.parentSpecimen = parentSpecimen;
	}

	public Set<SpecimenEventParameters> getEventCollection() {
		return eventCollection;
	}

	public void setEventCollection(Set<SpecimenEventParameters> specimenEventCollection) {
		this.eventCollection = specimenEventCollection;
	}

	public Set<Specimen> getChildCollection() {
		return childCollection;
	}

	public void setChildCollection(Set<Specimen> childSpecimenCollection) {
		this.childCollection = childSpecimenCollection;
	}

	public Set<Biohazard> getBiohazardCollection() {
		return biohazardCollection;
	}

	public void setBiohazardCollection(Set<Biohazard> biohazardCollection) {
		this.biohazardCollection = biohazardCollection;
	}

	public Set<ExternalIdentifier> getExternalIdentifierCollection() {
		return externalIdentifierCollection;
	}

	public void setExternalIdentifierCollection(Set<ExternalIdentifier> externalIdentifierCollection) {
		this.externalIdentifierCollection = externalIdentifierCollection;
	}

	public void setActive() {
		setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public boolean isActive() {
		return Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(this.getActivityStatus());
	}

	public boolean isCollected() {
		return Status.SPECIMEN_COLLECTION_STATUS_COLLECTED.getStatus().equals(this.collectionStatus);
	}

	public void delete(boolean isIncludeChildren) {
		if (isIncludeChildren) {
			for (Specimen specimen : this.getChildCollection()) {
				specimen.delete(isIncludeChildren);
			}
		}
		else {
			checkActiveDependents();
		}
		if (this.specimenPosition != null) {
			//			this.specimenPosition.setSpecimen(null);
			this.setSpecimenPosition(null);
		}

		this.barcode = Utility.getDisabledValue(barcode);
		this.label = Utility.getDisabledValue(label);
		this.activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
	}

	private void checkActiveDependents() {
		for (Specimen specimen : this.getChildCollection()) {
			if (specimen.isActive()) {
				throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
			}
		}
	}

	public void update(Specimen specimen) {

	}

	
	@Override
	public int hashCode() {
		return 31 * 1 + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Specimen other = (Specimen) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
