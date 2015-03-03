package com.krishagni.catissueplus.core.administrative.services.impl;

public class ScheduledJobsMonitor implements Runnable {
	
	@Override
	public void run() {
		try {
			ScheduledTaskManager.checkJobsStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
