package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class EntityFormsEvent extends ResponseEvent {
	private Long entityId;
	
	private List<FormCtxtSummary> forms;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public List<FormCtxtSummary> getForms() {
		return forms;
	}

	public void setForms(List<FormCtxtSummary> forms) {
		this.forms = forms;
	}
	
	public static EntityFormsEvent ok(List<FormCtxtSummary> forms) {
		EntityFormsEvent resp = new EntityFormsEvent();
		resp.setForms(forms);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
}
