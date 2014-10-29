/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.jobmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.hibernate.Hibernate;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultJobStatusListner.
 */
public class DefaultJobStatusListner implements JobStatusListener
{

	/**
	 * LOGGER Logger - Generic LOGGER.
	 */
	private static final Logger logger = Logger.getCommonLogger(DefaultJobStatusListner.class);

	/* (non-Javadoc)
	 * @see com.krishagni.catissueplus.core.common.jobManager.JobStatusListener#jobStatusCreated
	 * (com.krishagni.catissueplus.core.common.jobManager.JobData)
	 */
	public void jobStatusCreated(final JobData jobData)
	{
		DAO dao = null;
		try
		{
			JobDetails jobDetails = new JobDetails();
			jobDetails.setJobName(jobData.getJobName());
			jobDetails.setJobStartedBy(Long.valueOf(jobData.getJobStartedBy()));
			jobDetails.setStartTime(jobData.getStartedTime());
			jobDetails.setStatus(jobData.getJobStatus());

			final IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(
					CommonServiceLocator.getInstance().getAppName());
			dao = daofactory.getDAO();
			dao.openSession(null);

			dao.insert(jobDetails);
			dao.commit();
			jobData.setJobID(jobDetails.getId());
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
		}
		finally
		{
			try
			{
				if (dao != null)
				{
					dao.closeSession();
				}
			}
			catch (final DAOException daoExp)
			{
				logger.error(daoExp.getMessage(), daoExp);
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.krishagni.catissueplus.core.common.jobManager.JobStatusListener#jobStatusUpdated
	 * (com.krishagni.catissueplus.core.common.jobManager.JobData)
	 */
	public void jobStatusUpdated(final JobData jobData)
	{

		FileInputStream fin = null;
		DAO dao = null;
		JobDetails jobDetails = new JobDetails();
		jobDetails.setId(jobData.getJobID());
		jobDetails.setJobName(jobData.getJobName());
		jobDetails.setJobStartedBy(Long.valueOf(jobData.getJobStartedBy()));
		jobDetails.setStatus(jobData.getJobStatus());
		jobDetails.setStartTime(jobData.getStartedTime());
		final Object fileObj = jobData.getJobStatusEntry().get(JobData.LOG_FILE_KEY);
		jobDetails.setCurrentRecordsProcessed(Long.valueOf(jobData.getJobStatusEntryValue(
				JobData.NO_OF_RECORDS_PROCESSED_KEY).toString()));
		jobDetails.setFailedRecordsCount(Long.valueOf(jobData.getJobStatusEntryValue(
				JobData.NO_OF_FAILED_RECORDS_KEY).toString()));
		jobDetails.setTimeTaken(Long.valueOf(jobData.getJobStatusEntryValue(JobData.TIME_TAKEN_KEY)
				.toString()));
		jobDetails.setTotalRecordsCount(Long.valueOf(jobData.getJobStatusEntryValue(
				JobData.NO_OF_TOTAL_RECORDS_KEY).toString()));
		jobDetails.setLogFileName(jobData.getJobStatusEntryValue(JobData.LOG_FILE_NAME_KEY)
				.toString());
		try
		{
			if (fileObj instanceof File)
			{
				fin = new FileInputStream((File) fileObj);

				jobDetails.setLogFile(Hibernate.createBlob(fin));
			}
			final IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(
					CommonServiceLocator.getInstance().getAppName());
			dao = daofactory.getDAO();
			dao.openSession(null);

			dao.update(jobDetails);
			dao.commit();
		}
		catch (FileNotFoundException ex)
		{
			logger.error(ex.getMessage(), ex);
		}
		catch (IOException ex)
		{
			logger.error(ex.getMessage(), ex);
		}
		catch (DAOException ex)
		{
			logger.error(ex.getMessage(), ex);
		}
		finally
		{
			try
			{
				if (dao != null)
				{
					dao.closeSession();
				}
			}
			catch (final DAOException daoExp)
			{
				logger.error(daoExp.getMessage(), daoExp);
			}
		}

	}

}
