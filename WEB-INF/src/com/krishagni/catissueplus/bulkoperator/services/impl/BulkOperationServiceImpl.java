package com.krishagni.catissueplus.bulkoperator.services.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.krishagni.catissueplus.bulkoperator.common.BulkImporterTask;
import com.krishagni.catissueplus.bulkoperator.domain.BulkOperation;
import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationErrorCode;
import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationJob;
import com.krishagni.catissueplus.bulkoperator.events.BulkOperationDetail;
import com.krishagni.catissueplus.bulkoperator.events.ImportRecordsOpDetail;
import com.krishagni.catissueplus.bulkoperator.events.JobDetail;
import com.krishagni.catissueplus.bulkoperator.events.ListJobsCriteria;
import com.krishagni.catissueplus.bulkoperator.events.LogFileContent;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationDao;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationJobDao;
import com.krishagni.catissueplus.bulkoperator.services.BulkOperationService;
import com.krishagni.catissueplus.bulkoperator.services.ObjectImporterFactory;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BulkOperationServiceImpl implements BulkOperationService {
	private BulkOperationDao bulkOperationDao;
	
	private BulkOperationJobDao jobDao;
	
	private UserDao userDao;
	
	private ObjectImporterFactory importerFactory;
	
	private static final String IMPORT_STARTED_SUCCESSFULLY = "Import started successfully.";
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(5);

	public void setBulkOperationDao(BulkOperationDao bulkOperationDao) {
		this.bulkOperationDao = bulkOperationDao;
	}

	public void setJobDao(BulkOperationJobDao jobDao) {
		this.jobDao = jobDao;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setImporterFactory(ObjectImporterFactory importerFactory) {
		this.importerFactory = importerFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<BulkOperationDetail>> getBulkOperations(RequestEvent<?> req) {
		try {
			List<BulkOperation> operations = bulkOperationDao.getBulkOperations();
			return ResponseEvent.response(BulkOperationDetail.from(operations));
		} catch (Exception e) {
			return ResponseEvent.error(new OpenSpecimenException(e));
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<JobDetail>> getJobs(RequestEvent<ListJobsCriteria> req) {
		try {
			ListJobsCriteria crit = req.getPayload();
			
			int startAt = crit.startAt();
			if (startAt < 0) {
				startAt = 0;
			}
			
			int maxRecs = crit.maxResults();
			if (maxRecs <= 0) {
				maxRecs = 50;
			}
			
			List<BulkOperationJob> jobs = jobDao.getJobs(startAt, maxRecs);
			return ResponseEvent.response(JobDetail.from(jobs));
		} catch (Exception e) {
			return ResponseEvent.error(new OpenSpecimenException(e));
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<String> bulkImportRecords(RequestEvent<ImportRecordsOpDetail> req) {
		try {
			ImportRecordsOpDetail opDetail = req.getPayload(); 
			
			BulkOperation bulkOperation = bulkOperationDao.getBulkOperation(opDetail.getOperationName());
			if (bulkOperation == null) {
				return ResponseEvent.userError(BulkOperationErrorCode.INVALID_OP_NAME);
			}
			
			BulkImporterTask bulkImporterTask = new BulkImporterTask(
					importerFactory, 
					userDao, 
					jobDao, 
					bulkOperation, 
					req.getSessionDataBean(), 
					opDetail.getFileIn());
			bulkImporterTask.validateBulkOperation();
			
			threadPool.execute(bulkImporterTask);
			return ResponseEvent.response(IMPORT_STARTED_SUCCESSFULLY);
		} catch (BulkOperationException be) {
			return ResponseEvent.userError(BulkOperationErrorCode.INVALID_CSV_TEMPLATE);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<LogFileContent> getLogFileContent(RequestEvent<Long> req) {
		try {
			Long jobId = req.getPayload();
			
			if (jobId == null) {
				return ResponseEvent.userError(BulkOperationErrorCode.REQUIRED_JOB_ID);
			}
			
			BulkOperationJob job = jobDao.getJob(jobId);
			if (job == null) {
				return ResponseEvent.userError(BulkOperationErrorCode.INVALID_JOB_ID);
			}
			
			LogFileContent fileContent = new LogFileContent();
			fileContent.setFileName(job.getLogFileName());
			fileContent.setLogFileContents(job.getLogFileBytes());			
			return ResponseEvent.response(fileContent); 
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
