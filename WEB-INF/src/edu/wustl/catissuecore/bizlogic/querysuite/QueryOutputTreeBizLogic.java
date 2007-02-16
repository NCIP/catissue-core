
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * Creates QueryOutputTree Object as per the data filled by the user on AddLimits section.
 * Creates QueryOutputTree Table.
 * @author deepti_shelar
 *
 */
public class QueryOutputTreeBizLogic
{
	/**
	 * This map stores all nodes added to tree with their ids as key of the map.
	 */
	Map<Long, IOutputTreeNode> idNodesMap = new HashMap<Long, IOutputTreeNode>();

	/**
	 * Creates new table which has the same structure and also same data , as the output tree structurs has.  
	 * @param String selectSql , from this sql , new table will be created .
	 * @param sessionData to be added to tablename to have unique table for each session user.
	 * @param String tableName 
	 */
	public String createOutputTreeTable(String selectSql, SessionDataBean sessionData)
	{
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String createTableSql = Constants.CREATE_TABLE + tableName + " " + Constants.AS + " " + selectSql;
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDao.openSession(null);
			jdbcDao.delete(tableName);
			jdbcDao.executeUpdate(createTableSql);
			jdbcDao.commit();
			jdbcDao.closeSession();
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return tableName;
	}
	/**
	 * 
	 * @param node
	 * @param columnMap
	 * @return
	 */
	String getColumnNamesForSelectpart(IOutputTreeNode node,Map<Long, Map<AttributeInterface, String>> columnMap)
	{
		String columnNames = "";
		String idColumnName = null;
		Map<AttributeInterface, String> columns = columnMap.get(node.getId());
		if(columns != null)
		{
			Set<AttributeInterface> set1 = columns.keySet();
			for (Iterator<AttributeInterface> iterator = set1.iterator(); iterator.hasNext();)
			{
				AttributeInterface attr = iterator.next();
				if (attr.getName().equalsIgnoreCase(Constants.ID))
				{
					idColumnName = columns.get(attr);
				}
				else
				{
					columnNames = columnNames + columns.get(attr) + " , ";
				}
			}
		}
		columnNames = idColumnName + " , " + columnNames;
		columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		return columnNames; 
		
	}
	/**
	 * This method creates first level(Default) output tree data.
	 * @param tableName name fo the table created
	 * @param query IQuery obj created out of user inputs to dag view.
	 * @param sessionData session data to get the user id.
	 * @param nodeAttributeColumnNameMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return Vector<QueryTreeNodeData> data structure to form tree out of it.
	 */
	public Vector<QueryTreeNodeData> createDefaultOutputTreeData(String tableName, IQuery query, SessionDataBean sessionData,
			Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap)
			{
		IOutputTreeNode root = query.getRootOutputClass();
		String columnNames = getColumnNamesForSelectpart(root, nodeAttributeColumnNameMap);
		String selectSql = "select "+columnNames+" from "+tableName;
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List dataList = null;
		try
		{
			jdbcDao.openSession(sessionData);
			dataList = jdbcDao.executeQuery(selectSql,sessionData, false, false, null);
			jdbcDao.closeSession();
		}
		catch (DAOException excp)
		{
			Logger.out.error(excp.getMessage(), excp);
			excp.printStackTrace();
		}
		catch (ClassNotFoundException excp)
		{
			Logger.out.error(excp.getMessage(), excp);
			excp.printStackTrace();
		}
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
			treeDataVector = addNodeToTree(dataList, treeNode, root, treeDataVector, nodeAttributeColumnNameMap);
		}
		return treeDataVector;
			}

	/**
	 * This method adds the node to tree.
	 * @param dataList all records in database satisfying the criteria.
	 * @param parentNode parent node of tree data
	 * @param node the node to be added of IOutputTreeNode.
	 * @param treeDataVector  data structure to form tree out of it.
	 * @param nodeAttributeColumnNameMap map which strores all node ids  with their information like attributes and actual column names in database.
	 * @return treeDataVector  data structure to form tree out of it.
	 */
	Vector<QueryTreeNodeData> addNodeToTree(List dataList, QueryTreeNodeData parentNode, IOutputTreeNode node,
			Vector<QueryTreeNodeData> treeDataVector, Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap)
			{
		Iterator dataListIterator = dataList.iterator();
		List rowList = new ArrayList();
		while (dataListIterator.hasNext())
		{
			rowList = (List) dataListIterator.next();
			idNodesMap.put(node.getId(), node);
			boolean isNodeAlreadyPresentInTree = false;
			QueryTreeNodeData treeNode = null;
			String treeNodeId = (String) rowList.get(0);
			String nodeId = node.getId() + Constants.UNDERSCORE + treeNodeId;
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
				String fullyQualifiedEntityName = node.getOutputEntity().getDynamicExtensionsEntity().getName();
				int lastIndex = fullyQualifiedEntityName.lastIndexOf(".");
				String entityName = fullyQualifiedEntityName.substring(lastIndex + 1);
				treeNode.setObjectName(fullyQualifiedEntityName);
				String displayName = entityName + "_" + treeNodeId;
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
	 */
	public String buildTreeForNode(String nodeId, Map<Long, IOutputTreeNode> idNodeMap, Map<Long, Map<AttributeInterface, String>> columnMap,
			SessionDataBean sessionData)
	{
		String[] nodeIds = nodeId.split(Constants.UNDERSCORE);
		Long id = new Long(nodeIds[0]);
		String parentIdColumnName = null;
		IOutputTreeNode parentNode = idNodeMap.get(id);
		Map<AttributeInterface, String> columns1 = columnMap.get(parentNode.getId());
		Set<AttributeInterface> set = columns1.keySet();
		for (Iterator<AttributeInterface> iterator = set.iterator(); iterator.hasNext();)
		{
			AttributeInterface attr = iterator.next();
			if (attr.getName().equalsIgnoreCase(Constants.ID))
			{
				parentIdColumnName = columns1.get(attr);
				break;
			}
		}
		List<IOutputTreeNode> children = getChildNodes(parentNode);
		if(children.isEmpty())
		{
			return "";
		}
		String columnNames = "";
		String idColumnName = null;
		Map<IOutputTreeNode, String> nodeIndexMap = new HashMap<IOutputTreeNode, String>();
		for (IOutputTreeNode childNode : children)
		{
			Map<AttributeInterface, String> columns = columnMap.get(childNode.getId());
			if(columns != null)
			{
				Set<AttributeInterface> set1 = columns.keySet();
				for (Iterator<AttributeInterface> iterator = set1.iterator(); iterator.hasNext();)
				{
					AttributeInterface attr = iterator.next();
					if (attr.getName().equalsIgnoreCase(Constants.ID))
					{
						idColumnName = columns.get(attr);
					}
					else
					{
						columnNames = columnNames + columns.get(attr) + " , ";
					}
				}
			}
			nodeIndexMap.put(childNode, idColumnName);
		}
		columnNames = idColumnName + " , " + columnNames;
		columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String selectSql = "select " + columnNames;
		selectSql = selectSql + " from " + tableName + " where " + parentIdColumnName + " = '" + nodeIds[1] + "'";
		List dataList = executeQuery(selectSql, sessionData);
		String outputTreeStr = buildOutputTreeString(dataList, children, nodeId, parentNode, idNodeMap);
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
	 */
	String buildOutputTreeString(List dataList, List<IOutputTreeNode> children, String nodeId, IOutputTreeNode parentNode,
			Map<Long, IOutputTreeNode> idNodeMap)
	{
		Iterator dataListIterator = dataList.iterator();
		List<String> existingNodesList = new ArrayList<String>();
		List rowList = new ArrayList();
		QueryTreeNodeData treeNode = null;
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
				String nodeIdToSet = childNode.getId() + Constants.UNDERSCORE + treeNodeId;
				String displayName = entityName + "_" + treeNodeId;
				String objectname = fullyQualifiedEntityName;
				String parentIdToSet = nodeId;
				String parentObjectName = parentNode.getOutputEntity().getDynamicExtensionsEntity().getName();
				if (!existingNodesList.contains(nodeIdToSet))
				{
					existingNodesList.add(nodeIdToSet);
					treeNode = new QueryTreeNodeData();
					treeNode.setIdentifier(nodeIdToSet);
					treeNode.setObjectName(objectname);
					treeNode.setDisplayName(displayName);
					treeNode.setParentIdentifier(parentIdToSet);
					treeNode.setParentObjectName(parentObjectName);
					idNodeMap.put(childNode.getId(), childNode);
					outputTreeStr = outputTreeStr + nodeIdToSet + "," + displayName + "," + fullyQualifiedEntityName + "," + parentIdToSet + "," + parentObjectName
					+ "|";
				}
			}
		}
		return outputTreeStr;
	}

	/**
	 * Executes the query and returns the results back.
	 * @param selectSql sql to be executed
	 * @param sessionData sessiondata
	 * @return list of results 
	 */
	public List executeQuery(String selectSql, SessionDataBean sessionData)
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List dataList = new ArrayList();
		try
		{
			dao.openSession(sessionData);
			dataList = dao.executeQuery(selectSql, sessionData, false, false, null);
			dao.commit();
			dao.closeSession();
		}
		catch (ClassNotFoundException e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (DAOException e)
		{
			Logger.out.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * Returns all immediate child nodes for the root node passed to it.
	 * @param node IOutputTreeNode ,
	 * @param allChildNodes list of nodes
	 * @return List of IOutputTreeNode all child nodes
	 */
	public List<IOutputTreeNode> getChildNodes(IOutputTreeNode node)
	{
		List<IOutputTreeNode> childNodes = new ArrayList<IOutputTreeNode>();
		idNodesMap.put(node.getId(), node);
		if (!node.isLeaf())
		{
			childNodes = (node.getChildren());
		}
		return childNodes;
	}

	/**
	 * returns the nodes and their id's map which are already added to tree.
	 * @return Map<Long, IOutputTreeNode> idNodesMap
	 */
	public Map<Long, IOutputTreeNode> getIdNodesMap()
	{
		return idNodesMap;
	}
}