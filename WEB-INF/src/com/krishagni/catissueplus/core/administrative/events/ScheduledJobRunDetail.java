package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ScheduledJobRunDetail {
	private Long id;
	
	private ScheduledJobDetail scheduledJob;
	
	private Date startedAt;
	
	private Date finishedAt;
	
	private String status;
	
	private String logFilePath;
	
	private String message;

	private UserSummary runBy;

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

	public static ScheduledJobRunDetail from(ScheduledJobRun job) {
		ScheduledJobRunDetail detail = new ScheduledJobRunDetail();
		detail.setId(job.getId());
		detail.setScheduledJob(ScheduledJobDetail.from(job.getScheduledJob()));
		detail.setStatus(job.getStatus().toString());
		detail.setMessage(job.getMessage());
		detail.setLogFilePath(job.getLogFilePath());
		detail.setStartedAt(job.getStartedAt());
		detail.setFinishedAt(job.getFinishedAt());

		if (job.getScheduledJob().isOnDemand() && job.getRunBy() != null) {
			detail.setRunBy(UserSummary.from(job.getRunBy()));
		}

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
