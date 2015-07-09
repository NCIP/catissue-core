package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobFactory;
import com.krishagni.catissueplus.core.administrative.events.JobExportDetail;
import com.krishagni.catissueplus.core.administrative.events.JobRunsListCriteria;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobRunDetail;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskManager;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class ScheduledJobServiceImpl implements ScheduledJobService, InitializingBean {
	private DaoFactory daoFactory;
	
	private ScheduledJobFactory jobFactory;
	
	private ScheduledTaskManager taskMgr;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setJobFactory(ScheduledJobFactory jobFactory) {
		this.jobFactory = jobFactory;
	}

	public void setTaskMgr(ScheduledTaskManager taskMgr) {
		this.taskMgr = taskMgr;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ScheduledJobDetail>> getScheduledJobs(RequestEvent<ScheduledJobListCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();
			List<ScheduledJob> jobs = daoFactory.getScheduledJobDao().getScheduledJobs(req.getPayload());

			return ResponseEvent.response(ScheduledJobDetail.from(jobs));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> getScheduledJob(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();
			Long jobId = req.getPayload();
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(jobId);
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}


	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> createScheduledJob(RequestEvent<ScheduledJobDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureCreateScheduledJobRights();
			ScheduledJobDetail detail = req.getPayload();
			detail.setCreatedBy(getCurrentLoggedInUser());
			ScheduledJob job = jobFactory.createScheduledJob(detail);
			ensureUniqueName(job);
			
			daoFactory.getScheduledJobDao().saveOrUpdate(job);
			if (!detail.getRepeatSchedule().equals(ScheduledJob.RepeatSchedule.ONDEMAND.toString())) {
				taskMgr.schedule(job);
			}
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> updateScheduledJob(RequestEvent<ScheduledJobDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUpdateScheduledJobRights();
			Long jobId = req.getPayload().getId();
			ScheduledJob existing = daoFactory.getScheduledJobDao().getById(jobId);
			if (existing == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.NOT_FOUND);
			}
			
			ScheduledJobDetail detail = req.getPayload();
			detail.setCreatedBy(getCurrentLoggedInUser());
			ScheduledJob job = jobFactory.createScheduledJob(detail);
			if (!existing.getName().equals(job.getName())) {
				ensureUniqueName(job);
			}

			existing.update(job);
			daoFactory.getScheduledJobDao().saveOrUpdate(existing);

			if (!detail.getRepeatSchedule().equals(ScheduledJob.RepeatSchedule.ONDEMAND.toString())) {
				taskMgr.schedule(existing);
			}

			return ResponseEvent.response(ScheduledJobDetail.from(existing));
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
			AccessCtrlMgr.getInstance().ensureDeleteScheduledJobRights();
			Long jobId = req.getPayload();
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(jobId);
			if (job == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.NOT_FOUND);
			}

			taskMgr.cancel(job);
			job.delete();
			daoFactory.getScheduledJobDao().saveOrUpdate(job);
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> executeJob(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureRunScheduledJobRights();
			Long jobId = req.getPayload();
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(jobId);
			if (job == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.NOT_FOUND);
			}
			taskMgr.run(job, AuthUtil.getCurrentUser());
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	
	/*
	 *	Job run api's 
	 */

	@Override
	@PlusTransactional
	public ResponseEvent<List<ScheduledJobRunDetail>> getJobRuns(RequestEvent<JobRunsListCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();
			List<ScheduledJobRun> result = daoFactory.getScheduledJobDao().getJobRuns(req.getPayload());
			return ResponseEvent.response(ScheduledJobRunDetail.from(result));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
		
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobRunDetail> getJobRun(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();
			Long id = req.getPayload();
			ScheduledJobRun jobRun = daoFactory.getScheduledJobDao().getJobRun(id);
			
			if (jobRun == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.JOB_RUN_NOT_FOUND);
			}
			
			return ResponseEvent.response(ScheduledJobRunDetail.from(jobRun));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<JobExportDetail> getExportDataFile(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureRunScheduledJobRights();
			Long jobRunId = req.getPayload();
			ScheduledJobRun jobRun = daoFactory.getScheduledJobDao().getJobRun(jobRunId);
			if (jobRun == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.JOB_RUN_NOT_FOUND);
			}
			
			String path = jobRun.getLogFilePath();
			if (StringUtils.isBlank(path)) {
				return ResponseEvent.userError(ScheduledJobErrorCode.EXPORT_DATA_FILE_NOT_AVAILABLE);
			}
			
			File f = new File(path);
			if (f.exists()) {
				ScheduledJobRunDetail detail = ScheduledJobRunDetail.from(jobRun);
				return ResponseEvent.response(new JobExportDetail(detail, f));
			} else {
				return ResponseEvent.userError(ScheduledJobErrorCode.EXPORT_DATA_FILE_NOT_FOUND);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			loadAllJobs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PlusTransactional
	public void loadAllJobs() {
		try {
			ScheduledJobListCriteria crit = new ScheduledJobListCriteria()
					.maxResults(5000000);
			List<ScheduledJob> jobs = daoFactory.getScheduledJobDao().getScheduledJobs(crit);
			for (ScheduledJob job : jobs) {
				taskMgr.schedule(job);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * Private methods
	 */
	private UserSummary getCurrentLoggedInUser() {
		return UserSummary.from(AuthUtil.getCurrentUser());
	}
	
	private void ensureUniqueName(ScheduledJob job) {

		if (daoFactory.getScheduledJobDao().getJobByName(job.getName()) != null) {
			throw OpenSpecimenException.userError(ScheduledJobErrorCode.DUP_JOB_NAME);
		}
	}
}
