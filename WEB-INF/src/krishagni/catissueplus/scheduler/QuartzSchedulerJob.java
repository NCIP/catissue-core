
package krishagni.catissueplus.scheduler;

import java.lang.reflect.InvocationTargetException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import edu.wustl.catissuecore.keywordsearch.TitliIndexer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.XMLPropertyHandler;
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
        String[] jobarr =  XMLPropertyHandler.getValue("schedularJobList").split(",");
        Class schedularObj;
        try
        {
            for(int i=0;i < jobarr.length;i++){
                logger.info("Started execution of "+jobarr[i]);
                schedularObj = Class.forName(jobarr[i]);
                schedularObj.getMethod("init").invoke(null);
                logger.info("End execution of "+jobarr[i]);
            }
        }
        catch (Exception e)
        {
           
        }
        Class containerSpecimenCountJob;
        try
        {
            containerSpecimenCountJob = Class.forName("krishagni.catissueplus.scheduler.ContainerSpecimenCountJob");
            containerSpecimenCountJob.getMethod("executeContainerSpecimenCountJob").invoke(null);
            logger.info("End of execution of ContainerSpecimenCountJob");
        
        }
        
        catch (Exception e)
        {
           
        }
        logger.info("Starting execution of TitliIndexerJob");
        TitliIndexer.main(null);//run titli indexer
        logger.info("End of execution of TitliIndexerJob");

       
    }
    
 
}
