package com.krishagni.catissueplus.core.administrative.services.impl;

public class ScheduledJobsMonitor implements Runnable {
	
	@Override
	public void run() {
		ScheduledTaskManager.Refresh();
	}

}
