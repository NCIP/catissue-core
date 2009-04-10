package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.exception.UserNotAuthorizedException;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * This is a default biz logic class for catissue. All BizLogic classes should extend from this.
 * @author deepti_shelar
 *
 */
public class CatissueDefaultBizLogic extends DefaultBizLogic
{
	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#isAuthorized
	 * @param dao The dao object.
	 * @param domainObject domain Object
	 * @param sessionDataBean session specific Data
	 * @return isAuthorized
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 * @ generic DAOException
	 */
	public boolean isAuthorized(DAO dao, Object domainObject,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		boolean isAuthorized = false;
		String protectionElementName = null;
		// Customize check for DE, return true if sessionDataBean is NULL
		if (sessionDataBean == null || (sessionDataBean != null && sessionDataBean.isAdmin()))
		{
			isAuthorized = true;
		}
		else
		{
			//	Get the base object id against which authorization will take place
			if (domainObject instanceof List)
			{
				List list = (List) domainObject;
				for (Object domainObject2 : list)
				{
					protectionElementName = getObjectId(dao, domainObject2);
				}
			}
			else
			{
				protectionElementName = getObjectId(dao, domainObject);
			}
			//TODO To revisit this piece of code --> Vishvesh
			if (Constants.ALLOW_OPERATION.equals(protectionElementName))
			{
				isAuthorized = true;
			}
		}
		//Get the required privilege name which we would like to check for the logged in user.
		String privilegeName = getPrivilegeName(domainObject);
		try
		{
		PrivilegeCache privilegeCache = getPrivilegeCache(sessionDataBean);
		//Checking whether the logged in user has the required privilege on the given protection element

		if (!isAuthorized)
		{
			if (!protectionElementName.equalsIgnoreCase("ADMIN_PROTECTION_ELEMENT"))
			{
				String[] prArray = protectionElementName.split("_");
				String baseObjectId = prArray[0];
				String objId = "";
				for (int i = 1; i < prArray.length; i++)
				{
					objId = baseObjectId + "_" + prArray[i];
					isAuthorized = privilegeCache.hasPrivilege(objId, privilegeName);
					if (!isAuthorized)
					{
						break;
					}
				}
			}
			else
			{
				isAuthorized = privilegeCache.hasPrivilege(protectionElementName, privilegeName);
			}
		}
		if (!isAuthorized)
		{
			UserNotAuthorizedException exception = AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName);
			exception.setPrivilegeName(privilegeName);
			if (protectionElementName != null
					&& (protectionElementName.contains("Site") || protectionElementName
							.contains("CollectionProtocol")))
			{
				String[] arr = protectionElementName.split("_");
				String[] nameArr = arr[0].split("\\.");
				String baseObject = nameArr[nameArr.length - 1];
				exception.setBaseObject(baseObject);
				exception.setBaseObjectIdentifier(arr[1]);
			}
			throw getBizLogicException(exception, exception.getErrorKeyAsString(), exception.getLogMessage());
		}
		}catch(SMException exception)
		{
			throw getBizLogicException(exception, exception.getErrorKeyAsString(), exception.getLogMessage());
		}
		return isAuthorized;
	}

	/**
	 * This method gets the instance of privilege cache which is used for authorization.
	 * @param sessionDataBean session specific Data
	 * @return privilegeCache
	 * @throws SMException 
	 */
	private PrivilegeCache getPrivilegeCache(SessionDataBean sessionDataBean) throws SMException
	{
		PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(sessionDataBean
				.getUserName());
		return privilegeCache;
	}

	protected JDBCDAO openJDBCSession() throws BizLogicException
	{
		JDBCDAO jdbcDAO = null;
		try
		{
			String applicationName = CommonServiceLocator.getInstance().getAppName();
			jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
			jdbcDAO.openSession(null);
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		return jdbcDAO;
	}
	
	protected DAO closeJDBCSession(JDBCDAO jdbcDAO) throws BizLogicException
	{
		try
		{
			jdbcDAO.closeSession();
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		return jdbcDAO;
	}
	
	protected DAO openDAOSession(SessionDataBean sessionDataBean) throws BizLogicException
	{
			DAO dao = null;
			try
			{
				String applicationName = CommonServiceLocator.getInstance().getAppName();
				dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getDAO();
				dao.openSession(sessionDataBean);
			}
			catch(DAOException daoExp)
			{
				throw getBizLogicException(daoExp, "dao.error", "");
			}
			return dao;
	}
	
	protected DAO closeDAOSession(DAO dao) throws BizLogicException
	{
		try
		{
			dao.closeSession();
		}
		catch(DAOException daoExp)
		{
			throw getBizLogicException(daoExp, "dao.error", "");
		}
		return dao;
	}
}
