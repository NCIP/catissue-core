
package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;

@ListenAttributeChanges
public class SpecimenInfo extends AttributeModifiedSupport implements Comparable<SpecimenInfo>, Serializable {
	
	private static final long serialVersionUID = -2766658206691562011L;

	private Long id;

	private Long cpId;

	private Long cprId;

	private String ppid;
	
	private Long eventId;
	
	private Long visitId;
	
	private String visitName;
	
	private String cpShortTitle;
	
	private Long reqId;
	
	private Integer sortOrder;
	
	private String label;
	
	private String barcode;

	private String type;
	
	private String specimenClass;
		
	private String lineage;

	private String anatomicSite;

	private String laterality;
	
	private String status;
	
	private String reqLabel;
	
	private String pathology;
	
	private BigDecimal initialQty;
	
	private BigDecimal availableQty;
	
	private BigDecimal concentration;
	
	private Long parentId;
	
	private String parentLabel;
	
	private StorageLocationSummary storageLocation;
	
	private String storageType;
	
	private String collectionContainer;

	private String storageSite;
	
	private String activityStatus;
	
	private Date createdOn;
	
	private String code;

	private String distributionStatus;
	
	private Integer freezeThawCycles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
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

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public Long getReqId() {
		return reqId;
	}

	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReqLabel() {
		return reqLabel;
	}

	public void setReqLabel(String reqLabel) {
		this.reqLabel = reqLabel;
	}

	public String getPathology() {
		return pathology;
	}

	public void setPathology(String pathology) {
		this.pathology = pathology;
	}

	public BigDecimal getInitialQty() {
		return initialQty;
	}

	public void setInitialQty(BigDecimal initialQty) {
		this.initialQty = initialQty;
	}

	public BigDecimal getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(BigDecimal availableQty) {
		this.availableQty = availableQty;
	}

	public BigDecimal getConcentration() {
		return concentration;
	}

	public void setConcentration(BigDecimal concentration) {
		this.concentration = concentration;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentLabel() {
		return parentLabel;
	}

	public void setParentLabel(String parentLabel) {
		this.parentLabel = parentLabel;
	}

	public StorageLocationSummary getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(StorageLocationSummary storageLocation) {
		this.storageLocation = storageLocation;
	}
	
	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getCollectionContainer() {
		return collectionContainer;
	}

	public void setCollectionContainer(String collectionContainer) {
		this.collectionContainer = collectionContainer;
	}

	public String getStorageSite() {
		return storageSite;
	}

	public void setStorageSite(String storageSite) {
		this.storageSite = storageSite;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDistributionStatus() {
		return distributionStatus;
	}

	public void setDistributionStatus(String distributionStatus) {
		this.distributionStatus = distributionStatus;
	}

	public Integer getFreezeThawCycles() {
		return freezeThawCycles;
	}

	public void setFreezeThawCycles(Integer freezeThawCycles) {
		this.freezeThawCycles = freezeThawCycles;
	}

	public static SpecimenInfo from(Specimen specimen) {
		return fromTo(specimen, new SpecimenInfo());
	}
	
	public static List<SpecimenInfo> from(List<Specimen> specimens) {
		return specimens.stream().map(SpecimenInfo::from).collect(Collectors.toList());
	}

	public static SpecimenInfo fromTo(Specimen specimen, SpecimenInfo result) {
		result.setId(specimen.getId());
		
		SpecimenRequirement sr= specimen.getSpecimenRequirement();
		result.setReqId(sr != null ? sr.getId() : null);
		result.setReqLabel(sr != null ? sr.getName() : null);
		result.setSortOrder(sr != null ? sr.getSortOrder() : null);
		result.setLabel(specimen.getLabel());
		result.setBarcode(specimen.getBarcode());
		result.setType(specimen.getSpecimenType());
		result.setSpecimenClass(specimen.getSpecimenClass());
		result.setLineage(specimen.getLineage());
		result.setAnatomicSite(specimen.getTissueSite());
		result.setLaterality(specimen.getTissueSide());
		result.setStatus(specimen.getCollectionStatus());
		result.setPathology(specimen.getPathologicalStatus());
		result.setInitialQty(specimen.getInitialQuantity());
		result.setAvailableQty(specimen.getAvailableQuantity());
		result.setConcentration(specimen.getConcentration());
		if (specimen.getParentSpecimen() != null) {
			result.setParentId(specimen.getParentSpecimen().getId());
			result.setParentLabel(specimen.getParentSpecimen().getLabel());
		}
	
		StorageLocationSummary location = null;
		StorageContainerPosition position = specimen.getPosition();
		if (position == null) {
			location = new StorageLocationSummary();
			location.setId(-1L);
		} else {
			location = StorageLocationSummary.from(position);
			result.setStorageSite(position.getContainer().getSite().getName());
		}
		result.setStorageLocation(location);

		result.setActivityStatus(specimen.getActivityStatus());
		result.setCreatedOn(specimen.getCreatedOn());
		result.setStorageType(sr != null ? sr.getStorageType() : null);
		result.setVisitId(specimen.getVisit().getId());
		result.setVisitName(specimen.getVisit().getName());
		result.setCprId(specimen.getRegistration().getId());
		result.setPpid(specimen.getRegistration().getPpid());
		result.setCpId(specimen.getCollectionProtocol().getId());
		result.setCpShortTitle(specimen.getCollectionProtocol().getShortTitle());
		result.setFreezeThawCycles(specimen.getFreezeThawCycles());

		if (specimen.getCollRecvDetails() != null) {
			result.setCollectionContainer(specimen.getCollRecvDetails().getCollContainer());
		} else if (specimen.isPrimary() && specimen.getSpecimenRequirement() != null) {
			result.setCollectionContainer(specimen.getSpecimenRequirement().getCollectionContainer());
		}

		return result;
	}	
	
	public static SpecimenInfo fromTo(SpecimenRequirement anticipated, SpecimenInfo result) {
		result.setId(null);	
		result.setReqId(anticipated.getId());
		result.setReqLabel(anticipated.getName());
		result.setSortOrder(anticipated.getSortOrder());
		result.setBarcode(null);
		result.setType(anticipated.getSpecimenType());
		result.setSpecimenClass(anticipated.getSpecimenClass());
		result.setLineage(anticipated.getLineage());
		result.setAnatomicSite(anticipated.getAnatomicSite());
		result.setLaterality(anticipated.getLaterality());
		result.setPathology(anticipated.getPathologyStatus());
		result.setInitialQty(anticipated.getInitialQuantity());
		result.setConcentration(anticipated.getConcentration());
		result.setParentId(null);
		result.setCollectionContainer(anticipated.getCollectionContainer());

		StorageLocationSummary location = new StorageLocationSummary();
		result.setStorageLocation(location);
		result.setStorageType(anticipated.getStorageType());
		return result;
	}	
	
	@Override
	public int compareTo(SpecimenInfo other) {
		if (sortOrder != null && other.sortOrder != null) {
			return sortOrder.compareTo(other.sortOrder);
		} else if (sortOrder != null) {
			return -1;
		} else if (other.sortOrder != null) {
			return 1;
		} else if (reqId != null && other.reqId != null && reqId != other.reqId) {
			return reqId.compareTo(other.reqId);
		} else if (reqId == other.reqId) {
			return id.compareTo(other.id);
		} else if (reqId != null) {
			return -1;
		} else if (other.reqId != null) {
			return 1;
		} else if (id != null && other.id != null) {
			return id.compareTo(other.id);
		}
		
		// 
		// TODO: ERROR: need to put a logger here
		// This scenario should not happen, as this means we neither have
		// anticipated specimen nor actual specimen
		//
		return 0;
	}	
}
