package com.krishagni.catissueplus.core.de.events;


import java.util.concurrent.Future;

public class QueryDataExportResult {
	private String dataFile;
	
	private boolean completed;

	private Future<Boolean> promise;

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

	public Future<Boolean> getPromise() {
		return promise;
	}

	public void setPromise(Future<Boolean> promise) {
		this.promise = promise;
	}

	public static QueryDataExportResult create(String dataFile, boolean completed, Future<Boolean> promise) {
		QueryDataExportResult resp = new QueryDataExportResult();
		resp.setCompleted(completed);
		resp.setDataFile(dataFile);
		resp.setPromise(promise);
		return resp;
	}
}
