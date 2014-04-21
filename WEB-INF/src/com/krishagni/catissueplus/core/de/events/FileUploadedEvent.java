package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FileUploadedEvent  extends ResponseEvent{
	private FileDetail file;

	public FileDetail getFile() {
		return file;
	}

	public void setFile(FileDetail file) {
		this.file = file;
	}
	
	public static FileUploadedEvent ok(FileDetail file) {
		FileUploadedEvent resp = new FileUploadedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFile(file);
		return resp;		
	}
	
	public static FileUploadedEvent serverError() {
		FileUploadedEvent resp = new FileUploadedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		return resp;
	}
}
