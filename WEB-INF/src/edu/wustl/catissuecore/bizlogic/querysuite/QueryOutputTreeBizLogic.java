
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * Creates QueryOutputTree Object as per the data filled by the user on AddLimits section.
 * Creates QueryOutputTree Table.
 * @author deepti_shelar
 */
public class QueryOutputTreeBizLogic
{

	/**
	 * Creates new table which has the same structure and also same data , as the output tree structurs has.  
	 * @param String selectSql , from this sql , new table will be created .
	 * @param sessionData to be added to tablename to have unique table for each session user.
	 * @param String tableName 
	 * @throws Exception Exception
	 */
	public String createOutputTreeTable(String selectSql, SessionDataBean sessionData) throws DAOException
	{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String createTableSql = Constants.CREATE_TABLE + tableName + " " + Constants.AS + " " + selectSql;
		QueryModuleUtil.executeCreateTable(tableName, createTableSql, sessionData);
		return tableName;
	}

	

	/**
	 * Returns true if the attribute name can be used to form label for tree node.
	 * Label can be formed from attributes like 'first name', 'last name', 'title', 'label' ,'name' etc.
	 * @param attrName String
	 * @return true if the attribute name can be used to form label for tree node otherwise returns false
	 */
	boolean ifAttributeIsDisplayName(String attrName)
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
	 * This method creates first level(Default) output tree data.
	 * @param tableName name fo the table created
	 * @param query IQuery obj created out of user inputs to dag view.
	 * @param sessionData session data to get the user id.
	 * @param nodeAttributeColumnNameMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return Vector<QueryTreeNodeData> data structure to form tree out of it.
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public Vector<QueryTreeNodeData> createDefaultOutputTreeData(OutputTreeDataNode root, SessionDataBean sessionData,
			Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap) throws DAOException, ClassNotFoundException
			{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME+sessionData.getUserId();
		String selectSql = QueryModuleUtil.getSQLForRootNode(root, tableName, nodeAttributeColumnNameMap);
		String[] sqlIndex = selectSql.split(Constants.NODE_SEPARATOR);
		selectSql = sqlIndex[0];
		int index = Integer.parseInt(sqlIndex[1]);

		List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
		Vector<QueryTreeNodeData> treeDataVector = new Vector<QueryTreeNodeData>();
		if (dataList != null)
		{
			QueryTreeNodeData treeNode = new QueryTreeNodeData();
			String name = root.getOutputEntity().getDynamicExtensionsEntity().getName();
			name = name.substring(name.lastIndexOf(".") + 1, name.length());
			String displayName = name + " (" + dataList.size() + ")";
		//	String parentNodeId = name + root.getId() + Constants.UNDERSCORE + Constants.LABEL_TREE_NODE;
			String nodeId = Constants.NULL_ID + Constants.NODE_SEPARATOR + root.getId() + Constants.UNDERSCORE + name + Constants.LABEL_TREE_NODE;
			displayName = Constants.TREE_NODE_FONT+displayName+Constants.TREE_NODE_FONT_CLOSE;
			treeNode.setIdentifier(nodeId);
			treeNode.setObjectName(name);
			treeNode.setDisplayName(displayName);
			treeNode.setParentIdentifier(Constants.ZERO_ID);
			treeNode.setParentObjectName("");
			treeDataVector.add(treeNode);
			treeDataVector = addNodeToTree(index, dataList, treeNode, root, treeDataVector, nodeAttributeColumnNameMap);
		}
		return treeDataVector;
			}

	/**
	 * This method adds the node to tree.The id for new node is set as 'Id of OutputTreeNode _id value of that node in newly created table'
	 * @param dataList all records in database satisfying the criteria.
	 * @param parentNode parent node of tree data
	 * @param node the node to be added of OutputTreeDataNode.
	 * @param treeDataVector  data structure to form tree out of it.
	 * @param nodeAttributeColumnNameMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return treeDataVector  data structure to form tree out of it.
	 */
	Vector<QueryTreeNodeData> addNodeToTree(int index, List dataList, QueryTreeNodeData parentNode, OutputTreeDataNode node,
			Vector<QueryTreeNodeData> treeDataVector, Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap)
			{
		Iterator dataListIterator = dataList.iterator();
		List rowList = new ArrayList();
		Long outputTreeNodeId = node.getId();
		while (dataListIterator.hasNext())
		{
			rowList = (List) dataListIterator.next();
			boolean isNodeAlreadyPresentInTree = false;
			QueryTreeNodeData treeNode = null;
			String treeNodeId = (String) rowList.get(0);
			String nodeId = parentNode.getIdentifier().toString() + Constants.NODE_SEPARATOR + outputTreeNodeId + Constants.UNDERSCORE + treeNodeId;
			//String nodeId = outputTreeNodeId + Constants.UNDERSCORE + treeNodeId;
			Iterator iterTreeData = treeDataVector.iterator();
			while (iterTreeData.hasNext())
			{
				QueryTreeNodeData nodeInTree = (QueryTreeNodeData) iterTreeData.next();
				if (nodeInTree.getIdentifier().equals(nodeId))
				{
					isNodeAlreadyPresentInTree = true;
					break;
				}
			}
			if (!isNodeAlreadyPresentInTree)
			{
				treeNode = new QueryTreeNodeData();
				treeNode.setIdentifier(nodeId);
				EntityInterface dynExtEntity = node.getOutputEntity().getDynamicExtensionsEntity();
				String fullyQualifiedEntityName = dynExtEntity.getName();
				int lastIndex = fullyQualifiedEntityName.lastIndexOf(".");
				String entityName = fullyQualifiedEntityName.substring(lastIndex + 1);
				treeNode.setObjectName(fullyQualifiedEntityName);
				String displayName = entityName + Constants.UNDERSCORE + treeNodeId;
				if (index != -1)
				{
					displayName = (String) rowList.get(1);
				}
				if (displayName.equalsIgnoreCase(""))
				{
					displayName = entityName + Constants.UNDERSCORE + treeNodeId;
				}
				//displayName = Constants.TREE_NODE_FONT+displayName+Constants.TREE_NODE_FONT_CLOSE;
				treeNode.setDisplayName(displayName);
				treeNode.setParentIdentifier(parentNode.getIdentifier().toString());
				treeNode.setParentObjectName(parentNode.getObjectName());
				treeDataVector.add(treeNode);
			}
		}
		return treeDataVector;
			}

	/**
	 * Updates tree when user clicks on any of the nodes.
	 * @param id string id of the node 
	 * @param node node cicked
	 * @param idColumnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param parentNodeId string id of parent
	 * @param sessionData SessionDataBean to be sent for execute query
	 * @return string representing tree node structure
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	public String updateTree(String id, OutputTreeDataNode node,  Map<Long, Map<AttributeInterface, String>> idColumnMap,
			String parentNodeId, SessionDataBean sessionData) throws ClassNotFoundException, DAOException
			{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME+sessionData.getUserId();
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
		List<OutputTreeDataNode> children = node.getChildren();
		String outputTreeStr = "";
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
			String parId = id.substring(id.lastIndexOf(Constants.NODE_SEPARATOR) + 2, id.length());
			String nodeId = parId + Constants.NODE_SEPARATOR + childNode.getId() + Constants.UNDERSCORE + name + Constants.LABEL_TREE_NODE;
			String displayName = name + " (" + size + ")";
			displayName = Constants.TREE_NODE_FONT+displayName+Constants.TREE_NODE_FONT_CLOSE;
			String objectName = name;
			String parentId = id;
			String fullName = node.getOutputEntity().getDynamicExtensionsEntity().getName();
			String parentObjectName = fullName.substring(fullName.lastIndexOf(".") + 1, fullName.length());
			outputTreeStr = outputTreeStr + "|" + nodeId + "," + displayName + "," + objectName + "," + parentId + "," + parentObjectName;
		}
		return outputTreeStr;
			}

	/**
	 * This method is called when user clicks on a node present in a tree, to get all the child nodes added to tree. 
	 * @param nodeId id of the node clicked.
	 * @param idNodeMap map which stores id and nodes already added to tree.
	 * @param columnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @param sessionData sessionData session data to get the user id.
	 * @return String outputTreeStr which is then parsed and then sent to client to form tree. 
	 * String for one node is comma seperated for its id, display name, object name , parentId, parent Object name.
	 * Such string elements for child nodes are seperated by "|".
	 */
	public String updateTreeForLabelNode(String nodeId, Map<Long, OutputTreeDataNode> idNodeMap, Map<Long, Map<AttributeInterface, String>> columnMap,
			SessionDataBean sessionData) throws ClassNotFoundException, DAOException
			{
		String id1 = nodeId.substring(nodeId.lastIndexOf(Constants.NODE_SEPARATOR) + 2, nodeId.length());
		id1 = id1.substring(0, id1.indexOf(Constants.UNDERSCORE));
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String selectSql = "";
		int index = -1;
		String actualParentNodeId = nodeId.substring(0, nodeId.indexOf(Constants.NODE_SEPARATOR));//+2,nodeId.length());
		String[] nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		Long id = new Long(nodeIds[0]);
		String parentIdColumnName = null;
		OutputTreeDataNode parentNode = idNodeMap.get(new Long(id));
		OutputTreeDataNode currentNode = idNodeMap.get(new Long(id1));
		if (!actualParentNodeId.equalsIgnoreCase(Constants.NULL_ID))
		{
			Map<AttributeInterface, String> columns1 = columnMap.get(parentNode.getId());
			Set<AttributeInterface> setColumns = columns1.keySet();
			for (Iterator<AttributeInterface> iterator = setColumns.iterator(); iterator.hasNext();)
			{
				AttributeInterface attr = iterator.next();
				if (attr.getName().equalsIgnoreCase(Constants.ID))
				{
					parentIdColumnName = columns1.get(attr);
					break;
				}
			}
			List<OutputTreeDataNode> children = parentNode.getChildren();
			if (children.isEmpty())
			{
				return "";
			}
			String columnNames = "";
			columnNames = QueryModuleUtil.getColumnNamesForSelectpart(currentNode, columnMap);
			String indexStr = columnNames.substring(columnNames.lastIndexOf(";") + 1, columnNames.length());
			if (!indexStr.equalsIgnoreCase("null"))
			{
				index = new Integer(indexStr);
			}
			columnNames = columnNames.substring(0, columnNames.lastIndexOf(";"));
			selectSql = "select distinct " + columnNames;
			selectSql = selectSql + " from " + tableName + " where " + parentIdColumnName + " = '" + nodeIds[1] + "'";
		}
		if (actualParentNodeId.equalsIgnoreCase(Constants.NULL_ID))
		{
			selectSql = QueryModuleUtil.getSQLForRootNode(currentNode, tableName, columnMap);
		}
		List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
		String outputTreeStr = buildOutputTreeString(index, dataList, currentNode, nodeId, parentNode, idNodeMap);
		return outputTreeStr;
			}

	/**
	 * This method builds a string from the input data , based on this string tree will be formed.
	 * @param dataList List of result records
	 * @param children List<OutputTreeDataNode> child nodes 
	 * @param nodeId String id which will be parent id for the new nodes added to tree. 
	 * @param parentNode parent node 
	 * @param idNodeMap map which stores id and nodes already added to tree.
	 * @return String outputTreeStr which is then parsed and then sent to client to form tree. 
	 * String for one node is comma seperated for its id, display name, object name , parentId, parent Object name.
	 * Such string elements for child nodes are seperated by "|".
	 **/
	String buildOutputTreeString(int index, List dataList, OutputTreeDataNode currentNode, String nodeId, OutputTreeDataNode parentNode,
			Map<Long, OutputTreeDataNode> idNodeMap)
	{
		Iterator dataListIterator = dataList.iterator();
		List<String> existingNodesList = new ArrayList<String>();
		List rowList = new ArrayList();
		String outputTreeStr = "";
		while (dataListIterator.hasNext())
		{
			rowList = (List) dataListIterator.next();
			String treeNodeId = (String) rowList.get(0);
			String fullyQualifiedEntityName = currentNode.getOutputEntity().getDynamicExtensionsEntity().getName();
			int lastIndex = fullyQualifiedEntityName.lastIndexOf(".");
			String entityName = fullyQualifiedEntityName.substring(lastIndex + 1);
			String nodeIdToSet = nodeId + Constants.NODE_SEPARATOR + currentNode.getId() + Constants.UNDERSCORE + treeNodeId;
			//	String nodeIdToSet = childNode.getId() + Constants.UNDERSCORE + treeNodeId;
			String displayName = entityName + Constants.UNDERSCORE + treeNodeId;
			if (index != -1)
			{
				displayName = (String) rowList.get(1);
			}
			if (displayName.equalsIgnoreCase(""))
			{
				displayName = entityName + Constants.UNDERSCORE + treeNodeId;
			}
			//displayName = Constants.TREE_NODE_FONT+displayName+Constants.TREE_NODE_FONT_CLOSE;
			String objectname = fullyQualifiedEntityName;
			String parentIdToSet = nodeId;
			String parentObjectName = parentNode.getOutputEntity().getDynamicExtensionsEntity().getName();
			if (!existingNodesList.contains(nodeIdToSet))
			{
				existingNodesList.add(nodeIdToSet);
				idNodeMap.put(currentNode.getId(), currentNode);
				outputTreeStr = outputTreeStr + nodeIdToSet + "," + displayName + "," + objectname + "," + parentIdToSet + "," + parentObjectName
				+ "|";
			}
		}
		return outputTreeStr;
	}
}