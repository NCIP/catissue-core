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


public interface BulkJobInterface extends Runnable
{
	/**
	 * Gets the job data.
	 *
	 * @return the job data
	 */
	public JobData getJobData();

	/**
	 * Gets the job name.
	 * @return the job name
	 */
	public String getJobName();

	/**
	 * Gets the job started by.
	 *
	 * @return the job started by
	 */
	public String getJobStartedBy();



	/**
	 * Do job.
	 */
	public abstract void doJob();
}
