package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FormContextsEvent extends ResponseEvent {
	private List<FormContextDetail> formCtxts;

	public List<FormContextDetail> getFormCtxts() {
		return formCtxts;
	}

	public void setFormCtxts(List<FormContextDetail> formCtxts) {
		this.formCtxts = formCtxts;
	}
	
	public static FormContextsEvent ok(List<FormContextDetail> formCtxts) {
		FormContextsEvent resp = new FormContextsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormCtxts(formCtxts);
		return resp;
	}
}
