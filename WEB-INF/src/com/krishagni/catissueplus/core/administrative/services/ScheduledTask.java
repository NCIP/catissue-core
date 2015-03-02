package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;

public interface ScheduledTask {

	public void doJob(ScheduledJobDetail detail) throws Exception;
}
