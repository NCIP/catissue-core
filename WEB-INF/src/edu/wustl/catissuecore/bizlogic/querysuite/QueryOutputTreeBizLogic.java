
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
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * Creates QueryOutputTree Object as per the data filled by the user on AddLimits section.
 * Creates QueryOutputTree Table.
 * @author deepti_shelar
 *
 */
public class QueryOutputTreeBizLogic
{

	/**
	 * Creates new table which has the same structure and also same data , as the output tree structurs has.  
	 * @param String selectSql , from this sql , new table will be created .
	 * @param sessionData to be added to tablename to have unique table for each session user.
	 * @param String tableName 
	 * @throws Exception 
	 */
	public String createOutputTreeTable(String selectSql, SessionDataBean sessionData) throws DAOException
	{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String createTableSql = Constants.CREATE_TABLE + tableName + " " + Constants.AS + " " + selectSql;
		QueryModuleUtil.executeCreateTable(tableName, createTableSql, sessionData);
		return tableName;
	}
	/**
	 * Forms select part of the query.
	 * @param node Node of Uotput tree .
	 * @param columnMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return String having all columnnames for select part.
	 */
	String getColumnNamesForSelectpart(IOutputTreeNode node,Map<Long, Map<AttributeInterface, String>> columnMap)
	{
		String columnNames = "";
		String idColumnName = null;
		String displayNameColumnName = null;
		String index = null;
		Map<AttributeInterface, String> columns = columnMap.get(node.getId());
		if(columns != null)
		{
			Set<AttributeInterface> setColumns = columns.keySet();
			for (Iterator<AttributeInterface> iterator = setColumns.iterator(); iterator.hasNext();)
			{
				AttributeInterface attr = iterator.next();
				String columnName = columns.get(attr);
				if (attr.getName().equalsIgnoreCase(Constants.ID))
				{
					idColumnName = columnName;
				}
				if(ifAttributeIsDisplayName(attr.getName()))
				{
					index = columnName.substring(Constants.COLUMN_NAME.length(),columnName.length());
					displayNameColumnName = columnName;
				}
				else
				{
					columnNames = columnNames + columnName + " , ";
				}
			}
		}
		if(displayNameColumnName != null)
		{
			columnNames = idColumnName + " , " + displayNameColumnName + " , " + columnNames;
		} 
		else
		{
			columnNames = idColumnName + " , " + columnNames;
		}
		columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		columnNames = columnNames + ";" + index;
		return columnNames; 

	}
	/**
	 * Returns true if the attribute name can be used to form label for tree node.
	 * @param attrName String
	 * @return true if the attribute name can be used to form label for tree node otherwise returns false
	 */
	boolean ifAttributeIsDisplayName(String attrName)
	{
		String [] attrNamesForLabel = Constants.ATTRIBUTE_NAMES_FOR_TREENODE_LABEL;
		for(int i=0;i<attrNamesForLabel.length;i++)
		{
			String name = attrNamesForLabel[i];
			if(attrName.equalsIgnoreCase(name))
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
	 * @throws DAOException 
	 * @throws ClassNotFoundException 
	 */
	public Vector<QueryTreeNodeData> createDefaultOutputTreeData(String tableName, IQuery query, SessionDataBean sessionData,
			Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap) throws DAOException, ClassNotFoundException
			{
		IOutputTreeNode root = query.getRootOutputClass();
		String columnNames = getColumnNamesForSelectpart(root, nodeAttributeColumnNameMap);
		String indexStr = columnNames.substring(columnNames.lastIndexOf(";")+1,columnNames.length());
		int index = -1;
		if(!indexStr.equalsIgnoreCase("null"))
		{
			index = new Integer(indexStr);
		}
		columnNames = columnNames.substring(0,columnNames.lastIndexOf(";"));
		String selectSql = "select "+columnNames+" from "+tableName;
		List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
		Vector<QueryTreeNodeData> treeDataVector = new Vector<QueryTreeNodeData>();
		if (dataList != null)
		{
			QueryTreeNodeData treeNode = new QueryTreeNodeData();
			treeNode.setIdentifier(Constants.ALL);
			treeNode.setObjectName(Constants.ALL);
			treeNode.setDisplayName(Constants.ALL);
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
	 * @param node the node to be added of IOutputTreeNode.
	 * @param treeDataVector  data structure to form tree out of it.
	 * @param nodeAttributeColumnNameMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return treeDataVector  data structure to form tree out of it.
	 */
	Vector<QueryTreeNodeData> addNodeToTree(int index,List dataList, QueryTreeNodeData parentNode, IOutputTreeNode node,
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
			String nodeId = parentNode.getIdentifier().toString() + "::" + outputTreeNodeId + Constants.UNDERSCORE + treeNodeId;
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
				String displayName = entityName + "_" + treeNodeId;
				if(index != -1)
				{
					displayName = (String)rowList.get(1);
				}
				if(displayName.equalsIgnoreCase(""))
				{
					displayName = entityName + "_" + treeNodeId;
				}
				treeNode.setDisplayName(displayName);
				treeNode.setParentIdentifier(parentNode.getIdentifier().toString());
				treeNode.setParentObjectName(parentNode.getObjectName());
				treeDataVector.add(treeNode);
			}
		}
		return treeDataVector;
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
	public String buildTreeForNode(String nodeId, Map<Long, IOutputTreeNode> idNodeMap, Map<Long, Map<AttributeInterface, String>> columnMap,
			SessionDataBean sessionData) throws ClassNotFoundException, DAOException
			{
		String actualParentNodeId = nodeId.substring(nodeId.lastIndexOf("::")+2,nodeId.length());
		String[] nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		Long id = new Long(nodeIds[0]);
		String parentIdColumnName = null;
		IOutputTreeNode parentNode = idNodeMap.get(id);
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
		List<IOutputTreeNode> children = parentNode.getChildren();
		if(children.isEmpty())
		{
			return "";
		}
		String columnNames = "";
		int index = -1;
		for (IOutputTreeNode childNode : children)
		{
			columnNames = getColumnNamesForSelectpart(childNode, columnMap);
			String indexStr = columnNames.substring(columnNames.lastIndexOf(";")+1,columnNames.length());

			if(!indexStr.equalsIgnoreCase("null"))
			{
				index = new Integer(indexStr);
			}
			columnNames = columnNames.substring(0,columnNames.lastIndexOf(";"));
		}
		columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String selectSql = "select distinct " + columnNames;
		selectSql = selectSql + " from " + tableName + " where " + parentIdColumnName + " = '" + nodeIds[1]+"'";
		System.out.println(selectSql);
		List dataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
		String outputTreeStr = buildOutputTreeString(index,dataList, children, nodeId, parentNode, idNodeMap);
		return outputTreeStr;
			}

	/**
	 * This method builds a string from the input data , based on this string tree will be formed.
	 * @param dataList List of result records
	 * @param children List<IOutputTreeNode> child nodes 
	 * @param nodeId String id which will be parent id for the new nodes added to tree. 
	 * @param parentNode parent node 
	 * @param idNodeMap map which stores id and nodes already added to tree.
	 * @return String outputTreeStr which is then parsed and then sent to client to form tree. 
	 * String for one node is comma seperated for its id, display name, object name , parentId, parent Object name.
	 * Such string elements for child nodes are seperated by "|".
	 **/
	String buildOutputTreeString(int index,List dataList, List<IOutputTreeNode> children, String nodeId, IOutputTreeNode parentNode,
			Map<Long, IOutputTreeNode> idNodeMap)
	{
		Iterator dataListIterator = dataList.iterator();
		List<String> existingNodesList = new ArrayList<String>();
		List rowList = new ArrayList();
		String outputTreeStr = "";
		while (dataListIterator.hasNext())
		{
			rowList = (List) dataListIterator.next();
			for (IOutputTreeNode childNode : children)
			{
				String treeNodeId = (String) rowList.get(0);
				String fullyQualifiedEntityName = childNode.getOutputEntity().getDynamicExtensionsEntity().getName();
				int lastIndex = fullyQualifiedEntityName.lastIndexOf(".");
				String entityName = fullyQualifiedEntityName.substring(lastIndex + 1);
				String nodeIdToSet = nodeId + "::" + childNode.getId() + Constants.UNDERSCORE + treeNodeId;
				//	String nodeIdToSet = childNode.getId() + Constants.UNDERSCORE + treeNodeId;
				String displayName = entityName + "_" + treeNodeId;
				if(index != -1)
				{
					displayName = (String)rowList.get(1);
				}
				if(displayName.equalsIgnoreCase(""))
				{
					displayName = entityName + "_" + treeNodeId;
				}
				String objectname = fullyQualifiedEntityName;
				String parentIdToSet = nodeId;
				String parentObjectName = parentNode.getOutputEntity().getDynamicExtensionsEntity().getName();
				if (!existingNodesList.contains(nodeIdToSet))
				{
					existingNodesList.add(nodeIdToSet);
					idNodeMap.put(childNode.getId(), childNode);
					outputTreeStr = outputTreeStr + nodeIdToSet + "," + displayName + "," + objectname + "," + parentIdToSet + "," + parentObjectName
					+ "|";
				}
			}
		}
		return outputTreeStr;
	}
}