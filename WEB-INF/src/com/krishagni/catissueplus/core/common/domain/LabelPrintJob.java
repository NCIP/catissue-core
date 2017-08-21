package com.krishagni.catissueplus.core.common.domain;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class LabelPrintJob extends BaseEntity {
	private String itemType;
	
	private User submittedBy;
	
	private Date submissionDate;
	
	private Set<LabelPrintJobItem> items = new LinkedHashSet<LabelPrintJobItem>();

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
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

	public Set<LabelPrintJobItem> getItems() {
		return items;
	}

	public void setItems(Set<LabelPrintJobItem> items) {
		this.items = items;
	}
}