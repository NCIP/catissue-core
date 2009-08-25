/*
 * Created on Jan 20, 2006
 *
 * Listener for cleanup after session invalidates.
 * 
 */

package edu.wustl.catissuecore.util.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * @author poornima_govindrao
 *
 * Listener for cleanup after session invalidates.
 */
public class CatissueCoreSessionListener implements HttpSessionListener
{

	private transient final Logger logger = Logger
			.getCommonLogger(CatissueCoreSessionListener.class);

	public void sessionCreated(HttpSessionEvent arg0)
	{

	}

	//Cleanup after session invalidates.
	public void sessionDestroyed(HttpSessionEvent arg0)
	{
		final HttpSession session = arg0.getSession();
		final SessionDataBean sessionData = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);

		if (sessionData != null)
		{
			this.cleanUp(sessionData, (String) session.getAttribute(Constants.RANDOM_NUMBER));
		}

		// To remove PrivilegeCache from the session, requires user LoginName
		// Singleton instance of PrivilegeManager
		/*if(sessionData != null)
		{
			PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
			privilegeManager.removePrivilegeCache(sessionData.getUserName());
		}*/
	}

	private void cleanUp(SessionDataBean sessionData, String randomNumber)
	{
		//Delete Advance Query table if exists
		//Advance Query table name with userID attached
		final String tempTableName = Constants.QUERY_RESULTS_TABLE + "_" + sessionData.getUserId();
		try
		{
			final String appName = CommonServiceLocator.getInstance().getAppName();
			final JDBCDAO jdbcDao = DAOConfigFactory.getInstance().getDAOFactory(appName)
					.getJDBCDAO();
			jdbcDao.openSession(sessionData);
			jdbcDao.deleteTable(tempTableName);
			jdbcDao.closeSession();
		}
		catch (final DAOException ex)
		{
			this.logger.error("Could not delete the Advance Search temporary table."
					+ ex.getMessage(), ex);
		}
		final String tempTableNameForQuery = Constants.TEMP_OUPUT_TREE_TABLE_NAME
				+ sessionData.getUserId() + randomNumber;
		try
		{
			final String appName = CommonServiceLocator.getInstance().getAppName();
			final JDBCDAO jdbcDao = DAOConfigFactory.getInstance().getDAOFactory(appName)
					.getJDBCDAO();
			jdbcDao.openSession(sessionData);
			jdbcDao.deleteTable(tempTableNameForQuery);
			jdbcDao.closeSession();
		}
		catch (final DAOException ex)
		{
			this.logger.error("Could not delete the Query Module Search temporary table."
					+ ex.getMessage(), ex);
		}
	}
}
