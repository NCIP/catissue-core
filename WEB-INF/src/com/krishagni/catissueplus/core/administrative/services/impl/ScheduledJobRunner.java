package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;

public class ScheduledJobRunner implements Runnable {
	private ScheduledTask task;
	
	private ScheduledJobDetail jobDetail;
	
	public ScheduledJobRunner(ScheduledTask task, ScheduledJobDetail jobDetail) {
		this.task = task;
		this.jobDetail = jobDetail;
	}
	
	@Override
	public void run() {
		
		try {
			task.doJob(jobDetail);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
