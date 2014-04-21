package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.domain.nui.Container;

public class FormDefinitionEvent extends ResponseEvent {
	private Long formId;
	
	private Container formDef;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Container getFormDef() {
		return formDef;
	}

	public void setFormDef(Container formDef) {
		this.formDef = formDef;
	}
	
	public static FormDefinitionEvent ok(Container formDef) {
		FormDefinitionEvent resp = new FormDefinitionEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormDef(formDef);
		resp.setFormId(formDef.getId());
		return resp;
	}
	
	public static FormDefinitionEvent notFound(Long formId) {
		FormDefinitionEvent resp = new FormDefinitionEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setFormId(formId);
		return resp;		
	}
}
