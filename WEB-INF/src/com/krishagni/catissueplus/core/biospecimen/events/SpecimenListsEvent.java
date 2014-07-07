package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenListsEvent extends ResponseEvent {
	private List<SpecimenListSummary> lists;

	public List<SpecimenListSummary> getLists() {
		return lists;
	}

	public void setLists(List<SpecimenListSummary> lists) {
		this.lists = lists;
	}

	public static SpecimenListsEvent ok(List<SpecimenListSummary> lists) {
		SpecimenListsEvent resp = new SpecimenListsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setLists(lists);
		return resp;
	}
		
	public static SpecimenListsEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
		
	private static SpecimenListsEvent errorResp(EventStatus status, String message, Throwable t) {
		SpecimenListsEvent resp = new SpecimenListsEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}