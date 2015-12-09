package com.krishagni.catissueplus.core.importer.events;

import java.util.HashMap;
import java.util.Map;

public class ImportDetail {
	private String objectType;
	
	private String importType;
	
	private String csvType;
	
	private String inputFileId;
	
	private Map<String, Object> objectParams = new HashMap<String, Object>();

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

	public String getCsvType() {
		return csvType;
	}

	public void setCsvType(String csvType) {
		this.csvType = csvType;
	}

	public String getInputFileId() {
		return inputFileId;
	}

	public void setInputFileId(String inputFileId) {
		this.inputFileId = inputFileId;
	}

	public Map<String, Object> getObjectParams() {
		return objectParams;
	}

	public void setObjectParams(Map<String, Object> objectParams) {
		this.objectParams = objectParams;
	}
}
