package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.InputStream;


public class ConsentFormDetail {
	private Long cprId;
	
	private String fileName;
	
	private InputStream inputStream;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}
