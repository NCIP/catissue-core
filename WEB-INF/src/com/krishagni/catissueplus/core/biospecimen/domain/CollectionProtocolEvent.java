package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Site;

public class CollectionProtocolEvent {
	private Long id;

	private String collectionPointLabel;

	private Double studyCalendarEventPoint;

	private CollectionProtocol collectionProtocol;
	
	private String labelFormat;
	
	private Site defaultSite;
	
	private String activityStatus;
	
	private String clinicalDiagnosis;
	
	private String clinicalStatus;
	
	private Site specimenCollectionSite;

	private Set<SpecimenRequirement> specimenRequirements = new HashSet<SpecimenRequirement>();

	private Set<SpecimenCollectionGroup> specimenCollectionGroups = new HashSet<SpecimenCollectionGroup>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollectionPointLabel() {
		return collectionPointLabel;
	}

	public void setCollectionPointLabel(String collectionPointLabel) {
		this.collectionPointLabel = collectionPointLabel;
	}

	public Double getStudyCalendarEventPoint() {
		return studyCalendarEventPoint;
	}

	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint) {
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getLabelFormat() {
		return labelFormat;
	}

	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}

	public Site getDefaultSite() {
		return defaultSite;
	}

	public void setDefaultSite(Site defaultSite) {
		this.defaultSite = defaultSite;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
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

	public Site getSpecimenCollectionSite() {
		return specimenCollectionSite;
	}

	public void setSpecimenCollectionSite(Site specimenCollectionSite) {
		this.specimenCollectionSite = specimenCollectionSite;
	}

	public Set<SpecimenRequirement> getSpecimenRequirements() {
		return specimenRequirements;
	}

	public void setSpecimenRequirements(
			Set<SpecimenRequirement> specimenRequirements) {
		this.specimenRequirements = specimenRequirements;
	}

	public Set<SpecimenCollectionGroup> getSpecimenCollectionGroups() {
		return specimenCollectionGroups;
	}

	public void setSpecimenCollectionGroups(
			Set<SpecimenCollectionGroup> specimenCollectionGroups) {
		this.specimenCollectionGroups = specimenCollectionGroups;
	}
}
