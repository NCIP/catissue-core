package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobInstance;
import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobFactory;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobInstanceDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ScheduledJobServiceImpl implements ScheduledJobService {

	private DaoFactory daoFactory;
	
	private ScheduledJobFactory jobFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setJobFactory(ScheduledJobFactory jobFactory) {
		this.jobFactory = jobFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ScheduledJobDetail>> getScheduledJobs(RequestEvent<ScheduledJobListCriteria> req) {
		try {
			List<ScheduledJob> jobs = daoFactory.getScheduledJobDao().getScheduledJobs(req.getPayload());
			return ResponseEvent.response(ScheduledJobDetail.from(jobs));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> createScheduledJob(RequestEvent<ScheduledJobDetail> req) {
		try {
			ScheduledJob job = jobFactory.createScheduledJob(req.getPayload());
			ensureUniqueName(job);
			
			daoFactory.getScheduledJobDao().saveOrUpdate(job);
			ScheduledJobDetail jobDetail = ScheduledJobDetail.from(job);
			ScheduledTaskManager.registerJob(jobDetail);
			return ResponseEvent.response(jobDetail);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> deleteScheduledJob(RequestEvent<Long> req) {
		try {
			Long jobId = req.getPayload();
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(jobId);
			if (job == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.NOT_FOUND);
			}
			
			job.delete();
			daoFactory.getScheduledJobDao().saveOrUpdate(job);
			ScheduledTaskManager.cancelJob(jobId);
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobInstanceDetail> getJobInstance(RequestEvent<Long> req) {
		try {
			Long id = req.getPayload();
			ScheduledJobInstance jobInstance = daoFactory.getScheduledJobDao().getScheduledJobInstance(id);
			
			if (jobInstance == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.JOB_INSTANCE_NOT_FOUND);
			}
			
			return ResponseEvent.response(ScheduledJobInstanceDetail.from(jobInstance));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobInstanceDetail> createScheduledJobInstance(RequestEvent<ScheduledJobInstanceDetail> req) {
		try {
			ScheduledJobDetail detail = req.getPayload().getScheduledJob();
			Long jobId = detail == null ? null : detail.getId();
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(jobId);
			if (job == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.NOT_FOUND);
			}

			ScheduledJobInstance instance = createJobInstance(req.getPayload());
			daoFactory.getScheduledJobDao().saveScheduledJobInstance(instance);
			return ResponseEvent.response(ScheduledJobInstanceDetail.from(instance));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobInstanceDetail> updateScheduledJobInstance(RequestEvent<ScheduledJobInstanceDetail> req) {
		try {
			ScheduledJobInstanceDetail detail = req.getPayload();
			Long jobInstanceId = detail.getId();
			ScheduledJobInstance existing = daoFactory.getScheduledJobDao().getScheduledJobInstance(jobInstanceId);
			
			if (existing == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.JOB_INSTANCE_NOT_FOUND);
			}
			
			ScheduledJobInstance instance = createJobInstance(detail);
			existing.update(instance);
			daoFactory.getScheduledJobDao().saveScheduledJobInstance(instance);
			return ResponseEvent.response(ScheduledJobInstanceDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private ScheduledJobInstance createJobInstance(ScheduledJobInstanceDetail detail) {
		ScheduledJobInstance job = new ScheduledJobInstance();
		job.setId(detail.getId());
		job.setLogFilePath(detail.getLogFilePath());
		job.setMessage(detail.getMessage());
		job.setScheduledJob(getScheduledJob(detail.getScheduledJob()));
		
		try {
			ScheduledJobInstance.Status status = ScheduledJobInstance.Status.valueOf(detail.getStatus()); 
			job.setStatus(status);
		} catch (IllegalArgumentException ile) {
			throw OpenSpecimenException.userError(ScheduledJobErrorCode.INVALID_STATUS);
		}
		
		return job;
	}
	
	private ScheduledJob getScheduledJob(ScheduledJobDetail detail) {
		ScheduledJob job = null;
		Long jobId = detail == null ? null : detail.getId();
		
		if (jobId != null) {
			job = daoFactory.getScheduledJobDao().getById(jobId);
		}
		
		if (job == null) {
			throw OpenSpecimenException.userError(ScheduledJobErrorCode.NOT_FOUND);
		}
		
		return job;
	}

	private void ensureUniqueName(ScheduledJob job) {

		if (daoFactory.getScheduledJobDao().getJobByName(job.getName()) != null) {
			throw OpenSpecimenException.userError(ScheduledJobErrorCode.DUP_JOB_NAME);
		}
	}
}
