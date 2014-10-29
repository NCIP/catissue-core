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

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

// TODO: Auto-generated Javadoc
/**
 * The Class JobDetails.
 */
public class JobDetails implements Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 2993040313453649560L;

	/** The job id. */
	private long id;

	/** The status. */
	private String status;

	/** The log file. */
	private transient Blob logFile;

	/** The log file. */
	private String logFileName;

	/** The start time. */
	private Timestamp startTime;

	/** The job started by. */
	private long jobStartedBy;

	/** The job name. */
	private String jobName;

	/** The time taken. */
	private long timeTaken;

	/** The failed records count. */
	private long failedRecordsCount;

	/** The total records count. */
	private long totalRecordsCount;

	/** The current records processed. */
	private long currentRecordsProcessed;
	/**
	 * logFileBytes.
	 */
	private byte logFileBytes[];

	/**
	 * @return the arrayOfBytes
	 */
	public byte[] getLogFileBytes()
	{
		return logFileBytes;
	}

	/**
	 * @param arrayOfBytes the arrayOfBytes to set
	 */
	public void setLogFilesBytes(byte[] arrayOfBytes)
	{
		this.logFileBytes = arrayOfBytes;
	}

	/**
	 * Gets the job id.
	 *
	 * @return the job id
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * Sets the job id.
	 *
	 * @param jobId the new job id
	 */
	public void setId(long jobId)
	{
		this.id = jobId;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * Gets the log file.
	 *
	 * @return the log file
	 */
	public Blob getLogFile()
	{
		return logFile;
	}

	/**
	 * Sets the log file.
	 *
	 * @param logFile the new log file
	 */
	public void setLogFile(Blob logFile)
	{
		this.logFile = logFile;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Timestamp getStartTime()
	{
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(Timestamp startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * Gets the job started by.
	 *
	 * @return the job started by
	 */
	public long getJobStartedBy()
	{
		return jobStartedBy;
	}

	/**
	 * Sets the job started by.
	 *
	 * @param jobStartedBy the new job started by
	 */
	public void setJobStartedBy(long jobStartedBy)
	{
		this.jobStartedBy = jobStartedBy;
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
	 * Sets the job name.
	 *
	 * @param jobName the new job name
	 */
	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	/**
	 * Gets the time taken.
	 *
	 * @return the time taken
	 */
	public long getTimeTaken()
	{
		return timeTaken;
	}

	/**
	 * Sets the time taken.
	 *
	 * @param timeTaken the new time taken
	 */
	public void setTimeTaken(long timeTaken)
	{
		this.timeTaken = timeTaken;
	}

	/**
	 * Gets the failed records count.
	 *
	 * @return the failed records count
	 */
	public long getFailedRecordsCount()
	{
		return failedRecordsCount;
	}

	/**
	 * Sets the failed records count.
	 *
	 * @param failedRecordsCount the new failed records count
	 */
	public void setFailedRecordsCount(long failedRecordsCount)
	{
		this.failedRecordsCount = failedRecordsCount;
	}

	/**
	 * Gets the total records count.
	 *
	 * @return the total records count
	 */
	public long getTotalRecordsCount()
	{
		return totalRecordsCount;
	}

	/**
	 * Sets the total records count.
	 *
	 * @param totalRecordsCount the new total records count
	 */
	public void setTotalRecordsCount(long totalRecordsCount)
	{
		this.totalRecordsCount = totalRecordsCount;
	}

	/**
	 * Gets the current records processed.
	 *
	 * @return the current records processed
	 */
	public long getCurrentRecordsProcessed()
	{
		return currentRecordsProcessed;
	}

	/**
	 * Sets the current records processed.
	 *
	 * @param currentRecordsProcessed the new current records processed
	 */
	public void setCurrentRecordsProcessed(long currentRecordsProcessed)
	{
		this.currentRecordsProcessed = currentRecordsProcessed;
	}

	/**
	 * @return the logFileName
	 */
	public String getLogFileName()
	{
		return logFileName;
	}

	/**
	 * @param logFileName the logFileName to set
	 */
	public void setLogFileName(String logFileName)
	{
		this.logFileName = logFileName;
	}
}