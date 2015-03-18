package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;

public interface ScheduledTaskManager {
	public void schedule(ScheduledJob job);
	
	public void cancel(ScheduledJob job);
}
