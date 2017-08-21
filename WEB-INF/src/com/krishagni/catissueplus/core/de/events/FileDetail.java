package com.krishagni.catissueplus.core.de.events;

import edu.common.dynamicextensions.napi.FileControlValue;

public class FileDetail {
	private String filename;
	
	private String fileId;
	
	private long size;
	
	private String contentType;
	
	private String path;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public static FileDetail from(FileControlValue fcv) {
		FileDetail fileDetail = new FileDetail();
		fileDetail.setContentType(fcv.getContentType());
		fileDetail.setFileId(fcv.getFileId());
		fileDetail.setFilename(fcv.getFilename());
		fileDetail.setPath(fcv.getPath());
		
		return fileDetail;
	}
}
