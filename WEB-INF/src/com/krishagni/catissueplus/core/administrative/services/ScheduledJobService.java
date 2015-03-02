package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobInstanceDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ScheduledJobService {
	/*
	 * job api's
	 */
	public ResponseEvent<List<ScheduledJobDetail>> getScheduledJobs(RequestEvent<ScheduledJobListCriteria> req);
	
	public ResponseEvent<ScheduledJobDetail> createScheduledJob(RequestEvent<ScheduledJobDetail> req);
	
	public ResponseEvent<ScheduledJobDetail> deleteScheduledJob(RequestEvent<Long> req);
	
	/*
	 * Job instance api's
	 */
	public ResponseEvent<ScheduledJobInstanceDetail> getJobInstance(RequestEvent<Long> req);
	
	public ResponseEvent<ScheduledJobInstanceDetail> createScheduledJobInstance(RequestEvent<ScheduledJobInstanceDetail> req);
	
	public ResponseEvent<ScheduledJobInstanceDetail> updateScheduledJobInstance(RequestEvent<ScheduledJobInstanceDetail> req);
}




