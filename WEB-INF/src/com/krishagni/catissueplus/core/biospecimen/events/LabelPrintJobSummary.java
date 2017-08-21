package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class LabelPrintJobSummary {
	private Long id;
	
	private UserSummary submittedBy;
	
	private Date submissionDate;
	
	private String itemType;
	
	private int itemsCount;

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

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public int getItemsCount() {
		return itemsCount;
	}

	public void setItemsCount(int itemsCount) {
		this.itemsCount = itemsCount;
	}

	public static LabelPrintJobSummary from(LabelPrintJob job) {
		LabelPrintJobSummary result = new LabelPrintJobSummary();
		result.setId(job.getId());
		result.setSubmissionDate(job.getSubmissionDate());
		result.setSubmittedBy(UserSummary.from(job.getSubmittedBy()));
		result.setItemType(job.getItemType());
		result.setItemsCount(job.getItems().size());
		return result;
	}
}
