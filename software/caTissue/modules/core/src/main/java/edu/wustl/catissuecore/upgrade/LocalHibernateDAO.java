/**
 * <p>Title: HibernateDAO Class>
 * <p>Description:	HibernateDAO is default implemention of DAO through Hibernate ORM tool.
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 16, 2005
 */

package edu.wustl.catissuecore.upgrade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AuditEvent;
import edu.wustl.common.domain.LoginDetails;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.AbstractDAOImpl;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.AuditException;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOUtility;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.NamedQueryParam;

/**
 * Default implementation of DAO through Hibernate ORM tool.
 * @author kapil_kaveeshwar
 */
public class LocalHibernateDAO extends AbstractDAOImpl implements HibernateDAO
{

	/**
	* LOGGER Logger - class logger.
	*/
	private static final Logger logger = Logger.getCommonLogger(LocalHibernateDAO.class);

	/**
	 * specify Session instance.
	 */
	private Session session;

	/**
	 * AuditManager for auditing.
	 */
	private AuditManager auditManager;

	/**
	 * Hibernate Metadata associated to the application.
	 */
	private HibernateMetaData hibernateMetaData;

	/**
	 * Session data bean.
	 */
	private SessionDataBean sessionDataBean;

	/**
	 * This method will be used to establish the session with the database.
	 * Declared in  class.
	 * @param sessionDataBean session Data.
	 * @throws DAOException generic DAOException.
	 */
	public void openSession(SessionDataBean sessionDataBean) throws DAOException
	{
		logger.debug("Open the session");
		session = connectionManager.getSession();
		auditManager = new AuditManager(sessionDataBean, hibernateMetaData);
		this.sessionDataBean = sessionDataBean;
	}

	/**
	 * This method will be used to close the session with the database.
	 * Declared in  class.
	 * @throws DAOException generic DAOException.
	 */
	public void closeSession() throws DAOException
	{
		logger.debug("Close the session");
		connectionManager.closeSession();
		auditManager = null;
	}

	/**
	 * Commit the database level changes.
	 * Declared in  class.
	 * @throws DAOException generic DAOException.
	 */
	public void commit() throws DAOException
	{
		logger.debug("Session commit");
		if (session.getTransaction() == null || !session.getTransaction().isActive())
		{
			beginTransaction();
		}
		connectionManager.commit();
	}

	/**
	 * RollBack all the changes after last commit.
	 * Declared in  class.
	 * @throws DAOException generic DAOException.
	 */
	public void rollback() throws DAOException
	{
		logger.debug("Session rollback");
		connectionManager.rollback();
	}

	/**
	 * Merge. This method merges the object passed as parameter with the same object present in
	 * database. If no old object is present in db then new object is inserted in db.
	 * @param objectToBeMerged the object to be merged
	 */
	public void merge(Object objectToBeMerged)
	{
		session.merge(objectToBeMerged);
	}

	/**
	* This method will be called to begin new transaction.
	* @throws DAOException : It will throw DAOException.
	*/
	public void beginTransaction() throws DAOException
	{
		logger.debug("Begin transaction .");
		connectionManager.beginTransaction();
	}

	/**
	 * Saves the persistent object in the database.
	 * @param obj The object to be saved.
	 * @throws DAOException generic DAOException.
	 */
	public void insert(Object obj) throws DAOException
	{
		logger.debug("Insert Object");
		try
		{
			if (session.getTransaction() == null || !session.getTransaction().isActive())
			{
				beginTransaction();
			}
			session.save(obj);
			auditManager.audit(obj, null, "INSERT");
			insertAudit();
		}
		catch (HibernateException hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.insert.data.error",
					"HibernateDAOImpl.java ");

		}
		catch (AuditException exp)
		{

			logger.warn(exp.getMessage());
			logger.debug(exp.getMessage(), exp);
			//throw DAOUtility.getInstance().getDAOException(exp, exp.getErrorKeyName(),
			//	exp.getMsgValues());
		}
		finally
		{
			connectionManager.commit();
		}
	}

	/**
	 * updates the object into the database.
	 * @param currentObj Object to be updated in database
	 * @throws DAOException : generic DAOException
	 */
	public void update(Object currentObj) throws DAOException
	{
		logger.debug("Update Object");
		try
		{
			if (session.getTransaction() == null || !session.getTransaction().isActive())
			{
				beginTransaction();
			}
			Object previousObj = retrieveOldObject(currentObj);
			update(currentObj, previousObj);
			connectionManager.commit();
		}
		catch (HibernateException hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.update.data.error",
					"HibernateDAOImpl.java ");
		}
	}

	/**
	 * This method will be called to retrieve the oldObject.
	 * @param currentObj Object whose old values has to be fetched from database.
	 * @return old Object.
	 * @throws DAOException database exception.
	 */
	private Object retrieveOldObject(Object currentObj) throws DAOException
	{
		Session session = null;
		try
		{
			Long objectId = auditManager.getObjectId(currentObj);
			session = connectionManager.getSessionFactory().openSession();
			return session.get(Class.forName(currentObj.getClass().getName()), objectId);
		}
		catch (AuditException exp)
		{
			logger.warn(exp.getMessage());
			logger.debug(exp.getMessage(), exp);
			return null;
			/*throw DAOUtility.getInstance().getDAOException(exp, exp.getErrorKeyName(),
					exp.getMsgValues());*/
		}
		catch (HibernateException hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			return null;
			//	throw DAOUtility.getInstance().getDAOException(hibExp, "db.update.data.error",
			//"HibernateDAOImpl.java ");
		}
		catch (ClassNotFoundException exp)
		{
			logger.error(exp.getMessage(), exp);
			return null;
			//throw DAOUtility.getInstance().getDAOException(exp, "class.not.found.error",
			//currentObj.getClass().getName());
		}
		finally
		{
			if (session != null)
			{
				session.close();
				session = null;
			}
		}
	}

	/**
	 * This method will be called when user need to audit and update the changes.
	 * @param currentObj object with new changes
	 * * @param previousObj persistent object fetched from database.
	 * @throws DAOException : generic DAOException
	 */
	public void update(Object currentObj, Object previousObj) throws DAOException
	{
		logger.debug("Update Object");
		try
		{
			session.update(currentObj);
			auditManager.audit(currentObj, previousObj, "UPDATE");
			insertAudit();
		}
		catch (HibernateException hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.update.data.error",
					"HibernateDAOImpl.java ");
		}
		catch (AuditException exp)
		{
			logger.warn(exp.getMessage());
			logger.debug(exp.getMessage(), exp);
			//throw DAOUtility.getInstance().getDAOException(exp, exp.getErrorKeyName(),
			//	exp.getMsgValues());
		}
	}

	/**
	 * This method inserts audit Event details in database.
	 * @throws DAOException generic DAOException.
	 */
	public void insertAudit() throws DAOException
	{
		if (auditManager.getAuditEvent() != null
				&& !auditManager.getAuditEvent().getAuditEventLogCollection().isEmpty())
		{
			session.save(auditManager.getAuditEvent());
		}
		auditManager.setAuditEvent(new AuditEvent());
		auditManager.initializeAuditManager(sessionDataBean);
	}

	/**
	 * Sets the status of LoginAttempt to loginStatus provided as an argument.
	 * @param loginStatus LoginStatus boolean value.
	 * @param loginDetails LoginDetails object.
	 * @throws AuditException AuditException
	 */
	public void auditLoginEvents(boolean loginStatus, LoginDetails loginDetails)
			throws AuditException
	{
		auditManager.setLoginDetails(loginDetails);
		try
		{
			auditManager.getLoginEvent().setIsLoginSuccessful(loginStatus);
			session.save(auditManager.getLoginEvent());
		}
		catch (HibernateException daoException)
		{
			logger.error("Exception while Auditing Login Attempt. " + daoException.getMessage(),
					daoException);

			throw new AuditException(ErrorKey.getErrorKey("error.in.login.audit"), daoException, "");

		}

	}

	/**
	 * Deletes the persistent object from the database.
	 * @param obj The object to be deleted.
	 * @throws DAOException generic DAOException.
	 */
	public void delete(Object obj) throws DAOException
	{
		logger.debug("Delete Object");
		try
		{
			session.delete(obj);
		}
		catch (HibernateException hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.delete.data.error",
					"HibernateDAOImpl.java ");

		}
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according
	 * to field values passed in the passed session.
	 * @param sourceObjectName source Object Name.
	 * @param selectColumnName select Column Name.
	 * @param queryWhereClause where column conditions
	 * @param onlyDistinctRows true if only distinct rows should be selected.
	 * @return List.
	 * @deprecated
	 * @throws DAOException generic DAOException.
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause, boolean onlyDistinctRows) throws DAOException
	{
		logger.debug("Inside retrieve method");
		List list;
		try
		{
			StringBuffer queryStrBuff = new StringBuffer();
			String className = DAOUtility.getInstance().parseClassName(sourceObjectName);
			Query query;

			generateSelectPartOfQuery(selectColumnName, queryStrBuff, className);
			generateFromPartOfQuery(sourceObjectName, queryStrBuff, className);

			if (queryWhereClause != null)
			{
				queryStrBuff.append(queryWhereClause.toWhereClause());
			}
			query = session.createQuery(queryStrBuff.toString());

			list = query.list();

		}
		catch (HibernateException hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java ");

		}
		return list;
	}

	/**
	 * Generate Select Block.
	 * @param selectColumnName select Column Name.
	 * @param sqlBuff sqlBuff
	 * @param className class Name.
	 */
	private void generateSelectPartOfQuery(String[] selectColumnName, StringBuffer sqlBuff,
			String className)
	{
		logger.debug("Prepare select part of query.");
		if (selectColumnName != null && selectColumnName.length > 0)
		{
			sqlBuff.append("Select ");
			for (int i = 0; i < selectColumnName.length; i++)
			{
				sqlBuff.append(DAOUtility.getInstance().createAttributeNameForHQL(className,
						selectColumnName[i]));
				if (i != selectColumnName.length - 1)
				{
					sqlBuff.append(", ");
				}
			}
			sqlBuff.append("   ");
		}
	}

	/**
	 * @param sourceObjectName source Object Name.
	 * @param sqlBuff query buffer
	 * @param className gives the class name
	 */
	private void generateFromPartOfQuery(String sourceObjectName, StringBuffer sqlBuff,
			String className)
	{
		logger.debug("Prepare from part of query");
		sqlBuff.append("from " + sourceObjectName + " " + className);
	}

	/**
	 * Retrieve Object.
	 * @param sourceObjectName source Object Name.
	 * @param identifier identifier.
	 * @return object.
	 * @throws DAOException generic DAOException.
	 */
	public Object retrieveById(String sourceObjectName, Long identifier) throws DAOException
	{
		logger.debug("Inside retrieve method");
		try
		{
			Object object = session.load(Class.forName(sourceObjectName), identifier);
			return HibernateMetaData.getProxyObjectImpl(object);

			/*Object object = session.get(Class.forName(sourceObjectName), identifier);
			return object;*/
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
			throw DAOUtility.getInstance().getDAOException(exp, "db.retrieve.data.error",
					"HibernateDAOImpl.java ");
		}

	}

	/**
	 * Executes the HQL query.
	 * @param query HQL query to execute.
	 * @return List.
	 * @throws DAOException generic DAOException.
	 * @deprecated executeQuery(String query,
	 * List<ColumnValueBean> columnValueBeans)
	 */
	public List executeQuery(String query) throws DAOException
	{
		return executeQuery(query, null, null, null);
	}

	/**
	 * Executes the HQL query.
	 * @param query HQL query to execute.
	 * @param columnValueBeans column data beans.
	 * @return list of data.
	 * @throws DAOException Database exception.
	 */
	public List executeParamHQL(String query, List<ColumnValueBean> columnValueBeans)
			throws DAOException
	{
		logger.debug("Execute hql param query");
		try
		{
			Query hibernateQuery = session.createQuery(query);
			if (columnValueBeans != null)
			{
				Iterator<ColumnValueBean> colValItr = columnValueBeans.iterator();
				while (colValItr.hasNext())
				{
					ColumnValueBean colValueBean = colValItr.next();
					hibernateQuery.setParameter(colValueBean.getColumnName(), colValueBean
							.getColumnValue());
				}
			}
			return hibernateQuery.list();

		}
		catch (HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java " + query);
		}
	}

	/**
	 * Executes the HQL query.
	 * @param query HQL query to execute.
	 * @param columnValueBeans column data beans.
	 * @return list of data.
	 * @throws DAOException Database exception.
	 */
	public List executeQuery(String query, List<ColumnValueBean> columnValueBeans)
			throws DAOException
	{
		return executeQuery(query, columnValueBeans, 0);
	}

	/**
	 * Executes the HQL query.
	 *
	 * @param query HQL query to execute.
	 * @param columnValueBeans column data beans.
	 * @param maxResults the max number of results, the query should return
	 * if 0 is passed, all results will be shown.
	 * @return list of data.
	 *
	 * @throws DAOException Database exception.
	 */
	public List executeQuery(String query, List<ColumnValueBean> columnValueBeans, int maxResults)
			throws DAOException
	{
		logger.debug("Execute query  %%% query !!! ");
		try
		{
			Query hibernateQuery = session.createQuery(query);
			logger.debug("created hibernate query instance .");

			if (columnValueBeans != null)
			{
				int index = 0;
				Iterator<ColumnValueBean> colValItr = columnValueBeans.iterator();
				while (colValItr.hasNext())
				{
					ColumnValueBean colValueBean = colValItr.next();
					hibernateQuery.setParameter(index, colValueBean.getColumnValue());
					index++;
				}
			}
			if (maxResults > 0)
			{
				hibernateQuery.setMaxResults(maxResults);
			}
			return hibernateQuery.list();

		}
		catch (HibernateException hiberExp)
		{
			logger.error("Hibernate Exception occurred . ", hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java " + query);
		}
	}

	/**
	 * Executes the HQL query. for given startIndex and max
	 * records to retrieve
	 * @param query  HQL query to execute
	 * @param startIndex Starting index value
	 * @param maxRecords max number of records to fetch
	 * @param paramValues List of parameter values.
	 * @return List of data.
	 * @throws DAOException database exception.
	 */
	public List executeQuery(String query, Integer startIndex, Integer maxRecords, List paramValues)
			throws DAOException
	{
		logger.debug("Execute query");
		try
		{
			Query hibernateQuery = session.createQuery(query);
			if (startIndex != null && maxRecords != null)
			{
				hibernateQuery.setFirstResult(startIndex.intValue());
				hibernateQuery.setMaxResults(maxRecords.intValue());
			}
			if (paramValues != null)
			{
				for (int i = 0; i < paramValues.size(); i++)
				{
					hibernateQuery.setParameter(i, paramValues.get(i));
				}
			}
			return hibernateQuery.list();

		}
		catch (HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java " + query);
		}
	}

	/**
	 * This method returns named query.
	 * @param queryName : handle for named query.
	 * @param namedQueryParams : Map holding the parameter type and parameter value.
	 * @return the list of data.
	 * @throws DAOException : database exception.
	 */
	public List executeNamedQuery(String queryName, Map<String, NamedQueryParam> namedQueryParams)
			throws DAOException
	{
		logger.debug("Execute named query");
		try
		{
			Query query = session.getNamedQuery(queryName);
			DAOUtility.getInstance().substitutionParameterForQuery(query, namedQueryParams);
			return query.list();
		}
		catch (HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java " + queryName);
		}
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according
	 * to field values passed in the passed session.
	 * @param sourceObjectName source Object Name.
	 * @param selectColumnName select Column Name.
	 * @param queryWhereClause where column conditions
	 * @return List.
	 * @deprecated
	 * @throws DAOException generic DAOException.
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause) throws DAOException
	{
		return retrieve(sourceObjectName, selectColumnName, queryWhereClause, false);
	}

	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName Contains the class Name whose records are to be retrieved.
	 * @return List.
	 * @throws DAOException generic DAOException.
	 */
	public List retrieve(String sourceObjectName) throws DAOException
	{
		logger.debug("Inside retrieve method");
		String[] selectColumnName = null;
		return retrieve(sourceObjectName, selectColumnName, null, false);
	}

	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName Contains the class Name whose records are to be retrieved.
	 * @param whereColumnName Column name to be included in where clause.
	 * @param whereColumnValue Value of the Column name that included in where clause.
	 * @return List.
	 * @deprecated
	 * @throws DAOException generic DAOException.
	 */
	public List retrieve(String sourceObjectName, String whereColumnName, Object whereColumnValue)
			throws DAOException
	{
		logger.debug("Inside retrieve method");
		String[] selectColumnName = null;

		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(whereColumnName, whereColumnValue));

		return retrieve(sourceObjectName, selectColumnName, queryWhereClause, false);
	}

	/**
	 * @param sourceObjectName Contains the class Name whose records are to be retrieved.
	 * @param selectColumnName select Column Name.
	 * @return List.
	 * @throws DAOException generic DAOException.
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName) throws DAOException
	{
		logger.debug("Inside retrieve method");
		return retrieve(sourceObjectName, selectColumnName, null, false);
	}

	/**
	 * Retrieve Attribute.
	 * @param objClass object.
	 * @param identifier identifier.
	 * @param attributeName attribute Name.
	 * @param columnName Name of the column.
	 * @return Object.
	 * @deprecated
	 * @throws DAOException generic DAOException.
	 */
	public List retrieveAttribute(Class objClass, String columnName, Long identifier,
			String attributeName) throws DAOException
	{
		logger.debug("Retrieve attributes");
		try
		{
			String[] selectColumnName = {attributeName};
			QueryWhereClause queryWhereClause = new QueryWhereClause(objClass.getName());
			queryWhereClause.addCondition(new EqualClause(columnName, identifier));
			return retrieve(objClass.getName(), selectColumnName, queryWhereClause, false);
		}
		catch (HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java " + attributeName);
		}
	}

	/**
	 * @return the hibernateMetaData
	 */
	public HibernateMetaData getHibernateMetaData()
	{
		return hibernateMetaData;
	}

	/**
	 * @param hibernateMetaData the hibernateMetaData to set
	 */
	public void setHibernateMetaData(HibernateMetaData hibernateMetaData)
	{
		this.hibernateMetaData = hibernateMetaData;
	}

	/**
	 * Retrieve and returns the list of all source objects that satisfy the
	 * for given conditions on a various columns.
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @param selectColumnName Column names in SELECT clause of the query.
	 * @param queryWhereClause : This will hold following:
	 * 1.whereColumnName Array of column name to be included in where clause.
	 * 2.whereColumnCondition condition to be satisfy between column and its value.
	 * e.g. "=", "!=", "<", ">", "in", "null" etc
	 * 3. whereColumnValue Value of the column name that included in where clause.
	 * 4.joinCondition join condition between two columns. (AND, OR)
	 * @param onlyDistinctRows true if only distinct rows should be selected.
	 * @param columnValueBeans columnValueBeans
	 * @return the list of all source objects that satisfy the search conditions.
	 * @throws DAOException generic DAOException.
	 *  */
	public List retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause, boolean onlyDistinctRows,
			List<ColumnValueBean> columnValueBeans) throws DAOException
	{
		logger.debug("Inside retrieve method !!");
		try
		{
			StringBuffer queryStrBuff = new StringBuffer();
			String className = DAOUtility.getInstance().parseClassName(sourceObjectName);

			generateSelectPartOfQuery(selectColumnName, queryStrBuff, className);
			generateFromPartOfQuery(sourceObjectName, queryStrBuff, className);

			if (queryWhereClause != null)
			{
				queryStrBuff.append(queryWhereClause.toWhereClause());
			}
			return executeQuery(queryStrBuff.toString(), columnValueBeans);

		}
		catch (HibernateException hibExp)
		{
			logger.error(hibExp.getMessage(), hibExp);
			throw DAOUtility.getInstance().getDAOException(hibExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java ");

		}

	}

	/**
	 * Retrieve and returns the list of all source objects that satisfy the
	 * for given conditions on a various columns.
	 * @param sourceObjectName Source object's name to be retrieved from database.
	 * @param selectColumnName Column names in SELECT clause of the query.
	 * @param queryWhereClause : This will hold following:
	 * 1.whereColumnName Array of column name to be included in where clause.
	 * 2.whereColumnCondition condition to be satisfy between column and its value.
	 * e.g. "=", "<", ">", "=<", ">=" etc
	 * 3. whereColumnValue Value of the column name that included in where clause.
	 * 4.joinCondition join condition between two columns. (AND, OR)
	 * @return the list of all source objects that satisfy the search conditions.
	 * @throws DAOException generic DAOException.
	 * @param columnValueBeans columnValueBeans
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause, List<ColumnValueBean> columnValueBeans)
			throws DAOException
	{
		return retrieve(sourceObjectName, selectColumnName, queryWhereClause, false,
				columnValueBeans);
	}

	/**
	 * Retrieves attribute value for given class name and identifier.
	 * @param objClass source Class object
	 * @param attributeName attribute to be retrieved
	 * @param columnValueBean columnValueBean
	 * @return List.
	 * @throws DAOException generic DAOException.
	 */
	public List retrieveAttribute(Class objClass, ColumnValueBean columnValueBean,
			String attributeName) throws DAOException
	{
		logger.debug("Retrieve attributes");
		try
		{
			String[] selectColumnName = {attributeName};
			QueryWhereClause queryWhereClause = new QueryWhereClause(objClass.getName());
			queryWhereClause.addCondition(new EqualClause(columnValueBean.getColumnName(), '?'));

			List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
			columnValueBeans.add(columnValueBean);
			return retrieve(objClass.getName(), selectColumnName, queryWhereClause, false,
					columnValueBeans);
		}
		catch (HibernateException hiberExp)
		{
			logger.error(hiberExp.getMessage(), hiberExp);
			throw DAOUtility.getInstance().getDAOException(hiberExp, "db.retrieve.data.error",
					"HibernateDAOImpl.java " + attributeName);
		}
	}

	/**
	 * Returns the ResultSet containing all the rows from the table represented in sourceObjectName
	 * according to the where clause.It will create the where condition clause which holds where column name,
	 * value and conditions applied.
	 * @param sourceObjectName The table name.
	 * @param columnValueBean columnValueBean
	 * @return The ResultSet containing all the rows from the table represented
	 * in sourceObjectName which satisfies the where condition
	 * @throws DAOException : DAOException
	 */
	public List retrieve(String sourceObjectName, ColumnValueBean columnValueBean)
			throws DAOException
	{
		logger.debug("Inside retrieve method");
		String[] selectColumnName = null;

		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(columnValueBean.getColumnName(), '?'));

		List<ColumnValueBean> columnValueBeans = new ArrayList<ColumnValueBean>();
		columnValueBeans.add(columnValueBean);
		return retrieve(sourceObjectName, selectColumnName, queryWhereClause, false,
				columnValueBeans);
	}

}