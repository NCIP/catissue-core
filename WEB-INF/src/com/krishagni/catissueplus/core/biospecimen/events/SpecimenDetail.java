
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.domain.ExternalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenPosition;

public class SpecimenDetail {
	private Long id;

	private String anatomicSite;

	private String laterality;

	private String pathologicalStatus;

	private String lineage;

	private Double initialQuantity;

	private String specimenClass;

	private String type;

	private Double concentration;

	private String label;

	private String activityStatus;

	private Boolean isAvailable;

	private String barcode;

	private String comment;

	private Date createdOn;

	private Double availableQuantity;

	private String collectionStatus;

	private Long visitId;
	
	private String visitName;

	private Long requirementId;

	private Long parentSpecimenId;
	
	private String parentSpecimenLabel;

	private String containerName;

	private Long containerId;

	private String pos1;

	private String pos2;

	private Boolean enablePrintLabels = false;
	
	private CollectionEventParametersDetail collectionEvent;
	
	private ReceivedEventParametersDetail receivedEvent;

	private List<ExternalIdentifierDetail> externalIdentifierDetails = new ArrayList<ExternalIdentifierDetail>();

	private List<BiohazardDetail> biohazardDetails = new ArrayList<BiohazardDetail>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnatomicSite() {
		return anatomicSite;
	}

	public void setAnatomicSite(String anatomicSite) {
		this.anatomicSite = anatomicSite;
	}

	public String getLaterality() {
		return laterality;
	}

	public void setLaterality(String laterality) {
		this.laterality = laterality;
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
		return type;
	}

	public void setSpecimenType(String specimenType) {
		this.type = specimenType;
	}

	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double concentration) {
		this.concentration = concentration;
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

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getVisitName() {
		return visitName;
	}

	public void setVisitName(String visitName) {
		this.visitName = visitName;
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

	public String getParentSpecimenLabel() {
		return parentSpecimenLabel;
	}

	public void setParentSpecimenLabel(String parentSpecimenLabel) {
		this.parentSpecimenLabel = parentSpecimenLabel;
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
	
	public Boolean isPrintLabelsEnabled() {
		return enablePrintLabels;
	}
	
	public void setEnablePrintLabels(Boolean enablePrintLabels) {
		this.enablePrintLabels = enablePrintLabels;
	}

	public CollectionEventParametersDetail getCollectionEvent() {
		return collectionEvent;
	}

	public void setCollectionEvent(CollectionEventParametersDetail collectionEvent) {
		this.collectionEvent = collectionEvent;
	}

	public ReceivedEventParametersDetail getReceivedEvent() {
		return receivedEvent;
	}

	public void setReceivedEvent(ReceivedEventParametersDetail receivedEvent) {
		this.receivedEvent = receivedEvent;
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

	public static SpecimenDetail from(Specimen specimen) {
		SpecimenDetail detail = new SpecimenDetail();
		detail.setActivityStatus(specimen.getActivityStatus());
		detail.setAvailableQuantity(specimen.getAvailableQuantity());
		detail.setBarcode(specimen.getBarcode());
		detail.setBiohazardDetails(getBiohazardDetail(specimen.getBiohazardCollection()));
		detail.setCollectionStatus(specimen.getCollectionStatus());
		detail.setComment(specimen.getComment());
		detail.setLineage(specimen.getLineage());
		detail.setConcentration(specimen.getConcentrationInMicrogramPerMicroliter());
		detail.setCreatedOn(specimen.getCreatedOn());
		detail.setExternalIdentifierDetails(getExternalIdentifierDetails(specimen.getExternalIdentifierCollection()));
		detail.setId(specimen.getId());
		detail.setInitialQuantity(specimen.getInitialQuantity());
		detail.setIsAvailable(specimen.getIsAvailable());
		detail.setLabel(specimen.getLabel());
		detail.setLineage(specimen.getLineage());
		detail.setPathologicalStatus(specimen.getPathologicalStatus());
		detail.setSpecimenClass(specimen.getSpecimenClass());
		detail.setSpecimenType(specimen.getSpecimenType());
		detail.setLaterality(specimen.getTissueSide());
		detail.setAnatomicSite(specimen.getTissueSite());

		if (specimen.getParentSpecimen() != null) {
			detail.setParentSpecimenId(specimen.getParentSpecimen().getId());
			detail.setParentSpecimenLabel(specimen.getParentSpecimen().getLabel());
		}
		
		if (specimen.getSpecimenCollectionGroup() != null) {
			detail.setVisitId(specimen.getSpecimenCollectionGroup().getId());
			detail.setVisitName(specimen.getSpecimenCollectionGroup().getName());
		}
		
		if (specimen.getSpecimenRequirement() != null) {
			detail.setRequirementId(specimen.getSpecimenRequirement().getId());
		}
		
		if (specimen.getSpecimenPosition() != null) {
			SpecimenPosition position = specimen.getSpecimenPosition(); 
			detail.setPos1(position.getPositionDimensionOneString());
			detail.setPos2(position.getPositionDimensionTwoString());
			
			if (position.getStorageContainer() != null) {
				StorageContainer container = position.getStorageContainer();
				detail.setContainerName(container.getName());
				detail.setContainerId(container.getId());
			}
		}
		return detail;
	}
	
	public static List<SpecimenDetail> fromSpecimens(List<Specimen> specimens) {
		List<SpecimenDetail> specimenDetails = new ArrayList<SpecimenDetail>();
		
		for (Specimen s : specimens) {
			specimenDetails.add(from(s));
		}
		
		return specimenDetails;
	}

	private static List<ExternalIdentifierDetail> getExternalIdentifierDetails(
			Set<ExternalIdentifier> externalIdentifierCollection) {
		List<ExternalIdentifierDetail> extIdentifiers = new ArrayList<ExternalIdentifierDetail>();
		for (ExternalIdentifier extIdentifier : externalIdentifierCollection) {
			ExternalIdentifierDetail detail = new ExternalIdentifierDetail();
			detail.setId(extIdentifier.getId());
			detail.setName(extIdentifier.getName());
			detail.setValue(extIdentifier.getValue());
			extIdentifiers.add(detail);
		}
		return extIdentifiers;
	}

	private static List<BiohazardDetail> getBiohazardDetail(Set<Biohazard> biohazardCollection) {
		List<BiohazardDetail> biohazards = new ArrayList<BiohazardDetail>();

		for (Biohazard biohazard : biohazardCollection) {
			BiohazardDetail detail = new BiohazardDetail();
			detail.setComment(biohazard.getComment());
			detail.setId(biohazard.getId());
			detail.setName(biohazard.getName());
			detail.setType(biohazard.getType());
			biohazards.add(detail);
		}
		return biohazards;
	}
}
