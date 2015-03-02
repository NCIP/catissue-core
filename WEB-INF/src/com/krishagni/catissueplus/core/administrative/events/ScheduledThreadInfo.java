package com.krishagni.catissueplus.core.administrative.events;

import java.util.concurrent.ScheduledFuture;

public class ScheduledThreadInfo {
	private ScheduledFuture task;
	
	private ScheduledJobDetail jobDetail;
	
	public ScheduledThreadInfo(ScheduledFuture task, ScheduledJobDetail jobDetail) {
		this.task = task;
		this.jobDetail = jobDetail;
	}

	public ScheduledFuture getTask() {
		return task;
	}

	public void setTask(ScheduledFuture task) {
		this.task = task;
	}

	public ScheduledJobDetail getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(ScheduledJobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public void cancelJob() {
		if (task != null) {
			task.cancel(false);
		}
	}
}
