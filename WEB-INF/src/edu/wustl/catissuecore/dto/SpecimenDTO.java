package edu.wustl.catissuecore.dto;

import java.util.Collection;
import java.util.Date;


public class SpecimenDTO 
{
	private String label;
	private Long id;
	private String className;
	private String type;
	private Boolean isVirtual;
	private String generateLabel;
	private String operation;
	private Long parentSpecimenId;
	private Long requirementId;
	private Long specimenCollectionGroupId;
	private String specimenCollectionGroupName;
	private String parentSpecimenName;
	private String lineage;
	private String barcode;
	private String tissueSite;
	private String tissueSide;
	private String pathologicalStatus;
	private Date createdDate;
	private Double quantity;
	private Double concentration;
	private Boolean available;
	private Double availableQuantity;
	private String collectionStatus; 
	private String activityStatus;
	private String comments;
	private String containerName;
	private Long containerId;
	private String pos1;
	private String pos2;
	private Collection<ExternalIdentifierDTO> externalIdentifiers;
	private Collection<BiohazardDTO> bioHazards;
	private String biohazardType;
	private boolean isToPrintLabel;
	private Long userId;
	private Date disposalDate;
	private String disposalHours;
	private String disposalMins;
	
	public boolean isToPrintLabel()
	{
		return isToPrintLabel;
	}
	
	public void setToPrintLabel(boolean isToPrintLabel)
	{
		this.isToPrintLabel = isToPrintLabel;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Boolean getIsVirtual() {
		return isVirtual;
	}
	public void setIsVirtual(Boolean isVirtual) {
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
	public Long getParentSpecimenId() {
		return parentSpecimenId;
	}
	public void setParentSpecimenId(Long parentSpecimenId) {
		this.parentSpecimenId = parentSpecimenId;
	}
	public Long getSpecimenCollectionGroupId() {
		return specimenCollectionGroupId;
	}
	public void setSpecimenCollectionGroupId(Long specimenCollectionGroupId) {
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getConcentration() {
		return concentration;
	}
	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}
	public Boolean isAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
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
	public Long getContainerId() {
		return containerId;
	}
	public void setContainerId(Long containerId) {
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

	public Collection<ExternalIdentifierDTO> getExternalIdentifiers()
	{
		return externalIdentifiers;
	}

	public void setExternalIdentifiers(Collection<ExternalIdentifierDTO> externalIdentifiers)
	{
		this.externalIdentifiers = externalIdentifiers;
	}

	public Collection<BiohazardDTO> getBioHazards()
	{
		return bioHazards;
	}

	public void setBioHazards(Collection<BiohazardDTO> bioHazards)
	{
		this.bioHazards = bioHazards;
	}
	
	/**
	 * @return the biohazardType
	 */
	public String getBiohazardType()
	{
		return biohazardType;
	}

	/**
	 * @param biohazardType the biohazardType to set
	 */
	public void setBiohazardType(String biohazardType)
	{
		this.biohazardType = biohazardType;
	}

	
	public Long getRequirementId() {
		return requirementId;
	}
	
	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Date getDisposalDate() {
		return disposalDate;
	}

	public void setDisposalDate(Date disposalDate) {
		this.disposalDate = disposalDate;
	}
	
	public String getDisposalHours() {
		return disposalHours;
	}

	public void setDisposalHours(String disposalHours) {
		this.disposalHours = disposalHours;
	}

	public String getDisposalMins() {
		return disposalMins;
	}
	
	public void setDisposalMins(String disposalMins) {
		this.disposalMins = disposalMins;
	}
}
 