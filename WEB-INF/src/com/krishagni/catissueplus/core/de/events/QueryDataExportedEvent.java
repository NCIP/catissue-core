package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryDataExportedEvent extends ResponseEvent {
	private String dataFile;
	
	private boolean completed;

	public String getDataFile() {
		return dataFile;
	}

	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public static QueryDataExportedEvent ok(String dataFile, boolean completed) {
		QueryDataExportedEvent resp = new QueryDataExportedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setCompleted(completed);
		resp.setDataFile(dataFile);
		return resp;
	}
	
	public static QueryDataExportedEvent badRequest(String msg, Throwable e) {
		QueryDataExportedEvent resp = new QueryDataExportedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(msg);
		resp.setException(e);
		return resp;
	}

	public static QueryDataExportedEvent notAuthorized(String msg, Throwable e) {
		QueryDataExportedEvent resp = new QueryDataExportedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		resp.setMessage(msg);
		resp.setException(e);
		return resp;
	}
	
	public static QueryDataExportedEvent serverError(String msg, Throwable e) {
		QueryDataExportedEvent resp = new QueryDataExportedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		resp.setMessage(msg != null ? msg : e != null ? e.getMessage() : null);
		return resp;
	}
}
