package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;

public class FormContextDetail {
	private Long formCtxtId;
	
	private CollectionProtocolSummary collectionProtocol;
	
	private String level;
	
	private Long formId;

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
}
