package com.krishagni.catissueplus.core.de.events;


public class QueryDataExportResult {
	private String dataFile;
	
	private boolean completed;

	public String getDataFile() {
		return dataFile;
	}

	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public static QueryDataExportResult create(String dataFile, boolean completed) {
		QueryDataExportResult resp = new QueryDataExportResult();
		resp.setCompleted(completed);
		resp.setDataFile(dataFile);
		return resp;
	}
}
