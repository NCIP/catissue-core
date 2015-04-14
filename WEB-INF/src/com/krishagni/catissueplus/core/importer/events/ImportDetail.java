package com.krishagni.catissueplus.core.importer.events;

public class ImportDetail {
	private String objectType;
	
	private String importType;
	
	private String inputFileId;

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getInputFileId() {
		return inputFileId;
	}

	public void setInputFileId(String inputFileId) {
		this.inputFileId = inputFileId;
	}
}
