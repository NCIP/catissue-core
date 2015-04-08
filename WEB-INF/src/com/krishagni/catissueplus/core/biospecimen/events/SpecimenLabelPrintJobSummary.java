package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenLabelPrintJob;
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

	public static SpecimenLabelPrintJobSummary from(SpecimenLabelPrintJob job) {
		SpecimenLabelPrintJobSummary result = new SpecimenLabelPrintJobSummary();
		result.setId(job.getId());
		result.setSubmissionDate(job.getSubmissionDate());
		result.setSubmittedBy(UserSummary.from(job.getSubmittedBy()));
		return result;
	}
}
