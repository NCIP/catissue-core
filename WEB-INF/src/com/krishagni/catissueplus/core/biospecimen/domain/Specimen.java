
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class Specimen extends BaseEntity {
	public static final String NEW = "New";
	
	public static final String ALIQUOT = "Aliquot";
	
	public static final String DERIVED = "Derived";
	
	public static final String COLLECTED = "Collected";
	
	public static final String NOT_COLLECTED = "Not Collected";
	
	public static final String PENDING = "Pending";

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

	private Visit visit;

	private SpecimenRequirement specimenRequirement;

	private SpecimenPosition specimenPosition;

	private Specimen parentSpecimen;

	private Set<Specimen> childCollection = new HashSet<Specimen>();

	private Set<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();

	public String getTissueSite() {
		return tissueSite;
	}

	public void setTissueSite(String tissueSite) {
		if (StringUtils.isNotBlank(this.tissueSite) && !this.tissueSite.equals(tissueSite)) {
			for (Specimen child : getChildCollection()) {
				child.setTissueSite(tissueSite);
			}
		}
		
		this.tissueSite = tissueSite;
	}

	public String getTissueSide() {
		return tissueSide;
	}

	public void setTissueSide(String tissueSide) {
		if (StringUtils.isNotBlank(this.tissueSide) && !this.tissueSide.equals(tissueSide)) {
			for (Specimen child : getChildCollection()) {
				child.setTissueSide(tissueSide);
			}
		}
		
		this.tissueSide = tissueSide;
	}

	public String getPathologicalStatus() {
		return pathologicalStatus;
	}

	public void setPathologicalStatus(String pathologicalStatus) {
		if (StringUtils.isNotBlank(this.pathologicalStatus) && !this.pathologicalStatus.equals(pathologicalStatus)) {
			for (Specimen child : getChildCollection()) {
				child.setPathologicalStatus(pathologicalStatus);
			}
		}
				
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
		if (StringUtils.isNotBlank(this.specimenClass) && !this.specimenClass.equals(specimenClass)) {
			for (Specimen child : getChildCollection()) {
				if (child.isAliquot()) {
					child.setSpecimenClass(specimenClass);
				}				
			}
		}
		
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		if (StringUtils.isNotBlank(this.specimenType) && !this.specimenType.equals(specimenType)) {
			for (Specimen child : getChildCollection()) {
				if (child.isAliquot()) {
					child.setSpecimenType(specimenType);
				}				
			}
		}
				
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
		if (this.activityStatus != null && this.activityStatus.equals(activityStatus)) {
			return;
		}
		
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (this.activityStatus != null && Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus)) {
			delete();
		}		

		this.activityStatus = activityStatus;
	}

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
		if (this.collectionStatus != null && !this.collectionStatus.equals(collectionStatus)) {
			if (!isCollected()) {
				// TODO: check whether this is distributed
				
				this.isAvailable = false;
				this.availableQuantity = 0.0d;
				this.specimenPosition = null;
				for (Specimen child : getChildCollection()) {
					child.setCollectionStatus(collectionStatus);
				}
			} else if (!getVisit().isCompleted()) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.VISIT_NOT_COMPLETED);
			} else {
				decAliquotedQtyFromParent(); 
			}
		}
		
		this.collectionStatus = collectionStatus;		
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
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

	public Set<Specimen> getChildCollection() {
		return childCollection;
	}

	public void setChildCollection(Set<Specimen> childSpecimenCollection) {
		this.childCollection = childSpecimenCollection;
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
	
	public boolean isAliquot() {
		return lineage.equals(ALIQUOT);
	}
	
	public boolean isDerivative() {
		return lineage.equals(DERIVED);
	}
	
	public boolean isCollected() {
		return Status.SPECIMEN_COLLECTION_STATUS_COLLECTED.getStatus().equals(this.collectionStatus);
	}

	public void delete() {
		checkActiveDependents();
		
		if (this.specimenPosition != null) {
			//this.specimenPosition.setSpecimen(null);
			this.setSpecimenPosition(null);
		}

		this.barcode = Utility.getDisabledValue(barcode);
		this.label = Utility.getDisabledValue(label);
		this.activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
	}

	private void checkActiveDependents() {
		for (Specimen specimen : this.getChildCollection()) {
			if (specimen.isActive()) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.REF_ENTITY_FOUND);
			}
		}
	}

	public void update(Specimen specimen) {
		setLabel(specimen.getLabel());
		setBarcode(specimen.getBarcode());
		setCollectionStatus(specimen.getCollectionStatus());

		if (isAliquot()) {
			setSpecimenClass(parentSpecimen.getSpecimenClass());
			setSpecimenType(parentSpecimen.getSpecimenType());
		} else {
			setSpecimenClass(specimen.getSpecimenClass());
			setSpecimenType(specimen.getSpecimenType());
		}
		
		if (parentSpecimen == null) {
			setTissueSite(specimen.getTissueSite());
			setTissueSide(specimen.getTissueSide());
			setPathologicalStatus(specimen.getPathologicalStatus());			
		} else {
			setTissueSite(parentSpecimen.getTissueSite());
			setTissueSide(parentSpecimen.getTissueSide());
			setPathologicalStatus(parentSpecimen.getPathologicalStatus());			
		}
		
		setInitialQuantity(specimen.getInitialQuantity());		
		setAvailableQuantity(specimen.getAvailableQuantity());
		setIsAvailable(specimen.getIsAvailable());
				
		setSpecimenPosition(specimen.getSpecimenPosition());				
		setComment(specimen.getComment());		
		setActivityStatus(specimen.getActivityStatus());
		
		checkQtyConstraints();
	}
	
	public void addSpecimen(Specimen specimen) {
		specimen.setParentSpecimen(this);
		
		if (specimen.getLineage().equals(DERIVED)) {
			childCollection.add(specimen);
			return;
		}
		
		double qty = getInitialQuantity();
		for (Specimen child : getChildCollection()) {
			if (child.getLineage().equals(ALIQUOT)) {
				qty -= child.getInitialQuantity();
			}
		}
		
		if (qty < specimen.getInitialQuantity()) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.INSUFFICIENT_QTY);
		}
		
		childCollection.add(specimen);
	}
	
	public CollectionProtocol getCollectionProtocol() {
		return visit.getCollectionProtocol();
	}
	
	public void setPending() {
		setCollectionStatus(PENDING);
	}
	
	public void checkQtyConstraints() {
		if (!isCollected()) { // No checks on un-collected specimens
			return;
		}
		
		ensureAliquotQtyOk(
				SpecimenErrorCode.INIT_QTY_LT_ALIQUOT_QTY, 
				SpecimenErrorCode.AVBL_QTY_GT_ACTUAL);
		
		if (isAliquot()) {
			//
			// Ensure initial quantity is less than parent specimen quantity
			//
			if (initialQuantity > parentSpecimen.getInitialQuantity()) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.ALIQUOT_QTY_GT_PARENT_QTY);
			}
			
			parentSpecimen.ensureAliquotQtyOk(
					SpecimenErrorCode.PARENT_INIT_QTY_LT_ALIQUOT_QTY, 
					SpecimenErrorCode.PARENT_AVBL_QTY_GT_ACTUAL);
		}
 
	}
	
	public void decAliquotedQtyFromParent() {
		if (isCollected() && isAliquot()) {
			parentSpecimen.setAvailableQuantity(parentSpecimen.getAvailableQuantity() - initialQuantity); 
		}
	}
			
	private double getAliquotQuantity() {
		double aliquotQty = 0.0;
		for (Specimen child : getChildCollection()) {
			if (child.isAliquot() && child.isCollected()) {
				aliquotQty += child.getInitialQuantity();
			}
		}
		
		return aliquotQty;		
	}
	
	private void ensureAliquotQtyOk(SpecimenErrorCode initGtAliquotQty, SpecimenErrorCode avblQtyGtAct) {		
		double initialQty = getInitialQuantity();
		double aliquotQty = getAliquotQuantity();
		
		if (initialQty < aliquotQty) {
			throw OpenSpecimenException.userError(initGtAliquotQty);
		}
		
		double actAvailableQty = initialQty - aliquotQty;
		if (getAvailableQuantity() > actAvailableQty) {
			throw OpenSpecimenException.userError(avblQtyGtAct);
		}
	}
}