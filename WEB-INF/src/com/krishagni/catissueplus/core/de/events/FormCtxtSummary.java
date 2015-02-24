package com.krishagni.catissueplus.core.de.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class FormCtxtSummary {
	private Long formCtxtId;
	
	private Long formId;
	
	private String formCaption;
	
	private Integer noOfRecords;
	
	private boolean multiRecord;
	
	private boolean sysForm;
	
	private UserSummary createdBy;
	
	private Date creationTime;
	
	private Date modificationTime;
	
	public Long getFormCtxtId() {
		return formCtxtId;
	}

	public void setFormCtxtId(Long formCtxtId) {
		this.formCtxtId = formCtxtId;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getFormCaption() {
		return formCaption;
	}

	public void setFormCaption(String formCaption) {
		this.formCaption = formCaption;
	}

	public Integer getNoOfRecords() {
		return noOfRecords;
	}

	public void setNoOfRecords(Integer noOfRecords) {
		this.noOfRecords = noOfRecords;
	}
	
	public boolean isMultiRecord() {
		return multiRecord;
	}

	public void setMultiRecord(boolean isMultiRecord) {
		this.multiRecord = isMultiRecord;
	}

	public boolean isSysForm() {
		return sysForm;
	}

	public void setSysForm(boolean sysForm) {
		this.sysForm = sysForm;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}
}
