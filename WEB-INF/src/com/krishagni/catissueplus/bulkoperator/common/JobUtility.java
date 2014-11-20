package com.krishagni.catissueplus.bulkoperator.common;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Hibernate;

import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationJob;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationJobDao;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class JobUtility {

	public static BulkOperationJob createJob(String operationName, Long userId, BulkOperationJobDao jobDao, UserDao userDao) {
		try {
			BulkOperationJob job = new BulkOperationJob();
			job.setName(operationName);
			
			User user = userDao.getUser(userId);
			
			if (user == null) {
				ObjectCreationException oce = new ObjectCreationException();
				oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, "user-id");
				throw oce;
			}
			
			job.setStartedBy(user);
			job.setStartTime(new Timestamp(new Date().getTime()));
			job.setStatus(JobStatusConstants.JOB_IN_PROGRESS_STATUS);
			jobDao.saveOrUpdate(job);
			return job;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static BulkOperationJob updateJob(Long jobId, String status, Long numProcessedRecords, Long failedRecords, Long totalRecords, Long timeTaken, 
			String logFileName, File logFile, BulkOperationJobDao jobDao) {
		BulkOperationJob job = jobDao.getJob(jobId);
		
		try {
			if (job != null) {
				job.setStatus(status);
				job.setProcessedRecords(numProcessedRecords);
				job.setFailedRecordsCount(failedRecords);
				job.setTotalRecordsCount(totalRecords);
				job.setTimeTaken(timeTaken);
				job.setLogFileName(logFileName);
				
				job.setLogFile(getFileContents(logFile));
				jobDao.saveOrUpdate(job);
			} else {
				throw new RuntimeException("Invalid Job Id");
			}
			
			return job;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static Blob getFileContents(File logFile) {
		if (logFile != null) {
			FileInputStream fin = null;
			
			try {
				fin = new FileInputStream(logFile);
				return Hibernate.createBlob(fin);
			} catch (Exception e) {
				throw new RuntimeException("Error saving logfile to database: " + e.getMessage());
			}
		} else {
			return null;
		}
	}
}
