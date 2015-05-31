package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.File;

public class FileDetail {

	private String fileName;
	
	private File file;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
