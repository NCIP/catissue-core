
package krishagni.catissueplus.scheduler;

import java.lang.reflect.InvocationTargetException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import edu.wustl.catissuecore.keywordsearch.TitliIndexer;
import edu.wustl.common.util.logger.Logger;

/**
 * Quartz Job scheduler to run cron jobs
 * @author Ashraf
 *
 */
public class QuartzSchedulerJob implements Job
{

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getCommonLogger(QuartzSchedulerJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        logger.info("Starting execution of ContainerSpecimenCountJob");
        /*Class containerSpecimenCountJob;
       try
        {
            containerSpecimenCountJob = Class.forName("krishagni.catissueplus.scheduler.ContainerSpecimenCountJob");
            containerSpecimenCountJob.getMethod("executeContainerSpecimenCountJob").invoke(null);
            logger.info("End of execution of ContainerSpecimenCountJob");
        
        }
        
        catch (Exception e)
        {
           
        }*/
        logger.info("Starting execution of TitliIndexerJob");
        TitliIndexer.main(null);//run titli indexer
        logger.info("End of execution of TitliIndexerJob");

       
    }
    
 
}
