
package edu.wustl.catissuecore.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import edu.wustl.catissuecore.keywordsearch.TitliIndexer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * Quartz Job for indexing keyword search.
 * @author Ashraf
 *
 */
public class TitliIndexerJob implements Job
{

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getCommonLogger(TitliIndexerJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
	{
		logger.debug("Starting execution of TitliIndexerJob");
		TitliIndexer.main(null);//run indexer
		logger.debug("End of execution of TitliIndexerJob");
	}

	/**
	 * This method calls the Quartz job.
	 * @throws Exception 
	 */

	public static void scheduleTitliIndexerJob() throws Exception
	{
		final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		final Scheduler scheduler = schedulerFactory.getScheduler();//get scheduler
		scheduler.start();//start scheduler

		final JobDetail detail = new JobDetail("TitliIndexerJob", Scheduler.DEFAULT_GROUP,
				TitliIndexerJob.class);//create a job detail with name "TitliIndexerJob"

		final Long repeatInterval = 86400000L;//(for daily)interval in milliseconds after which job will get repeated

		logger.debug("Repeat interval for TitliIndexerJob : " + repeatInterval);

		final SimpleTrigger trigger = new SimpleTrigger("TitliIndexerJobTrigger",
				Scheduler.DEFAULT_GROUP, getStartTime(), null, SimpleTrigger.REPEAT_INDEFINITELY,
				repeatInterval);//create a trigger to run the job

		scheduler.scheduleJob(detail, trigger);//scheduling the job
	}

	/**
	 * Returns start time for TitliIndexerJob.
	 * @return
	 * @throws Exception
	 */
	private static Date getStartTime() throws Exception
	{
		Integer hours = getHours();//get hours part of time
		Integer minutes = getMinutes();//get minutes part of time

		//set date and time
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		logger.debug("Start Time for TitliIndexerJob : " + hours + ":" + minutes);

		return calendar.getTime();
	}

	/**
	 * Returns Minutes part of the time at which scheduler should run.
	 * @return
	 * @throws Exception 
	 */
	private static Integer getMinutes() throws Exception
	{
		Integer minutes = null;
		String minuteString = XMLPropertyHandler
				.getValue(Constants.KEYWORD_SEARCH_INDEXER_SCHEDULER_MINUTES);//get minutes value from properties file

		if (minuteString.trim().equals(""))
		{
			minutes = new Integer(Constants.DEFAULT_SCHEDULER_RUN_TIME_MINUTES);//set default minutes if property is empty
		}
		else
		{
			//validate minutes from properties file
			if (!minuteString.matches("([0-5][0-9])"))
			{
				logger.error(
						"Failed to schedule TitliIndexerJob, invalid keyword.search.indexer.scheduler.minutes property",
						new Exception());
				throw new Exception(
						"Failed to schedule TitliIndexerJob, invalid keyword.search.indexer.scheduler.minutes property");
			}
			minutes = new Integer(minuteString);//set minutes from property
		}

		return minutes;
	}

	/**
	 * Returns Hours part of the time at which scheduler should run.
	 * @return
	 * @throws Exception 
	 */
	private static Integer getHours() throws Exception
	{
		Integer hours = null;

		String hourString = XMLPropertyHandler
				.getValue(Constants.KEYWORD_SEARCH_INDEXER_SCHEDULER_HOURS_OF_DAY);//get hours value from properties file 

		if (hourString.trim().equals(""))
		{
			hours = new Integer(Constants.DEFAULT_SCHEDULER_RUN_TIME_HOURS);//set default hours if property is empty
		}
		else
		{
			//Validate hours from properties file
			if (!hourString.matches("([0-1][0-9]|2[0-3])"))
			{
				logger.error(
						"Failed to schedule TitliIndexerJob, invalid keyword.search.indexer.scheduler.hours.of.day property",
						new Exception());
				throw new Exception(
						"Failed to schedule TitliIndexerJob, invalid keyword.search.indexer.scheduler.hours.of.day property");
			}
			hours = new Integer(hourString);//set hours from property
		}

		return hours;
	}
}
