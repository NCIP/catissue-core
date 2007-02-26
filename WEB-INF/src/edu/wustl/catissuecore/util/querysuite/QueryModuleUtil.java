
package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This is a Utility class to provide methods which are called at multiple places.
 * Mostly database operations are done in this class.
 * @author deepti_shelar
 */
public abstract class QueryModuleUtil
{
	/**
	 * Returns the label for attribute's name. It compares ascii value of each char for lower or upper case and then forms a capitalized lebel. 
	 * eg firstName is converted to First Name
	 * @param attrName name of the attribute
	 * @return capitalized label
	 */
	static public String getAttributeLabel(String attrName)
	{
		String attrLabel = "";
		boolean isPreviousLetterLowerCase = false;
		int len = attrName.length();
		for (int i = 0; i < len; i++)
		{
			char attrChar = attrName.charAt(i);
			int asciiValue = attrChar;
			if (i == 0)
			{
				int capitalAsciiValue = asciiValue - 32;
				attrLabel = attrLabel + (char) capitalAsciiValue;
			}
			else
			{
				if (asciiValue >= 65 && asciiValue <= 90)
				{
					attrLabel = attrLabel + " " + attrChar;
					for (int k = i + 1; k < len; k++)
					{
						attrChar = attrName.charAt(k);
						asciiValue = attrChar;
						if (asciiValue >= 65 && asciiValue <= 90)
						{
							if (isPreviousLetterLowerCase)
							{
								attrLabel = attrLabel + " " + attrChar;
							}
							else
							{
								attrLabel = attrLabel + attrChar;
							}
							i++;
						}
						else
						{
							isPreviousLetterLowerCase = true;
							attrLabel = attrLabel + attrChar;
							i++;
						}
					}
				}
				else
				{
					attrLabel = attrLabel + attrChar;
				}
			}
		}
		return attrLabel;
	}

	/**
	 * Executes the query and returns the results back.
	 * @param selectSql sql to be executed
	 * @param sessionData sessiondata
	 * @return list of results 
	 * @throws ClassNotFoundException 
	 * @throws DAOException 
	 */
	public static List<List<String>> executeQuery(String selectSql, SessionDataBean sessionData) throws ClassNotFoundException, DAOException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List<List<String>> dataList = new ArrayList<List<String>>();
		try
		{
			dao.openSession(sessionData);
			dataList = dao.executeQuery(selectSql, sessionData, false, false, null);
			dao.commit();
		}
		finally
		{
			dao.closeSession();
		}
		return dataList;
	}

	/**
	 * Creates a new table in database. First the table is deleted if exist already.
	 * @param tableName name of the table to be deleted before creating new one. 
	 * @param createTableSql sql to create table
	 * @param sessionData session data.
	 * @throws DAOException DAOException 
	 */
	public static void executeCreateTable(String tableName, String createTableSql, SessionDataBean sessionData) throws DAOException
	{
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDao.openSession(sessionData);
			jdbcDao.delete(tableName);
			jdbcDao.executeUpdate(createTableSql);
			jdbcDao.commit();
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			jdbcDao.closeSession();
		}
	}
}
