package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FormFieldsEvent extends ResponseEvent {
	private Long formId;
	
	private List<FormFieldSummary> fields;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public List<FormFieldSummary> getFields() {
		return fields;
	}

	public void setFields(List<FormFieldSummary> fields) {
		this.fields = fields;
	}
	
	public static FormFieldsEvent ok(Long formId, List<FormFieldSummary> fields) {
		FormFieldsEvent resp = new FormFieldsEvent();
		resp.setFields(fields);
		resp.setFormId(formId);
		resp.setStatus(EventStatus.OK);
		return resp;		
	}
	
	public static FormFieldsEvent notFound(Long formId) {
		FormFieldsEvent resp = new FormFieldsEvent();
		resp.setFormId(formId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
