
package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This is an utility class to provide methods required for query interface.
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
	 * Executes the query and returns the results.
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
			StringBuffer gridStrBuff  = new StringBuffer();
			for(Object columnData : row)
			{
				Object gridObj = (Object)Utility.toNewGridFormat(columnData);
				String gridStr = gridObj.toString();
				gridStrBuff.append(gridStr+",");
			}
			dataStr = dataStr + "|" + gridStrBuff.toString();
		}
		List columnsList = (List) spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST);
		String columns = columnsList.toString();
		columns = columns.replace("[", "");
		columns = columns.replace("]", "");
		outputSpreadsheetDataStr = columns + "&" + dataStr;
		return outputSpreadsheetDataStr;
	}
	/**
	 * Returns SQL for root node in tree.
	 * @param root root node of the tree
	 * @param tableName name of the temp table created
	 * @return String sql for root node
	 */
	public static String getSQLForRootNode(OutputTreeDataNode root, String tableName)
	{
		String columnNames = getColumnNamesForSelectpart(root);
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
		String idColumnName = columnNames;
		if(columnNames.indexOf(",") != -1)
		{
			idColumnName = columnNames.substring(0,columnNames.indexOf(","));
		}
		String selectSql = "select distinct " + columnNames + " from " + tableName +" where "+idColumnName +" is not null";
		selectSql = selectSql + Constants.NODE_SEPARATOR + index;
		return selectSql;
	}
	/**
	 * Forms select part of the query.
	 * @param node Node of Uotput tree .
	 * @param columnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return String having all columnnames for select part.
	 */
	public static String getColumnNamesForSelectpart(OutputTreeDataNode node)
	{
		String columnNames = "";
		String idColumnName = null;
		String displayNameColumnName = null;
		String index = null;
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String columnName = attributeMetaData.getColumnName();
			if (idColumnName != null && displayNameColumnName != null)
			{
				break;
			}
			if (attribute.getName().equalsIgnoreCase(Constants.ID))
			{
				idColumnName = columnName;
			}
			else if (ifAttributeIsDisplayName(attribute.getName()))
			{
				index = columnName.substring(Constants.COLUMN_NAME.length(), columnName.length());
				displayNameColumnName = columnName;
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
	/**
	 * This is used to set the default selections for the UI components when the screen is loaded for the first time.
	 * @param actionForm form bean
	 * @return CategorySearchForm formbean
	 */
	public static CategorySearchForm setDefaultSelections(CategorySearchForm actionForm)
	{
		if (actionForm.getClassChecked() == null)
		{
			actionForm.setClassChecked("on");
		}
		if (actionForm.getAttributeChecked() == null)
		{
			actionForm.setAttributeChecked("on");
		}
		if (actionForm.getPermissibleValuesChecked() == null)
		{
			actionForm.setPermissibleValuesChecked("off");
		}
		//TODO check if null and then set the value of seleted.
		actionForm.setSelected("text_radioButton");
		return actionForm;
	}
	
	/**
	 * When passes treeNumber , this method returns the root node of that tree. 
	 * @param rootOutputTreeNodeList tree deta
	 * @param treeNo number of tree
	 * @return root node of the tree
	 */
	public static OutputTreeDataNode getRootNodeOfTree(List<OutputTreeDataNode> rootOutputTreeNodeList,String treeNo)
	{
		for(OutputTreeDataNode node :rootOutputTreeNodeList)
		{
			if (node.getTreeNo() == new Integer(treeNo).intValue())
				return node;
		}
		return null;
	}
	/**
	 * Returns column name of nodes id when passed a node to it 
	 * @param node {@link OutputTreeDataNode}
	 * @return String id Columns name
	 */
	public static String getParentIdColumnName(OutputTreeDataNode node)
	{
		if(node != null)
		{
			List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
			for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
			{
				AttributeInterface attribute = attributeMetaData.getAttribute();
				if(attribute.getName().equalsIgnoreCase(Constants.ID))
				{
					String sqlColumnName = attributeMetaData.getColumnName();
					return sqlColumnName;
				}
			}
		}
		return null;
	}
}
