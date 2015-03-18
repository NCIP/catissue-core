package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskListener;

public class ScheduledTaskWrapper implements Runnable {
	private ScheduledJob job;
	
	private ScheduledTaskListener callback;
	
	public ScheduledTaskWrapper(ScheduledJob job, ScheduledTaskListener callback) {
		this.callback = callback;
		this.job = job;
	}
	
	@Override
	public void run() {
		ScheduledJobRun jobRun = null;
		try {
			jobRun = callback.started(job);
			job.newTask().doJob(jobRun);
			callback.completed(jobRun); 
		} catch (Exception e) {
			callback.failed(jobRun, e);
		} 
	}
}
