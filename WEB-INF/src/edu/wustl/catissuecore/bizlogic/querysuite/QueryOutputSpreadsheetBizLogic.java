
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * Creates QueryOutput spreadsheet data as per the inputs given by user on AddLimits and define results section .
 * This loads spreadsheet data in both the cases , once while loading first level (default) tree is shown and 
 * secondly when user clicks on any of the node of the tree , the appropriate spreadsheet data is also loaded.
 * @author deepti_shelar
 */
public class QueryOutputSpreadsheetBizLogic
{
	/**
	 * This loads spreadsheet data when first level (default) tree is shown.This method is also called when data is to be 
	 * loaded when user clicks on a leaf node of tree.
	 * @param curentNodeId long id for the node user has clicked
	 * @param node clicked by user
	 * @param idColumnMap  map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param isFirstLevel whether data is getting loaded for first level tree or on click of a node
	 * @param parentNodeId the string id of the parent 
	 * @return map having data for column headers and data records.
	 * @throws DAOException  DAOException 
	 */
	public Map<String, List<String>> createSpreadsheetData(Long curentNodeId, OutputTreeDataNode node,
			Map<Long, Map<AttributeInterface, String>> idColumnMap, String parentNodeId, SessionDataBean sessionData) throws DAOException,
			ClassNotFoundException
			{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		Map spreadSheetDataMap = new HashMap();
		String parentIdColumnName = null;
		Map<AttributeInterface, String> columnsMap = idColumnMap.get(node.getId());
		Set<AttributeInterface> setForParent = columnsMap.keySet();
		for (Iterator<AttributeInterface> iterator = setForParent.iterator(); iterator.hasNext();)
		{
			AttributeInterface attr = iterator.next();
			if (attr.getName().equalsIgnoreCase(Constants.ID))
			{
				parentIdColumnName = columnsMap.get(attr);
				break;
			}
		}
		if (parentNodeId != null)
		{
			columnsMap = idColumnMap.get(curentNodeId);
		}
		String selectSql = "select distinct ";
		List<String> columnsList = new ArrayList<String>();
		columnsList.add("");
		Set<AttributeInterface> set = columnsMap.keySet();
		for (Iterator<AttributeInterface> iterator = set.iterator(); iterator.hasNext();)
		{
			AttributeInterface attribute = iterator.next();
			String className = attribute.getEntity().getName();
			className = className.substring(className.lastIndexOf('.') + 1, className.length());
			String sqlColumnName = columnsMap.get(attribute);
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			String attrLabel = QueryModuleUtil.getAttributeLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
		selectSql = selectSql + " from " + tableName;
		if (parentNodeId != null)
		{
			selectSql = selectSql + " where " + parentIdColumnName + " = '" + parentNodeId + "'";
		}
		List spreadsheetDataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
		spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
		return spreadSheetDataMap;
			}

	/**
	 * Updates spreadsheet when user clicks on tree node
	 * @param id long id for the node user has clicked
	 * @param node OutputTreeDataNode clicked by user
	 * @param idColumnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param parentNodeId String id of parent
	 * @param sessionData Session data bean
	 * @return Map<String, List<String>> Map for columns and data list
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	public Map<String, List<String>> updateSpreadsheet(Long id,OutputTreeDataNode node, Map<Long, Map<AttributeInterface, String>> idColumnMap,
			String parentNodeId, SessionDataBean sessionData) throws ClassNotFoundException, DAOException
			{
		Map<AttributeInterface, String> columnsMap = idColumnMap.get(node.getId());
		String parentIdColumnName = null;
		Set<AttributeInterface> setForParent = columnsMap.keySet();
		for (Iterator<AttributeInterface> iterator = setForParent.iterator(); iterator.hasNext();)
		{
			AttributeInterface attr = iterator.next();
			if (attr.getName().equalsIgnoreCase(Constants.ID))
			{
				parentIdColumnName = columnsMap.get(attr);
				break;
			}
		}
		return createSpreadsheetDataMap(node, id, parentIdColumnName, parentNodeId, idColumnMap, sessionData);				
			}
	/**
	 * Creates columns list for spreadsheet.
	 * @return List column headers
	 */
	private List createColumnHeadersList()
	{
		List<String> columnsList = new ArrayList<String>();
		columnsList.add(Constants.ENTITY_NAME);
		columnsList.add(Constants.COUNT);
		return columnsList;
	}
	/**
	 * Ccreates map for spreadsheet data. Creates data for all child nodes under the node the user has clicked and adds it to a map.
	 * @param node OutputTreeDataNode clicked by user
	 * @param id long id for the node user has clicked
	 * @param parentIdColumnName string column name of parent node
	 * @param parentNodeId String id of parent
	 * @param idColumnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param sessionData Session data bean
	 * @return Map<String, List<String>> Map for columns and data list
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	private Map createSpreadsheetDataMap(OutputTreeDataNode node,Long id,String parentIdColumnName,
			String parentNodeId,Map<Long, Map<AttributeInterface, String>> idColumnMap,SessionDataBean sessionData) throws ClassNotFoundException, DAOException
			{
		Map spreadSheetDataMap = new HashMap();
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, createColumnHeadersList());
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		List spreadsheetDataList = new ArrayList();
		List<OutputTreeDataNode> children = node.getChildren();
		for (OutputTreeDataNode childNode : children)
		{
			Map<AttributeInterface, String> columnsMap = columnsMap = idColumnMap.get(childNode.getId());
			String selectSql = getSql(parentIdColumnName, parentNodeId, tableName, columnsMap);
			List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
			int size = dataList.size();
			if(size == 0)
			{
				spreadSheetDataMap = createSpreadsheetData(id, node, idColumnMap, parentNodeId, sessionData);
			} 
			else
			{
				String name = childNode.getOutputEntity().getDynamicExtensionsEntity().getName();
				name = name.substring(name.lastIndexOf(".") + 1, name.length());
				List data = new ArrayList();
				data.add(name);
				data.add(size);
				spreadsheetDataList.add(data);
				spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
			}
		}
		return spreadSheetDataMap;
			}

	/**
	 * Returns SQL for each child node to be processed to generate spreadsheet data.
	 * @param parentIdColumnName column name of parent node's id
	 * @param parentNodeId String id of parent node
	 * @param tableName name of the table 
	 * @param columnsMap map of attribute name and its column name in temporary table
	 * @return String sql to be fired to get spreadsheet data
	 */
	private String getSql(String parentIdColumnName, String parentNodeId, String tableName, Map<AttributeInterface, String> columnsMap)
	{
		String selectSql = "select distinct ";
		Set<AttributeInterface> set = columnsMap.keySet();
		String idColumnOfCurrentNode = "";
		for (Iterator<AttributeInterface> iterator = set.iterator(); iterator.hasNext();)
		{
			AttributeInterface attribute = iterator.next();
			String sqlColumnName = columnsMap.get(attribute);
			if(attribute.getName().equals("id"))
			{
				idColumnOfCurrentNode = sqlColumnName;
			}
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
		}
		selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
		selectSql = selectSql + " from " + tableName;
		if (parentNodeId != null)
		{
			selectSql = selectSql + " where (" + parentIdColumnName + " = '" + parentNodeId + "' and "+idColumnOfCurrentNode+" is not null)";
		}
		return selectSql;
	}
}
