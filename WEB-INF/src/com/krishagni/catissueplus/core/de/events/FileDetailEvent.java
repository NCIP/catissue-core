package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FileDetailEvent extends ResponseEvent {
	
	private FileDetail fileDetail;
	
	public FileDetail getFileDetail() {
		return fileDetail;
	}


	public void setFileDetail(FileDetail fileDetail) {
		this.fileDetail = fileDetail;
	}

	public static FileDetailEvent ok(FileDetail fileDetail) {
		FileDetailEvent resp = new FileDetailEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFileDetail(fileDetail);
		return resp;
	}
	
	public static FileDetailEvent notFound() {
		FileDetailEvent resp = new FileDetailEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
