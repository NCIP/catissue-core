package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;

public class FormContextsAddedEvent extends FormContextsEvent {
	public static FormContextsAddedEvent ok(List<FormContextDetail> formCtxts) {
		FormContextsAddedEvent resp = new FormContextsAddedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormCtxts(formCtxts);
		return resp;
	}

}
