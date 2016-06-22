package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.File;
import java.io.InputStream;

public class FileDetail {
	private Long id;

	private String name;

	private String filename;
	
	private File fileOut;

	private InputStream fileIn;

	private String contentType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public File getFileOut() {
		return fileOut;
	}

	public void setFileOut(File fileOut) {
		this.fileOut = fileOut;
	}

	public InputStream getFileIn() {
		return fileIn;
	}

	public void setFileIn(InputStream fileIn) {
		this.fileIn = fileIn;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
