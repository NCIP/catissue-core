package edu.wustl.catissuecore.bizlogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class is used add, update and retrieve report loader queue  
 * information using Hibernate.
 */
public class ReportLoaderQueueBizLogic extends IntegrationBizLogic
{

	/**
	 * Saves the ReportLoaderQueue object in the database.
	 * @param obj The storageType object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue) obj;
		dao.insert(reportLoaderQueue, sessionDataBean, true, false);
 	}

	/**
	 * Deletes the ReportLoaderQueue object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void delete(Object obj, DAO dao) throws DAOException, UserNotAuthorizedException
	{
		dao.delete(obj);
	}


	/**
	 * Updates the ReportLoaderQueue object in the database.
	 * @param obj The object to be updated.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException
	{
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue)obj;
		dao.update(reportLoaderQueue, sessionDataBean, true, false, false);
	}
	
	/**
	 * This function takes identifier as parameter and returns corresponding TextContent
	 * @return - TextContent object
	 */
	public ReportLoaderQueue getReportLoaderQueueById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String sourceObjectName = ReportLoaderQueue.class.getName();

		// getting all the participants from the database 
		List reportLoaderQueueList = bizLogic.retrieve(sourceObjectName, Constants.ID, identifier);
		ReportLoaderQueue reportLoaderQueue = (ReportLoaderQueue) reportLoaderQueueList.get(0);
		return reportLoaderQueue;

	}
	
	
	
}
