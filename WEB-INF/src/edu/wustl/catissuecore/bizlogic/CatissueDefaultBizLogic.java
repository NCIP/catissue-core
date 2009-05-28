
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
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

	private transient Logger logger = Logger.getCommonLogger(CatissueDefaultBizLogic.class);

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#isAuthorized
	 * @param dao The dao object.
	 * @param domainObject domain Object
	 * @param sessionDataBean session specific Data
	 * @return isAuthorized
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 * @ generic DAOException
	 */
	public boolean isAuthorized(DAO dao, Object domainObject, SessionDataBean sessionDataBean)
			throws BizLogicException
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
		String privilegeName = null;
		try
		{
			PrivilegeCache privilegeCache = null;
			//Checking whether the logged in user has the required privilege on the given protection element

			if (!isAuthorized)
			{
				privilegeName = getPrivilegeName(domainObject);
				privilegeCache = getPrivilegeCache(sessionDataBean);
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
					isAuthorized = privilegeCache
							.hasPrivilege(protectionElementName, privilegeName);
				}
			}
			if (!isAuthorized)
			{
				BizLogicException exception = AppUtility.getUserNotAuthorizedException(
						privilegeName, protectionElementName,domainObject.getClass().getSimpleName());
				throw exception;
			}
		}
		catch (SMException exception)
		{
			logger.debug(exception.getMessage(), exception);
			throw getBizLogicException(exception, exception.getErrorKeyAsString(), exception
					.getLogMessage());
		}
		
		return isAuthorized;
	}

	/**
	* @param name
	* @return
	*/
	public String getActualClassName(String name)
	{
		if (name != null && name.trim().length() != 0)
		{
			String splitter = "\\.";
			String[] arr = name.split(splitter);
			if (arr != null && arr.length != 0)
			{
				return arr[arr.length - 1];
			}
		}
		return name;
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
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return jdbcDAO;
	}

	protected void closeJDBCSession(JDBCDAO jdbcDAO) throws BizLogicException
	{
		try
		{
			jdbcDAO.closeSession();
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

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
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return dao;
	}

	protected void closeDAOSession(DAO dao) throws BizLogicException
	{
		try
		{
			dao.closeSession();
		}
		catch (DAOException daoExp)
		{
			logger.debug(daoExp.getMessage(), daoExp);
			throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}

		List cpIdsList = new ArrayList();
		Set<Long> cpIds = new HashSet<Long>();

		cpIdsList = edu.wustl.query.util.global.Utility.getCPIdsList(objName, identifier,
				sessionDataBean);

		if (cpIdsList == null)
		{
			return false;
		}

		for (Object cpId : cpIdsList)
		{
			cpId = cpIdsList.get(0);
			cpIds.add(Long.valueOf(cpId.toString()));
		}
		try
		{
			PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			StringBuffer sb = new StringBuffer();
			sb.append(edu.wustl.catissuecore.util.global.Constants.COLLECTION_PROTOCOL_CLASS_NAME)
					.append("_");
			boolean isPresent = false;

			for (Long cpId : cpIds)
			{
				String privilegeName = getReadDeniedPrivilegeName();

				String[] privilegeNames = privilegeName.split(",");
				if (privilegeNames.length > 1)
				{
					if ((privilegeCache.hasPrivilege(sb.toString() + cpId.toString(),
							privilegeNames[0])))
					{
						isPresent = privilegeCache.hasPrivilege(sb.toString() + cpId.toString(),
								privilegeNames[1]);
						isPresent = !isPresent;
					}
				}
				else
				{
					isPresent = privilegeCache.hasPrivilege(sb.toString() + cpId.toString(),
							privilegeName);
				}

				if (privilegeName != null
						&& privilegeName
								.equalsIgnoreCase(edu.wustl.security.global.Permissions.READ_DENIED))
				{
					isPresent = !isPresent;
				}
				if (!isPresent)
				{
					return false;
				}
			}

		}
		catch (SMException e)
		{
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		return true;
	}
	
	
	
	

}
