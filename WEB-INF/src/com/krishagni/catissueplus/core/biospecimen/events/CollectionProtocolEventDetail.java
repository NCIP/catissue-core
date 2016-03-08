package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;

@JsonFilter("withoutId")
@JsonInclude(Include.NON_NULL)
public class CollectionProtocolEventDetail {
	private Long id;
	
	private String eventLabel;
	
	private Double eventPoint;
	
	private String collectionProtocol;
	
	private String defaultSite;
	
	private String clinicalDiagnosis;
	
	private String clinicalStatus;
	
	private String activityStatus;
	
	private String code;
	
	//
	// mostly used for export
	//
	private List<SpecimenRequirementDetail> specimenRequirements;

	private int offset;

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

	public String getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(String collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getDefaultSite() {
		return defaultSite;
	}

	public void setDefaultSite(String defaultSite) {
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
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<SpecimenRequirementDetail> getSpecimenRequirements() {
		return specimenRequirements;
	}

	public void setSpecimenRequirements(List<SpecimenRequirementDetail> specimenRequirements) {
		this.specimenRequirements = specimenRequirements;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public static CollectionProtocolEventDetail from(CollectionProtocolEvent event) {
		return from(event, false);
	}
	
	public static CollectionProtocolEventDetail from(CollectionProtocolEvent event, boolean fullObject) {
		CollectionProtocolEventDetail detail = new CollectionProtocolEventDetail();
		
		detail.setId(event.getId());
		detail.setEventLabel(event.getEventLabel());
		detail.setEventPoint(event.getEventPoint());
		detail.setClinicalDiagnosis(event.getClinicalDiagnosis());
		detail.setClinicalStatus(event.getClinicalStatus());
		detail.setCollectionProtocol(event.getCollectionProtocol().getTitle());
		detail.setActivityStatus(event.getActivityStatus());
		detail.setCode(event.getCode());
		detail.setOffset(event.getOffset());
		
		if (event.getDefaultSite() != null) {
			detail.setDefaultSite(event.getDefaultSite().getName());
		}
		
		if (fullObject) {
			Set<SpecimenRequirement> srs = new HashSet<SpecimenRequirement>();
			for (SpecimenRequirement sr : event.getTopLevelAnticipatedSpecimens()) {
				srs.add(sr);
			}

			detail.setSpecimenRequirements(SpecimenRequirementDetail.from(srs));
		}
		
		return detail;
	}
	
	public static List<CollectionProtocolEventDetail> from(Collection<CollectionProtocolEvent> events) {
		return from(events, false);
	}
		
	public static List<CollectionProtocolEventDetail> from(Collection<CollectionProtocolEvent> events, boolean fullObject) {
		List<CollectionProtocolEventDetail> result = new ArrayList<CollectionProtocolEventDetail>();
		
		for (CollectionProtocolEvent event : events) {
			result.add(CollectionProtocolEventDetail.from(event, fullObject));
		}
		
		return result;
	}
}
