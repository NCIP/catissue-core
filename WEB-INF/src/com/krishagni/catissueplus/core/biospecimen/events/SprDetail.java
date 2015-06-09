package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.InputStream;

public class SprDetail {
	
	private Long visitId;

	private String name;
	
	private InputStream inputStream;
	
	private String fileContentType;
	
	private String sprText;

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getSprText() {
		return sprText;
	}

	public void setSprText(String sprText) {
		this.sprText = sprText;
	}
	
}
