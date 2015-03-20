package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class SpecimenLabelPrintJob extends BaseEntity {
	private User submittedBy;
	
	private Date submissionDate;
	
	private Set<SpecimenLabelPrintJobItem> items = new HashSet<SpecimenLabelPrintJobItem>();

	public User getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(User submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public Set<SpecimenLabelPrintJobItem> getItems() {
		return items;
	}

	public void setItems(Set<SpecimenLabelPrintJobItem> items) {
		this.items = items;
	}

}
