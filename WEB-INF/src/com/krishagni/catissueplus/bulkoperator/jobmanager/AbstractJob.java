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

// TODO: Auto-generated Javadoc
/**
 * The Class Job.
 *
 * @author nitesh_marwaha
 */
public abstract class AbstractJob implements BulkJobInterface
{

	/** The job data. */
	private JobData jobData;

	/** The job status listener. */
	private JobStatusListener jobStatusListener;

	/** The job name. */
	private String jobName;

	/** The job started by. */
	private String jobStartedBy;

	/**
	 * Instantiates a new job.
	 *
	 * @param jobName the job name
	 * @param jobStartedBy the job started by
	 * @param jobStatusListener the job status listener
	 */
	protected AbstractJob(final String jobName, final String jobStartedBy,
			final JobStatusListener jobStatusListener)
	{
		this.jobStatusListener = jobStatusListener;
		this.jobName = jobName;
		this.jobStartedBy = jobStartedBy;
	}

	/**
	 * Instantiates a new job.
	 *
	 * @param jobName the job name
	 * @param jobStartedBy the job started by
	 * @param jobStatusListener the job status listener
	 */
	protected AbstractJob(final String jobName, final String jobStartedBy,
			final JobStatusListener jobStatusListener,String jobType,Integer threshhold)
	{
		this.jobStatusListener = jobStatusListener;
		this.jobName = jobName;
		this.jobStartedBy = jobStartedBy;

	}


	/**
	 * Gets the job data.
	 *
	 * @return the job data
	 */
	public JobData getJobData()
	{
		return jobData;
	}

	/**
	 * Gets the job name.
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
	 * Run method
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		jobData = new JobData(jobName, jobStartedBy, jobStatusListener);
		doJob();
	}

	/**
	 * Do job.
	 */
	public abstract void doJob();

}
