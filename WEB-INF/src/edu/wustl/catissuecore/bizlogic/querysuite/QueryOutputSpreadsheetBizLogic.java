
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.OutputAttribute;
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
	 * @param queryResultObjectDataMap 
	 * @return Map for data
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForLabelNode(Map<String, OutputTreeDataNode> idNodesMap,
			List<OutputTreeDataNode> rootOutputTreeNodeList, 
			Map<Long, Map<AttributeInterface, String>> columnMap, 
			SessionDataBean sessionData, String idOfClickedNode, int recordsPerPage,SelectedColumnsMetadata selectedColumnMetaData,String randomNumber,boolean hasConditionOnIdentifiedField, Map<Long, QueryResultObjectDataBean> queryResultObjectDataMap)
	throws DAOException, ClassNotFoundException
	{
		this.selectedColumnMetaData = selectedColumnMetaData;
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		Map spreadSheetDataMap = new HashMap();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId()+randomNumber;
		String[] nodeIds = idOfClickedNode.split(Constants.NODE_SEPARATOR);
		String parentNode = nodeIds[0];//data
		String[] spiltParentNodeId = parentNode.split(Constants.UNDERSCORE);
		String treeNo = spiltParentNodeId[0];
		String parentNodeId = spiltParentNodeId[1];
		Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap;
		if(parentNode.contains("NULL"))
		{
			OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(rootOutputTreeNodeList,treeNo);
			
			    QueryResultObjectDataBean queryResulObjectDataBean = QueryModuleUtil.getQueryResulObjectDataBean(root);
				queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
				queryResultObjectDataBeanMap.put(root.getId(), queryResulObjectDataBean);
				if(!selectedColumnMetaData.isDefinedView())
				    spreadSheetDataMap = createSpreadsheetData(treeNo,root, sessionData,null,recordsPerPage,this.selectedColumnMetaData,randomNumber,idNodesMap,queryResultObjectDataBeanMap,hasConditionOnIdentifiedField);
				else
				{
					if(queryResultObjectDataMap==null)
						queryResultObjectDataMap = new HashMap<Long, QueryResultObjectDataBean>();
					 spreadSheetDataMap = createSpreadsheetData(treeNo,root, sessionData,null,recordsPerPage,this.selectedColumnMetaData,randomNumber,idNodesMap,queryResultObjectDataMap,hasConditionOnIdentifiedField);
				}
			this.selectedColumnMetaData.setCurrentSelectedObject(root);
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
			
			QueryResultObjectDataBean queryResulObjectDataBean = QueryModuleUtil.getQueryResulObjectDataBean(currentTreeNode);
			queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
			queryResultObjectDataBeanMap.put(currentTreeNode.getId(), queryResulObjectDataBean);
			
			if(!selectedColumnMetaData.isDefinedView())
				 defineGridViewBizLogic.getColumnsMetadataForSelectedNode(currentTreeNode,this.selectedColumnMetaData);
			List resultList = createSQL(spreadSheetDataMap, currentTreeNode,parentIdColumnName,parentData,tableName,queryResultObjectDataBeanMap);
			
			String selectSql = (String)resultList.get(0);
			queryResultObjectDataBeanMap = (Map<Long, QueryResultObjectDataBean>)resultList.get(1);
			int startIndex = 0;
			QuerySessionData querySessionData;
			if(!selectedColumnMetaData.isDefinedView())
			   querySessionData = getQuerySessionData(sessionData, recordsPerPage,startIndex, spreadSheetDataMap, selectSql,queryResultObjectDataBeanMap,hasConditionOnIdentifiedField);
			else
				querySessionData = getQuerySessionData(sessionData, recordsPerPage,startIndex, spreadSheetDataMap, selectSql,queryResultObjectDataMap,hasConditionOnIdentifiedField);
			spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
			this.selectedColumnMetaData.setCurrentSelectedObject(currentTreeNode);
		}
		spreadSheetDataMap.put(Constants.SELECTED_COLUMN_META_DATA,this.selectedColumnMetaData);
		spreadSheetDataMap.put(Constants.QUERY_REASUL_OBJECT_DATA_MAP, queryResultObjectDataBeanMap);
		return spreadSheetDataMap;
	}
	/**
	 * Processes spreadsheet data for data node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @param recordsPerPage 
	 * @param queryResultObjectDataMap 
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForDataNode(Map<String, OutputTreeDataNode> idNodesMap,
			List<OutputTreeDataNode> rootOutputTreeNodeList, SessionDataBean sessionData, String actualParentNodeId,
			int recordsPerPage,SelectedColumnsMetadata selectedColumnMetaData,String randomNumber,boolean hasConditionOnIdentifiedField, Map<Long, QueryResultObjectDataBean> queryResultObjectDataMap)
	throws DAOException, ClassNotFoundException
	{
		this.selectedColumnMetaData = selectedColumnMetaData;
		Map spreadSheetDatamap = null;
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		String[] nodeIds;
		nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		String treeNo = nodeIds[0];
		String parentId = nodeIds[1];
		String parentData =nodeIds[2];
		String uniqueNodeId = treeNo+"_"+parentId;
		OutputTreeDataNode parentNode = idNodesMap.get(uniqueNodeId);
		if(! selectedColumnMetaData.isDefinedView())
			 defineGridViewBizLogic.getColumnsMetadataForSelectedNode(parentNode,this.selectedColumnMetaData);
		
		QueryResultObjectDataBean queryResulObjectDataBean = QueryModuleUtil.getQueryResulObjectDataBean(parentNode);
		Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
		queryResultObjectDataBeanMap.put(parentNode.getId(), queryResulObjectDataBean);
		
		if(!selectedColumnMetaData.isDefinedView())
		  spreadSheetDatamap = createSpreadsheetData(treeNo,parentNode,  sessionData,parentData,recordsPerPage,this.selectedColumnMetaData,randomNumber,idNodesMap,queryResultObjectDataBeanMap,hasConditionOnIdentifiedField);
		else
		{
			if(queryResultObjectDataMap==null)
				queryResultObjectDataMap = new HashMap<Long, QueryResultObjectDataBean>();
			spreadSheetDatamap = createSpreadsheetData(treeNo,parentNode,  sessionData,parentData,recordsPerPage,this.selectedColumnMetaData,randomNumber,idNodesMap,queryResultObjectDataMap,hasConditionOnIdentifiedField);
		}
		/*if (parentNode.getChildren().isEmpty())
		{
			spreadSheetDatamap = createSpreadsheetData(treeNo,parentNode,  sessionData,parentData,recordsPerPage);
		}
		else
		{
			spreadSheetDatamap = updateSpreadsheet(treeNo,parentNode, rootOutputTreeNodeList, sessionData,parentData,recordsPerPage);
		}*/
		this.selectedColumnMetaData.setCurrentSelectedObject(parentNode);
		spreadSheetDatamap.put(Constants.SELECTED_COLUMN_META_DATA,this.selectedColumnMetaData);
		spreadSheetDatamap.put(Constants.QUERY_REASUL_OBJECT_DATA_MAP, queryResultObjectDataBeanMap);
		return spreadSheetDatamap;
	}

	/**
	 * This loads spreadsheet data when first level (default) tree is shown.This method is also called when data is to be 
	 * loaded when user clicks on a leaf node of tree.
	 * @param treeNo tree number user has clicked
	 * @param node clicked by user
	 * @param selectedColumnsMetadata 
	 * @param outputTreeMap  map which stores all details of tree
	 * @param parentNodeData the string id of the parent 
	 * @return map having data for column headers and data records.
	 * @throws DAOException  DAOException 
	 */
	public Map<String, List<String>> createSpreadsheetData(String treeNo,OutputTreeDataNode node,
			 SessionDataBean sessionData, String parentData, int recordsPerPage,
			SelectedColumnsMetadata selectedColumnsMetadata,String randomNumber,
			Map<String, OutputTreeDataNode> idNodesMap,
			Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap,boolean hasConditionOnIdentifiedField)
			throws DAOException, ClassNotFoundException
	{
		this.selectedColumnMetaData = selectedColumnsMetadata;
		Map spreadSheetDataMap = updateSpreadsheetData(sessionData, parentData, node,
				recordsPerPage,randomNumber, idNodesMap, queryResultObjectDataBeanMap,hasConditionOnIdentifiedField);
		spreadSheetDataMap.put(Constants.SELECTED_COLUMN_META_DATA, this.selectedColumnMetaData);
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
	private Map updateSpreadsheetData(SessionDataBean sessionData, String parentData, OutputTreeDataNode node, int recordsPerPage,String randomNumber,Map<String, OutputTreeDataNode> idNodesMap,Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap,boolean hasConditionOnIdentifiedField) throws ClassNotFoundException, DAOException
	{
		Map spreadSheetDataMap = new HashMap();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId()+randomNumber;
		String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(node);
		String selectSql = createSQL(parentData, tableName, spreadSheetDataMap, parentIdColumnName, node,queryResultObjectDataBeanMap);
		int startIndex = 0;
		QuerySessionData querySessionData = getQuerySessionData(sessionData, recordsPerPage,startIndex, spreadSheetDataMap, selectSql,queryResultObjectDataBeanMap,hasConditionOnIdentifiedField);
		spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
		spreadSheetDataMap.put(Constants.QUERY_REASUL_OBJECT_DATA_MAP, queryResultObjectDataBeanMap);
		
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
	private String createSQL(String parentData, String tableName, Map spreadSheetDataMap, String parentIdColumnName, OutputTreeDataNode node,Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap)
	{    
		String selectSql = Constants.SELECT_DISTINCT;
		String idColumnOfCurrentNode = "";
		List<String> columnsList = new ArrayList<String>();
		List<IOutputAttribute> selectedOutputAttributeList = new ArrayList<IOutputAttribute>();
		QueryResultObjectDataBean queryResultObjectDataBean = new QueryResultObjectDataBean();
		if(!selectedColumnMetaData.isDefinedView())
		   queryResultObjectDataBean = queryResultObjectDataBeanMap.get(node.getId());
		Vector<Integer> identifiedDataColumnIds = new Vector<Integer>();
		Vector<Integer> objectDataColumnIds = new Vector<Integer>();
		int columnIndex = 0;
		
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String sqlColumnName = attributeMetaData.getColumnName();
			
			String className = attribute.getEntity().getName();
			className = Utility.parseClassName(className);
			if( !selectedColumnMetaData.isDefinedView() && edu.wustl.common.querysuite.security.utility.Utility.isIdentified(attribute))
			{
				identifiedDataColumnIds.add(columnIndex);
				queryResultObjectDataBean.setIdentifiedDataColumnIds(identifiedDataColumnIds);
			}
			if(attribute.getName().equals(Constants.ID))
			{
				idColumnOfCurrentNode = sqlColumnName;
				if(!selectedColumnMetaData.isDefinedView())
				{
				if(queryResultObjectDataBean.isMainEntity())
				   queryResultObjectDataBean.setMainEntityIdentifierColumnId(columnIndex);
				else
				   queryResultObjectDataBean.setEntityId(columnIndex);
				}
			}
			selectSql = selectSql + sqlColumnName + ",";
			
			String attrLabel = Utility.getDisplayLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
			IOutputAttribute attr = new OutputAttribute(node.getExpressionId(),attribute);
			selectedOutputAttributeList.add(attr);
			objectDataColumnIds.add(columnIndex);
			columnIndex++;
		}  
		if(!selectedColumnMetaData.isDefinedView())
		{
			selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
			spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
			selectedColumnMetaData.setSelectedAttributeMetaDataList(attributes);
			selectedColumnMetaData.setSelectedOutputAttributeList(selectedOutputAttributeList);
		}
		else
		{ 
			selectSql = getSQLForSelectedColumns(spreadSheetDataMap,queryResultObjectDataBeanMap);
		} 
		if(parentData != null && parentData.equals(Constants.HASHED_NODE_ID) && false)
		{
			addHashedRow(spreadSheetDataMap);
			return "";
		}
		if(!selectedColumnMetaData.isDefinedView() && queryResultObjectDataBean.getMainEntityIdentifierColumnId()==-1)
		{ 
			Map<EntityInterface, Integer> entityIdIndexMap =new HashMap<EntityInterface, Integer>();
		    selectSql = QueryModuleUtil.updateEntityIdIndexMap(queryResultObjectDataBean,columnIndex,selectSql,null,entityIdIndexMap);
		    entityIdIndexMap = queryResultObjectDataBean.getEntityIdIndexMap();
		    if(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity())!=null)
			{
				queryResultObjectDataBean.setMainEntityIdentifierColumnId(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity()));
			} 
		}
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
		if(!selectedColumnMetaData.isDefinedView()){
		if(identifiedDataColumnIds.size()!=0)
			queryResultObjectDataBean.setHasAssociatedIdentifiedData(true);
		queryResultObjectDataBean.setObjectColumnIds(objectDataColumnIds);
		}
		return selectSql;
	}
	/**
	 * @param spreadSheetDataMap
	 */
	private void addHashedRow(Map spreadSheetDataMap)
	{
		List<String> columnsList = (List<String>)spreadSheetDataMap.get(Constants.SPREADSHEET_COLUMN_LIST);
		List<List<String>> dataList = new ArrayList<List<String>>();
		List<String> row = new ArrayList<String>();
		for(int columnIndex=0;columnIndex<columnsList.size();columnIndex++)
		{
			row.add(Constants.HASHED_OUT);
		}
		dataList.add(row);
		spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, dataList);
	}
	/**
	 * @param spreadSheetDataMap map to store data list
	 * @param queryResultObjectDataBeanMap 
	 * @return String sql
	 */
	private String getSQLForSelectedColumns(Map spreadSheetDataMap, Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap)
	{     
		String selectSql = "";
		List<String> definedColumnsList = new ArrayList<String>();
		//Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
		StringBuffer sqlColumnNames = new StringBuffer();
		List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnMetaData.getSelectedAttributeMetaDataList();
		List<IOutputAttribute> selectedOutputAttributeList = selectedColumnMetaData.getSelectedOutputAttributeList();
		Vector<Integer> objectIdsList = new Vector<Integer>();
		boolean isUniqueNode = false;
		int columnIndex = 0;
		 List<OutputTreeDataNode> defineViewNodeList = new ArrayList<OutputTreeDataNode>();
      	for(IOutputAttribute at :selectedOutputAttributeList)
		{
			for(QueryOutputTreeAttributeMetadata metaData : selectedAttributeMetaDataList) 
			{
				AttributeInterface attribute = at.getAttribute();
				if(metaData.getAttribute().equals(attribute)) 
				{
					QueryResultObjectDataBean queryResultObjectDataBean = new QueryResultObjectDataBean();
					String sqlColumnName = metaData.getColumnName();
					sqlColumnNames.append(sqlColumnName);
					sqlColumnNames.append(", ");
					String columnDisplayName = metaData.getDisplayName();
					definedColumnsList.add(columnDisplayName);
					if(!defineViewNodeList.contains(metaData.getTreeDataNode()))
					{
//						if(queryResultObjectDataBeanMap.get(metaData.getTreeDataNode().getId())!=null)
//							queryResultObjectDataBean = queryResultObjectDataBeanMap.get(metaData.getTreeDataNode().getId());
//						else
						   queryResultObjectDataBean = QueryModuleUtil.getQueryResulObjectDataBean(metaData.getTreeDataNode());
					    defineViewNodeList.add(metaData.getTreeDataNode());
					    //queryResultObjectDataBeanMap.put(metaData.getTreeDataNode().getId(), queryResultObjectDataBean);
					    isUniqueNode = true;
					}
					else
						isUniqueNode = false;
					if(attribute.getName().equalsIgnoreCase(Constants.ID))
					{
						if(queryResultObjectDataBean.isMainEntity())
							   queryResultObjectDataBean.setMainEntityIdentifierColumnId(columnIndex);
							else
							   queryResultObjectDataBean.setEntityId(columnIndex);
					}
					if(edu.wustl.common.querysuite.security.utility.Utility.isIdentified(attribute))
					{
						queryResultObjectDataBean.getIdentifiedDataColumnIds().add(columnIndex);
					}
					queryResultObjectDataBean.getObjectColumnIds().add(columnIndex);
					//if(isUniqueNode)
						//queryResultObjectDataBeanMap.put(metaData.getTreeDataNode().getId(), queryResultObjectDataBean);
					columnIndex++;
					break;
				}
			}
		}
		int lastindexOfComma =  sqlColumnNames.lastIndexOf(",");
		if(lastindexOfComma != -1)
		{
		String columnsInSql = sqlColumnNames.substring(0, lastindexOfComma).toString();
		Map<EntityInterface, Integer> entityIdIndexMap =new HashMap<EntityInterface, Integer>();
		columnsInSql = QueryModuleUtil.updateEntityIdIndexMap(null, columnIndex, columnsInSql,defineViewNodeList,entityIdIndexMap);
		Iterator<QueryResultObjectDataBean> iterator = queryResultObjectDataBeanMap.values().iterator();
		/*while (iterator.hasNext())
		{
			QueryResultObjectDataBean elem = (QueryResultObjectDataBean) iterator.next();
			if(elem.getMainEntityIdentifierColumnId()==-1)
			{
				if(!elem.isMainEntity())
					elem.setMainEntityIdentifierColumnId(entityIdIndexMap.get(elem.getMainEntity()));
				else
					elem.setMainEntityIdentifierColumnId(entityIdIndexMap.get(elem.getEntity()));
			}
			
		}*/
		selectSql =  Constants.SELECT_DISTINCT + columnsInSql;
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, definedColumnsList);
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
		String selectSql = Constants.SELECT_DISTINCT;
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
			QuerySessionData querySessionData = getQuerySessionData(sessionData, recordsPerPage,startIndex, spreadSheetDataMap, selectSql,null,false);
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
	public QuerySessionData getQuerySessionData(SessionDataBean sessionData, int recordsPerPage, int startIndex, Map spreadSheetDataMap, String selectSql,Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap,boolean hasConditionOnIdentifiedField) throws ClassNotFoundException, DAOException
	{         
		QuerySessionData querySessionData = new QuerySessionData();
		querySessionData.setSql(selectSql);
		querySessionData.setQueryResultObjectDataMap(queryResultObjectDataBeanMap);
		querySessionData.setSecureExecute(true);
		querySessionData.setHasConditionOnIdentifiedField(hasConditionOnIdentifiedField);
		querySessionData.setRecordsPerPage(recordsPerPage);
		QueryBizLogic qBizLogic = new QueryBizLogic(); 
		if(selectSql.equals(""))
		{
			addHashedRow(spreadSheetDataMap);
			querySessionData.setTotalNumberOfRecords(1);
		}
		else
		{
		PagenatedResultData pagenatedResultData = qBizLogic.execute(sessionData, querySessionData, startIndex);
		spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, pagenatedResultData.getResult());
		/**
		 * Name: Prafull
		 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
		 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
		 * 
		 *  saving required query data in Session so that can be used later on while navigating through result pages using pagination.  
		 */
		querySessionData.setTotalNumberOfRecords(pagenatedResultData.getTotalRecords());
		}
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
	private List createSQL(Map spreadSheetDataMap, OutputTreeDataNode node, String parentIdColumnName, String parentData, String tableName,Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap)
	{ 
		String selectSql = Constants.SELECT_DISTINCT;
		List<String> columnsList = new ArrayList<String>();
		String idColumnOfCurrentNode = "";
		QueryResultObjectDataBean queryResultObjectDataBean = queryResultObjectDataBeanMap.get(node.getId());
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		Vector<Integer> identifiedDataColumnIds = new Vector<Integer>();
		Vector<Integer> objectColumnIds = new Vector<Integer>();
		List resultList = new ArrayList();
		int columnIndex =0;
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String className = attribute.getEntity().getName();
			className = Utility.parseClassName(className);
			String sqlColumnName = attributeMetaData.getColumnName();
			
			if(attribute.getName().equalsIgnoreCase(Constants.ID))
			{
				idColumnOfCurrentNode = sqlColumnName;
				if(queryResultObjectDataBean.isMainEntity())
					   queryResultObjectDataBean.setMainEntityIdentifierColumnId(columnIndex);
					else
					   queryResultObjectDataBean.setEntityId(columnIndex);
			}
			if(edu.wustl.common.querysuite.security.utility.Utility.isIdentified(attribute))
			{
				identifiedDataColumnIds.add(columnIndex);
				queryResultObjectDataBean.setIdentifiedDataColumnIds(identifiedDataColumnIds);
			}
			objectColumnIds.add(columnIndex);

			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			String attrLabel = Utility.getDisplayLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
			columnIndex++;
		}
		if(! selectedColumnMetaData.isDefinedView())
		{
			spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
			selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
			selectedColumnMetaData.setSelectedAttributeMetaDataList(attributes);
		}
		else 
		{
			selectSql = getSQLForSelectedColumns(spreadSheetDataMap,queryResultObjectDataBeanMap);
		}
		if(!selectedColumnMetaData.isDefinedView() && queryResultObjectDataBean.getMainEntityIdentifierColumnId() == -1)
		{
			Map<EntityInterface, Integer> entityIdIndexMap =new HashMap<EntityInterface, Integer>();
		    selectSql = QueryModuleUtil.updateEntityIdIndexMap(queryResultObjectDataBean, columnIndex, selectSql,null,entityIdIndexMap);
		    entityIdIndexMap = queryResultObjectDataBean.getEntityIdIndexMap();
			 if(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity())!=null)
				  queryResultObjectDataBean.setMainEntityIdentifierColumnId(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity()));
			 else
				 queryResultObjectDataBean.setMainEntityIdentifierColumnId(-1);
		}
		selectSql = selectSql + " from " + tableName;
		if (parentData != null)
		{
			selectSql = selectSql + " where (" + parentIdColumnName + " = '" + parentData + "' "+LogicalOperator.And + " " + idColumnOfCurrentNode+" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull)+")";
		}else
		{
			selectSql = selectSql + " where "+idColumnOfCurrentNode+" "+RelationalOperator.getSQL(RelationalOperator.IsNotNull);	
		}
		//return selectSql;
		if(identifiedDataColumnIds.size()!=0)
			queryResultObjectDataBean.setHasAssociatedIdentifiedData(true);
		queryResultObjectDataBean.setObjectColumnIds(objectColumnIds);
		 
		resultList.add(selectSql);
		resultList.add(queryResultObjectDataBeanMap);
		return resultList;
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
