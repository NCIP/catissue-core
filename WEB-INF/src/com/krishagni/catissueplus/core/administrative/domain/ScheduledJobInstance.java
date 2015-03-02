package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class ScheduledJobInstance extends BaseEntity {
	public enum Status {
		PENDING,
		SUCCEEDED,
		FAILURE
	}
	
	private ScheduledJob scheduledJob;
	
	private Status status;
	
	private String logFilePath;
	
	private String message;

	public ScheduledJob getScheduledJob() {
		return scheduledJob;
	}

	public void setScheduledJob(ScheduledJob scheduledJob) {
		this.scheduledJob = scheduledJob;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void update(ScheduledJobInstance other) {
		setStatus(other.status);
		setLogFilePath(other.logFilePath);
		setMessage(other.message);
	}
}