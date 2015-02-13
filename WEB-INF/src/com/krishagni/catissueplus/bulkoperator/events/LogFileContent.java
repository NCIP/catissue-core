package com.krishagni.catissueplus.bulkoperator.events;

public class LogFileContent {
	private byte [] logFileContents;

	private String fileName;
	
	public byte[] getLogFileContents() {
		return logFileContents;
	}
	
	public void setLogFileContents(byte[] logFileContents) {
		this.logFileContents = logFileContents;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
