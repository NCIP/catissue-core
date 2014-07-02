package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryExecutedEvent extends ResponseEvent {
	private String[] columnLabels;
	
	private List<String[]> rows;

	public String[] getColumnLabels() {
		return columnLabels;
	}

	public void setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
	}

	public List<String[]> getRows() {
		return rows;
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}
	
	public static QueryExecutedEvent ok(String[] labels, List<String[]> rows) {
		QueryExecutedEvent resp = new QueryExecutedEvent();
		resp.setColumnLabels(labels);
		resp.setRows(rows);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static QueryExecutedEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static QueryExecutedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryExecutedEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryExecutedEvent resp = new QueryExecutedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}