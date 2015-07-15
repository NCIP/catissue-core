package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ScheduledJobRunDetail {
	private Long id;
	
	private Long jobId;
	
	private String jobName;
	
	private Date startedAt;
	
	private Date finishedAt;
	
	private String status;
	
	private String logFilePath;
	
	private String message;

	private UserSummary runBy;
	
	private String rtArgs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

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

	public UserSummary getRunBy() {
		return runBy;
	}

	public void setRunBy(UserSummary runBy) {
		this.runBy = runBy;
	}

	public String getRtArgs() {
		return rtArgs;
	}

	public void setRtArgs(String rtArgs) {
		this.rtArgs = rtArgs;
	}

	public static ScheduledJobRunDetail from(ScheduledJobRun job) {
		ScheduledJobRunDetail detail = new ScheduledJobRunDetail();
		detail.setId(job.getId());
		detail.setJobId(job.getScheduledJob().getId());
		detail.setJobName(job.getScheduledJob().getName());
		detail.setStatus(job.getStatus().toString());
		detail.setMessage(job.getMessage());
		detail.setLogFilePath(job.getLogFilePath());
		detail.setStartedAt(job.getStartedAt());
		detail.setFinishedAt(job.getFinishedAt());
		detail.setRunBy(UserSummary.from(job.getRunBy()));
		detail.setRtArgs(job.getRtArgs());
		return detail;
	}
	
	public static List<ScheduledJobRunDetail> from(List<ScheduledJobRun> jobs) {
		List<ScheduledJobRunDetail> list = new ArrayList<ScheduledJobRunDetail>();
		
		for (ScheduledJobRun job : jobs) {
			list.add(from(job));
		}
		
		return list;
	}
}
