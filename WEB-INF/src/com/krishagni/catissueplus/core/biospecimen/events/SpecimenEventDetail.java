package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenEventDetail {
	private Long id;
	
	private Date time;
	
	private UserSummary user;
	
	private String comments;
	
	private String specimenLabel;
	
	private Long specimenId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public UserSummary getUser() {
		return user;
	}

	public void setUser(UserSummary user) {
		this.user = user;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSpecimenLabel() {
		return specimenLabel;
	}

	public void setSpecimenLabel(String specimenLabel) {
		this.specimenLabel = specimenLabel;
	}

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}	
}
