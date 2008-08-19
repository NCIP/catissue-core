
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
import edu.wustl.catissuecore.util.querysuite.QueryCSMUtil;
import edu.wustl.catissuecore.util.querysuite.QueryDetails;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.catissuecore.util.querysuite.TemporalColumnUIBean;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.OutputAttribute;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.querysuite.utils.QueryUtility;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

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
	 * @param selectedColumnMetaData metadata for selected columns 
	 * @param queryResultObjectDataMap 
	 * @param mainEntityMap 
	 * @param constraints 
	 * @return Map for data
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForLabelNode(QueryDetails queryDetailsObj,			
			Map<Long, Map<AttributeInterface, String>> columnMap, String idOfClickedNode,
			int recordsPerPage, SelectedColumnsMetadata selectedColumnMetaData,
			boolean hasConditionOnIdentifiedField, Map<Long, 
			QueryResultObjectDataBean> queryResultObjectDataMap,IConstraints constraints,
			Map<String, IOutputTerm> outputTermsColumns)
	throws DAOException, ClassNotFoundException
	{ 
		this.selectedColumnMetaData = selectedColumnMetaData;
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		Map spreadSheetDataMap = new HashMap();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + queryDetailsObj
		.getSessionData().getUserId()+ queryDetailsObj.getRandomNumber();
		String[] nodeIds = idOfClickedNode.split(Constants.NODE_SEPARATOR);
		String parentNode = nodeIds[0];//data
		String[] spiltParentNodeId = parentNode.split(Constants.UNDERSCORE);
		String treeNo = spiltParentNodeId[0];
		String parentNodeId = spiltParentNodeId[1];
		Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap;
		if(parentNode.contains("NULL"))
		{
			OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(queryDetailsObj,treeNo);
			QueryResultObjectDataBean queryResulObjectDataBean = QueryCSMUtil
			.getQueryResulObjectDataBean(root, queryDetailsObj);
			queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
			queryResultObjectDataBeanMap.put(root.getId(), queryResulObjectDataBean);
			if(!selectedColumnMetaData.isDefinedView())
			{
				spreadSheetDataMap = createSpreadsheetData(treeNo, root, queryDetailsObj,
					null, recordsPerPage, this.selectedColumnMetaData,
					queryResultObjectDataBeanMap, hasConditionOnIdentifiedField, 
					constraints, outputTermsColumns);
				spreadSheetDataMap.put(Constants.QUERY_REASUL_OBJECT_DATA_MAP,
						queryResultObjectDataBeanMap);
			}
			else
			{
				if (queryResultObjectDataMap == null) {
					queryResultObjectDataMap = new HashMap<Long, QueryResultObjectDataBean>();
				}
				spreadSheetDataMap = createSpreadsheetData(treeNo, root, queryDetailsObj,
						null, recordsPerPage, this.selectedColumnMetaData,
						queryResultObjectDataMap, hasConditionOnIdentifiedField,constraints,outputTermsColumns);
				spreadSheetDataMap.put(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP,
						queryResultObjectDataMap);
			}
			this.selectedColumnMetaData.setCurrentSelectedObject(root);
		} 
		else
		{ 
			String parentData = spiltParentNodeId[2];
			String uniqueParentNodeId = treeNo+"_"+parentNodeId;
			OutputTreeDataNode parentTreeNode = queryDetailsObj.getUniqueIdNodesMap().get(uniqueParentNodeId);

			String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(parentTreeNode);
			String currentNode = nodeIds[1];//label
			String[] spiltCurrentNodeId = currentNode.split(Constants.UNDERSCORE);
			String currentNodeId = spiltCurrentNodeId[1];
			String uniqueCurrentNodeId = treeNo+"_"+currentNodeId;
			OutputTreeDataNode currentTreeNode = queryDetailsObj.getUniqueIdNodesMap().get(uniqueCurrentNodeId);

			QueryResultObjectDataBean queryResulObjectDataBean = QueryCSMUtil
			.getQueryResulObjectDataBean(currentTreeNode, queryDetailsObj);
			queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
			queryResultObjectDataBeanMap.put(currentTreeNode.getId(), queryResulObjectDataBean);

			if(!selectedColumnMetaData.isDefinedView())
			{
				defineGridViewBizLogic.getColumnsMetadataForSelectedNode(currentTreeNode, 
						this.selectedColumnMetaData, constraints);
			}
			List resultList = createSQL(spreadSheetDataMap, currentTreeNode,
					parentIdColumnName, parentData, tableName,
					queryResultObjectDataBeanMap, queryDetailsObj,outputTermsColumns);

			String selectSql = (String)resultList.get(0);
			queryResultObjectDataBeanMap = (Map<Long, QueryResultObjectDataBean>)resultList.get(1);
			int startIndex = 0;
			QuerySessionData querySessionData;
			querySessionData = getQuerySessionData(queryDetailsObj, recordsPerPage,
					startIndex, spreadSheetDataMap, selectSql, queryResultObjectDataBeanMap,
					hasConditionOnIdentifiedField);
			spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
			this.selectedColumnMetaData.setCurrentSelectedObject(currentTreeNode);
			if(!selectedColumnMetaData.isDefinedView())
			{
				spreadSheetDataMap.put(Constants.QUERY_REASUL_OBJECT_DATA_MAP, 
						queryResultObjectDataBeanMap);
			}
			else
			{
				spreadSheetDataMap.put(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP,
						queryResultObjectDataBeanMap);
			}
		}
		spreadSheetDataMap.put(Constants.SELECTED_COLUMN_META_DATA, this.selectedColumnMetaData);
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
	 * @param mainEntityMap 
	 * @param constraints 
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Map processSpreadsheetForDataNode(QueryDetails queryDetailsObj,			
			String actualParentNodeId, int recordsPerPage,
			SelectedColumnsMetadata selectedColumnMetaData, 
			boolean hasConditionOnIdentifiedField,
			Map<Long, QueryResultObjectDataBean> queryResultObjectDataMap,
			IConstraints constraints, Map<String, IOutputTerm> outputTermsColumns)
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
		OutputTreeDataNode parentNode = queryDetailsObj.getUniqueIdNodesMap().get(uniqueNodeId);
		if(! selectedColumnMetaData.isDefinedView())
		{
			 defineGridViewBizLogic.getColumnsMetadataForSelectedNode(parentNode,
					 this.selectedColumnMetaData, constraints);
		}
		
		QueryResultObjectDataBean queryResulObjectDataBean = QueryCSMUtil
			.getQueryResulObjectDataBean(parentNode, queryDetailsObj);
		Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap = new HashMap<Long,
			QueryResultObjectDataBean>();
		queryResultObjectDataBeanMap.put(parentNode.getId(), queryResulObjectDataBean);
		
		if(!selectedColumnMetaData.isDefinedView())
		{
		  spreadSheetDatamap = createSpreadsheetData(treeNo, parentNode, queryDetailsObj, 
				  parentData, recordsPerPage, this.selectedColumnMetaData, 
				  queryResultObjectDataBeanMap, hasConditionOnIdentifiedField,
				  constraints, outputTermsColumns);
		  spreadSheetDatamap.put(Constants.QUERY_REASUL_OBJECT_DATA_MAP, queryResultObjectDataBeanMap);
		}
		else
		{
			if(queryResultObjectDataMap==null)
				queryResultObjectDataMap = new HashMap<Long, QueryResultObjectDataBean>();
			spreadSheetDatamap = createSpreadsheetData(treeNo, parentNode, queryDetailsObj, 
					parentData, recordsPerPage, this.selectedColumnMetaData,  
					queryResultObjectDataMap, hasConditionOnIdentifiedField,
					constraints, outputTermsColumns);
			spreadSheetDatamap.put(Constants.DEFINE_VIEW_QUERY_REASULT_OBJECT_DATA_MAP,
					queryResultObjectDataMap);
		}
		this.selectedColumnMetaData.setCurrentSelectedObject(parentNode);
		spreadSheetDatamap.put(Constants.SELECTED_COLUMN_META_DATA,this.selectedColumnMetaData);
		return spreadSheetDatamap;
	}

	/**
	 * This loads spreadsheet data when first level (default) tree is shown.This method is also called when data is to be 
	 * loaded when user clicks on a leaf node of tree.
	 * @param treeNo tree number user has clicked
	 * @param node clicked by user
	 * @param selectedColumnsMetadata 
	 * @param mainEntityMap 
	 * @param constraints 
	 * @param outputTreeMap  map which stores all details of tree
	 * @param parentNodeData the string id of the parent 
	 * @return map having data for column headers and data records.
	 * @throws DAOException  DAOException 
	 */
	public Map<String, List<String>> createSpreadsheetData(String treeNo, OutputTreeDataNode node,
			QueryDetails queryDetailsObj, String parentData, int recordsPerPage,
			SelectedColumnsMetadata selectedColumnsMetadata, Map<Long, QueryResultObjectDataBean> 
			queryResultObjectDataBeanMap, boolean hasConditionOnIdentifiedField,
			IConstraints constraints, Map<String, IOutputTerm> outputTermsColumns)
			throws DAOException, ClassNotFoundException
	{
		this.selectedColumnMetaData = selectedColumnsMetadata;
		Map spreadSheetDataMap = updateSpreadsheetData(queryDetailsObj, parentData, node,
				recordsPerPage, queryResultObjectDataBeanMap, 
				hasConditionOnIdentifiedField, constraints, outputTermsColumns);
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
	 * @param mainEntityMap 
	 * @param constraints 
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	private Map updateSpreadsheetData(QueryDetails queryDetailsObj, String parentData,
			OutputTreeDataNode node, int recordsPerPage,			
			Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap,
			boolean hasConditionOnIdentifiedField,IConstraints constraints, 
			Map<String, IOutputTerm> outputTermsColumns)
			throws ClassNotFoundException, DAOException
	{ 
		Map spreadSheetDataMap = new HashMap();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + queryDetailsObj.getSessionData()
		.getUserId() + queryDetailsObj.getRandomNumber();
		String parentIdColumnName = QueryModuleUtil.getParentIdColumnName(node);
		String selectSql = createSQL(parentData, tableName, spreadSheetDataMap,
				parentIdColumnName, node, queryResultObjectDataBeanMap,
				queryDetailsObj,constraints, outputTermsColumns);
		int startIndex = 0;
		QuerySessionData querySessionData = getQuerySessionData(queryDetailsObj,
				recordsPerPage, startIndex, spreadSheetDataMap, selectSql,
				queryResultObjectDataBeanMap, hasConditionOnIdentifiedField);
		spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
		spreadSheetDataMap.put(Constants.QUERY_REASUL_OBJECT_DATA_MAP,
				queryResultObjectDataBeanMap);

		return spreadSheetDataMap;
	}
	
	/**
	 * Creates sql string for given data.
	 * @param parentData
	 * @param tableName name of the table
	 * @param spreadSheetDataMap map to store data list
	 * @param parentIdColumnName String column name of id of parents node
	 * @param node OutputTreeDataNode
	 * @param idNodesMap 
	 * @param constraints 
	 * @return String sql
	 */
	private String createSQL(String parentData, String tableName,
			Map spreadSheetDataMap, String parentIdColumnName,
			OutputTreeDataNode node,
			Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap,
			QueryDetails queryDetailsObj, IConstraints constraints,Map<String, IOutputTerm> outputTermsColumns)
	{     
		String selectSql = "";
		String idColumnOfCurrentNode = "";
		List<String> columnsList = new ArrayList<String>();
		List<IOutputAttribute> selectedOutputAttributeList = new ArrayList<IOutputAttribute>();
		QueryResultObjectDataBean queryResultObjectDataBean = new QueryResultObjectDataBean();
		if(!selectedColumnMetaData.isDefinedView())
		   queryResultObjectDataBean = queryResultObjectDataBeanMap.get(node.getId());
		Vector<Integer> identifiedDataColumnIds = new Vector<Integer>();
		Vector<Integer> objectDataColumnIds = new Vector<Integer>();
		Map<Integer, QueryOutputTreeAttributeMetadata> fileTypeAtrributeIndexMetadataMap = new HashMap<Integer, 
						QueryOutputTreeAttributeMetadata>();
		int columnIndex = 0;
		int totalFileTypeAttributes = 0;
		
		List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
		for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
		{ 
			AttributeInterface attribute = attributeMetaData.getAttribute();
			String sqlColumnName = attributeMetaData.getColumnName();
			
			String className = attribute.getEntity().getName();
			className = Utility.parseClassName(className);
			if( !selectedColumnMetaData.isDefinedView() && (attribute.getIsIdentified()!=null && attribute
					.getIsIdentified()))
			{ 
				identifiedDataColumnIds.add(columnIndex);
				queryResultObjectDataBean.setIdentifiedDataColumnIds(identifiedDataColumnIds);
				queryResultObjectDataBean.setHasAssociatedIdentifiedData(true);
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
			if(!attribute.getAttributeTypeInformation().getDataType().equalsIgnoreCase("file"))

			{
				selectSql = selectSql + sqlColumnName + ",";
				String attrLabel = Utility.getDisplayLabel(attribute.getName());
				columnsList.add(attrLabel + " : " + className);
				IOutputAttribute attr = new OutputAttribute(constraints.getExpression(node.getExpressionId()), attribute);
				selectedOutputAttributeList.add(attr);
				objectDataColumnIds.add(columnIndex);
				columnIndex++;
			}
			else
			{
				int fileTypeIndex = columnIndex + fileTypeAtrributeIndexMetadataMap.size();
				queryResultObjectDataBean.setClobeType(true);
				fileTypeAtrributeIndexMetadataMap.put(fileTypeIndex, attributeMetaData);
			}
		}
		if (queryResultObjectDataBean.isClobeType()) 
		{
			totalFileTypeAttributes = fileTypeAtrributeIndexMetadataMap.size();
			queryResultObjectDataBean.setFileTypeAtrributeIndexMetadataMap(
					fileTypeAtrributeIndexMetadataMap);
		}
		if(!selectedColumnMetaData.isDefinedView())
		{
			selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
			spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
			selectedColumnMetaData.setSelectedAttributeMetaDataList(attributes);
		}
		else
		{
			queryResultObjectDataBeanMap.clear();
			selectSql = getSQLForSelectedColumns(spreadSheetDataMap, queryResultObjectDataBeanMap,
					queryDetailsObj,outputTermsColumns);
			columnsList = (List<String>)spreadSheetDataMap.get(Constants.SPREADSHEET_COLUMN_LIST);
		} 
		if(parentData != null && parentData.equals(Constants.HASHED_NODE_ID) && false)
		{
			addHashedRow(spreadSheetDataMap);
			return "";
		}
	 
		if(!selectedColumnMetaData.isDefinedView() )
		{ 
			TemporalColumnUIBean temporalColumnUIBean = new TemporalColumnUIBean(node, selectSql, columnsList, outputTermsColumns,columnIndex);
			modifySqlForTemporalColumns(temporalColumnUIBean); 
			selectSql = temporalColumnUIBean.getSql();
			columnIndex = temporalColumnUIBean.getColumnIndex();
			if(queryResultObjectDataBean.getMainEntityIdentifierColumnId()==-1)
			{
				Map<EntityInterface, Integer> entityIdIndexMap =new HashMap<EntityInterface, Integer>();
			    selectSql = QueryCSMUtil.updateEntityIdIndexMap(queryResultObjectDataBean, columnIndex,
			    		selectSql, null, entityIdIndexMap, queryDetailsObj);
			    entityIdIndexMap = queryResultObjectDataBean.getEntityIdIndexMap();
			    if(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity())!=null)
				{
					queryResultObjectDataBean.setMainEntityIdentifierColumnId(entityIdIndexMap
							.get(queryResultObjectDataBean.getMainEntity()));
				} 
		    }
		}
		selectSql = selectSql + " from " + tableName;
		if (parentData != null)
		{
			selectSql = selectSql + " where (" + parentIdColumnName + " = '" + parentData + "' "+ 
			LogicalOperator.And+ " "+idColumnOfCurrentNode+" "+ RelationalOperator
				.getSQL(RelationalOperator.IsNotNull) +")";
		}
		else
		{
			selectSql = selectSql + " where "+idColumnOfCurrentNode +" "+ RelationalOperator
				.getSQL(RelationalOperator.IsNotNull);	
		}
		if(!selectedColumnMetaData.isDefinedView())
		{
			if(identifiedDataColumnIds.size()!=0)
			{
				queryResultObjectDataBean.setHasAssociatedIdentifiedData(true);
			}
			queryResultObjectDataBean.setObjectColumnIds(objectDataColumnIds);
		}
		if(selectSql.indexOf(Constants.SELECT_DISTINCT) == -1)
			selectSql = Constants.SELECT_DISTINCT + selectSql;
		return selectSql;
	}
	/**
	 * 
	 * @param temporalColumnUIBean
	 */
	public void modifySqlForTemporalColumns(TemporalColumnUIBean temporalColumnUIBean) 
	{
		OutputTreeDataNode node = temporalColumnUIBean.getNode();
		Map<String, IOutputTerm> outputTermsColumns = temporalColumnUIBean.getOutputTermsColumns();
		Set<String> keySet = outputTermsColumns.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			String columnName = (String) iterator.next();
			IOutputTerm outputTerm = outputTermsColumns.get(columnName);
			String displayColumnName = outputTerm.getName();
			ITerm term = outputTerm.getTerm();
			Set<IExpression> expressionsInTerm = QueryUtility.getExpressionsInTerm(term);

			if(node == null || expressionsInTerm.size() == 1)
			{
				modifyTemporalColumnBean(temporalColumnUIBean,displayColumnName,columnName);
			}
			else if(isContainingExpressionInTQ(node, expressionsInTerm) && isValidAssociationForTQ(node, expressionsInTerm))
			{
				modifyTemporalColumnBean(temporalColumnUIBean,displayColumnName,columnName);
			}
		}
	}
	/**
	 * @param node
	 * @param expressionsInTerm
	 */
	private boolean isValidAssociationForTQ(OutputTreeDataNode node, Set<IExpression> expressionsInTerm) {
		org.apache.log4j.Logger logger =Logger.getLogger(CatissueCoreServletContextListener.class);
		Collection<AssociationInterface> associationCollection = 
			node.getOutputEntity().getDynamicExtensionsEntity().getAllAssociations();
		boolean isValidAssociationForTQ = false;
		for(AssociationInterface asso :associationCollection)
		{
			EntityInterface targetEntity = asso.getTargetEntity();
			for(IExpression exp : expressionsInTerm)
			{
				EntityInterface tqEntity = exp.getQueryEntity().getDynamicExtensionsEntity();
				if(tqEntity.equals(targetEntity))
				{
					ConstraintPropertiesInterface constraintProperties = asso.getConstraintProperties();
					if (constraintProperties.getSourceEntityKey() != null
							&& constraintProperties.getTargetEntityKey() != null)// Many to Many Case
					{
						logger.info("many to many association between "+tqEntity + 
								"and "+targetEntity +"So not showing custom column name in grid");
					} else  if (constraintProperties.getSourceEntityKey() != null)// Many Side
					{
						logger.info("many to one association between "+tqEntity + 
								"and "+targetEntity +"So showing custom column name in grid");
						isValidAssociationForTQ = true;
						break;
					} else 
					{
						logger.info("one to many association between "+tqEntity + 
								"and "+targetEntity +"So not showing custom column name in grid");
					}
				}
			}
		}
		return isValidAssociationForTQ;
	}
		/**
		 * 
		 * @param temporalColumnUIBean
		 * @param displayColumnName
		 * @param columnName
		 */
		private void modifyTemporalColumnBean(TemporalColumnUIBean temporalColumnUIBean, String displayColumnName, String columnName) {
			temporalColumnUIBean.getColumnsList().add(displayColumnName);
			String sql = temporalColumnUIBean.getSql() + ", " +columnName; 
			temporalColumnUIBean.setSql(sql);
			temporalColumnUIBean.setColumnIndex(temporalColumnUIBean.getColumnIndex()+1);

		}
		/**
		 * @param node
		 * @param expressionsInTerm
		 * @return
		 */
		private boolean isContainingExpressionInTQ(OutputTreeDataNode node, Set<IExpression> expressionsInTerm) {
			for(IExpression ex :expressionsInTerm)
			{
				if(node.getExpressionId() == ex.getExpressionId())
				{
					return true;
				}
			}
			return false;
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
			 * @param mainEntityMap 
			 * @param idNodesMap 
			 * @return String sql
			 */
			private String getSQLForSelectedColumns(Map spreadSheetDataMap,
					Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap,
					QueryDetails queryDetailsObj, Map<String, IOutputTerm> outputTermsColumns)
			{ 
				String selectSql = "";
				List<String> definedColumnsList = new ArrayList<String>();
				StringBuffer sqlColumnNames = new StringBuffer();
				List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnMetaData
				.getSelectedAttributeMetaDataList();
				int columnIndex = 0;
				int addedFileTypeAttributes = 0;
				List<EntityInterface> defineViewNodeList = new ArrayList<EntityInterface>();
				List<NameValueBean> selectedColumnNameValue = new ArrayList<NameValueBean>();

				for (QueryOutputTreeAttributeMetadata metaData : selectedAttributeMetaDataList)
				{
					AttributeInterface attribute = metaData.getAttribute();
					if (metaData.getAttribute().equals(attribute))
					{
						NameValueBean nameValueBean = new NameValueBean();
						QueryResultObjectDataBean queryResultObjectDataBean = queryResultObjectDataBeanMap
						.get(metaData.getTreeDataNode().getId());
						String attributeWithClassName = metaData.getDisplayName();
						String treeAttributeNodeId = metaData.getUniqueId();
						nameValueBean.setName(attributeWithClassName);
						nameValueBean.setValue(treeAttributeNodeId);
						selectedColumnNameValue.add(nameValueBean);

						if (queryResultObjectDataBean==null)
						{
							queryResultObjectDataBean = QueryCSMUtil
							.getQueryResulObjectDataBean(metaData
									.getTreeDataNode(), queryDetailsObj);
							defineViewNodeList.add(attribute.getEntity());
						}

						if (attribute.getName().equalsIgnoreCase(Constants.ID))
						{
							if (queryResultObjectDataBean.isMainEntity())
								queryResultObjectDataBean.setMainEntityIdentifierColumnId(columnIndex);
							else
								queryResultObjectDataBean.setEntityId(columnIndex);
						}
						if (attribute.getIsIdentified() != null && attribute.getIsIdentified())
						{
							queryResultObjectDataBean.getIdentifiedDataColumnIds().add(columnIndex);
							queryResultObjectDataBean.setHasAssociatedIdentifiedData(true);
						}
						if(attribute.getDataType().equalsIgnoreCase(Constants.FILE_TYPE))
						{
							queryResultObjectDataBean.setClobeType(true);
							Map beanMetadataMap = queryResultObjectDataBean
							.getFileTypeAtrributeIndexMetadataMap();
							int fileTypeIndex = columnIndex + addedFileTypeAttributes;
							addedFileTypeAttributes++;
							beanMetadataMap.put(fileTypeIndex, metaData);
							queryResultObjectDataBean.setFileTypeAtrributeIndexMetadataMap(beanMetadataMap);
						}
						else
						{
							String sqlColumnName = metaData.getColumnName();
							sqlColumnNames.append(sqlColumnName);
							sqlColumnNames.append(", ");
							String columnDisplayName = metaData.getDisplayName();
							definedColumnsList.add(columnDisplayName);
							queryResultObjectDataBean.getObjectColumnIds().add(columnIndex);
							columnIndex++;
						}
						queryResultObjectDataBeanMap.put(metaData.getTreeDataNode().getId(),
								queryResultObjectDataBean);
					}
				} 
				this.selectedColumnMetaData.setSelectedColumnNameValueBeanList(selectedColumnNameValue);
				int lastindexOfComma = sqlColumnNames.lastIndexOf(",");
				String sql = sqlColumnNames.toString();
				TemporalColumnUIBean temporalColumnUIBean = new TemporalColumnUIBean(null, selectSql, definedColumnsList, outputTermsColumns,columnIndex);
				modifySqlForTemporalColumns(temporalColumnUIBean);
				String selectTQSql = temporalColumnUIBean.getSql();
				columnIndex = temporalColumnUIBean.getColumnIndex();

				if (lastindexOfComma != -1 || selectTQSql.equals(""))
				{
					String columnsInSql = "";
					if (lastindexOfComma != -1)
						columnsInSql = sql.substring(0, lastindexOfComma).toString();
					if(!selectTQSql.equals(""))
					{
						columnsInSql = columnsInSql + selectTQSql;
					}
					Map<EntityInterface, Integer> entityIdIndexMap = new HashMap<EntityInterface, Integer>();
					columnsInSql = QueryCSMUtil.updateEntityIdIndexMap(null, columnIndex, columnsInSql,
							defineViewNodeList, entityIdIndexMap,queryDetailsObj);
					Iterator<QueryResultObjectDataBean> iterator = queryResultObjectDataBeanMap.values()
					.iterator();
					while (iterator.hasNext())
					{
						QueryResultObjectDataBean elem = (QueryResultObjectDataBean) iterator.next();
						if (elem.getMainEntityIdentifierColumnId() == -1)
						{
							if (!elem.isMainEntity())
								elem.setMainEntityIdentifierColumnId(entityIdIndexMap.get(elem
										.getMainEntity()));
							else
								elem.setMainEntityIdentifierColumnId(entityIdIndexMap
										.get(elem.getEntity()));
						}

					}
					selectSql = Constants.SELECT_DISTINCT + columnsInSql;

				}

				spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, definedColumnsList);
				return selectSql;
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
			public QuerySessionData getQuerySessionData(QueryDetails queryDetailsObj,
					int recordsPerPage, int startIndex, Map spreadSheetDataMap,
					String selectSql, Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap,
					boolean hasConditionOnIdentifiedField) throws ClassNotFoundException, DAOException
					{          
				QuerySessionData querySessionData = new QuerySessionData();
				querySessionData.setSql(selectSql);
				querySessionData.setQueryResultObjectDataMap(queryResultObjectDataBeanMap);
				querySessionData.setSecureExecute(queryDetailsObj.getSessionData().isSecurityRequired());
				querySessionData.setHasConditionOnIdentifiedField(hasConditionOnIdentifiedField);
				querySessionData.setRecordsPerPage(recordsPerPage);
				QueryBizLogic qBizLogic = new QueryBizLogic(); 
				PagenatedResultData pagenatedResultData = qBizLogic.execute(queryDetailsObj.getSessionData(), querySessionData, startIndex);
				List<List <String>> dataList = pagenatedResultData.getResult();

				for (Iterator<Long> beanMapIterator = queryResultObjectDataBeanMap.keySet().iterator(); beanMapIterator.hasNext(); )
				{
					Long id = beanMapIterator.next();
					QueryResultObjectDataBean queryResultObjectDataBean = queryResultObjectDataBeanMap.get(id);
					if (queryResultObjectDataBean.isClobeType())
					{	
						List<String> columnList = (List<String>)spreadSheetDataMap.get(Constants.SPREADSHEET_COLUMN_LIST);
						Map<Integer, Integer> fileTypeIndexMainEntityIndexMap = updateSpreadSheetColumnList(columnList, queryResultObjectDataBeanMap);	
						Map exportMetataDataMap = updateDataList(dataList, fileTypeIndexMainEntityIndexMap);	
						spreadSheetDataMap.put(Constants.ENTITY_IDS_MAP,exportMetataDataMap.get(Constants.ENTITY_IDS_MAP));
						spreadSheetDataMap.put(Constants.EXPORT_DATA_LIST,exportMetataDataMap.get(Constants.EXPORT_DATA_LIST));
						break;
					}
				}
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
			 * if file type attribute column is present in the spreadsheet view add the column to the datalist
			 * @param dataList
			 * @param fileTypeIndexMainEntityIndexMap
			 */
			public static Map updateDataList(List<List<String>> dataList, Map<Integer, Integer> fileTypeIndexMainEntityIndexMap)
			{
				Map<Integer, String> entityIdIndexMainEntityIdMap = new HashMap<Integer, String>();
				Map<String, Object> exportMetataDataMap = new HashMap<String, Object>();
				List<List<String>> newDataList = new ArrayList<List<String>>();
				List<String> exportRow = new ArrayList<String>();
				Map<Integer, List> entityIdsMap = new HashMap<Integer, List>();
				//int i = 0;
				int rowNo = 0;
				for(List<String> row : dataList)
				{
					List<String> entityIdsList = new ArrayList<String>();
					exportRow = new ArrayList<String>();
					exportRow.addAll(row);
					for (Iterator<Integer> fileTypeIterator = fileTypeIndexMainEntityIndexMap.keySet().iterator(); fileTypeIterator.hasNext(); )
					{
						int fileTypeIndex = fileTypeIterator.next();
						int mainEntityIdIndex = fileTypeIndexMainEntityIndexMap.get(fileTypeIndex);
						String mainEntityId = row.get(mainEntityIdIndex);
						entityIdIndexMainEntityIdMap.put(fileTypeIndex, mainEntityId);					
					}
					int fileTypeIndex = 0;
					for (Iterator<Integer> fileTypeIterator = fileTypeIndexMainEntityIndexMap.keySet().iterator(); fileTypeIterator.hasNext(); )
					{
						fileTypeIndex = fileTypeIterator.next();
						String mainEntityId = entityIdIndexMainEntityIdMap.get(fileTypeIndex);
						String newColumn = "<img src='images/ic_view_up_file.gif' onclick='javascript:viewSPR(\""+ mainEntityId
						+"\")' alt='click here' style='cursor:pointer;'>";
						String fileName =  Constants.EXPORT_FILE_NAME_START +mainEntityId+".txt";
						row.add(fileTypeIndex, newColumn);
						exportRow.add(fileTypeIndex,fileName);
						entityIdsList.add(mainEntityId);
						//i++;
					}
					newDataList.add(exportRow);
					entityIdsMap.put(rowNo, entityIdsList);
					rowNo++;
				}			
				exportMetataDataMap.put(Constants.ENTITY_IDS_MAP, entityIdsMap);
				exportMetataDataMap.put(Constants.EXPORT_DATA_LIST, newDataList);

				return exportMetataDataMap;
			}
			/**
			 * 
			 * @param spreadSheetDataMap
			 * @param queryResultObjectDataBeanMap
			 * @return
			 */
			public Map<Integer, Integer> updateSpreadSheetColumnList(List<String> spreadsheetColumnsList,Map<Long,QueryResultObjectDataBean> queryResultObjectDataBeanMap)
			{
				Map<Integer, String> fileTypeIndexColumnNameMap = new TreeMap<Integer, String>();
				Map<Integer, Integer> fileTypeIndexMainEntityIndexMap = new TreeMap<Integer, Integer>();
				// Stores all the file type attribute column names of all the entities in the map i.e. indexDisplayNameMap
				for (Iterator<Long> beanMapIterator = queryResultObjectDataBeanMap.keySet().iterator(); beanMapIterator.hasNext(); )
				{
					Long id = beanMapIterator.next();
					QueryResultObjectDataBean queryResultObjectDataBean = queryResultObjectDataBeanMap.get(id);
					if (queryResultObjectDataBean.isClobeType())
					{	
						Map<Integer, QueryOutputTreeAttributeMetadata> fileTypeAtrributeIndexMetadataMap
						= (Map<Integer, QueryOutputTreeAttributeMetadata>)
						queryResultObjectDataBean.getFileTypeAtrributeIndexMetadataMap();
						int mainEntityIdColumn = queryResultObjectDataBean.getMainEntityIdentifierColumnId();
						for (Iterator<Integer> iterator = fileTypeAtrributeIndexMetadataMap
								.keySet().iterator(); iterator.hasNext(); )
						{
							int fileTypeColumnId = iterator.next();
							QueryOutputTreeAttributeMetadata metaData 
							= (QueryOutputTreeAttributeMetadata)fileTypeAtrributeIndexMetadataMap
							.get(fileTypeColumnId);
							String displayName = metaData.getDisplayName();
							fileTypeIndexColumnNameMap.put(fileTypeColumnId, displayName);
							fileTypeIndexMainEntityIndexMap.put(fileTypeColumnId, mainEntityIdColumn);
						}
					}
				}
				for (Iterator<Integer> columnNameIterator = fileTypeIndexColumnNameMap
						.keySet().iterator(); columnNameIterator.hasNext(); )
				{
					int index = (Integer)columnNameIterator.next();
					String displayName = (String)fileTypeIndexColumnNameMap.get(index);
					if (!spreadsheetColumnsList.contains(displayName))
					{
						spreadsheetColumnsList.add(index, displayName);
					}
					else
					{
						int indexOfColumn = spreadsheetColumnsList.lastIndexOf(displayName);
						if(index == spreadsheetColumnsList.size())
						{
							spreadsheetColumnsList.add(index, displayName);
						}
						else
						{
							if ((indexOfColumn != index) && (!spreadsheetColumnsList.get(index)
									.equals(displayName)))
								spreadsheetColumnsList.add(index, displayName);
						}

					}
				}
				return fileTypeIndexMainEntityIndexMap;
			}

			/**
			 * @param spreadSheetDataMap
			 * @param columnsMap
			 * @param tableName 
			 * @param parentData 
			 * @param parentIdColumnName 
			 * @param mainEntityMap 
			 * @param outputTermsColumns 
			 * @return
			 */
			private List createSQL(Map spreadSheetDataMap, OutputTreeDataNode node,
					String parentIdColumnName, String parentData, String tableName,
					Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap,
					QueryDetails queryDetailsObj,Map<String, IOutputTerm> outputTermsColumns)
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
				int addedFileTypeAttributes = 0;
				Map<Integer, QueryOutputTreeAttributeMetadata> fileTypeAtrributeIndexMetadataMap = new HashMap<Integer, 
				QueryOutputTreeAttributeMetadata>();
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
					if(attribute.getIsIdentified()!=null && attribute.getIsIdentified())
					{
						identifiedDataColumnIds.add(columnIndex);
						queryResultObjectDataBean.setIdentifiedDataColumnIds(identifiedDataColumnIds);
					}
					if(!attribute.getAttributeTypeInformation().getDataType().equalsIgnoreCase(Constants.FILE_TYPE))
					{
						objectColumnIds.add(columnIndex);
						selectSql = selectSql + sqlColumnName + ",";
						sqlColumnName = sqlColumnName.substring(
								SqlGenerator.COLUMN_NAME.length(), sqlColumnName
								.length());
						String attrLabel = Utility.getDisplayLabel(attribute.getName());
						columnsList.add(attrLabel + " : " + className);
						columnIndex++;
					}
					else 
					{
						queryResultObjectDataBean.setClobeType(true);
						int fileTypeIndex = columnIndex + addedFileTypeAttributes;
						fileTypeAtrributeIndexMetadataMap.put(fileTypeIndex, attributeMetaData);
						addedFileTypeAttributes++;
					}
				}
				if (queryResultObjectDataBean.isClobeType())
				{
					queryResultObjectDataBean.setFileTypeAtrributeIndexMetadataMap(
							fileTypeAtrributeIndexMetadataMap);
				}
				if(! selectedColumnMetaData.isDefinedView())
				{
					spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
					selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
					TemporalColumnUIBean temporalColumnUIBean = new TemporalColumnUIBean(node, selectSql, columnsList, outputTermsColumns,columnIndex);
					modifySqlForTemporalColumns(temporalColumnUIBean);
					selectSql = temporalColumnUIBean.getSql();
					columnIndex = temporalColumnUIBean.getColumnIndex();
					selectedColumnMetaData.setSelectedAttributeMetaDataList(attributes);
				}
				else 
				{
					queryResultObjectDataBeanMap = new HashMap<Long, QueryResultObjectDataBean>();
					selectSql = getSQLForSelectedColumns(spreadSheetDataMap, queryResultObjectDataBeanMap,
							queryDetailsObj, outputTermsColumns);
					columnsList = (List<String>)spreadSheetDataMap.get(Constants.SPREADSHEET_COLUMN_LIST);
				}
				if(!selectedColumnMetaData.isDefinedView() && queryResultObjectDataBean
						.getMainEntityIdentifierColumnId() == -1)
				{
					Map<EntityInterface, Integer> entityIdIndexMap = new HashMap<EntityInterface, Integer>();
					selectSql = QueryCSMUtil.updateEntityIdIndexMap(queryResultObjectDataBean, columnIndex, 
							selectSql, null, entityIdIndexMap, queryDetailsObj);
					entityIdIndexMap = queryResultObjectDataBean.getEntityIdIndexMap();
					if(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity())!=null)
						queryResultObjectDataBean.setMainEntityIdentifierColumnId(entityIdIndexMap
								.get(queryResultObjectDataBean.getMainEntity()));
					else
						queryResultObjectDataBean.setMainEntityIdentifierColumnId(-1);
				}
				selectSql = selectSql + " from " + tableName;
				if (parentData != null)
				{
					selectSql = selectSql + " where (" + parentIdColumnName + " = '" + parentData + "' "
					+ LogicalOperator.And+ " " + idColumnOfCurrentNode+" "+RelationalOperator
					.getSQL(RelationalOperator.IsNotNull)+")";
				}
				else
				{
					selectSql = selectSql + " where "+idColumnOfCurrentNode+" "+RelationalOperator
					.getSQL(RelationalOperator.IsNotNull);	
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