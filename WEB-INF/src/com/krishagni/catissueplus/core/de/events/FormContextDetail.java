package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;

public class FormContextDetail {
	private Long formCtxtId;
	
	private CollectionProtocolSummary collectionProtocol;
	
	private String level;
	
	private Long formId;
	
	private Integer sortOrder;

	private boolean multiRecord;
	
	private boolean sysForm;

	public Long getFormCtxtId() {
		return formCtxtId;
	}

	public void setFormCtxtId(Long formCtxtId) {
		this.formCtxtId = formCtxtId;
	}

	public CollectionProtocolSummary getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocolSummary collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isMultiRecord() {
		return multiRecord;
	}

	public void setMultiRecord(boolean multiRecord) {
		this.multiRecord = multiRecord;
	}

	public boolean isSysForm() {
		return sysForm;
	}

	public void setSysForm(boolean sysForm) {
		this.sysForm = sysForm;
	}
}
