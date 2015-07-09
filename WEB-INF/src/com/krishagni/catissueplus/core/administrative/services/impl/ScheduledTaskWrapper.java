package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskListener;

public class ScheduledTaskWrapper implements Runnable {
	private ScheduledJob job;
	
	private ScheduledTaskListener callback;

	private User runBy;

	public ScheduledTaskWrapper(ScheduledJob job, ScheduledTaskListener callback) {
		this.callback = callback;
		this.job = job;
	}

	public ScheduledTaskWrapper(ScheduledJob job, User runBy, ScheduledTaskListener callback) {
		this.callback = callback;
		this.job = job;
		this.runBy = runBy;
	}

	@Override
	public void run() {
		ScheduledJobRun jobRun = null;
		try {
			jobRun = callback.started(job);
			jobRun.setRunBy(runBy);
			job.newTask().doJob(jobRun);
			callback.completed(jobRun); 
		} catch (Exception e) {
			callback.failed(jobRun, e);
		} 
	}
}
