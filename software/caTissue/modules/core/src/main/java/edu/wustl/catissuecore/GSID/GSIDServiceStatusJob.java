/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.GSID;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GSIDServiceStatusJob implements Job {
	
	public static String JOB_NAME = "GSIID_SERVICE_JOB";
	public static String TRIGGER_NAME = "GSIID_SERVICE_JOB_TRIGGER";
	public static String CTX_ARRY = "CTX_GSID_ARRAY";
	
	//private static final Log LOG = LogFactory.getLog(GSIDServiceStatusJob.class);
	private static Logger LOG = Logger.getLogger(GSIDClient.class);
	
	@Override
	public void execute(JobExecutionContext cntxt) throws JobExecutionException {
		 // if the gsid service is up , will send an email to admins and and unschedule if the job exists and return  ...
		
		LOG.debug("Executing GSIDServiceStatusJob .. ");
		
		//Object arrayObj = cntxt.getJobDetail().getJobDataMap().get(CTX_ARRY);
		//if (arrayObj != null) {
			//String[] arrayObjS = (String[])arrayObj;
			GSIDServiceStatusNotifier notifier = new GSIDServiceStatusNotifier();
			if (notifier.checkServiceStatus()) {
				notifier.sendServiceStatusSuccessEmail();
				notifier.unScheduleJob();
			}
		//}
		 
	}
}
