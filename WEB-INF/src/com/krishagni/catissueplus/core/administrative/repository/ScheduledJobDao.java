package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.events.JobRunsListCriteria;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ScheduledJobDao extends Dao<ScheduledJob> {
	public List<ScheduledJob> getScheduledJobs(ScheduledJobListCriteria listCriteria);

	public Long getScheduledJobsCount(ScheduledJobListCriteria listCriteria);

	public ScheduledJobRun getJobRun(Long id);
	
	public ScheduledJob getJobByName(String name);
	
	public List<ScheduledJobRun> getJobRuns(JobRunsListCriteria listCriteria);

	public void saveOrUpdateJobRun(ScheduledJobRun job);
}
