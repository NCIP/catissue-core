
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
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
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
	private SelectedColumnsMetadata selectedColumnMetaData;
	/**
	 * Processes spreadsheet data for label node which user has clicked.
     * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree	 * @param rootOutputTreeNodeList
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param idOfClickedNode
	 * @param recordsPerPage number of recordsPerPage
	 * @param selectedColumnMetaData metadat for selected columns 
	 * @return Map for data
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForLabelNode(Map<String, OutputTreeDataNode> idNodesMap,
			List<OutputTreeDataNode> rootOutputTreeNodeList, 
			Map<Long, Map<AttributeInterface, String>> columnMap, 
			SessionDataBean sessionData, String idOfClickedNode, int recordsPerPage,SelectedColumnsMetadata selectedColumnMetaData)
	throws DAOException, ClassNotFoundException
	{
		this.selectedColumnMetaData = selectedColumnMetaData;
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		Map spreadSheetDataMap = new HashMap();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String[] nodeIds = idOfClickedNode.split(Constants.NODE_SEPARATOR);
		String parentNode = nodeIds[0];//data
		String[] spiltParentNodeId = parentNode.split(Constants.UNDERSCORE);
		String treeNo = spiltParentNodeId[0];
		String parentNodeId = spiltParentNodeId[1];
		if(parentNode.contains("NULL"))
		{
			OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(rootOutputTreeNodeList,treeNo);
			spreadSheetDataMap = createSpreadsheetData(treeNo,root, sessionData,null,recordsPerPage);
			spreadSheetDataMap.put(Constants.CURRENT_SELECTED_OBJECT,root);
		} else
		{
			String parentData = spiltParentNodeId[2];
			String uniqueParentNodeId = treeNo+"_"+parentNodeId;
			OutputTreeDataNode parentTreeNode = idNodesMap.get(uniqueParentNodeId);
			String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(parentTreeNode);
			String currentNode = nodeIds[1];//label
			String[] spiltCurrentNodeId = currentNode.split(Constants.UNDERSCORE);
			String currentNodeId = spiltCurrentNodeId[1];
			String uniqueCurrentNodeId = treeNo+"_"+currentNodeId;
			OutputTreeDataNode currentTreeNode = idNodesMap.get(uniqueCurrentNodeId);
			if(selectedColumnMetaData == null)
				this.selectedColumnMetaData = defineGridViewBizLogic.getColumnsMetadataForSelectedNode(currentTreeNode);
			String selectSql = createSQL(spreadSheetDataMap, currentTreeNode,parentIdColumnName,parentData,tableName);
			int startIndex = 0;
			QuerySessionData querySessionData = getQuerySessionData(sessionData, recordsPerPage,startIndex, spreadSheetDataMap, selectSql);
			spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
			spreadSheetDataMap.put(Constants.CURRENT_SELECTED_OBJECT,currentTreeNode);
		}
		return spreadSheetDataMap;
	}
	/**
	 * Processes spreadsheet data for data node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @param recordsPerPage 
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForDataNode(Map<String, OutputTreeDataNode> idNodesMap,
			List<OutputTreeDataNode> rootOutputTreeNodeList, SessionDataBean sessionData, String actualParentNodeId,
			int recordsPerPage,SelectedColumnsMetadata selectedColumnMetaData)
	throws DAOException, ClassNotFoundException
	{
		this.selectedColumnMetaData = selectedColumnMetaData;
		Map spreadSheetDatamap = null;
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		String[] nodeIds;
		nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		String treeNo = nodeIds[0];
		String parentId = nodeIds[1];
		String parentData = nodeIds[2];
		String uniqueNodeId = treeNo+"_"+parentId;
		OutputTreeDataNode parentNode = idNodesMap.get(uniqueNodeId);
		if(selectedColumnMetaData == null)
			this.selectedColumnMetaData = defineGridViewBizLogic.getColumnsMetadataForSelectedNode(parentNode);
		spreadSheetDatamap = createSpreadsheetData(treeNo,parentNode,  sessionData,parentData,recordsPerPage);
		/*if (parentNode.getChildren().isEmpty())
		{
			spreadSheetDatamap = createSpreadsheetData(treeNo,parentNode,  sessionData,parentData,recordsPerPage);
		}
		else
		{
			spreadSheetDatamap = updateSpreadsheet(treeNo,parentNode, rootOutputTreeNodeList, sessionData,parentData,recordsPerPage);
		}*/
		spreadSheetDatamap.put(Constants.CURRENT_SELECTED_OBJECT,parentNode);
		return spreadSheetDatamap;
	}

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
			 SessionDataBean sessionData,String parentData,int recordsPerPage) throws DAOException,
			ClassNotFoundException
			{
		Map spreadSheetDataMap = updateSpreadsheetData(sessionData, parentData,  node,recordsPerPage);
		return spreadSheetDataMap;
			}
	/**
	 * @param sessionData
	 * @param parentData
	 * @param tableName
	 * @param spreadSheetDataMap
	 * @param node
	 * @param recordsPerPage 
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	private Map updateSpreadsheetData(SessionDataBean sessionData, String parentData, OutputTreeDataNode node, int recordsPerPage) throws ClassNotFoundException, DAOException
	{
		Map spreadSheetDataMap = new HashMap();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(node);
		String selectSql = createSQL(parentData, tableName, spreadSheetDataMap, parentIdColumnName, node);
		int startIndex = 0;
		QuerySessionData querySessionData = getQuerySessionData(sessionData, recordsPerPage,startIndex, spreadSheetDataMap, selectSql);
		spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
		return spreadSheetDataMap;
	}
	
	/**
	 * Creates sql string for given data.
	 * @param parentData
	 * @param tableName name of the table
	 * @param spreadSheetDataMap map to store data list
	 * @param parentIdColumnName String column name of id of parents node
	 * @param node OutputTreeDataNode
	 * @return String sql
	 */
	private String createSQL(String parentData, String tableName, Map spreadSheetDataMap, String parentIdColumnName, OutputTreeDataNode node)
	{
		String selectSql = "select distinct ";
		String idColumnOfCurrentNode = "";
		List<String> columnsList = new ArrayList<String>();
		
		//columnsList.add("");
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		List <AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			attributeList.add(attribute);
			String sqlColumnName = attributeMetaData.getColumnName();
			String className = attribute.getEntity().getName();
			className = Utility.parseClassName(className);
			if(attribute.getName().equals(Constants.ID))
			{
				idColumnOfCurrentNode = sqlColumnName;
			}
			selectSql = selectSql + sqlColumnName + ",";
			String attrLabel = Utility.getDisplayLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
		}
		if(selectedColumnMetaData == null)
		{
			selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
			spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		}
		else
		{
			selectSql = getSQLForSelectedColumns(spreadSheetDataMap);
		}
		spreadSheetDataMap.put(Constants.ATTRIBUTES, attributeList);
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
	 * @param spreadSheetDataMap map to store data list
	 * @return String sql
	 */
	private String getSQLForSelectedColumns(Map spreadSheetDataMap)
	{
		String selectSql = "";
		if(selectedColumnMetaData != null)
		{
			List<String> definedColumnsList = new ArrayList<String>();
			StringBuffer sqlColumnNames = new StringBuffer();
			List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnMetaData.getSelectedAttributeMetaDataList();
			Iterator<QueryOutputTreeAttributeMetadata> iter = selectedAttributeMetaDataList.iterator();
			while(iter.hasNext())
			{
				QueryOutputTreeAttributeMetadata metaData = iter.next();
				String sqlColumnName = metaData.getColumnName();
				sqlColumnNames.append(sqlColumnName);
				sqlColumnNames.append(", ");
				String columnDisplayName = metaData.getDisplayName();
				definedColumnsList.add(columnDisplayName);
			}
			String columnsInSql = sqlColumnNames.toString();
			columnsInSql = columnsInSql.substring(0, columnsInSql.lastIndexOf(","));
			selectSql =  "select distinct " + columnsInSql;
			spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, definedColumnsList);
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
	 * @param recordsPerPage 
	 * @return Map<String, List<String>> Map for columns and data list
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	private Map<String, List<String>> updateSpreadsheet(String treeNo,OutputTreeDataNode node,List<OutputTreeDataNode> rootOutputTreeNodeList,
			SessionDataBean sessionData,String parentData, int recordsPerPage) throws ClassNotFoundException, DAOException
			{
		OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(rootOutputTreeNodeList,treeNo);
		String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(node);
		return createSpreadsheetDataMap(root,node, parentIdColumnName, parentData, sessionData,recordsPerPage);				
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
	 * @param recordsPerPage 
	 * @return Map<String, List<String>> Map for columns and data list
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	private Map createSpreadsheetDataMap(OutputTreeDataNode root,OutputTreeDataNode node,String parentIdColumnName,
			String parentData,SessionDataBean sessionData, int recordsPerPage) throws ClassNotFoundException, DAOException
			{
		Map spreadSheetDataMap = new HashMap();
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, createColumnHeadersList());
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		List<List<String>> spreadsheetDataList = new ArrayList<List<String>>();
		List<OutputTreeDataNode> children = node.getChildren();
		for (OutputTreeDataNode childNode : children)
		{
			String selectSql = getSql(parentIdColumnName, parentData, tableName, childNode);
			int startIndex = 0;
			QuerySessionData querySessionData = getQuerySessionData(sessionData, recordsPerPage,startIndex, spreadSheetDataMap, selectSql);
		
			List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
			int size = dataList.size();
			String sizeStr = new Integer(size).toString();
			String name = childNode.getOutputEntity().getDynamicExtensionsEntity().getName();
			name = Utility.parseClassName(name);
			name = Utility.getDisplayLabel(name);
			List<String> data = new ArrayList<String>();
			data.add(name);
			data.add(sizeStr);
			spreadsheetDataList.add(data);
			spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
			querySessionData.setTotalNumberOfRecords(spreadsheetDataList.size());
			spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
		}
		return spreadSheetDataMap;
			}
	/**
	 * @param sessionData
	 * @param recordsPerPage
	 * @param startIndex 
	 * @param spreadSheetDataMap
	 * @param selectSql
	 * @return
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	public QuerySessionData getQuerySessionData(SessionDataBean sessionData, int recordsPerPage, int startIndex, Map spreadSheetDataMap, String selectSql) throws ClassNotFoundException, DAOException
	{
		QuerySessionData querySessionData = new QuerySessionData();
		querySessionData.setSql(selectSql);
		querySessionData.setQueryResultObjectDataMap(null);
		querySessionData.setSecureExecute(false);
		querySessionData.setHasConditionOnIdentifiedField(false);
		querySessionData.setRecordsPerPage(recordsPerPage);
		QueryBizLogic qBizLogic = new QueryBizLogic(); 
		PagenatedResultData pagenatedResultData = qBizLogic.execute( sessionData, querySessionData, startIndex);
		spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, pagenatedResultData.getResult());
		/**
		 * Name: Prafull
		 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
		 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
		 * 
		 *  saving required query data in Session so that can be used later on while navigating through result pages using pagination.  
		 */
		querySessionData.setTotalNumberOfRecords(pagenatedResultData.getTotalRecords());
		return querySessionData;
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
		String idColumnOfCurrentNode = "";
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
		
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			attributeList.add(attribute);
			String className = attribute.getEntity().getName();
			className = Utility.parseClassName(className);
			String sqlColumnName = attributeMetaData.getColumnName();
			if(attribute.getName().equalsIgnoreCase(Constants.ID))
			{
				idColumnOfCurrentNode = sqlColumnName;
			}
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			String attrLabel = Utility.getDisplayLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
		}
		if(selectedColumnMetaData == null)
		{
			spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
			selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
		}
		else 
		{
			selectSql = getSQLForSelectedColumns(spreadSheetDataMap);
		}
		spreadSheetDataMap.put(Constants.ATTRIBUTES, attributeList);
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
	
	/**
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param actualParentNodeId string id of parent
	 * @return boolean true if node is a leafNode else false.
	 */
	public boolean isLeafNode(Map<String, OutputTreeDataNode> idNodesMap,String actualParentNodeId)
	{
		String[] nodeIds;
		nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		String treeNo = nodeIds[0];
		String parentId = nodeIds[1];
		String uniqueNodeId = treeNo+"_"+parentId;
		OutputTreeDataNode parentNode = idNodesMap.get(uniqueNodeId);
		if (parentNode.getChildren().isEmpty())
		{
			return true;
		}
		return false;
	}
}
