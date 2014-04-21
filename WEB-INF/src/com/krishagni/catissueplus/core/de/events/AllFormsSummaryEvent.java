package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllFormsSummaryEvent extends ResponseEvent {
	private List<FormSummary> forms;

	public List<FormSummary> getForms() {
		return forms;
	}

	public void setForms(List<FormSummary> forms) {
		this.forms = forms;
	}
	
	public static AllFormsSummaryEvent ok(List<FormSummary> forms) {
		AllFormsSummaryEvent resp = new AllFormsSummaryEvent();
		resp.setStatus(EventStatus.OK);
		resp.setForms(forms);
		return resp;
	}
}
