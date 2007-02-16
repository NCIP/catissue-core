
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * Creates QueryOutput spreadsheet data as per the inputs given by user on AddLimits section.
 * This loads spreadsheet data in both the cases , once while loading first level (default) tree is shown and 
 * secondly when user clicks on any of the node of the tree , the appropriate spreadsheet data is also loaded.
 * @author deepti_shelar
 */
public class QueryOutputSpreadsheetBizLogic
{
	/**
	 * This loads spreadsheet data in both the cases , once while loading first level (default) tree is shown and 
	 * secondly when user clicks on any of the node of the tree , the appropriate spreadsheet data is also loaded.
	 * @param tableName name of the newly created table
	 * @param node node clicked by user
	 * @param idColumnMap  map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param isFirstLevel whether data is getting loaded for first level tree or on click of a node
	 * @param parentNodeId the id of the parent 
	 * @return map having data for column headers and data records.
	 */
	public Map<String, List<String>> createSpreadsheetData(String tableName, IOutputTreeNode node, Map<Long, Map<AttributeInterface, String>> idColumnMap,
			boolean isFirstLevel, String parentNodeId,SessionDataBean sessionData)
	{
		Map<String, List<String>> spreadSheetDataMap = new HashMap<String, List<String>>();
		Map<AttributeInterface, String> columnsMap = null;
		String parentIdColumnName = null;
		columnsMap = idColumnMap.get(node.getId());
		Set<AttributeInterface> set = columnsMap.keySet();
		for (Iterator<AttributeInterface> iterator = set.iterator(); iterator.hasNext();)
		{
			AttributeInterface attr = iterator.next();
			if (attr.getName().equalsIgnoreCase(Constants.ID))
			{
				parentIdColumnName = columnsMap.get(attr);
				break;
			}
		}
		if (!isFirstLevel)
		{
			List<IOutputTreeNode> children = getChildNodes(node);
			for (IOutputTreeNode childNode : children)
			{
				columnsMap = idColumnMap.get(childNode.getId());
				break;
			}
		}
		String selectSql = "select distinct ";
		List<String> columnsList = new ArrayList<String>();
		columnsList.add("");
		Set<AttributeInterface> set1 = columnsMap.keySet();
		for (Iterator<AttributeInterface> iterator = set1.iterator(); iterator.hasNext();)
		{
			AttributeInterface attribute = iterator.next();
			String className = attribute.getEntity().getName();
			className = className.substring(className.lastIndexOf('.') + 1, className.length());
			String sqlColumnName = columnsMap.get(attribute);
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			columnsList.add(attribute.getName() + " : " + className);
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
		selectSql = selectSql + " from " + tableName;
		if (!isFirstLevel && parentNodeId != null)
		{
			selectSql = selectSql + " where " + parentIdColumnName + " = '" + parentNodeId + "'";
		}
		List<String> recordsList = getRecordsForNode(selectSql,sessionData);
		spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, recordsList);
		return spreadSheetDataMap;
	}

	/**
	 * Fires the sql and returns the resulted records. 
	 * @param selectSql String sql to be fired
	 * @return list of records
	 */
	public List<String> getRecordsForNode(String selectSql,SessionDataBean sessionData)
	{
		List<String> recordslist = new ArrayList<String>();
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			dao.openSession(sessionData);
			recordslist = dao.executeQuery(selectSql, null, false, false, null);
			dao.closeSession();
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return recordslist;
	}

	/**
	 * Returns all immediate child nodes for the root node passed to it.
	 * @param node IOutputTreeNode ,
	 * @param allChildNodes list of nodes
	 * @return List of IOutputTreeNode all child nodes
	 */
	public List<IOutputTreeNode> getChildNodes(IOutputTreeNode node)
	{
		List<IOutputTreeNode> childNodes = new ArrayList<IOutputTreeNode>();
		if (!node.isLeaf())
		{
			childNodes = (node.getChildren());
		}
		return childNodes;
	}
}
