package com.krishagni.catissueplus.core.de.events;

import java.io.File;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ExportDataFileEvent extends ResponseEvent {
	private String fileId;
	
	private File file;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public static ExportDataFileEvent ok(String fileId, File file) {
		ExportDataFileEvent resp = new ExportDataFileEvent();
		resp.setFileId(fileId);
		resp.setFile(file);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static ExportDataFileEvent notFound(String fileId) {
		ExportDataFileEvent resp = new ExportDataFileEvent();
		resp.setFileId(fileId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static ExportDataFileEvent serverError(String fileId, String msg, Throwable e) {
		ExportDataFileEvent resp = new ExportDataFileEvent();
		resp.setFileId(fileId);
		resp.setMessage(msg != null ? msg : e != null ? e.getMessage() : null);
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
