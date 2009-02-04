
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
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.util.Utility;
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
	 * @param treeNo tree number user has clicked
	 * @param node clicked by user
	 * @param outputTreeMap  map which strores all details of tree
	 * @param parentNodeData the string id of the parent 
	 * @return map having data for column headers and data records.
	 * @throws DAOException  DAOException 
	 */
	public Map<String, List<String>> createSpreadsheetData(String treeNo,OutputTreeDataNode node,
			 SessionDataBean sessionData,String parentData) throws DAOException,
			ClassNotFoundException
			{
		Map spreadSheetDataMap = updateSpreadsheetData(sessionData, parentData,  node);
		return spreadSheetDataMap;
			}
	/**
	 * @param sessionData
	 * @param parentData
	 * @param tableName
	 * @param spreadSheetDataMap
	 * @param node
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	private Map updateSpreadsheetData(SessionDataBean sessionData, String parentData, OutputTreeDataNode node) throws ClassNotFoundException, DAOException
	{
		Map<String, List<List<String>>> spreadSheetDataMap = new HashMap<String, List<List<String>>>();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(node);
		String selectSql = createSQL(parentData, tableName, spreadSheetDataMap, parentIdColumnName, node);
		List<List<String>> spreadsheetDataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
		spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
		return spreadSheetDataMap;
	}
	
	/**
	 * @param parentData
	 * @param tableName
	 * @param spreadSheetDataMap
	 * @param parentIdColumnName
	 * @param node
	 * @return
	 */
	private String createSQL(String parentData, String tableName, Map spreadSheetDataMap, String parentIdColumnName, OutputTreeDataNode node)
	{
		String selectSql = "select distinct ";
		List<String> columnsList = new ArrayList<String>();
		//columnsList.add("");
		String idColumnOfCurrentNode = "";
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String sqlColumnName = attributeMetaData.getColumnName();
			String className = attribute.getEntity().getName();
			className = Utility.parseClassName(className);
			if(attribute.getName().equals(Constants.ID))
			{
				idColumnOfCurrentNode = sqlColumnName;
			}
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			String attrLabel = QueryModuleUtil.getAttributeLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
		selectSql = selectSql + " from " + tableName;
		if (parentData != null)
		{
			selectSql = selectSql + " where (" + parentIdColumnName + " = '" + parentData + "' "+ 
			LogicalOperator.And+ " "+idColumnOfCurrentNode+" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull) +")";
		}
		else
		{
			selectSql = selectSql + " where "+idColumnOfCurrentNode +" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull);	
		}
		return selectSql;
	}
	/**
	 * Returns SQL for each child node to be processed to generate spreadsheet data.
	 * @param parentIdColumnName column name of parent node's id
	 * @param parentNodeId String id of parent node
	 * @param tableName name of the table 
	 * @param childNode map of attribute name and its column name in temporary table
	 * @return String sql to be fired to get spreadsheet data
	 */
	private String getSql(String parentIdColumnName, String parentData, String tableName, OutputTreeDataNode node)
	{
		String selectSql = "select distinct ";
		String idColumnOfCurrentNode = "";
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String sqlColumnName = attributeMetaData.getColumnName();
			if(attribute.getName().equals(Constants.ID))
			{
				idColumnOfCurrentNode = sqlColumnName;
			}
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
		}
		selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
		selectSql = selectSql + " from " + tableName;
		if (parentData != null)
		{
			selectSql = selectSql + " where (" + parentIdColumnName + " = '" + parentData + "' "+LogicalOperator.And
			+ " "+idColumnOfCurrentNode+" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull) +")";
		}
		else
		{
			selectSql = selectSql + " where "+idColumnOfCurrentNode+" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull);	
		}
		return selectSql;
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
	private Map<String, List<String>> updateSpreadsheet(String treeNo,OutputTreeDataNode node,List<OutputTreeDataNode> rootOutputTreeNodeList,
			SessionDataBean sessionData,String parentData) throws ClassNotFoundException, DAOException
			{
		OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(rootOutputTreeNodeList,treeNo);
		String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(node);
		return createSpreadsheetDataMap(root,node, parentIdColumnName, parentData, sessionData);				
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
	private Map createSpreadsheetDataMap(OutputTreeDataNode root,OutputTreeDataNode node,String parentIdColumnName,
			String parentData,SessionDataBean sessionData) throws ClassNotFoundException, DAOException
			{
		Map<String,List<List<String>>> spreadSheetDataMap = new HashMap<String,List<List<String>>>();
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, createColumnHeadersList());
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		List<List<String>> spreadsheetDataList = new ArrayList<List<String>>();
		List<OutputTreeDataNode> children = node.getChildren();
		for (OutputTreeDataNode childNode : children)
		{
			//Map<AttributeInterface, String> columnsMap = outputTreeMap.get(root).get(childNode.getId());
			String selectSql = getSql(parentIdColumnName, parentData, tableName, childNode);
			List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
			int size = dataList.size();
			String sizeStr = new Integer(size).toString();
			if(size == 0)
			{
				//columnsMap = outputTreeMap.get(root).get(node.getId());
				spreadSheetDataMap = updateSpreadsheetData(sessionData, parentData,  node);
			} 
			else
			{
				String name = childNode.getOutputEntity().getDynamicExtensionsEntity().getName();
				name = Utility.parseClassName(name);
				List<String> data = new ArrayList<String>();
				data.add(name);
				data.add(sizeStr);
				spreadsheetDataList.add(data);
				spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
			}
		}
		return spreadSheetDataMap;
			}
	/**
	 * Processes spreadsheet data for label node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @param nodeIds string array of node ids
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForLabelNode(Map<String, OutputTreeDataNode> idNodesMap,List<OutputTreeDataNode> rootOutputTreeNodeList, Map<Long, Map<AttributeInterface, String>> columnMap, SessionDataBean sessionData, String idOfClickedNode) throws DAOException, ClassNotFoundException
	{
		Map spreadSheetDataMap = new HashMap();
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String[] nodeIds = idOfClickedNode.split(Constants.NODE_SEPARATOR);
		String parentNode = nodeIds[0];//data
		String[] spiltParentNodeId = parentNode.split(Constants.UNDERSCORE);
		String treeNo = spiltParentNodeId[0];
		String parentNodeId = spiltParentNodeId[1];
		if(parentNode.contains("NULL"))
		{
			OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(rootOutputTreeNodeList,treeNo);
			spreadSheetDataMap = outputSpreadsheetBizLogic.createSpreadsheetData(treeNo,root, sessionData,null);
		} else
		{
			String parentData = spiltParentNodeId[2];
			String uniqueParentNodeId = treeNo+"_"+parentNodeId;
			OutputTreeDataNode parentTreeNode = idNodesMap.get(uniqueParentNodeId);
			OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(rootOutputTreeNodeList,treeNo);
			String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(parentTreeNode);
			String currentNode = nodeIds[1];//label
			String[] spiltCurrentNodeId = currentNode.split(Constants.UNDERSCORE);
			String currentNodeId = spiltCurrentNodeId[1];
			String uniqueCurrentNodeId = treeNo+"_"+currentNodeId;
			OutputTreeDataNode currentTreeNode = idNodesMap.get(uniqueCurrentNodeId);
			String selectSql = createSQL(spreadSheetDataMap, currentTreeNode,parentIdColumnName,parentData,tableName);
			List spreadsheetDataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
			spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
		}
		return spreadSheetDataMap;
	}
	/**
	 * Processes spreadsheet data for data node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForDataNode(Map<String, OutputTreeDataNode> idNodesMap,
			List<OutputTreeDataNode> rootOutputTreeNodeList, SessionDataBean sessionData, String actualParentNodeId)
	throws DAOException, ClassNotFoundException
	{
		Map spreadSheetDatamap = null;
		String[] nodeIds;
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		String treeNo = nodeIds[0];
		String parentId = nodeIds[1];
		String parentData = nodeIds[2];
		String uniqueNodeId = treeNo+"_"+parentId;
		OutputTreeDataNode parentNode = idNodesMap.get(uniqueNodeId);
		if (parentNode.getChildren().isEmpty())
		{
			spreadSheetDatamap = createSpreadsheetData(treeNo,parentNode,  sessionData,parentData);
		}
		else
		{
			spreadSheetDatamap = updateSpreadsheet(treeNo,parentNode, rootOutputTreeNodeList, sessionData,parentData);
		}
		return spreadSheetDatamap;
	}
	/**
	 * @param spreadSheetDataMap
	 * @param columnsMap
	 * @param tableName 
	 * @param parentData 
	 * @param parentIdColumnName 
	 * @return
	 */
	private String createSQL(Map spreadSheetDataMap, OutputTreeDataNode node, String parentIdColumnName, String parentData, String tableName)
	{
		String selectSql = "select distinct ";
		List<String> columnsList = new ArrayList<String>();
		//columnsList.add("");
		String idColumnOfCurrentNode = "";
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String className = attribute.getEntity().getName();
			className = Utility.parseClassName(className);
			String sqlColumnName = attributeMetaData.getColumnName();
			if(attribute.getName().equalsIgnoreCase(Constants.ID))
			{
				idColumnOfCurrentNode = sqlColumnName;
			}
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			String attrLabel = QueryModuleUtil.getAttributeLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
		selectSql = selectSql + " from " + tableName;
		if (parentData != null)
		{
			selectSql = selectSql + " where (" + parentIdColumnName + " = '" + parentData + "' "+LogicalOperator.And + " " + idColumnOfCurrentNode+" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull)+")";
		}else
		{
			selectSql = selectSql + " where "+idColumnOfCurrentNode+" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull);	
		}
		return selectSql;
	}
}
