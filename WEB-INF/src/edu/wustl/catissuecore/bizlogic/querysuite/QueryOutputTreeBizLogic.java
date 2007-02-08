
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
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryengine.ISqlGenerator;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * Creates QueryOutputTree Object as per the data filled by the user on AddLimits section.
 * Creates QueryOutputTree Table.
 * Creates Dyn Ext entity which represents the newly created table for out put tree.
 * @author deepti_shelar
 *
 */
public class QueryOutputTreeBizLogic
{
	Map<Long, String> nodeColumnIdMap = new HashMap<Long, String>();

	/**
	 * This method is called from ViewSearchResultsAction. It first creates a temp table for output tree.
	 * Then it creates a dynamic extensions entity which represents the newly created table.
	 * @param query IQuery , this object has tree associated with it
	 */
	public Vector createOutputTree(IQuery query, SessionDataBean sessionData)
	{
		ISqlGenerator sqlGenerator = SqlGeneratorFactory.getInstance();
		String selectSql = "";
		try
		{
			selectSql = sqlGenerator.generateSQL(query);
			System.out.println(selectSql);
		}
		catch (MultipleRootsException e)
		{
			e.printStackTrace();
		}
		catch (SqlException e)
		{
			e.printStackTrace();
		}
		Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap = sqlGenerator.getColumnMap();
		String tableName = createOutputTreeTable(query);
		Vector treeDataVector = createOutputTreeDataVector(tableName, query, sessionData, nodeAttributeColumnNameMap);
		System.out.println(treeDataVector);
		return treeDataVector;
	}

	/**
	 * 
	 * @param tableName
	 * @param query
	 * @param sessionData
	 * @param nodeAttributeColumnNameMap
	 * @return
	 */
	Vector<QueryTreeNodeData> createOutputTreeDataVector(String tableName, IQuery query, SessionDataBean sessionData,
			Map<Long, Map<AttributeInterface, String>> nodeAttributeColumnNameMap)
			{
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		List dataList = null;
		try
		{
			jdbcDao.openSession(sessionData);
			dataList = jdbcDao.retrieve(tableName);
		}
		catch (DAOException excp)
		{
			Logger.out.error(excp.getMessage(), excp);
			excp.printStackTrace();
		}
		finally
		{
			try
			{
				jdbcDao.closeSession();
			}
			catch (DAOException e)
			{
				e.printStackTrace();
			}
		}
		Vector<QueryTreeNodeData> treeDataVector = new Vector<QueryTreeNodeData>();
		if(dataList != null)
		{
			Iterator dataListIterator = dataList.iterator();
			List rowList = new ArrayList();
			IOutputTreeNode root = query.getRootOutputClass();
			List<IOutputTreeNode> allChildNodes = new ArrayList<IOutputTreeNode>();
			List<IOutputTreeNode> childNodes = getAllChildNodes(root, allChildNodes);
			while (dataListIterator.hasNext())
			{
				rowList = (List) dataListIterator.next();
				treeDataVector = addNodeToTree(rowList, childNodes, treeDataVector, nodeAttributeColumnNameMap);
			}
		}
		return treeDataVector;
			}

	/**
	 * Adds the nodes one by one to the vector.
	 * @param rowList
	 * @param childNodes
	 * @param treeDataVector
	 */
	Vector<QueryTreeNodeData> addNodeToTree(List rowList,List<IOutputTreeNode> childNodes,Vector<QueryTreeNodeData>treeDataVector,
			Map<Long, Map<AttributeInterface, String>>  nodeAttributeColumnNameMap)
			{
		boolean isNodeAlreadyPresentInTree = false;
		QueryTreeNodeData treeNode = null;
		for (IOutputTreeNode node : childNodes)
		{
			Map<AttributeInterface, String> map = nodeAttributeColumnNameMap.get(node.getId());
			Set<AttributeInterface> set1 = map.keySet();
			for (Iterator<AttributeInterface> iterator = set1.iterator(); iterator.hasNext();)
			{
				AttributeInterface attr = iterator.next();
				if(attr.getName().equalsIgnoreCase(Constants.ID))
				{
					String idColumnName = map.get(attr);
					int index = Integer.parseInt(idColumnName.substring("Column".length(),idColumnName.length()));
					System.out.println(index);
					String treeNodeId = (String)rowList.get(index);
					String nodeId = node.getId()+"_"+treeNodeId;
					Iterator iterTreeData = treeDataVector.iterator();
					while(iterTreeData.hasNext())
					{
						treeNode = (QueryTreeNodeData)iterTreeData.next();
						if(treeNode.getIdentifier().equals(nodeId))
						{
							String nodeParentId = "";
							if(node.getParent() != null)
							{
								nodeParentId = node.getParent().getId()+"_"+treeNodeId;
							}
							String treeNodeParentId = "";
							if(treeNode.getParentTreeNode().getIdentifier() != null)
							{
								treeNodeParentId = 	treeNode.getParentTreeNode().getIdentifier().toString();
							}
							if(nodeParentId.equalsIgnoreCase(treeNodeParentId))
							{
								isNodeAlreadyPresentInTree = true;
							}
						}
					}
					if(!isNodeAlreadyPresentInTree)
					{
						treeNode = new QueryTreeNodeData();
						treeNode.setIdentifier(nodeId);
						treeNode.setObjectName(node.toString());
						String fullyQualifiedEntityName = node.getOutputEntity().getDynamicExtensionsEntity().getName();
						int lastIndex = fullyQualifiedEntityName.lastIndexOf(".");
						String entityName = fullyQualifiedEntityName.substring(lastIndex + 1);
						String displayName = entityName +"_"+treeNodeId;
						treeNode.setDisplayName(displayName);
						IOutputTreeNode parentNode = node.getParent();
						if(parentNode != null)
						{
							Map<AttributeInterface, String> map1 = nodeAttributeColumnNameMap.get(parentNode.getId());
							Set<AttributeInterface> set2 = map1.keySet();
							for (Iterator<AttributeInterface> iterator1 = set2.iterator(); iterator1.hasNext();)
							{
								AttributeInterface attr1 = iterator1.next();
								if(attr1.getName().equalsIgnoreCase(Constants.ID))
								{
									idColumnName = map1.get(attr1);
									index = Integer.parseInt(idColumnName.substring("Column".length(),idColumnName.length()));
									System.out.println(index);
									treeNodeId = (String)rowList.get(index);
									String parentNodeId = parentNode.getId()+"_"+treeNodeId;
									treeNode.setParentIdentifier(parentNodeId);
									treeNode.setParentObjectName(parentNode.toString());
								}
							}
						}
						else
						{
							treeNode.setParentIdentifier("0");
							treeNode.setParentObjectName("");
						}
						treeDataVector.add(treeNode);
					}
				}
				System.out.println(attr.getName()+":"+map.get(attr));
			}

		}
		return treeDataVector;
			}

	/**
	 * Creates new table which has the same structure and also same data , as the output tree structurs has.  
	 * @param query IQuery , this object has tree associated with it
	 */
	String createOutputTreeTable(IQuery query)
	{
		ISqlGenerator sqlGenerator = SqlGeneratorFactory.getInstance();
		String selectSql = "";
		try
		{
			selectSql = sqlGenerator.generateSQL(query);
			System.out.println(selectSql);
		}
		catch (MultipleRootsException e)
		{
			e.printStackTrace();
		}
		catch (SqlException e)
		{
			e.printStackTrace();
		}
		//String selectSql = "Select Participant_1.IDENTIFIER, Participant_1.MIDDLE_NAME, Participant_1.FIRST_NAME,  Participant_1.GENDER, Participant_1.LAST_NAME ,  cpr_1.REGISTRATION_DATE From CATISSUE_PARTICIPANT Participant_1 ,catissue_coll_prot_reg cpr_1";
		String tableName = "temp_OutputTree";
		String createTableSql = "Create table " + tableName + " As " + selectSql;
		Logger.out.debug("sql for create table " + createTableSql);
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
	 * Returns all child nodes for the root node passed to it.This is a recursive method.
	 * @param node IOutputTreeNode ,
	 * @param allChildNodes list of nodes
	 * @return List of IOutputTreeNode all child nodes
	 */
	List<IOutputTreeNode> getAllChildNodes(IOutputTreeNode node, List<IOutputTreeNode> all)
	{
		List<IOutputTreeNode> childNodes = new ArrayList<IOutputTreeNode>();
		all.add(node);
		if (!node.isLeaf())
		{
			childNodes = (node.getChildren());
			for (IOutputTreeNode childNode : childNodes)
			{
				getAllChildNodes(childNode, all);
			}
		}
		return all;
	}

}