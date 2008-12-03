package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryCSMUtil;
import edu.wustl.catissuecore.util.querysuite.TemporalColumnUIBean;
import edu.wustl.catissuecore.util.querysuite.QueryDetails;
import edu.wustl.catissuecore.util.querysuite.QueryModuleConstants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IOutputAttribute;
import edu.wustl.common.querysuite.queryobject.IOutputEntity;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.impl.OutputAttribute;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.Utility;
/**
 * This bizlogic is called when user wants to define columns to be shown for grid results.
 * 
 * @author deepti_shelar
 *
 */
public class DefineGridViewBizLogic
{
	/**
	 * This list denotes the seleted columns in a session.
	 */
	List<NameValueBean> prevSelectedColumnNameValueBeanList;

	/**
	 * returns class name when passed a OutputTreeDataNode
	 * @param node OutputTreeDataNode
	 * @return String className
	 */
	public String getClassName(OutputTreeDataNode node)
	{
		if(node != null)
		{
			IOutputEntity outputEntity = node.getOutputEntity();
			EntityInterface dynamicExtensionsEntity = outputEntity.getDynamicExtensionsEntity();
			String className = dynamicExtensionsEntity.getName();
			className = Utility.parseClassName(className);
			return className;
		}
		return null;
	}
	/**
	 * Creates tree structure data required for defining grid view.
	 * @param categorySearchForm action form
	 * @param uniqueIdNodesMap map of nodes present in query
	 * @param treeDataVector vector to store tree data
	 * @param currentSelectedObject
	 * @param prevSelectedColumnNameValueBeanList 
	 */
	public void createTree(CategorySearchForm categorySearchForm, QueryDetails queryDetailsObj,
			Vector<QueryTreeNodeData> treeDataVector, OutputTreeDataNode currentSelectedObject,
			List<NameValueBean> prevSelectedColumnNameValueBeanList)
	{
		this.prevSelectedColumnNameValueBeanList = prevSelectedColumnNameValueBeanList;
		if(!queryDetailsObj.getUniqueIdNodesMap().isEmpty())
		{
			addRootNode(treeDataVector);
			Set keySet = queryDetailsObj.getUniqueIdNodesMap().keySet();
			Iterator iterator = keySet.iterator();
			Object id;
			OutputTreeDataNode node;
			while (iterator.hasNext())
			{
				id = (Object)iterator.next();
				node = null;
				if(id instanceof String)
				{
					String nodeId = id.toString();
					node = queryDetailsObj.getUniqueIdNodesMap().get(nodeId);
					addClassAndAttributeNodes(treeDataVector, node,
					categorySearchForm, currentSelectedObject);
				}
			}
		}
	}

	/**
	 * Adds root node to the tree, its a just a label node saying classes present in query.
	 * @param treeDataVector  vector to store tree data
	 */
	private void addRootNode(Vector<QueryTreeNodeData> treeDataVector)
	{
		QueryTreeNodeData classTreeNode = new QueryTreeNodeData();
		String rootNodeId = Constants.ROOT;
		classTreeNode.setIdentifier(rootNodeId);
		classTreeNode.setObjectName(rootNodeId);
		String rootDiplayName = Constants.CLASSES_PRESENT_IN_QUERY;
		classTreeNode.setDisplayName(rootDiplayName);
		classTreeNode.setParentIdentifier(Constants.ZERO_ID);
		classTreeNode.setParentObjectName("");
		treeDataVector.add(classTreeNode);
	}

	/**
	 * This method adds class node to the tree.And calls addAttributeNodes method to add attribute nodes to tree.
	 * @param treeDataVector  vector to store tree data
	 * @param node OutputTreeDataNode node to be added in tree
	 * @param categorySearchForm action form
	 * @param currentSelectedObject  OutputTreeDataNode
	 */
	private void addClassAndAttributeNodes(Vector<QueryTreeNodeData> treeDataVector, OutputTreeDataNode node, 
			CategorySearchForm categorySearchForm, OutputTreeDataNode currentSelectedObject)
	{
		long selectedObjectId = currentSelectedObject.getId();
		long id = node.getId();
		String uniqueNodeId = node.getUniqueNodeId();
		IOutputEntity outputEntity = node.getOutputEntity();
		EntityInterface dynamicExtensionsEntity = outputEntity.getDynamicExtensionsEntity();
		String className = dynamicExtensionsEntity.getName();
		className = Utility.parseClassName(className);
		String classDisplayName = Utility.getDisplayLabel(className);
		String treeClassNodeId = Constants.CLASS + Constants.UNDERSCORE + uniqueNodeId
			+ Constants.UNDERSCORE + className;
		QueryTreeNodeData classTreeNode = new QueryTreeNodeData();
		classTreeNode.setIdentifier(treeClassNodeId);
		classTreeNode.setObjectName(className);
		classTreeNode.setDisplayName(classDisplayName);
		classTreeNode.setParentIdentifier(Constants.ROOT);
		classTreeNode.setParentObjectName("");
		treeDataVector.add(classTreeNode);
		boolean isSelectedObject = false;
		if(selectedObjectId == id)
		{
			isSelectedObject = true;
			if(prevSelectedColumnNameValueBeanList == null)
				categorySearchForm.setCurrentSelectedNodeInTree(treeClassNodeId);
		}
		addAttributeNodes(treeDataVector, className, treeClassNodeId, categorySearchForm,
				node.getAttributes(), isSelectedObject);
	}
	/**
	 * Adds attribute nodes to tree.
	 * @param treeDataVector vector to store tree data
	 * @param className String class name
	 * @param treeClassNodeId parentid to add to tree
	 * @param categorySearchForm action form 
	 * @param attributeMetadataList  Metadata to be added
	 * @param isSelectedObject whether the object is selected
	 */
	private void addAttributeNodes(Vector<QueryTreeNodeData> treeDataVector,
			String className, String treeClassNodeId, CategorySearchForm categorySearchForm,
			List<QueryOutputTreeAttributeMetadata> attributeMetadataList, boolean isSelectedObject)
	{
		//List<QueryOutputTreeAttributeMetadata> attributeMetadataList = node.getAttributes();
		List<NameValueBean> selectedColumnNameValue = new ArrayList<NameValueBean>();
		AttributeInterface attribute;
		String attributeName;
		String attributeDisplayName;
		String attributeWithClassName;
		String treeAttributeNodeId;
		QueryTreeNodeData attributeTreeNode;
		NameValueBean nameValueBean;
		for(QueryOutputTreeAttributeMetadata attributeMetadata : attributeMetadataList)
		{
			attribute = attributeMetadata.getAttribute();
			attributeName = attribute.getName();
			attributeDisplayName = Utility.getDisplayLabel(attributeName);
			attributeWithClassName = attributeMetadata.getDisplayName();
			treeAttributeNodeId = attributeMetadata.getUniqueId();
			attributeTreeNode = new QueryTreeNodeData();
			attributeTreeNode.setIdentifier(treeAttributeNodeId);
			attributeTreeNode.setObjectName(attributeName);
			attributeTreeNode.setDisplayName(attributeDisplayName);
			attributeTreeNode.setParentIdentifier(treeClassNodeId);
			attributeTreeNode.setParentObjectName(className);
			treeDataVector.add(attributeTreeNode);
			nameValueBean = new NameValueBean();
			nameValueBean.setName(attributeWithClassName);
			nameValueBean.setValue(treeAttributeNodeId);
			selectedColumnNameValue.add(nameValueBean);
		}
		if(prevSelectedColumnNameValueBeanList != null)
		{
			categorySearchForm.setSelectedColumnNameValueBeanList(prevSelectedColumnNameValueBeanList);
		} 
		else if(isSelectedObject)
		{
			categorySearchForm.setSelectedColumnNameValueBeanList(selectedColumnNameValue);
		}
	}
	
	/**
	 * returns list of seletced columns
	 * @param selectedColumnIds
	 * @param constraints 
	 * @param uniqueIdNodesMap map of id and Node
	 * @param constraints 
	 * @return SelectedColumnsMetadata SelectedColumnsMetadata
	 */
	public void getSelectedColumnsMetadata(CategorySearchForm categorySearchForm, QueryDetails queryDetailsObj,
			 SelectedColumnsMetadata selectedColumnsMetadata, IConstraints constraints)
	{  
		String[] selectedColumnIds = categorySearchForm.getSelectedColumnNames();
		List<QueryOutputTreeAttributeMetadata> attribureMetadataList
			= new ArrayList<QueryOutputTreeAttributeMetadata>();
		List<IOutputAttribute> outputAttributeList = new ArrayList<IOutputAttribute>();
		List<NameValueBean> selectedColumnNameValue = new ArrayList<NameValueBean>();
		IOutputAttribute attr;
		String columnId;
		//String[] split;
		String expressionId;
		OutputTreeDataNode outputTreeDataNode;
		int lenOfCIds=selectedColumnIds.length;
		for (int i = 0; i < lenOfCIds; i++)
		{ 
			attr= null;
			columnId = selectedColumnIds[i];
			String[] split = columnId.split(Constants.EXPRESSION_ID_SEPARATOR);
			expressionId = split[QueryModuleConstants.INDEX_PARAM_ZERO] ;
			outputTreeDataNode = getMatchingOutputTreeDataNode(queryDetailsObj, expressionId);
			if(outputTreeDataNode != null)
			{
				List<QueryOutputTreeAttributeMetadata> attributes = outputTreeDataNode.getAttributes();
				NameValueBean nameValueBean;
				for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
				{
					if(columnId.equals(attributeMetaData.getUniqueId()))
					{
						attribureMetadataList.add(attributeMetaData);
						attr= new OutputAttribute(constraints.getExpression(outputTreeDataNode.getExpressionId()),attributeMetaData.getAttribute());
						outputAttributeList.add(attr);
						nameValueBean = new NameValueBean(attributeMetaData
								.getDisplayName(), attributeMetaData.getUniqueId());
						selectedColumnNameValue.add(nameValueBean);
						break;
					}
				}
			}
		}
		selectedColumnsMetadata.setSelectedAttributeMetaDataList(attribureMetadataList);
		selectedColumnsMetadata.setSelectedOutputAttributeList(outputAttributeList);
		selectedColumnsMetadata.setSelectedColumnNameValueBeanList(selectedColumnNameValue);
	}
	/**
	 * Returns the matching OutputTreeDataNode, compares expression id.
	 * @param uniqueIdNodesMap map of all nodes in query
	 * @param expressionId string id
	 * @return OutputTreeDataNode node
	 */
	private OutputTreeDataNode getMatchingOutputTreeDataNode(QueryDetails queryDetailsObj, String expressionId) {
		OutputTreeDataNode outputTreeDataNode = null;
		Set<String> keySet = queryDetailsObj.getUniqueIdNodesMap().keySet();
		Iterator<String> iterator = keySet.iterator();
		Object next;
		while (iterator.hasNext()) {
			next = iterator.next();
			if(next instanceof String)
			{
				outputTreeDataNode = queryDetailsObj.getUniqueIdNodesMap().get(next);
				if(new Integer(outputTreeDataNode.getExpressionId()).toString().equals(expressionId))
				{
					break;
				}
			}
		}
		return outputTreeDataNode;
	}
	/**
	 * 
	 * @param categorySearchForm
	 * @param selectedAttributeMetaDataList
	 * @param selectedColumnNames
	 * @param queryResultObjecctDataMap
	 * @param nodeData 
	 * @param mainEntityMap
	 * @param uniqueIdNodesMap
	 * @return
	 */
	public List<String> getSelectedColumnList(CategorySearchForm categorySearchForm,
			List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList, StringBuffer selectedColumnNames,
			Map<Long, QueryResultObjectDataBean> queryResultObjecctDataMap, QueryDetails queryDetailsObj,Map<String, IOutputTerm> outputTermsColumns, String nodeData)
	{
		queryResultObjecctDataMap.clear();
		List<String> definedColumnsList = new ArrayList<String>();
		List<NameValueBean> selectedColumnNameValue = new ArrayList<NameValueBean>();
//		List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnsMetadata
//			.getSelectedAttributeMetaDataList();
		QueryResultObjectDataBean queryResulObjectDataBean =null;
		//Vector<Integer> identifiedColumnIds = null;
		//Vector<Integer> objectColumnIds = null;
		List<EntityInterface> defineViewNodeList = new ArrayList<EntityInterface>();
		int columnIndex = 0;
		int totalFileTypeAttributes = 0;
		Iterator<QueryOutputTreeAttributeMetadata> iterator = selectedAttributeMetaDataList.iterator();
		QueryOutputTreeAttributeMetadata element;
		while (iterator.hasNext())
		{
			element = (QueryOutputTreeAttributeMetadata) iterator.next();
			queryResulObjectDataBean = queryResultObjecctDataMap.get(element.getTreeDataNode().getId());
			if(queryResulObjectDataBean==null)
			{
				//identifiedColumnIds = new Vector<Integer>();
				//objectColumnIds = new Vector<Integer>();
				queryResulObjectDataBean = QueryCSMUtil.getQueryResulObjectDataBean(element
						.getTreeDataNode(), queryDetailsObj);
				queryResultObjecctDataMap.put(element.getTreeDataNode().getId(),
						queryResulObjectDataBean);
				defineViewNodeList.add(element.getTreeDataNode().getOutputEntity()
						.getDynamicExtensionsEntity());
			}
			
			if(element.getAttribute().getIsIdentified()!=null && element.getAttribute().getIsIdentified())
			{
				queryResulObjectDataBean.getIdentifiedDataColumnIds().add(columnIndex);
			}
			if(Constants.ID.equals(element.getAttribute().getName()))
			{
				if(queryResulObjectDataBean.isMainEntity())
					queryResulObjectDataBean.setMainEntityIdentifierColumnId(columnIndex);
				else
					queryResulObjectDataBean.setEntityId(columnIndex);
			}
			if(Constants.FILE_TYPE.equalsIgnoreCase(element.getAttribute().getDataType()))
			{
				queryResulObjectDataBean.setClobeType(true);
				Map beanMetadataMap = queryResulObjectDataBean.getFileTypeAtrributeIndexMetadataMap();
				int fileTypeIndex = columnIndex + totalFileTypeAttributes;
				beanMetadataMap.put(fileTypeIndex, element);
				queryResulObjectDataBean.setFileTypeAtrributeIndexMetadataMap(beanMetadataMap);
				totalFileTypeAttributes++;
			}
			else
			{			
			queryResulObjectDataBean.getObjectColumnIds().add(columnIndex);
			selectedColumnNames.append(element.getColumnName());
			selectedColumnNames.append(Constants.DELIMETER);
			definedColumnsList.add(element.getDisplayName());			
			columnIndex++;
			}
			NameValueBean nameValueBean = new NameValueBean(element.getDisplayName(),element.getUniqueId());
			selectedColumnNameValue.add(nameValueBean);
		}
		Iterator<Long> mapItr = queryResultObjecctDataMap.keySet().iterator();
		String sql="";
		if (selectedColumnNames.lastIndexOf(Constants.DELIMETER) != -1)
		{
			sql = selectedColumnNames.substring(0, selectedColumnNames.lastIndexOf(Constants.DELIMETER));
		}
		List tqColumnMetadataList = new ArrayList();
		if(!outputTermsColumns.isEmpty())
		{
			QueryOutputSpreadsheetBizLogic gridBizLogic = new QueryOutputSpreadsheetBizLogic();
			IConstraints constraints = queryDetailsObj.getQuery().getConstraints();
			TemporalColumnUIBean temporalColumnUIBean = new TemporalColumnUIBean(null, sql, definedColumnsList, outputTermsColumns,columnIndex,constraints);
			tqColumnMetadataList = gridBizLogic.modifySqlForTemporalColumns(temporalColumnUIBean,queryDetailsObj,nodeData);
			sql = temporalColumnUIBean.getSql();
			columnIndex = temporalColumnUIBean.getColumnIndex();
		}
		selectedColumnNames.replace(0, selectedColumnNames.length(), sql);
		while(mapItr.hasNext())
		{
		queryResulObjectDataBean = queryResultObjecctDataMap.get(mapItr.next());
		if(queryResulObjectDataBean.getMainEntityIdentifierColumnId()==-1)
		{
			queryResulObjectDataBean.setTqColumnMetadataList(tqColumnMetadataList);
			Map<EntityInterface, Integer> entityIdIndexMap = new HashMap<EntityInterface, Integer>();
			sql = QueryCSMUtil.updateEntityIdIndexMap(queryResulObjectDataBean, columnIndex,
					sql, defineViewNodeList, entityIdIndexMap, queryDetailsObj);
			selectedColumnNames.replace(QueryModuleConstants.ARGUMENT_ZERO,
					selectedColumnNames.length(), sql);
			if(queryResulObjectDataBean.isMainEntity()) {
				EntityInterface entity = queryResulObjectDataBean.getEntity();
				Integer integer = queryResulObjectDataBean.getEntityIdIndexMap().get(entity);
				queryResulObjectDataBean.setMainEntityIdentifierColumnId(integer);
			} else
			{
				EntityInterface mainEntity = queryResulObjectDataBean.getMainEntity();
				if(queryResulObjectDataBean.getEntityIdIndexMap().get(mainEntity)!=null)
				  queryResulObjectDataBean.setMainEntityIdentifierColumnId(queryResulObjectDataBean
						  .getEntityIdIndexMap().get(mainEntity));
			}
		}
		else
		queryResulObjectDataBean.setTqColumnMetadataList(tqColumnMetadataList);
		}
		categorySearchForm.setSelectedColumnNameValueBeanList(selectedColumnNameValue);
		return definedColumnsList;
	}
	
	/**
	 * @param selectedColumnNames buffer to store selected columns 
	 * @param sql formed for newly defined columns
	 * @return String sql
	 */
	public String createSQLForSelectedColumn(String columnsInSql, String sql)
	{
		//String columnsInSql = selectedColumnNames.toString();
		int indexOfSelectDistict = sql.indexOf(Constants.SELECT_DISTINCT);
		int selectDistictLength = Constants.SELECT_DISTINCT.length();
		int indexOfFrom = sql.indexOf(Constants.FROM);
		StringBuffer newSql = new StringBuffer();
		newSql.append(sql.substring(indexOfSelectDistict,selectDistictLength));
		newSql.append(" ");
		newSql.append(columnsInSql);
		newSql.append(sql.substring(indexOfFrom));
		return newSql.toString();
	}
	/**
	 * returns list of seletced columns
	 * @param constraints 
	 * @param categorySearchForm form
	 * @param uniqueIdNodesMap map of id and Node
	 * @return SelectedColumnsMetadata SelectedColumnsMetadata
	 */
	public SelectedColumnsMetadata getColumnsMetadataForSelectedNode(OutputTreeDataNode outputTreeDataNode,
			SelectedColumnsMetadata selectedColumnsMetadata, IConstraints constraints)
	{
		if(outputTreeDataNode != null)
		{
			List<IOutputAttribute> selectedOutputAttributeList = new ArrayList<IOutputAttribute>();
			List<QueryOutputTreeAttributeMetadata> attributes = outputTreeDataNode.getAttributes();
			selectedColumnsMetadata.setSelectedAttributeMetaDataList(attributes);
			if(selectedColumnsMetadata.isDefinedView())
			{
				AttributeInterface attribute;
				OutputAttribute attr;
				for(QueryOutputTreeAttributeMetadata metadata:attributes)
				{
					attribute = metadata.getAttribute();
					attr = new OutputAttribute(constraints.getExpression(outputTreeDataNode.getExpressionId()),attribute);
					selectedOutputAttributeList.add(attr);
				}
				selectedColumnsMetadata.setSelectedOutputAttributeList(selectedOutputAttributeList);
			}
		}
		return selectedColumnsMetadata;
	}
	/**
	 * 
	 * @param values
	 * @param selectedAttributeList
	 * @param selectedColumnsMetadata
	 */
	public void getSelectedColumnMetadataForSavedQuery(Collection<OutputTreeDataNode> values,
			List<IOutputAttribute> selectedAttributeList, SelectedColumnsMetadata selectedColumnsMetadata)
	{
		//Collection<OutputTreeDataNode> values = uniqueIdNodesMap.values();
		List<QueryOutputTreeAttributeMetadata> attribureMetadataList 
			= new ArrayList<QueryOutputTreeAttributeMetadata>();
		Set newSet = new HashSet<QueryOutputTreeAttributeMetadata>();
		List<NameValueBean> selectedColumnNameValue = new ArrayList<NameValueBean>();
		for(OutputTreeDataNode node:values)
		{
			for(IOutputAttribute outAttr :selectedAttributeList)
			{
				if(outAttr.getAttribute().getEntity().getId().equals(node.getOutputEntity()
						.getDynamicExtensionsEntity().getId()))
				{
					List<QueryOutputTreeAttributeMetadata> attributes = node.getAttributes();
					for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
					{
						if(outAttr.getAttribute().equals(attributeMetaData.getAttribute()))
						{
							boolean add = newSet.add(attributeMetaData);
							NameValueBean nameValueBean;
							if(add)
							{
								attribureMetadataList.add(attributeMetaData);
								nameValueBean = new NameValueBean(attributeMetaData
								.getDisplayName(), attributeMetaData.getUniqueId());
								selectedColumnNameValue.add(nameValueBean);
								break;
							}
						}
					}
				}
			}
		}
		selectedColumnsMetadata.setSelectedAttributeMetaDataList(attribureMetadataList);
		selectedColumnsMetadata.setSelectedColumnNameValueBeanList(selectedColumnNameValue);
	}
}
