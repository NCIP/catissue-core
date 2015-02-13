package com.krishagni.catissueplus.core.de.events;

import edu.common.dynamicextensions.napi.FormData;

public class FormDataDetail {
	private Long formId;
	
	private Long recordId;
	
	private FormData formData;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public FormData getFormData() {
		return formData;
	}

	public void setFormData(FormData formData) {
		this.formData = formData;
	}
	
	public static FormDataDetail ok(Long formId, Long recordId, FormData formData) {
		FormDataDetail resp = new FormDataDetail();
		resp.setFormId(formId);
		resp.setRecordId(recordId);
		resp.setFormData(formData);
		return resp;		
	}	
}
