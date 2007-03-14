
package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This is a Utility class to provide methods which are called from multiple places.
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
				if (asciiValue >= 65 && asciiValue <= 90)
				{
					attrLabel = attrLabel + attrChar;
				} 
				else
				{
					int capitalAsciiValue = asciiValue - 32;
					attrLabel = attrLabel + (char) capitalAsciiValue;
				}
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
								isPreviousLetterLowerCase = false;
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
	public static void main(String args[])
	{
		System.out.println(QueryModuleUtil.getAttributeLabel("FirstMiddleURL"));
	}
	/**
	 * Takes data from the map and generates out put data accordingly so that spreadsheet will be updated.
	 * @param spreadSheetDatamap map which holds data for columns and records.
	 * @return this string consists of two strings seperated by '&', first part is for column names to be displayed in spreadsheet 
	 * and the second part is data in the spreadsheet.
	 */
	public static String prepareOutputSpreadsheetDataString(Map spreadSheetDatamap)
	{
		List<List<String>> dataList = (List<List<String>>) spreadSheetDatamap.get(Constants.SPREADSHEET_DATA_LIST);
		String outputSpreadsheetDataStr = "";
		String dataStr = "";
		for (List<String> row : dataList)
		{
			String rowStr = row.toString();
			rowStr = rowStr.replace("[", "");
			rowStr = rowStr.replace("]", "");
			dataStr = dataStr + "|" + rowStr;
		}
		List columnsList = (List) spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST);
		String columns = columnsList.toString();
		columns = columns.replace("[", "");
		columns = columns.replace("]", "");
		outputSpreadsheetDataStr = columns + "&" + dataStr;
		return outputSpreadsheetDataStr;
	}
	/**
	 * Returns SQL for root node.
	 * @param root root node of the tree
	 * @param tableName name of the temp table created
	 * @param nodeAttributeColumnNameMap  map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return String sql for root node
	 */
	public static String getSQLForRootNode(OutputTreeDataNode root, String tableName, Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap)
	{
		String columnNames = getColumnNamesForSelectpart(root, nodeAttributeColumnNameMap);
		String indexStr = columnNames.substring(columnNames.lastIndexOf(";") + 1, columnNames.length());
		int index = -1;
		if (!indexStr.equalsIgnoreCase("null"))
		{
			index = new Integer(indexStr);
		}
		if (columnNames.lastIndexOf(";") != -1)
		{
			columnNames = columnNames.substring(0, columnNames.lastIndexOf(";"));
		}
		String selectSql = "select distinct " + columnNames + " from " + tableName;
		selectSql = selectSql + Constants.NODE_SEPARATOR + index;
		return selectSql;
	}
	/**
	 * Forms select part of the query.
	 * @param node Node of Uotput tree .
	 * @param columnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return String having all columnnames for select part.
	 */
	public static String getColumnNamesForSelectpart(OutputTreeDataNode node, Map<Long, Map<AttributeInterface, String>> columnMap)
	{
		String columnNames = "";
		String idColumnName = null;
		String displayNameColumnName = null;
		String index = null;
		Map<AttributeInterface, String> columns = columnMap.get(node.getId());
		if (columns != null)
		{
			Set<AttributeInterface> setColumns = columns.keySet();
			for (Iterator<AttributeInterface> iterator = setColumns.iterator(); iterator.hasNext();)
			{
				AttributeInterface attr = iterator.next();
				String columnName = columns.get(attr);
				if (idColumnName != null && displayNameColumnName != null)
				{
					break;
				}
				if (attr.getName().equalsIgnoreCase(Constants.ID))
				{
					idColumnName = columnName;
				}
				else if (ifAttributeIsDisplayName(attr.getName()))
				{
					index = columnName.substring(Constants.COLUMN_NAME.length(), columnName.length());
					displayNameColumnName = columnName;
				}
			}
		}
		if (displayNameColumnName != null)
		{
			columnNames = idColumnName + " , " + displayNameColumnName + " , " + columnNames;
		}
		else
		{
			columnNames = idColumnName + " , " + columnNames;
		}
		columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		columnNames = columnNames + ";" + index;
		return columnNames;
	}
	/**
	 * Returns true if the attribute name can be used to form label for tree node.
	 * @param attrName String
	 * @return true if the attribute name can be used to form label for tree node otherwise returns false
	 */
	public static boolean ifAttributeIsDisplayName(String attrName)
	{
		String[] attrNamesForLabel = Constants.ATTRIBUTE_NAMES_FOR_TREENODE_LABEL;
		for (int i = 0; i < attrNamesForLabel.length; i++)
		{
			String name = attrNamesForLabel[i];
			if (attrName.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}
}
