package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Site;

public class CollectionProtocolEvent {
	private Long id;

	private String eventLabel;

	private Double eventPoint;

	private CollectionProtocol collectionProtocol;
	
	private Site defaultSite;

	private String clinicalDiagnosis;
	
	private String clinicalStatus;
	
	private String activityStatus;
	
	private Set<SpecimenRequirement> specimenRequirements = new HashSet<SpecimenRequirement>();

	private Set<SpecimenCollectionGroup> specimenCollectionGroups = new HashSet<SpecimenCollectionGroup>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	public Double getEventPoint() {
		return eventPoint;
	}

	public void setEventPoint(Double eventPoint) {
		this.eventPoint = eventPoint;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public Site getDefaultSite() {
		return defaultSite;
	}

	public void setDefaultSite(Site defaultSite) {
		this.defaultSite = defaultSite;
	}

	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Set<SpecimenRequirement> getSpecimenRequirements() {
		return specimenRequirements;
	}
	
	public void setSpecimenRequirements(Set<SpecimenRequirement> specimenRequirements) {
		this.specimenRequirements = specimenRequirements;
	}
	
	public Set<SpecimenRequirement> getTopLevelAnticipatedSpecimens() {
		Set<SpecimenRequirement> anticipated = new HashSet<SpecimenRequirement>();
		if (specimenRequirements == null) {
			return anticipated;
		}
		
		for (SpecimenRequirement sr : specimenRequirements) {
			if (sr.getParentSpecimenRequirement() == null) {
				anticipated.add(sr);
			}
		}
		
		return anticipated;
	}

	public Set<SpecimenCollectionGroup> getSpecimenCollectionGroups() {
		return specimenCollectionGroups;
	}

	public void setSpecimenCollectionGroups(Set<SpecimenCollectionGroup> specimenCollectionGroups) {
		this.specimenCollectionGroups = specimenCollectionGroups;
	}
}
