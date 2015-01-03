package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class SpecimenRequirement {
	private Long id;
	
	private String name;
	
	private String lineage;
		
	private String specimenClass;

	private String specimenType;
	
	private String anatomicSite;

	private String laterality;
			
	private String pathologyStatus;
	
	private String storageType;
	
	private Double initialQuantity;
	
	private Double concentration;
	
	private User collector;

	private String collectionProcedure;

	private String collectionContainer;

	private User receiver;

	private CollectionProtocolEvent collectionProtocolEvent;

	private String labelFormat;

	private String activityStatus;
			
	private SpecimenRequirement parentSpecimenRequirement;
	
	private Set<SpecimenRequirement> childSpecimenRequirements = new HashSet<SpecimenRequirement>();

	private Set<Specimen> specimens = new HashSet<Specimen>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
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

	public String getPathologyStatus() {
		return pathologyStatus;
	}

	public void setPathologyStatus(String pathologyStatus) {
		this.pathologyStatus = pathologyStatus;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public Double getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(Double initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}

	public User getCollector() {
		return collector;
	}

	public void setCollector(User collector) {
		this.collector = collector;
	}

	public String getCollectionProcedure() {
		return collectionProcedure;
	}

	public void setCollectionProcedure(String collectionProcedure) {
		this.collectionProcedure = collectionProcedure;
	}

	public String getCollectionContainer() {
		return collectionContainer;
	}

	public void setCollectionContainer(String collectionContainer) {
		this.collectionContainer = collectionContainer;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public CollectionProtocolEvent getCollectionProtocolEvent() {
		return collectionProtocolEvent;
	}

	public void setCollectionProtocolEvent(
			CollectionProtocolEvent collectionProtocolEvent) {
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	public String getLabelFormat() {
		return labelFormat;
	}

	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public SpecimenRequirement getParentSpecimenRequirement() {
		return parentSpecimenRequirement;
	}

	public void setParentSpecimenRequirement(SpecimenRequirement parentSpecimenRequirement) {
		this.parentSpecimenRequirement = parentSpecimenRequirement;
	}

	public Set<SpecimenRequirement> getChildSpecimenRequirements() {
		return childSpecimenRequirements;
	}

	public void setChildSpecimenRequirements(Set<SpecimenRequirement> childSpecimenRequirements) {
		this.childSpecimenRequirements = childSpecimenRequirements;
	}

	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
	}
	
	
	private static final String[] EXCLUDE_COPY_PROPS = {
		"id", 
		"parentSpecimenRequirement",
		"childSpecimenRequirements",
		"specimens"		
	};
			
	public SpecimenRequirement copy() {
		SpecimenRequirement copy = new SpecimenRequirement();
		BeanUtils.copyProperties(this, copy, EXCLUDE_COPY_PROPS); 
		return copy;
	}
	
	public void addChildRequirement(SpecimenRequirement childReq) {
		childReq.setParentSpecimenRequirement(this);
		childSpecimenRequirements.add(childReq);
	}
	
	public void addChildRequirements(Collection<SpecimenRequirement> children) {
		for (SpecimenRequirement childReq : children) {
			addChildRequirement(childReq);
		}
	}
	
	public Double getQtyAfterAliquotsUse() {
		Double available = initialQuantity;
		for (SpecimenRequirement childReq : childSpecimenRequirements) {
			if (childReq.getLineage().equals("Aliquot")) {
				available -= childReq.getInitialQuantity();
			}
		}
		
		return available;
	}
}
