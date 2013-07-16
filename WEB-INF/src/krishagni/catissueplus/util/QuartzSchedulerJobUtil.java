
package krishagni.catissueplus.util;

import java.util.Calendar;
import java.util.Date;

import krishagni.catissueplus.scheduler.QuartzSchedulerJob;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

public class QuartzSchedulerJobUtil
{

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getCommonLogger(QuartzSchedulerJobUtil.class);

    /**
     * Returns Minutes part of the time at which scheduler should run.
     * @return
     * @throws Exception 
     */
    private static Integer getMinutes(String minutePropertyString) throws Exception
    {
        Integer minutes = null;
        //get minutes value from properties file
        String minuteString = XMLPropertyHandler.getValue(minutePropertyString);

        if (minuteString.trim().equals(""))
        {
            //set default minutes if property is empty
            minutes = new Integer(Constants.DEFAULT_NIGHTLY_CRON_JOB_EXECUTION_TIME_MINUTES);
        }
        else
        {
            //validate minutes from properties file
            if (!minuteString.matches("([0-5][0-9])"))
            {
                logger.error("Failed to schedule Job, invalid " + minutePropertyString + " property", new Exception());
                throw new Exception("Failed to schedule Job, invalid " + minutePropertyString + " property");
            }
            //set minutes from property
            minutes = new Integer(minuteString);
        }

        return minutes;
    }

    /**
     * Returns Hours part of the time at which scheduler should run.
     * @return
     * @throws Exception 
     */
    private static Integer getHours(String hoursPropertyString) throws Exception
    {
        Integer hours = null;

        //get hours value from properties file
        String hourString = XMLPropertyHandler.getValue(hoursPropertyString);

        if (hourString.trim().equals(""))
        {
            //set default hours if property is empty
            hours = new Integer(Constants.DEFAULT_NIGHTLY_CRON_JOB_EXECUTION_TIME_HOURS);
        }
        else
        {
            //Validate hours from properties file
            if (!hourString.matches("([0-9]|[0-1][0-9]|2[0-3])"))
            {
                logger.error("Failed to schedule Job, invalid " + hoursPropertyString + " property", new Exception());
                throw new Exception("Failed to schedule Job, invalid " + hoursPropertyString + " property");
            }
            //set hours from property
            hours = new Integer(hourString);
        }

        return hours;
    }

    /**
     * This method calls the Quartz job.
     * @throws Exception 
     */
    public static void scheduleQuartzSchedulerJob() throws Exception
    {
        final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        final Scheduler scheduler = schedulerFactory.getScheduler();//get scheduler
        scheduler.start();//start scheduler

        //create a job detail with name "QuartzSchedulerJob"
        final JobDetail detail = new JobDetail("QuartzSchedulerJob", Scheduler.DEFAULT_GROUP, QuartzSchedulerJob.class);

        //(for daily)interval in milliseconds after which job will get repeated
        final Long repeatInterval = Constants.DAILY_INTERVAL_MILLISECONDS;

        logger.debug("Repeat interval for QuartzSchedulerJob : " + repeatInterval);

        //create a trigger to run the job
        final SimpleTrigger trigger = new SimpleTrigger("QuartzSchedulerJobTrigger", Scheduler.DEFAULT_GROUP,
                getSchedulerStartTime(), null, SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval);

        //scheduling the job
        scheduler.scheduleJob(detail, trigger);
    }

    /**
     * Returns start time for QuartzSchedulerJob.
     * @return
     * @throws Exception
     */
    private static Date getSchedulerStartTime() throws Exception
    {
        //get hours part of time
        Integer hours = getHours(Constants.NIGHTLY_CRON_JOB_EXECUTION_TIME_HOURS);
        //get minutes part of time
        Integer minutes = getMinutes(Constants.NIGHTLY_CRON_JOB_EXECUTION_TIME_MINUTES);

        //set date and time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        logger.info("Start Time for QuartzSchedulerJob : " + hours + ":" + minutes);

        return calendar.getTime();
    }
}
