package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;

public interface ScheduledTaskListener {

	public ScheduledJobRun started(ScheduledJob job);
	
	public void completed(ScheduledJobRun jobRun);
	
	public void failed(ScheduledJobRun jobRun, Exception e);
}
