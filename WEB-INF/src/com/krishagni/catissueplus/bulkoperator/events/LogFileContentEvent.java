package com.krishagni.catissueplus.bulkoperator.events;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class LogFileContentEvent extends ResponseEvent {
	private byte [] logFileContents;

	private String fileName;
	
	public byte[] getLogFileContents() {
		return logFileContents;
	}
	
	public void setLogFileContents(byte[] logFileContents) {
		this.logFileContents = logFileContents;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static LogFileContentEvent ok(byte[] logFileContents, String fileName) {
		LogFileContentEvent resp = new LogFileContentEvent();
		resp.setStatus(EventStatus.OK);
		resp.setLogFileContents(logFileContents);
		resp.setFileName(fileName);
		return resp;
	}
	
	public static LogFileContentEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		LogFileContentEvent resp = new LogFileContentEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
	
	public static LogFileContentEvent invalidRequest(CatissueErrorCode error, Throwable t) {
		LogFileContentEvent resp = new LogFileContentEvent();
		resp.setupResponseEvent(EventStatus.BAD_REQUEST, error, t);
		return resp;
	}
}
