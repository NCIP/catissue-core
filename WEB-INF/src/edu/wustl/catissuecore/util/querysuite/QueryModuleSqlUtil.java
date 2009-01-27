
package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author santhoshkumar_c
 *
 */
public final class QueryModuleSqlUtil
{

	/**
	 * Constructor.
	 */
	private QueryModuleSqlUtil()
	{
	}

	/**
	 * Executes the query and returns the results.
	 * @param sessionData sessiondata
	 * @param querySessionData querySessionData
	 * @return list of results
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	public static List<List<String>> executeQuery(final SessionDataBean sessionData,
			final QuerySessionData querySessionData)throws ClassNotFoundException, DAOException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List<List<String>> dataList = new ArrayList<List<String>>();
		try
		{
			dao.openSession(sessionData);
			dataList = dao.executeQuery(querySessionData.getSql(), sessionData,
			querySessionData.isSecureExecute(), querySessionData.isHasConditionOnIdentifiedField(),
			querySessionData.getQueryResultObjectDataMap());
			dao.commit();
		}
		finally
		{
			dao.closeSession();
		}
		return dataList;
	}

	/**
		 * Creates a new table in database. First the table is deleted if exists already.
		 * @param tableName name of the table to be deleted before creating new one.
		 * @param createTableSql sql to create table
		 * @param queryDetailsObj QueryDetails object.
		 * @throws DAOException DAOException
		 */
	public static void executeCreateTable(final String tableName, final String createTableSql,
			QueryDetails queryDetailsObj) throws DAOException
	{
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDao.openSession(queryDetailsObj.getSessionData());
			jdbcDao.delete(tableName);
			jdbcDao.executeUpdate(createTableSql);
			jdbcDao.commit();
		}
		catch (DAOException e)
		{
			Logger.out.error(e);
			throw e;
		}
		finally
		{
			jdbcDao.closeSession();
		}
	}

	/**
		 * @param tableName Name of the table (temporary table)
		 * @param columnNameIndex Map with key->index and value->column names
		 * @return selectSql The select query
		 */
	public static String getSQLForRootNode(final String tableName, Map<String, String> columnNameIndex)
	{
		String columnNames = columnNameIndex.get(Constants.COLUMN_NAMES);
		String indexStr = columnNameIndex.get(Constants.INDEX);
		int index = -1;
		if (indexStr != null && !Constants.NULL.equals(indexStr))
		{
			index = Integer.valueOf(indexStr);
		}
		String idColumnName = columnNames;
		if (columnNames.indexOf(',') != -1)
		{
			idColumnName = columnNames.substring(0, columnNames.indexOf(','));
		}
		StringBuffer selectSql = new StringBuffer(QueryModuleConstants.FIFTY);
		selectSql.append("select distinct ").append(columnNames).append(" from ")
		.append(tableName).append(" where ").append(idColumnName).append(" is not null");
		selectSql = selectSql.append(Constants.NODE_SEPARATOR).append(index);
		return selectSql.toString();
	}
}
