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

public class Constants
{
	/**
	 * bulk_operation_log table name
	 */
	public static final String BULK_OPERATION_LOG_TABLE = "bulk_operation_log";

	public static final String BULK_OPERATION_TYPE_MATCH = "BULK MATCHING";

	public static final String BULK_OPERATION_TYPE_LOAD = "BULK EMPI LOADING";

	public static final String BULK_OPERATION_TYPE_GENERATE = "BULK EMPI GENERATION";
	public static final String BULK_EMPI_SLEEP_TIME = "BulkEmpiSleepTime";
}
