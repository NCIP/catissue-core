package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class ScheduledJobRun extends BaseEntity {
	public enum Status {
		IN_PROGRESS,
		SUCCEEDED,
		FAILED
	}
	
	private Date startedAt;
	
	private Date finishedAt;
	
	private Status status;
	
	private String logFilePath;
	
	private String message;
	
	private ScheduledJob scheduledJob;

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
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
	
	public ScheduledJob getScheduledJob() {
		return scheduledJob;
	}

	public void setScheduledJob(ScheduledJob scheduledJob) {
		this.scheduledJob = scheduledJob;
	}

	public void inProgress(ScheduledJob job) {
		startedAt = new Date();
		status = Status.IN_PROGRESS;
		scheduledJob = job;
	}
	
	public void completed() {
		completed(null);
	}
	
	public void completed(String logFile) {
		finishedAt = new Date();
		status = Status.SUCCEEDED;
		logFilePath = logFile;
	}
	
	public void failed(Exception e) {
		finishedAt = new Date();
		status = Status.FAILED;
		
		if (e != null) {
			message = e.getMessage();
		}
	}

	public void update(ScheduledJobRun other) {
		setStatus(other.status);
		setLogFilePath(other.logFilePath);
		setMessage(other.message);
		setStartedAt(other.startedAt);
		setFinishedAt(other.finishedAt);
	}
}