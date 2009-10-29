
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import titli.controller.Name;
import titli.controller.RecordIdentifier;
import titli.controller.interfaces.IndexRefresherInterface;
import titli.controller.interfaces.ObjectMetadataInterface;
import titli.controller.interfaces.TitliInterface;
import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.TitliResultGroup;
import titli.model.util.TitliTableMapper;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.TitliSearchConstants;
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

	private transient final Logger logger = Logger.getCommonLogger(CatissueDefaultBizLogic.class);

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#isAuthorized
	 * @param dao The dao object.
	 * @param domainObject domain Object
	 * @param sessionDataBean session specific Data
	 * @return isAuthorized
	 * @throws UserNotAuthorizedException User Not Authorized Exception
	 * @ generic DAOException
	 */
	@Override
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
				final List list = (List) domainObject;
				for (final Object domainObject2 : list)
				{
					protectionElementName = this.getObjectId(dao, domainObject2);
				}
			}
			else
			{
				protectionElementName = this.getObjectId(dao, domainObject);
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
				privilegeName = this.getPrivilegeName(domainObject);
				privilegeCache = this.getPrivilegeCache(sessionDataBean);
				if (!protectionElementName.equalsIgnoreCase("ADMIN_PROTECTION_ELEMENT"))
				{
					final String[] prArray = protectionElementName.split("_");
					final String baseObjectId = prArray[0];
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
				final BizLogicException exception = AppUtility.getUserNotAuthorizedException(
						privilegeName, protectionElementName, domainObject.getClass()
								.getSimpleName());
				throw exception;
			}
		}
		catch (final SMException exception)
		{
			this.logger.error(exception.getMessage(), exception);
			exception.printStackTrace();
			throw this.getBizLogicException(exception, exception.getErrorKeyAsString(), exception
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
			final String splitter = "\\.";
			final String[] arr = name.split(splitter);
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
		final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(sessionDataBean
				.getUserName());
		return privilegeCache;
	}

	protected JDBCDAO openJDBCSession() throws BizLogicException
	{
		JDBCDAO jdbcDAO = null;
		try
		{
			final String applicationName = CommonServiceLocator.getInstance().getAppName();
			jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
			jdbcDAO.openSession(null);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return jdbcDAO;
	}

	protected void closeJDBCSession(JDBCDAO jdbcDAO) throws BizLogicException
	{
		try
		{
			if (jdbcDAO != null)
			{
				jdbcDAO.closeSession();
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	protected DAO openDAOSession(SessionDataBean sessionDataBean) throws BizLogicException
	{
		DAO dao = null;
		try
		{
			final String applicationName = CommonServiceLocator.getInstance().getAppName();
			dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getDAO();
			dao.openSession(sessionDataBean);
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return dao;
	}

	protected void closeDAOSession(DAO dao) throws BizLogicException
	{
		try
		{
			if (dao != null)
			{
				dao.closeSession();
			}
		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	@Override
	public boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}

		List cpIdsList = new ArrayList();
		final Set<Long> cpIds = new HashSet<Long>();

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
			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			final StringBuffer sb = new StringBuffer();
			sb.append(edu.wustl.catissuecore.util.global.Constants.COLLECTION_PROTOCOL_CLASS_NAME)
					.append("_");
			boolean isPresent = false;

			for (final Long cpId : cpIds)
			{
				final String privilegeName = this.getReadDeniedPrivilegeName();

				final String[] privilegeNames = privilegeName.split(",");
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
		catch (final SMException smEx)
		{
			this.logger.error(smEx.getMessage(),smEx);
			smEx.printStackTrace();
			throw this.getBizLogicException(smEx, smEx.getErrorKeyName(), smEx.getMsgValues());
		}
		return true;
	}

	/**
	 * refresh the titli search index to reflect the changes in the database.
	 * @param operation the operation to be performed : "insert", "update" or "delete"
	 * @param obj the object corresponding to the record to be refreshed
	 */
	protected void refreshTitliSearchIndexSingle(String operation, Object obj)
	{
		try
		{
			final Properties prop = new Properties();
			prop.load(CommonUtilities.getCurrClassLoader().getResourceAsStream("titli.properties"));
			final String className = prop.getProperty("titliObjectMetadataImplementor");
			final ObjectMetadataInterface objectMetadataInterface = (ObjectMetadataInterface) Class
					.forName(className).newInstance();
			String tableName = objectMetadataInterface.getTableName(obj);
			if (!tableName.equalsIgnoreCase(""))
			{
				final String objId = objectMetadataInterface.getUniqueIdentifier(obj);
				final String mainTableName = TitliTableMapper.getInstance().returnMainTable(
						tableName);
				if (mainTableName != null)
				{
					tableName = mainTableName;
				}
				if (operation != null)
				{
					this.performOperation(operation, tableName, objId);
				}
			}
		}
		catch (final Exception excep)
		{
			this.logger.error("Titli search index cound not be refreshed for opeartion."
					+ operation, excep);
			excep.printStackTrace();
		}

	}

	public void refreshTitliSearchIndexMultiple(Collection<AbstractDomainObject> objCollection,
			String operation) throws BizLogicException
	{
		for (final AbstractDomainObject obj : objCollection)
		{
			this.refreshTitliSearchIndexSingle(operation, obj);
		}
	}

	protected void refreshTitliSearchIndex(String operation, Object obj)
	{
		try
		{
			if (TitliResultGroup.isTitliConfigured)
			{
				if (obj instanceof LinkedHashSet)
				{
					this.refreshTitliSearchIndexMultiple((LinkedHashSet<AbstractDomainObject>) obj,
							operation);
				}
				else
				{

					this.refreshTitliSearchIndexSingle(operation, obj);
				}
			}
		}
		catch (final BizLogicException bizLogicExp)
		{
			this.logger.error(bizLogicExp.getMessage(),bizLogicExp);
			bizLogicExp.printStackTrace();
		}
	}

	/**
	 * This method perform insert,update or delete operation.
	 * @param operation type of operation.
	 * @param tableName Table Name
	 * @param objId Object Id
	 * @throws TitliException throws this exception if operation unsuccessful.
	 */
	private void performOperation(String operation, String tableName, String objId)
			throws TitliException
	{
		final Map<Name, String> uniqueKey = new HashMap<Name, String>();
		uniqueKey.put(new Name(Constants.IDENTIFIER), objId);
		final TitliInterface titli = Titli.getInstance();
		final Name dbName = (titli.getDatabases().keySet().toArray(new Name[0]))[0];
		final RecordIdentifier recordIdentifier = new RecordIdentifier(dbName, new Name(tableName),
				uniqueKey);

		final IndexRefresherInterface indexRefresher = titli.getIndexRefresher();

		if (TitliSearchConstants.TITLI_INSERT_OPERATION.equalsIgnoreCase(operation))
		{
			indexRefresher.insert(recordIdentifier);
		}
		else if (TitliSearchConstants.TITLI_UPDATE_OPERATION.equalsIgnoreCase(operation))
		{
			indexRefresher.update(recordIdentifier);
		}
		else if (TitliSearchConstants.TITLI_DELETE_OPERATION.equalsIgnoreCase(operation))
		{
			indexRefresher.delete(recordIdentifier);
		}
	}

	/**
	 *@param objCollection object collection.
	 *@param dao The dao object.
	 *@param sessionDataBean session specific Data
	 *@throws BizLogicException Generic BizLogic Exception
	 */
	@Override
	protected void postInsert(Collection<AbstractDomainObject> objCollection, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		super.postInsert(objCollection, dao, sessionDataBean);
		this.refreshTitliSearchIndex(TitliSearchConstants.TITLI_INSERT_OPERATION, objCollection);

	}

	/**
	 * This method gets called after insert method.
	 * Any logic after inserting object in database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	@Override
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		super.postInsert(obj, dao, sessionDataBean);
		this.refreshTitliSearchIndex(TitliSearchConstants.TITLI_INSERT_OPERATION, obj);
	}

	/**
	 * This method gets called after update method.
	 * Any logic after updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	@Override
	protected void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		super.postUpdate(dao, currentObj, oldObj, sessionDataBean);
		this.refreshTitliSearchIndex(TitliSearchConstants.TITLI_UPDATE_OPERATION, currentObj);
	}

	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @param dao The dao object.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	@Override
	public void delete(Object obj) throws BizLogicException
	{
		super.delete(obj);
		this.refreshTitliSearchIndex(TitliSearchConstants.TITLI_DELETE_OPERATION, obj);
	}

}
