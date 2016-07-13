package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

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
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class ScheduledJobServiceImpl implements ScheduledJobService, ApplicationListener<ContextRefreshedEvent> {
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
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> getScheduledJobsCount(RequestEvent<ScheduledJobListCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();
			return ResponseEvent.response(daoFactory.getScheduledJobDao().getScheduledJobsCount(req.getPayload()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> getScheduledJob(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();			
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(req.getPayload());
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}


	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> createScheduledJob(RequestEvent<ScheduledJobDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureCreateScheduledJobRights();
						
			ScheduledJob job = jobFactory.createScheduledJob(req.getPayload());
			job.setCreatedBy(AuthUtil.getCurrentUser());
			ensureUniqueJobName(job);
			
			daoFactory.getScheduledJobDao().saveOrUpdate(job);
			taskMgr.schedule(job);
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
			
			ScheduledJob job = jobFactory.createScheduledJob(req.getPayload());
			if (!existing.getName().equals(job.getName())) {
				ensureUniqueJobName(job);
			}

			existing.update(job);
			daoFactory.getScheduledJobDao().saveOrUpdate(existing);			
			taskMgr.schedule(existing);
			
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
			
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(req.getPayload());
			if (job == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.NOT_FOUND);
			}

			taskMgr.cancel(job);
			job.delete();
			daoFactory.getScheduledJobDao().saveOrUpdate(job);
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobDetail> executeJob(RequestEvent<ScheduledJobRunDetail> req) {
		try {			
			AccessCtrlMgr.getInstance().ensureRunJobRights();
			
			ScheduledJobRunDetail detail = req.getPayload();
			ScheduledJob job = daoFactory.getScheduledJobDao().getById(detail.getJobId());
			if (job == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.NOT_FOUND);
			}
			
			taskMgr.run(job, detail.getRtArgs());
			return ResponseEvent.response(ScheduledJobDetail.from(job));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	

	//////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Job Run APIs
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<ScheduledJobRunDetail>> getJobRuns(RequestEvent<JobRunsListCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();
			List<ScheduledJobRun> result = daoFactory.getScheduledJobDao().getJobRuns(req.getPayload());
			return ResponseEvent.response(ScheduledJobRunDetail.from(result));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}		
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ScheduledJobRunDetail> getJobRun(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadScheduledJobRights();
			
			ScheduledJobRun jobRun = daoFactory.getScheduledJobDao().getJobRun(req.getPayload());			
			if (jobRun == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.JOB_RUN_NOT_FOUND);
			}
			
			return ResponseEvent.response(ScheduledJobRunDetail.from(jobRun));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<JobExportDetail> getJobResultFile(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureRunJobRights();

			ScheduledJobRun jobRun = daoFactory.getScheduledJobDao().getJobRun(req.getPayload());
			if (jobRun == null) {
				return ResponseEvent.userError(ScheduledJobErrorCode.JOB_RUN_NOT_FOUND);
			}
			
			String path = jobRun.getLogFilePath();
			if (StringUtils.isBlank(path)) {
				return ResponseEvent.userError(ScheduledJobErrorCode.RESULT_DATA_FILE_NOT_AVAILABLE);
			}
			
			File f = new File(path);
			if (f.exists()) {
				ScheduledJobRunDetail detail = ScheduledJobRunDetail.from(jobRun);
				return ResponseEvent.response(new JobExportDetail(detail, f));
			} else {
				return ResponseEvent.userError(ScheduledJobErrorCode.RESULT_DATA_FILE_NOT_FOUND);
			}
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			loadAllJobs();
		} catch (Exception e) {
			throw new RuntimeException("Error loading all scheduled jobs", e);
		}
	}
	
	@PlusTransactional
	private void loadAllJobs() {
		ScheduledJobListCriteria crit = new ScheduledJobListCriteria().maxResults(5000000);			
		List<ScheduledJob> jobs = daoFactory.getScheduledJobDao().getScheduledJobs(crit);
			
		for (ScheduledJob job : jobs) {
			taskMgr.schedule(job);
		}			
	}
	
	private void ensureUniqueJobName(ScheduledJob job) {
		if (daoFactory.getScheduledJobDao().getJobByName(job.getName()) != null) {
			throw OpenSpecimenException.userError(ScheduledJobErrorCode.DUP_JOB_NAME);
		}
	}
}