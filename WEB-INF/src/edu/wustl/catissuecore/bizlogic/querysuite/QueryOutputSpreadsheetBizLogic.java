
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
	 * This loads spreadsheet data when first level (default) tree is shown. 
	 * @param node node clicked by user
	 * @param idColumnMap  map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param isFirstLevel whether data is getting loaded for first level tree or on click of a node
	 * @param parentNodeId the id of the parent 
	 * @return map having data for column headers and data records.
	 * @throws DAOException  DAOException 
	 */
	public Map<String, List<String>> createSpreadsheetData(Long curentNodeId, OutputTreeDataNode node,
			Map<Long, Map<AttributeInterface, String>> idColumnMap, String parentNodeId, SessionDataBean sessionData) throws DAOException,
			ClassNotFoundException
	{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		Map spreadSheetDataMap = new HashMap();
		Map<AttributeInterface, String> columnsMap = null;
		String parentIdColumnName = null;
		columnsMap = idColumnMap.get(node.getId());
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
	 * @param node OutputTreeDataNode clicked by user
	 * @param idColumnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param parentNodeId String id of parent
	 * @param sessionData Session data bean
	 * @return Map<String, List<String>> Map for columns and data list
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	public Map<String, List<String>> updateSpreadsheet(OutputTreeDataNode node, Map<Long, Map<AttributeInterface, String>> idColumnMap,
			String parentNodeId, SessionDataBean sessionData) throws ClassNotFoundException, DAOException
	{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		Map spreadSheetDataMap = new HashMap();
		List<String> columnsList = new ArrayList<String>();
		columnsList.add(Constants.ENTITY_NAME);
		columnsList.add(Constants.COUNT);
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		Map<AttributeInterface, String> columnsMap = null;
		String parentIdColumnName = null;
		columnsMap = idColumnMap.get(node.getId());
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
		List spreadsheetDataList = new ArrayList();
		List<OutputTreeDataNode> children = node.getChildren();
		for (OutputTreeDataNode childNode : children)
		{
			columnsMap = idColumnMap.get(childNode.getId());
			String selectSql = "select distinct ";
			Set<AttributeInterface> set = columnsMap.keySet();
			for (Iterator<AttributeInterface> iterator = set.iterator(); iterator.hasNext();)
			{
				AttributeInterface attribute = iterator.next();
				String sqlColumnName = columnsMap.get(attribute);
				selectSql = selectSql + sqlColumnName + ",";
				sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			}
			selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
			selectSql = selectSql + " from " + tableName;
			if (parentNodeId != null)
			{
				selectSql = selectSql + " where " + parentIdColumnName + " = '" + parentNodeId + "'";
			}
			String name = childNode.getOutputEntity().getDynamicExtensionsEntity().getName();
			name = name.substring(name.lastIndexOf(".") + 1, name.length());
			List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
			int size = dataList.size();
			List data = new ArrayList();
			data.add(name);
			data.add(size);
			spreadsheetDataList.add(data);
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
		return spreadSheetDataMap;
	}

}
