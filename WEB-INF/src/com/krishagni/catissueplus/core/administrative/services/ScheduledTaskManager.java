package com.krishagni.catissueplus.core.administrative.services;

import java.util.concurrent.ScheduledFuture;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;

public interface ScheduledTaskManager {
	void schedule(ScheduledJob job);
	
	void schedule(Long jobId);

	void run(ScheduledJob job, String args);

	void cancel(ScheduledJob job);

	//
	// for internal scheduled tasks
	//
	ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, int intervalInMinutes);
}
