package com.krishagni.catissueplus.bulkoperator.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationJob;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationJobDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class BulkOperationJobDaoImpl extends AbstractDao<BulkOperationJob> implements BulkOperationJobDao {
	private static final String FQN = BulkOperationJob.class.getName();
	
	private static final String GET_ALL_JOBS = FQN + ".getAllJobs";
			
	@Override
	public BulkOperationJob getJob(Long jobId) {
		return (BulkOperationJob)sessionFactory.getCurrentSession().get(BulkOperationJob.class, jobId);
	}

	//
	// TODO:
	// Changed this and got rid of addLimits
	//
	@Override
	@SuppressWarnings("unchecked")
	public List<BulkOperationJob> getJobs(int startAt, int maxRecords) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_ALL_JOBS)
				.setFirstResult(startAt)
				.setMaxResults(maxRecords)
				.list();
	}
}
