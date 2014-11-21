package com.krishagni.catissueplus.bulkoperator.domain;

import java.sql.Blob;
import java.sql.Timestamp;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class BulkOperationJob
{
	private Long id;

	private String status;

	private transient Blob logFile;

	private String logFileName;

	private Timestamp startTime;

	private User startedBy;

	private String name;

	private Long timeTaken;

	private Long failedRecordsCount;

	private Long totalRecordsCount;

	private Long processedRecords;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Blob getLogFile() {
		return logFile;
	}

	public void setLogFile(Blob logFile) {
		this.logFile = logFile;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public User getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(User startedBy) {
		this.startedBy = startedBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(Long timeTaken) {
		this.timeTaken = timeTaken;
	}

	public Long getFailedRecordsCount() {
		return failedRecordsCount;
	}

	public void setFailedRecordsCount(Long failedRecordsCount) {
		this.failedRecordsCount = failedRecordsCount;
	}

	public Long getTotalRecordsCount() {
		return totalRecordsCount;
	}

	public void setTotalRecordsCount(Long totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}

	public Long getProcessedRecords() {
		return processedRecords;
	}

	public void setProcessedRecords(Long currentRecordsProcessed) {
		this.processedRecords = currentRecordsProcessed;
	}

	public byte[] getLogFileBytes() {
		try {
			if (logFile != null) {
				int length = (int) logFile.length();
				byte [] logFileBytes = logFile.getBytes(1, length);
				return logFileBytes;
			} 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return null;
	}
}