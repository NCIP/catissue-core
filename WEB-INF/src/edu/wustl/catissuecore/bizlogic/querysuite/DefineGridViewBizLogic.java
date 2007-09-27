package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.querysuite.queryobject.IOutputEntity;
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
	public void createTree(CategorySearchForm categorySearchForm, Map<Long, OutputTreeDataNode> uniqueIdNodesMap, 
			Vector<QueryTreeNodeData> treeDataVector, OutputTreeDataNode currentSelectedObject, 
			List<NameValueBean> prevSelectedColumnNameValueBeanList)
	{
		this.prevSelectedColumnNameValueBeanList = prevSelectedColumnNameValueBeanList;
		if(!uniqueIdNodesMap.isEmpty())
		{
			addRootNode(treeDataVector);
			Set keySet = uniqueIdNodesMap.keySet();
			Iterator iterator = keySet.iterator();
			while (iterator.hasNext())
			{
				Object id = (Object)iterator.next();
				OutputTreeDataNode node = null;
				if(id instanceof String)
				{
					String nodeId = id.toString();
					node = uniqueIdNodesMap.get(nodeId);
					addClassAndAttributeNodes(treeDataVector, node,categorySearchForm,currentSelectedObject);
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
		String treeClassNodeId = Constants.CLASS + "_" + uniqueNodeId+"_"+className;
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
		addAttributeNodes(treeDataVector, className, treeClassNodeId,categorySearchForm,node,isSelectedObject);
	}
	/**
	 * Adds attribute nodes to tree.
	 * @param treeDataVector vector to store tree data
	 * @param dynamicExtensionsEntity EntityInterface
	 * @param className String class name
	 * @param treeClassNodeId parentid to add to tree
	 * @param categorySearchForm action form 
	 * @param node  OutputTreeDataNode to be added
	 * @param isSelectedObject whether the object is selected
	 */
	private void addAttributeNodes(Vector<QueryTreeNodeData> treeDataVector, String className, String treeClassNodeId, 
			CategorySearchForm categorySearchForm, OutputTreeDataNode node, boolean isSelectedObject)
	{
		List<QueryOutputTreeAttributeMetadata> attributeMetadataList = node.getAttributes();
		List<NameValueBean> selectedColumnNameValue = new ArrayList<NameValueBean>();
		for(QueryOutputTreeAttributeMetadata attributeMetadata : attributeMetadataList)
		{
			AttributeInterface attribute = attributeMetadata.getAttribute();
			String attributeName = attribute.getName();
			String attributeDisplayName = Utility.getDisplayLabel(attributeName);
			String attributeWithClassName = attributeMetadata.getDisplayName();
			String treeAttributeNodeId = attributeMetadata.getUniqueId();
			QueryTreeNodeData attributeTreeNode = new QueryTreeNodeData();
			attributeTreeNode.setIdentifier(treeAttributeNodeId);
			attributeTreeNode.setObjectName(attributeName);
			attributeTreeNode.setDisplayName(attributeDisplayName);
			attributeTreeNode.setParentIdentifier(treeClassNodeId);
			attributeTreeNode.setParentObjectName(className);
			treeDataVector.add(attributeTreeNode);
			NameValueBean nameValueBean = new NameValueBean();
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
	 * @param categorySearchForm form
	 * @param uniqueIdNodesMap map of id and Node
	 * @return SelectedColumnsMetadata SelectedColumnsMetadata
	 */
	public void getSelectedColumnsMetadata(CategorySearchForm categorySearchForm, Map<Long, OutputTreeDataNode> uniqueIdNodesMap, SelectedColumnsMetadata selectedColumnsMetadata)
	{
		String[] selectedColumnIds = categorySearchForm.getSelectedColumnNames();
		List<QueryOutputTreeAttributeMetadata> attribureMetadataList = new ArrayList<QueryOutputTreeAttributeMetadata>(); 
		for (int i = 0; i < selectedColumnIds.length; i++)
		{
			String columnId = selectedColumnIds[i];
			String[] split = columnId.split(":");
			String uniqueNodeId = split[0];
			OutputTreeDataNode outputTreeDataNode = uniqueIdNodesMap.get(uniqueNodeId);
			if(outputTreeDataNode != null)
			{
				List<QueryOutputTreeAttributeMetadata> attributes = outputTreeDataNode.getAttributes();
				for(QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
				{
					if(attributeMetaData.getUniqueId().equalsIgnoreCase(columnId))
					{
						attribureMetadataList.add(attributeMetaData);
						break;
					}
				}
			}
		}
		selectedColumnsMetadata.setSelectedAttributeMetaDataList(attribureMetadataList);
	}
	/**
	 * 
	 * @param categorySearchForm
	 * @param selectedColumnsMetadata
	 * @param selectedColumnNames
	 * @return
	 */
	public List<String> getSelectedColumnList(CategorySearchForm categorySearchForm,SelectedColumnsMetadata selectedColumnsMetadata,StringBuffer selectedColumnNames)
	{
		List<String> definedColumnsList = new ArrayList<String>();
		List<NameValueBean> selectedColumnNameValue = new ArrayList<NameValueBean>();
		List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnsMetadata.getSelectedAttributeMetaDataList();
		Iterator<QueryOutputTreeAttributeMetadata> iterator = selectedAttributeMetaDataList.iterator();
		while (iterator.hasNext())
		{
			QueryOutputTreeAttributeMetadata element = (QueryOutputTreeAttributeMetadata) iterator.next();
			selectedColumnNames.append(element.getColumnName());
			selectedColumnNames.append(", ");
			definedColumnsList.add(element.getDisplayName());
			NameValueBean nameValueBean = new NameValueBean(element.getDisplayName(),element.getUniqueId());
			selectedColumnNameValue.add(nameValueBean);
		}
		categorySearchForm.setSelectedColumnNameValueBeanList(selectedColumnNameValue);
		return definedColumnsList;
	}
	
	/**
	 * @param selectedColumnNames buffer to store selected columns 
	 * @param sql formed for newly defined columns
	 * @return String sql
	 */
	public String createSQLForSelectedColumn(StringBuffer selectedColumnNames, String sql)
	{
		String columnsInSql = selectedColumnNames.toString();
		if(columnsInSql.lastIndexOf(",") != -1)
		{
			columnsInSql = columnsInSql.substring(0, columnsInSql.lastIndexOf(","));
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
		return null;
	}
	/**
	 * returns list of seletced columns
	 * @param categorySearchForm form
	 * @param uniqueIdNodesMap map of id and Node
	 * @return SelectedColumnsMetadata SelectedColumnsMetadata
	 */
	public SelectedColumnsMetadata getColumnsMetadataForSelectedNode(OutputTreeDataNode outputTreeDataNode,SelectedColumnsMetadata selectedColumnsMetadata)
	{
		if(outputTreeDataNode != null)
		{
			List<QueryOutputTreeAttributeMetadata> attributes = outputTreeDataNode.getAttributes();
			selectedColumnsMetadata.setSelectedAttributeMetaDataList(attributes);
		}
		return selectedColumnsMetadata;
	}

}
