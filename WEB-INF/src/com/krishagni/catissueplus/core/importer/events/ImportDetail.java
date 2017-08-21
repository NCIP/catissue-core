package com.krishagni.catissueplus.core.importer.events;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.importer.services.ImportListener;

public class ImportDetail {
	private String objectType;
	
	private String importType;
	
	private String csvType;

	private String dateFormat;

	private String timeFormat;

	private String inputFileId;

	private ImportListener listener;
	
	private Map<String, String> objectParams = new HashMap<>();

	private boolean atomic;

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

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getInputFileId() {
		return inputFileId;
	}

	public void setInputFileId(String inputFileId) {
		this.inputFileId = inputFileId;
	}

	public Map<String, String> getObjectParams() {
		return objectParams;
	}

	public void setObjectParams(Map<String, String> objectParams) {
		this.objectParams = objectParams;
	}

	public ImportListener getListener() {
		return listener;
	}

	public void setListener(ImportListener listener) {
		this.listener = listener;
	}

	public boolean isAtomic() {
		return atomic;
	}

	public void setAtomic(boolean atomic) {
		this.atomic = atomic;
	}
}
