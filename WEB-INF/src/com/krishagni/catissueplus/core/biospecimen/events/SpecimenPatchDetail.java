
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

import edu.wustl.catissuecore.domain.SpecimenEventParameters;

public class SpecimenPatchDetail {

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

	private Long scgId;

	private Long requirementId;

	private Long parentSpecimenId;

	private String containerName;

	private Long containerId;

	private String pos1;

	private String pos2;

	private Boolean enablePrintLabels = false;

	private Set<SpecimenEventParameters> eventCollection = new HashSet<SpecimenEventParameters>();

	private List<ExternalIdentifierDetail> externalIdentifierDetails = new ArrayList<ExternalIdentifierDetail>();

	private List<BiohazardDetail> biohazardDetails = new ArrayList<BiohazardDetail>();

	private List<String> modifiedAttributes = new ArrayList<String>();

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

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
		this.collectionStatus = collectionStatus;
	}

	public Long getScgId() {
		return scgId;
	}

	public void setScgId(Long scgId) {
		this.scgId = scgId;
	}

	public Long getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}

	public Long getParentSpecimenId() {
		return parentSpecimenId;
	}

	public void setParentSpecimenId(Long parentSpecimenId) {
		this.parentSpecimenId = parentSpecimenId;
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

	public Set<SpecimenEventParameters> getEventCollection() {
		return eventCollection;
	}

	public void setEventCollection(Set<SpecimenEventParameters> eventCollection) {
		this.eventCollection = eventCollection;
	}

	public List<ExternalIdentifierDetail> getExternalIdentifierDetails() {
		return externalIdentifierDetails;
	}

	public void setExternalIdentifierDetails(List<ExternalIdentifierDetail> externalIdentifierDetails) {
		this.externalIdentifierDetails = externalIdentifierDetails;
	}

	public List<BiohazardDetail> getBiohazardDetails() {
		return biohazardDetails;
	}

	public void setBiohazardDetails(List<BiohazardDetail> biohazardDetails) {
		this.biohazardDetails = biohazardDetails;
	}

	public boolean isTissueSideModified() {
		return modifiedAttributes.contains("tissueSide");
	}

	public boolean isTissueSiteModified() {
		return modifiedAttributes.contains("tissueSite");
	}

	public boolean isPathologicalStatusModified() {
		return modifiedAttributes.contains("pathologicalStatus");
	}

	public boolean isLineageModified() {
		return modifiedAttributes.contains("lineage");
	}

	public boolean isInitialQuantityModified() {
		return modifiedAttributes.contains("initialQuantity");
	}

	public boolean isSpecimenClassModified() {
		return modifiedAttributes.contains("specimenClass");
	}

	public boolean isSpecimenTypeModified() {
		return modifiedAttributes.contains("specimenType");
	}

	public Boolean isPrintLabelsEnabled() {
		return enablePrintLabels;
	}

	public void setEnablePrintLabels(Boolean enablePrintLabels) {
		this.enablePrintLabels = enablePrintLabels;
	}

	public boolean isLabelModified() {
		return modifiedAttributes.contains("label");
	}

	public boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}

	public boolean isBarcodeModified() {
		return modifiedAttributes.contains("barcode");
	}

	public boolean isCreatedOnModified() {
		return modifiedAttributes.contains("createdOn");
	}

	public boolean isCommentModified() {
		return modifiedAttributes.contains("comment");
	}

	public boolean isCollectionStatusModified() {
		return modifiedAttributes.contains("collectionStatus");
	}

	public boolean isAvailableQuantityModified() {
		return modifiedAttributes.contains("availableQuantity");
	}

	public boolean isContainerNameModified() {
		return modifiedAttributes.contains("containerName");
	}

	public boolean isPos1Modified() {
		return modifiedAttributes.contains("pos1");
	}

	public boolean isPos2Modified() {
		return modifiedAttributes.contains("pos2");
	}

	public boolean isExternalIdentifierDetailsModified() {
		return modifiedAttributes.contains("externalIdentifierDetails");
	}

	public boolean isBiohazardDetailsModified() {
		return modifiedAttributes.contains("biohazardDetails");
	}

	public static SpecimenPatchDetail fromDomain(Specimen specimen) {
		SpecimenPatchDetail detail = new SpecimenPatchDetail();
		return detail;
	}
}
