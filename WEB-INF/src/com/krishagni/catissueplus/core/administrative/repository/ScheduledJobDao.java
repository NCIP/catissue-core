package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobInstance;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ScheduledJobDao extends Dao<ScheduledJob> {
	public List<ScheduledJob> getScheduledJobs(ScheduledJobListCriteria listCriteria);
	
	public ScheduledJobInstance getScheduledJobInstance(Long id);
	
	public ScheduledJob getJobByName(String name);
	
	public void saveScheduledJobInstance(ScheduledJobInstance job);
}
