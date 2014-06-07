package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SavedQueriesSummaryEvent extends ResponseEvent {
	
	private Long count;
	
	private List<SavedQuerySummary> savedQueries;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<SavedQuerySummary> getSavedQueries() {
		return savedQueries;
	}

	public void setSavedQueries(List<SavedQuerySummary> savedQueries) {
		this.savedQueries = savedQueries;
	}
	
	public static SavedQueriesSummaryEvent ok(List<SavedQuerySummary> queries, Long count) {
		SavedQueriesSummaryEvent resp = new SavedQueriesSummaryEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSavedQueries(queries);
		resp.setCount(count);
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
