package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenLabelPrintJobSummary {
	private Long id;
	
	private UserSummary submittedBy;
	
	private Date submissionDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserSummary getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(UserSummary submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

}
