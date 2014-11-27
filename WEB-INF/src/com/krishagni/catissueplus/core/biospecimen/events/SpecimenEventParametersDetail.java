package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

public class SpecimenEventParametersDetail {
	private Long id;
	
	private Date timeStamp;
	
	private String userLogin;
	
	private String comment;
	
	private String specimenLabel;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSpecimenLabel() {
		return specimenLabel;
	}

	public void setSpecimenLabel(String specimenLabel) {
		this.specimenLabel = specimenLabel;
	}
}
