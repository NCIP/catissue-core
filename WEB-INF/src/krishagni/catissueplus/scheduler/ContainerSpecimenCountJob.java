
package krishagni.catissueplus.scheduler;

import java.sql.SQLException;

import krishagni.catissueplus.bizlogic.StorageContainerBizlogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

public class ContainerSpecimenCountJob
{

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getCommonLogger(ContainerSpecimenCountJob.class);

    /**
     * Method to insert values into catissue_stor_cont_spec_counts table.
     */
    public static void executeContainerSpecimenCountJob()
    {
        logger.debug("Starting execution of ContainerSpecimenCountJob");
        HibernateDAO hibernateDAO = null;
        try
        {
            hibernateDAO = (HibernateDAO) AppUtility.openDAOSession(null);

            new StorageContainerBizlogic().saveSpecimenCountsOfParentContainer(hibernateDAO);
        }
        catch (ApplicationException e)
        {
            logger.error("Exception while opening DAO session for ContainerSpecimenCountJob", e);
        }
        catch (SQLException e)
        {
            logger.error("Exception while saving specimen counts into database for ContainerSpecimenCountJob", e);
        }
        finally
        {
            try
            {
                hibernateDAO.commit();
                AppUtility.closeDAOSession(hibernateDAO);
            }
            catch (DAOException e)
            {
                logger.error("Exception while commiting to database for ContainerSpecimenCountJob", e);
            }
            catch (ApplicationException e)
            {
                logger.error("Exception while closing DAO session for ContainerSpecimenCountJob", e);
            }
        }
        logger.debug("End of execution of ContainerSpecimenCountJob");
    }
}
