package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include=Inclusion.NON_NULL)
public class CprSummary {
	private ParticipantSummary participant;
	
	private Long cprId;
	
	private Date registrationDate;
	
	private String ppid;

	private Long cpId;

	private String cpShortTitle;
	
	private Long scgCount;
	
	private Long specimenCount;

	public ParticipantSummary getParticipant() {
		return participant;
	}

	public void setParticipant(ParticipantSummary participant) {
		this.participant = participant;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public Long getScgCount() {
		return scgCount;
	}

	public void setScgCount(Long scgCount) {
		this.scgCount = scgCount;
	}

	public Long getSpecimenCount() {
		return specimenCount;
	}

	public void setSpecimenCount(Long specimenCount) {
		this.specimenCount = specimenCount;
	}
}
