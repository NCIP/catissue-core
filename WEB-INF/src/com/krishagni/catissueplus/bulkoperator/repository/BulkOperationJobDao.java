package com.krishagni.catissueplus.bulkoperator.repository;

import java.util.List;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationJob;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface BulkOperationJobDao extends Dao<BulkOperationJob> {
	public BulkOperationJob getJob(Long jobId);
	
	public List<BulkOperationJob> getJobs(int startAt, int maxRecords);
}
