package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskListener;

public class ScheduledTaskWrapper implements Runnable {
	private ScheduledJob job;
	
	private String args;
	
	private User runBy;
	
	private ScheduledTaskListener callback;

	public ScheduledTaskWrapper(ScheduledJob job, String args, User runBy, ScheduledTaskListener callback) {
		this.job = job;
		this.args = args;
		this.runBy = runBy;
		this.callback = callback;		
	}

	@Override
	public void run() {
		ScheduledJobRun jobRun = null;
		try {			
			jobRun = callback.started(job, args, runBy);			
			job.newTask().doJob(jobRun);
			callback.completed(jobRun); 
		} catch (Exception e) {
			e.printStackTrace();
			callback.failed(jobRun, e);
		} 
	}
}