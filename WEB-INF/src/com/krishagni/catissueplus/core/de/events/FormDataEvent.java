package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.napi.FormData;

public class FormDataEvent extends ResponseEvent {
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
	
	public static FormDataEvent ok(Long formId, Long recordId, FormData formData) {
		FormDataEvent resp = new FormDataEvent();
		resp.setFormId(formId);
		resp.setRecordId(recordId);
		resp.setFormData(formData);
		resp.setStatus(EventStatus.OK);
		return resp;		
	}
	
	public static FormDataEvent notFound(Long formId, Long recordId) {
		FormDataEvent resp = new FormDataEvent();
		resp.setFormId(formId);
		resp.setRecordId(recordId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
	
	public static FormDataEvent badRequest() {
		FormDataEvent resp = new FormDataEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		return resp;
	}
}
