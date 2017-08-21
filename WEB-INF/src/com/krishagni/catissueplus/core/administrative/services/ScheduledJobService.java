package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.JobExportDetail;
import com.krishagni.catissueplus.core.administrative.events.JobRunsListCriteria;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobRunDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ScheduledJobService {
	/*
	 * job api's
	 */
	public ResponseEvent<List<ScheduledJobDetail>> getScheduledJobs(RequestEvent<ScheduledJobListCriteria> req);

	public ResponseEvent<Long> getScheduledJobsCount(RequestEvent<ScheduledJobListCriteria> req);

	public ResponseEvent<ScheduledJobDetail> getScheduledJob(RequestEvent<Long> req);

	public ResponseEvent<ScheduledJobDetail> createScheduledJob(RequestEvent<ScheduledJobDetail> req);
	
	public ResponseEvent<ScheduledJobDetail> updateScheduledJob(RequestEvent<ScheduledJobDetail> req);
	
	public ResponseEvent<ScheduledJobDetail> deleteScheduledJob(RequestEvent<Long> req);

	public ResponseEvent<ScheduledJobDetail>  executeJob(RequestEvent<ScheduledJobRunDetail> request);


	/*
	 * Job instance api's
	 */
	public ResponseEvent<List<ScheduledJobRunDetail>> getJobRuns(RequestEvent<JobRunsListCriteria> req);
	
	public ResponseEvent<ScheduledJobRunDetail> getJobRun(RequestEvent<Long> req);

	public ResponseEvent<JobExportDetail> getJobResultFile(RequestEvent<Long> req);

}




