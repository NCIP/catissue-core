package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;

public class CollectionProtocolEventDetail implements Comparable<CollectionProtocolEventDetail> {
	private Long id;
	
	private String eventLabel;
	
	private Double eventPoint;
	
	private String collectionProtocol;
	
	private String defaultSite;
	
	private String clinicalDiagnosis;
	
	private String clinicalStatus;
	
	private String activityStatus;

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
	
	public static CollectionProtocolEventDetail from(CollectionProtocolEvent event) {
		CollectionProtocolEventDetail detail = new CollectionProtocolEventDetail();
		
		detail.setId(event.getId());
		detail.setEventLabel(event.getEventLabel());
		detail.setEventPoint(event.getEventPoint());
		detail.setClinicalDiagnosis(event.getClinicalDiagnosis());
		detail.setClinicalStatus(event.getClinicalStatus());
		detail.setCollectionProtocol(event.getCollectionProtocol().getTitle());
		detail.setActivityStatus(event.getActivityStatus());
		
		if (event.getDefaultSite() != null) {
			detail.setDefaultSite(event.getDefaultSite().getName());
		}
		
		return detail;
	}
	
	public static List<CollectionProtocolEventDetail> from(Collection<CollectionProtocolEvent> events) {
		List<CollectionProtocolEventDetail> result = new ArrayList<CollectionProtocolEventDetail>();
		
		for (CollectionProtocolEvent event : events) {
			result.add(CollectionProtocolEventDetail.from(event));
		}
		
		Collections.sort(result);
		return result;
	}

	@Override
	public int compareTo(CollectionProtocolEventDetail other) {
		Double thisEventPoint = this.eventPoint == null ? 0d : this.eventPoint;
		Double otherEventPoint = other.eventPoint == null ? 0d : other.eventPoint;

		if (thisEventPoint.equals(otherEventPoint)) {
			return eventLabel.compareTo(other.eventLabel);
		} else {
			return thisEventPoint.compareTo(otherEventPoint);
		}		
	}
}
