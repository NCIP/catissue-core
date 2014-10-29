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

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class JobData.
 *
 * @author nitesh_marwaha
 */
public class JobData
{

	/** The job status listener. */
	private JobStatusListener jobStatusListener;

	/** The job status entry. */
	private Map<Object, Object> jobStatusEntry;

	/** The job status. */
	private String jobStatus;

	/** The job id. */
	private Long jobID;

	/** The job name. */
	private String jobName;

	/** The job started by. */
	private String jobStartedBy;

	/** The started time. */
	private Timestamp startedTime;

	/**
	 * Gets the started time.
	 *
	 * @return the started time
	 */
	public Timestamp getStartedTime()
	{
		return startedTime;
	}

	/** The Constant JOB_COMPLETED_STATUS. */
	public final static String JOB_COMPLETED_STATUS = "Completed";

	/** The Constant JOB_FAILED_STATUS. */
	public final static String JOB_FAILED_STATUS = "Failed";

	/** The Constant JOB_IN_PROGRESS_STATUS. */
	public final static String JOB_IN_PROGRESS_STATUS = "In Progress";

	/** The Constant NO_OF_RECORDS_PROCESSED_KEY. */
	public final static String NO_OF_RECORDS_PROCESSED_KEY = "NO_OF_RECORDS_PROCESSED";

	/** The Constant NO_OF_FAILED_RECORDS_KEY. */
	public final static String NO_OF_FAILED_RECORDS_KEY = "NO_OF_FAILED_RECORDS";

	public final static String NO_OF_TOTAL_RECORDS_KEY="NO_OF_TOTAL_RECORDS";

	/** The Constant JOB_STATUS_KEY. */
	public final static String JOB_STATUS_KEY = "JOB_STATUS_KEY";

	/** The Constant TIME_TAKEN_KEY. */
	public final static String TIME_TAKEN_KEY = "TIME_TAKEN_KEY";

	/** The constant LOG_FILE_KEY. */
	public final static String LOG_FILE_KEY = "LOG_FILE_KEY";

	/** The constant LOG_FILE_NAME_KEY. */
	public final static String LOG_FILE_NAME_KEY = "LOG_FILE_NAME_KEY";

	/**
	 * Instantiates a new job data.
	 *
	 * @param jobName the job name
	 * @param jobStartedBy the job started by
	 * @param listener the listener
	 */
	protected JobData(final String jobName, final String jobStartedBy,
			final JobStatusListener listener)
	{
		this.jobStatusListener = listener;
		this.jobStatusEntry = new Hashtable<Object, Object>();
		this.jobStatus = JOB_IN_PROGRESS_STATUS;
		this.jobName = jobName;
		this.jobStartedBy = jobStartedBy;
		this.startedTime = new Timestamp(System.currentTimeMillis());  
		this.jobStatusListener.jobStatusCreated(this);
	}

	/**
	 * Gets the job status entry value.
	 *
	 * @param key the key
	 *
	 * @return the job status entry value
	 */
	public Object getJobStatusEntryValue(final Object key)
	{
		return jobStatusEntry.get(key);
	}

	/**
	 * Gets the job id.
	 *
	 * @return the job id
	 */
	public Long getJobID()
	{
		return jobID;
	}

	/**
	 * Sets the job id.
	 *
	 * @param jobId the job id
	 *
	 * @return the long
	 */
	public void setJobID(final long jobId)
	{
		this.jobID = jobId;
	}

	/**
	 * Gets the job name.
	 *
	 * @return the job name
	 */
	public String getJobName()
	{
		return jobName;
	}

	/**
	 * Gets the job started by.
	 *
	 * @return the job started by
	 */
	public String getJobStartedBy()
	{
		return jobStartedBy;
	}

	/**
	 * Gets the job status.
	 *
	 * @return the job status
	 */
	public String getJobStatus()
	{
		return jobStatus;
	}

	/**
	 * Update job status.
	 *
	 * @param keys the keys
	 * @param values the values
	 * @param jobStatus the job status
	 */
	public void updateJobStatus(final Object[] keys, final Object[] values, final String jobStatus)
	{
		for (int i = 0; i < keys.length; i++)
		{
			jobStatusEntry.put(keys[i], values[i]);
		}
		this.jobStatus = jobStatus;
		jobStatusListener.jobStatusUpdated(this);
	}

	/**
	 * Gets the job status entry.
	 *
	 * @return the job status entry
	 */
	public Map getJobStatusEntry()
	{
		return this.jobStatusEntry;
	}
}
