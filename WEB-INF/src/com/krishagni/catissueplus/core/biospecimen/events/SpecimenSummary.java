package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenPosition;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;

import edu.emory.mathcs.backport.java.util.Collections;

public class SpecimenSummary {
	public static class StorageLocationSummary {
		public Long id;
		
		public String name;
		
		public String positionX;
		
		public String positionY;
	}
	
	private Long id;
	
	private Long cprId;
	
	private Long eventId;
	
	private Long reqId;

	private String label;
	
	private String barcode;

	private String type;
	
	private String specimenClass;
		
	private String lineage;

	private String tissueSite;

	private String tissueSide;
	
	private String status;
	
	private String reqLabel;
	
	private String pathology;
	
	private StorageLocationSummary storageLocation;
	
	private List<SpecimenSummary> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getReqId() {
		return reqId;
	}

	public void setReqId(Long reqId) {
		this.reqId = reqId;
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

	public StorageLocationSummary getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(StorageLocationSummary storageLocation) {
		this.storageLocation = storageLocation;
	}

	public List<SpecimenSummary> getChildren() {
		return children;
	}

	public void setChildren(List<SpecimenSummary> children) {
		this.children = children;
	}
	
	public static SpecimenSummary from(Specimen specimen) {
		SpecimenSummary result = new SpecimenSummary();
		result.setId(specimen.getId());
		
		SpecimenRequirement sr= specimen.getSpecimenRequirement();
		result.setReqId(sr != null ? sr.getId() : null);
		result.setLabel(specimen.getLabel());
		result.setBarcode(specimen.getBarcode());
		result.setType(specimen.getSpecimenType());
		result.setSpecimenClass(specimen.getSpecimenClass());
		result.setLineage(specimen.getLineage());
		result.setTissueSite(specimen.getTissueSite());
		result.setTissueSide(specimen.getTissueSide());
		result.setStatus(specimen.getCollectionStatus());
		result.setPathology(specimen.getPathologicalStatus());
	
		StorageLocationSummary location = new StorageLocationSummary();
		SpecimenPosition position = specimen.getSpecimenPosition();
		if (position == null) {
			location.id = -1L;
		} else {
			location.id = position.getStorageContainer().getId();
			location.name = position.getStorageContainer().getName();
			location.positionX = position.getPositionDimensionOneString();
			location.positionY = position.getPositionDimensionTwoString();
		}
		result.setStorageLocation(location);
		
		result.setChildren(from(specimen.getChildCollection()));		
		return result;
	}
	
	public static List<SpecimenSummary> from(Collection<Specimen> specimens) {
		List<SpecimenSummary> result = new ArrayList<SpecimenSummary>();
		
		if (CollectionUtils.isEmpty(specimens)) {
			return result;
		}
		
		for (Specimen specimen : specimens) {
			result.add(SpecimenSummary.from(specimen));
		}
		
		return result;
	}
	
	public static SpecimenSummary from(SpecimenRequirement anticipated) {
		SpecimenSummary result = new SpecimenSummary();
		
		result.setId(null);	
		result.setReqId(anticipated.getId());
		result.setLabel(anticipated.getSpecimenRequirementLabel());
		result.setBarcode(null);
		result.setType(anticipated.getSpecimenType());
		result.setSpecimenClass(anticipated.getSpecimenClass());
		result.setLineage(anticipated.getLineage());
		result.setTissueSite(anticipated.getTissueSite());
		result.setTissueSide(anticipated.getTissueSide());
		result.setPathology(anticipated.getPathologicalStatus());
	
		StorageLocationSummary location = new StorageLocationSummary();
		location.id = -1L;
		location.name = anticipated.getStorageType();		
		result.setStorageLocation(location);
		
		result.setChildren(fromAnticipated(anticipated.getChildSpecimenRequirements()));		
		return result;		
	}

	public static List<SpecimenSummary> fromAnticipated(Collection<SpecimenRequirement> anticipatedSpecimens) {
		List<SpecimenSummary> result = new ArrayList<SpecimenSummary>();
		
		if (CollectionUtils.isEmpty(anticipatedSpecimens)) {
			return result;
		}
		
		for (SpecimenRequirement anticipated : anticipatedSpecimens) {
			result.add(SpecimenSummary.from(anticipated));
		}
		
		return result;
	}	
	
	public static void sort(List<SpecimenSummary> specimens) {
		Collections.sort(specimens, new Comparator<SpecimenSummary>() {
			@Override
			public int compare(SpecimenSummary specimen1, SpecimenSummary specimen2) {
				return specimen1.getType().compareTo(specimen2.getType());
			}
		});
		
		for (SpecimenSummary specimen : specimens) {
			if (specimen.getChildren() != null) {
				sort(specimen.getChildren());
			}
		}
	}
}
