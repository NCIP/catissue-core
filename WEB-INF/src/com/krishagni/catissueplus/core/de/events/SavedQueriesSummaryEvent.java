package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SavedQueriesSummaryEvent extends ResponseEvent {
	
	private List<SavedQuerySummary> savedQueries;

	public List<SavedQuerySummary> getSavedQueries() {
		return savedQueries;
	}

	public void setSavedQueries(List<SavedQuerySummary> savedQueries) {
		this.savedQueries = savedQueries;
	}
	
	public static SavedQueriesSummaryEvent ok(List<SavedQuerySummary> queries) {
		SavedQueriesSummaryEvent resp = new SavedQueriesSummaryEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSavedQueries(queries);
		return resp;
	}
	
	public static SavedQueriesSummaryEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static SavedQueriesSummaryEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static SavedQueriesSummaryEvent errorResp(EventStatus status, String message, Throwable t) {
		SavedQueriesSummaryEvent resp = new SavedQueriesSummaryEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
