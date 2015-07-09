package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.User;

public interface ScheduledTaskManager {
	public void schedule(ScheduledJob job);

	public void run(ScheduledJob job, User currentUser);

	public void cancel(ScheduledJob job);
}
