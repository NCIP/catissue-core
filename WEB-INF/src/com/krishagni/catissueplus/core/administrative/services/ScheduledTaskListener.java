package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;

public interface ScheduledTaskListener {

	public ScheduledJobRun started(ScheduledJob job, String args, User runBy);
	
	public void completed(ScheduledJobRun jobRun);
	
	public void failed(ScheduledJobRun jobRun, Exception e);
}
