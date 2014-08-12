package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllCPFormsSummaryEvent extends ResponseEvent {
	
	private List<CPFormSummary> allCPForms;

	public List<CPFormSummary> getAllCPForms() {
		return allCPForms;
	}

	public void setAllCPForms(List<CPFormSummary> allCPForms) {
		this.allCPForms = allCPForms;
	}

	public static AllCPFormsSummaryEvent ok(List<CPFormSummary> allCPForms) {
		AllCPFormsSummaryEvent resp = new AllCPFormsSummaryEvent();
		resp.setStatus(EventStatus.OK);
		resp.setAllCPForms(allCPForms);
		return resp;
	}
}
