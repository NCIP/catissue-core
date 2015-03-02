package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobInstance;

public class ScheduledJobInstanceDetail {
	private Long id;
	
	private ScheduledJobDetail scheduledJob;
	
	private String status;
	
	private String logFilePath;
	
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ScheduledJobDetail getScheduledJob() {
		return scheduledJob;
	}

	public void setScheduledJob(ScheduledJobDetail sheduledJob) {
		this.scheduledJob = sheduledJob;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
	
	public static ScheduledJobInstanceDetail from(ScheduledJobInstance job) {
		ScheduledJobInstanceDetail detail = new ScheduledJobInstanceDetail();
		detail.setId(job.getId());
		detail.setScheduledJob(ScheduledJobDetail.from(job.getScheduledJob()));
		detail.setStatus(job.getStatus().toString());
		detail.setMessage(job.getMessage());
		detail.setLogFilePath(job.getLogFilePath());
		return detail;
	}
}
