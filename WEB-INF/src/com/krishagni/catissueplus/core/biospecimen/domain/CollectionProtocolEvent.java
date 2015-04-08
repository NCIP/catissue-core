package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.administrative.domain.Site;

@Audited
public class CollectionProtocolEvent {
	private static final String ENTITY_NAME = "collection_protocol_event";
	
	private Long id;

	private String eventLabel;

	private Double eventPoint;

	private CollectionProtocol collectionProtocol;
	
	private Site defaultSite;

	private String clinicalDiagnosis;
	
	private String clinicalStatus;
	
	private String activityStatus;
	
	private Set<SpecimenRequirement> specimenRequirements = new HashSet<SpecimenRequirement>();

	private Set<Visit> specimenCollectionGroups = new HashSet<Visit>();

	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
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

	public Set<Visit> getSpecimenCollectionGroups() {
		return specimenCollectionGroups;
	}

	public void setSpecimenCollectionGroups(Set<Visit> specimenCollectionGroups) {
		this.specimenCollectionGroups = specimenCollectionGroups;
	}
	
	// updates all but specimen requirements
	public void update(CollectionProtocolEvent other) { 
		setEventPoint(other.getEventPoint());
		setEventLabel(other.getEventLabel());
		setCollectionProtocol(other.getCollectionProtocol());
		setDefaultSite(other.getDefaultSite());
		setClinicalDiagnosis(other.getClinicalDiagnosis());
		setClinicalStatus(other.getClinicalStatus());
		setActivityStatus(other.getActivityStatus());
	}
	
	public void addSpecimenRequirement(SpecimenRequirement sr) {
		specimenRequirements.add(sr);
		sr.setCollectionProtocolEvent(this);		
	}
	
	public void copySpecimenRequirementsTo(CollectionProtocolEvent cpe) {
		for (SpecimenRequirement sr : specimenRequirements) {
			if (sr.getParentSpecimenRequirement() == null) {
				cpe.addSpecimenRequirement(sr.deepCopy(cpe));
			}			
		}		
	}
}
