package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import krishagni.catissueplus.beans.FormContextBean;

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
	
	public static FormCtxtSummary from(FormContextBean formCtxt) {
		FormCtxtSummary result = new FormCtxtSummary();
		result.setFormCtxtId(formCtxt.getIdentifier());
		result.setFormId(formCtxt.getContainerId());
		result.setSysForm(formCtxt.isSysForm());
		result.setMultiRecord(formCtxt.isMultiRecord());
		return result;
	}
	
	public static List<FormCtxtSummary> from(List<FormContextBean> formCtxts) {
		List<FormCtxtSummary> result = new ArrayList<FormCtxtSummary>();
		
		for (FormContextBean formCtxt : formCtxts) {
			result.add(FormCtxtSummary.from(formCtxt));
		}
		
		return result;
	}
}
