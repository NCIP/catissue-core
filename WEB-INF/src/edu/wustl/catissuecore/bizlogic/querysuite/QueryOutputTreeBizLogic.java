
package edu.wustl.catissuecore.bizlogic.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.querysuite.EntityManagerMock;
import edu.wustl.common.querysuite.QueryGeneratorMock;
import edu.wustl.common.querysuite.exceptions.DuplicateChildException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryengine.ISqlGenerator;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
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
	public String createOutputTree(IQuery query, SessionDataBean sessionData)
	{
		ISqlGenerator sqlGenerator = SqlGeneratorFactory.getInstance();
		String  selectSql = "";
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
		Map<Long, Map<AttributeInterface, String>>  nodeAttributeColumnNameMap = sqlGenerator.getColumnMap();
		String tableName = createOutputTreeTable(query);
		Vector treeDataVector = createOutputTreeDataVector(tableName, query, sessionData,nodeAttributeColumnNameMap);
		System.out.println(treeDataVector);
		return selectSql;
	}

	/**
	 * 
	 * @param tableName
	 * @param query
	 * @param sessionData
	 * @param nodeAttributeColumnNameMap
	 * @return
	 */
	Vector<QueryTreeNodeData> createOutputTreeDataVector(String tableName, IQuery query, SessionDataBean sessionData,Map<Long, Map<AttributeInterface, String>>  nodeAttributeColumnNameMap)
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
		Iterator dataListIterator = dataList.iterator();
		List rowList = new ArrayList();
		IOutputTreeNode root = query.getRootOutputClass();
		List<IOutputTreeNode> allChildNodes = new ArrayList<IOutputTreeNode>();
		List<IOutputTreeNode> childNodes = getAllChildNodes(root, allChildNodes);
		while (dataListIterator.hasNext())
		{
			rowList = (List) dataListIterator.next();
			treeDataVector = addNodeToTree(rowList,childNodes,treeDataVector,nodeAttributeColumnNameMap);
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
            			Iterator iterTreeData = treeDataVector.iterator();
            			while(iterTreeData.hasNext())
            			{
            				treeNode = (QueryTreeNodeData)iterTreeData.next();
            				if(treeNode.getIdentifier().equals(treeNodeId))
            				{
            					isNodeAlreadyPresentInTree = true;
            				}
            			}
            			if(!isNodeAlreadyPresentInTree)
            			{
            				treeNode = new QueryTreeNodeData();
            				treeNode.setIdentifier(treeNodeId);
            				treeNode.setObjectName(node.toString());
            				treeNode.setDisplayName(node.toString());
            				IOutputTreeNode parentNode = node.getParent();
            				if(parentNode != null)
            				{
            					idColumnName = nodeColumnIdMap.get(parentNode);
            					treeNodeId = (String)rowList.get(rowList.indexOf(idColumnName));
            					treeNode.setParentIdentifier(treeNodeId);
            					treeNode.setParentObjectName(parentNode.toString());
            				}
            				else
            				{
            					treeNode.setParentIdentifier(null);
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
		String  selectSql = "";
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
	 * Gets dummy tree. And returns the parent node.
	 * @return IOutputTreeNode
	 */
	IOutputTreeNode getDummyTreeNodes()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		IOutputTreeNode participantNode = null;
		try
		{
			EntityInterface participantEntity = entityManager.getEntityByName(EntityManagerMock.PARTICIPANT_NAME);
			participantNode = QueryObjectFactory.createOutputTreeNode(QueryGeneratorMock.createParticipantOutputEntity(participantEntity));
			AssociationInterface participanCPRegAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
					EntityManagerMock.PARTICIPANT_NAME, "participant"), EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
			IIntraModelAssociation iParticipanCPRegAssociation = QueryObjectFactory.createIntraModelAssociation(participanCPRegAssociation);
			EntityInterface cprEntity = entityManager.getEntityByName(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME);
			IOutputTreeNode cprNode = participantNode.addChild(iParticipanCPRegAssociation, QueryGeneratorMock
					.createCollProtoRegOutputEntity(cprEntity));
			AssociationInterface cprAndSpgAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
					EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME, "collectionProtocolRegistration"),
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME);
			IIntraModelAssociation iCprAndSpgAssociation = QueryObjectFactory.createIntraModelAssociation(cprAndSpgAssociation);
			EntityInterface specimenEntity = entityManager.getEntityByName(EntityManagerMock.SPECIMEN_NAME);
			EntityInterface scgEntity = entityManager.getEntityByName(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME);

			IOutputTreeNode scgNode = cprNode.addChild(iCprAndSpgAssociation, QueryGeneratorMock.createScgOutputEntity(scgEntity));
			AssociationInterface spgAndSpecimeAssociation = QueryGeneratorMock.getAssociationFrom(entityManager.getAssociation(
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME, "specimenCollectionGroup"), EntityManagerMock.SPECIMEN_NAME);
			IIntraModelAssociation iSpgAndSpecimeAssociation = QueryObjectFactory.createIntraModelAssociation(spgAndSpecimeAssociation);
			scgNode.addChild(iSpgAndSpecimeAssociation, QueryGeneratorMock.createSpecimenOutputEntity(specimenEntity));
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
		}
		catch (DuplicateChildException e)
		{
			e.printStackTrace();
		}
		return participantNode;
	}

	/**
	 * Creates a new DynExt entity, the query obj passed to it has a node associated with it.
	 * All the attributes of root entity and all of its child nodes' attributes are added to this newly created entity.
	 * 
	 * @param query IQuery , this object has tree associated with it
	 * @return EntityInterface DynExt entity
	 */
	private EntityInterface createDynExtEntity(IQuery query, String tableName, Map attributeColumnNameMap)
	{
		ISqlGenerator sqlGenerator = SqlGeneratorFactory.getInstance();
		try
		{
			String  str = sqlGenerator.generateSQL(query);
		}
		catch (MultipleRootsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SqlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IOutputTreeNode root =query.getRootOutputClass();
		Map<Long, Map<AttributeInterface, String>>  map = sqlGenerator.getColumnMap();
		EntityInterface entity = DomainObjectFactory.getInstance().createEntity();
		List<IOutputTreeNode> allChildNodes = new ArrayList<IOutputTreeNode>();
		List<IOutputTreeNode> childNodes = getAllChildNodes(root, allChildNodes);

		for (IOutputTreeNode node : childNodes)
		{
			entity = addAttributesToEntity(node, entity, tableName, attributeColumnNameMap);
		}
		return entity;
	}

	/**
	 * This method takes a map as an input , key : Attribute AND value : columnName.
	 * One by one these attributes are cloned and added to entity along with their tableproperties and column properties are also set. 
	 * @param entity EntityInterface obj to which the attributes are added
	 * @param tableName this name is then set as tableproperties for this entity
	 * @param attributeColumnNameMap Map of attribute and its columnname.
	 * @return EntityInterface the entity with all the attributes and their properties added with it. 
	 */
	EntityInterface addAttributesToEntity(IOutputTreeNode node, EntityInterface entity, String tableName,
			Map<AttributeInterface, String> attributeColumnNameMap)
	{
		TablePropertiesInterface tableProperty = new TableProperties();
		tableProperty.setName(tableName);
		entity.setTableProperties(tableProperty);
		Iterator iter = attributeColumnNameMap.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry<AttributeInterface, String> entry = (Map.Entry) iter.next();
			AttributeInterface attribute = entry.getKey();
			String columnName = entry.getValue();
			if (attribute.getName().equalsIgnoreCase(Constants.ID))
			{
				nodeColumnIdMap.put(node.getId(), columnName);
			}

			AttributeInterface attrCopy = (AttributeInterface) QueryObjectProcessor.getObjectCopy(attribute);
			ColumnPropertiesInterface columnProperty = new ColumnProperties();
			columnProperty.setName(columnName);
			attrCopy.setColumnProperties(columnProperty);
			entity.addAttribute(attrCopy);
		}
		return entity;
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