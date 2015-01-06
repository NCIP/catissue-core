
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class VisitDetail {
	private Long cprId;

	private Long cpeId;
	
	private String ppid;
	
	private String eventLabel;
	
	private String cpTitle;

	private Long id;

	private String name;
	
	private String clinicalDiagnosis;
	
	private String clinicalStatus;

	private String activityStatus;

	private String visitSite;

	private String visitStatus;

	private String comments;

	private String surgicalPathologyNumber;
	
	private Date visitDate;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getCpeId() {
		return cpeId;
	}

	public void setCpeId(Long cpeId) {
		this.cpeId = cpeId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}

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

	public String getVisitSite() {
		return visitSite;
	}

	public void setVisitSite(String visitSite) {
		this.visitSite = visitSite;
	}

	public String getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSurgicalPathologyNumber() {
		return surgicalPathologyNumber;
	}

	public void setSurgicalPathologyNumber(String surgicalPathologyNumber) {
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public static VisitDetail from(Visit visit) {
		VisitDetail detail = new VisitDetail();
		detail.setActivityStatus(visit.getActivityStatus());
		detail.setClinicalDiagnosis(visit.getClinicalDiagnosis());
		detail.setClinicalStatus(visit.getClinicalStatus());
		detail.setVisitStatus(visit.getStatus());
		detail.setComments(visit.getComments());
		detail.setId(visit.getId());
		detail.setName(visit.getName());
		detail.setSurgicalPathologyNumber(visit.getSurgicalPathologyNumber());
		detail.setVisitDate(visit.getVisitDate());

		
		CollectionProtocolRegistration cpr = visit.getRegistration();
		detail.setCprId(cpr.getId());
		detail.setPpid(cpr.getProtocolParticipantIdentifier());
		detail.setCpTitle(cpr.getCollectionProtocol().getTitle());
		
		detail.setCpeId(visit.getCpEvent().getId());
		detail.setEventLabel(visit.getCpEvent().getEventLabel());
		detail.setVisitSite(visit.getSite().getName());		
		
		return detail;
	}
}
