package com.krishagni.catissueplus.bulkoperator.services.impl;

import java.util.List;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperation;
import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationJob;
import com.krishagni.catissueplus.bulkoperator.domain.factory.BulkOperationErrorCode;
import com.krishagni.catissueplus.bulkoperator.events.BulkImportRecordsEvent;
import com.krishagni.catissueplus.bulkoperator.events.BulkOperationDetail;
import com.krishagni.catissueplus.bulkoperator.events.BulkOperationsEvent;
import com.krishagni.catissueplus.bulkoperator.events.BulkRecordsImportedEvent;
import com.krishagni.catissueplus.bulkoperator.events.JobDetail;
import com.krishagni.catissueplus.bulkoperator.events.JobsDetailEvent;
import com.krishagni.catissueplus.bulkoperator.events.LogFileContentEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqBulkOperationsEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqJobsDetailEvent;
import com.krishagni.catissueplus.bulkoperator.events.ReqLogFileContentEvent;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationDao;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationJobDao;
import com.krishagni.catissueplus.bulkoperator.services.BulkOperationManager;
import com.krishagni.catissueplus.bulkoperator.services.BulkOperationService;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.core.common.PlusTransactional;

public class BulkOperationServiceImpl implements BulkOperationService {
	private BulkOperationManager bulkOperationManager;

	private BulkOperationDao bulkOperationDao;
	
	private BulkOperationJobDao jobDao;

	public void setBulkOperationManager(BulkOperationManager bulkOperationManager) {
		this.bulkOperationManager = bulkOperationManager;
	}

	public void setBulkOperationDao(BulkOperationDao bulkOperationDao) {
		this.bulkOperationDao = bulkOperationDao;
	}

	public void setJobDao(BulkOperationJobDao jobDao) {
		this.jobDao = jobDao;
	}

	@Override
	@PlusTransactional
	public BulkOperationsEvent getBulkOperations(ReqBulkOperationsEvent req) {
		try {
			List<BulkOperation> operations = bulkOperationDao.getBulkOperations();
			return BulkOperationsEvent.ok(BulkOperationDetail.from(operations));
		} catch (Exception e) {
			return BulkOperationsEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public JobsDetailEvent getJobs(ReqJobsDetailEvent req) {
		try {
			int startAt = req.getStartAt();
			if (startAt < 0) {
				startAt = 0;
			}
			
			int maxRecs = req.getMaxRecords();
			if (maxRecs <= 0) {
				maxRecs = 50;
			}
			
			List<BulkOperationJob> jobs = jobDao.getJobs(startAt, maxRecs);
			return JobsDetailEvent.ok(JobDetail.from(jobs));
		} catch (Exception e) {
			return JobsDetailEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public BulkRecordsImportedEvent bulkImportRecords(BulkImportRecordsEvent req) {
		try {
			BulkOperation bulkOperation = bulkOperationDao.getBulkOperation(req.getOperationName());
			if (bulkOperation == null) {
				return BulkRecordsImportedEvent.invalidRequest(BulkOperationErrorCode.INVALID_OPERATION_NAME, null);
			}
			
			Long jobId = bulkOperationManager.importRecords(req.getSessionDataBean(), bulkOperation, req.getFileIn());
			return BulkRecordsImportedEvent.ok(jobId);
		} catch (BulkOperationException be) {
			return BulkRecordsImportedEvent.invalidRequest(BulkOperationErrorCode.INVALID_CSV_TEMPLATE, null);
		} catch (Exception e) {
			return BulkRecordsImportedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public LogFileContentEvent getLogFileContent(ReqLogFileContentEvent req) {
		try {
			if (req.getJobId() == null) {
				return LogFileContentEvent.invalidRequest(BulkOperationErrorCode.MISSING_JOB_ID, null);
			}
			
			BulkOperationJob job = jobDao.getJob(req.getJobId());
			if (job == null) {
				return LogFileContentEvent.invalidRequest(BulkOperationErrorCode.INVALID_JOB_DETAILS, null);
			}
			
			return LogFileContentEvent.ok(job.getLogFileBytes(), job.getLogFileName()); 
		} catch (Exception e) {
			return LogFileContentEvent.serverError(e);
		}
	}

}
