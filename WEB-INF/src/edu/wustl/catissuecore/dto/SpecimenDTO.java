package edu.wustl.catissuecore.dto;

import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;


public class SpecimenDTO 
{
	private String label;
	private long id;
	private String className;
	private String type;
	private boolean isVirtual;
	private String generateLabel;
	private String operation;
	private long parentSpecimenId;
	private long specimenCollectionGroupId;
	private String specimenCollectionGroupName;
	private String parentSpecimenName;
	private String lineage;
	private String barcode;
	private String tissueSite;
	private String tissueSide;
	private String pathologicalStatus;
	private String createdDate;
	private Double quantity;
	private String concentration;
	private boolean available;
	private Double availableQuantity;
	private String collectionStatus; 
	private String activityStatus;
	private String comments;
	private String containerName;
	private long containerId;
	private String pos1;
	private String pos2;
	private Collection<ExternalIdentifier> externalIdentifiers;
	private Collection<Biohazard> bioHazards;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean getIsVirtual() {
		return isVirtual;
	}
	public void setIsVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
	public String getGenerateLabel() {
		return generateLabel;
	}
	public void setGenerateLabel(String generateLabel) {
		this.generateLabel = generateLabel;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public long getParentSpecimenId() {
		return parentSpecimenId;
	}
	public void setParentSpecimenId(long parentSpecimenId) {
		this.parentSpecimenId = parentSpecimenId;
	}
	public long getSpecimenCollectionGroupId() {
		return specimenCollectionGroupId;
	}
	public void setSpecimenCollectionGroupId(long specimenCollectionGroupId) {
		this.specimenCollectionGroupId = specimenCollectionGroupId;
	}
	public String getSpecimenCollectionGroupName() {
		return specimenCollectionGroupName;
	}
	public void setSpecimenCollectionGroupName(String specimenCollectionGroupName) {
		this.specimenCollectionGroupName = specimenCollectionGroupName;
	}
	public String getParentSpecimenName() {
		return parentSpecimenName;
	}
	public void setParentSpecimenName(String parentSpecimenName) {
		this.parentSpecimenName = parentSpecimenName;
	}
	public String getLineage() {
		return lineage;
	}
	public void setLineage(String lineage) {
		this.lineage = lineage;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getConcentration() {
		return concentration;
	}
	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
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
	public String getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public long getContainerId() {
		return containerId;
	}
	public void setContainerId(long containerId) {
		this.containerId = containerId;
	}
	public String getPos1() {
		return pos1;
	}
	public void setPos1(String pos1) {
		this.pos1 = pos1;
	}
	public String getPos2() {
		return pos2;
	}
	public void setPos2(String pos2) {
		this.pos2 = pos2;
	}
	public Collection<ExternalIdentifier> getExternalIdentifiers() {
		return externalIdentifiers;
	}
	public void setExternalIdentifiers(Collection<ExternalIdentifier> externalIdentifiers) {
		this.externalIdentifiers = externalIdentifiers;
	}
	public Collection<Biohazard> getBioHazards() {
		return bioHazards;
	}
	public void setBioHazards(Collection<Biohazard> bioHazards) {
		this.bioHazards = bioHazards;
	}
	
}
 