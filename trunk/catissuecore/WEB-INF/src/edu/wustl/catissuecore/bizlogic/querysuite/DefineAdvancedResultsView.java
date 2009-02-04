
package edu.wustl.catissuecore.bizlogic.querysuite;

/**
 * This is the Biz logic class which has the API's to get the trees associated
 * with the categories/entity.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

/**
 * 
 * @author asawari_pawar
 *
 *This is the Biz logic class which has the API's to get the trees associated
 *with the categories/entity
 */
public class DefineAdvancedResultsView
{
	/**
	 * constructor for DefineAdvancedResultsView
	 *
	 */
	public DefineAdvancedResultsView()
	{
		super();
	}

	/**
	 * Returns the map of trees for each category user has selected.
	 * @param ListOfEntitiesInQuery
	 * @return Map treesMap<String, Vector<?>>
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Map<String, Vector<QueryTreeNodeData>> getTreeForThisCategory(List<EntityInterface> ListOfEntitiesInQuery)
	throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map<String, Vector<QueryTreeNodeData>> treesMap = new HashMap<String, Vector<QueryTreeNodeData>>();
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		//ListOfEntitiesInQuery = createParticipantEntity();
		for (EntityInterface searchedEntity : ListOfEntitiesInQuery)
		{
			Long searchedEntityId = searchedEntity.getId();
			String tableName = "CATISSUE_CATEGORY_TREES";
			String[] selectColumnNames = {"ENTITY_ID", "TREE_DISPLAY_NAME"};
			String[] whereColumnNames = {"SEARCH_CATEGORY_ID"};
			String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {searchedEntityId};
			List<List<String>> entitiesList = getEntitiesListFromDatabase(tableName, selectColumnNames, whereColumnNames, whereColumnCondition,
					whereColumnValue);
			for (List<String> objList : entitiesList)
			{
				String id = (String) objList.get(0);
				EntityInterface entityInterface = entityManagerInterface.getEntityByIdentifier(id);
				Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();
				treeData = generateTreeData(null, entityInterface, treeData);
				String treeName = (String) objList.get(1);
				treesMap.put(treeName, treeData);
			}
		}
		return treesMap;
	}

	/**
	 * 
	 * @param parentEntityInterface
	 * @param entityInterface
	 * @param treeData
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private Vector<QueryTreeNodeData> generateTreeData(EntityInterface parentEntityInterface, EntityInterface entityInterface,
			Vector<QueryTreeNodeData> treeData) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
			{
		QueryTreeNodeData node = new QueryTreeNodeData();
		node.setIdentifier(entityInterface.getId().toString());
		node.setObjectName(entityInterface.getName());
		int lastIndex = entityInterface.getName().lastIndexOf(".");
		String entityName = entityInterface.getName().substring(lastIndex + 1);
		node.setDisplayName(entityName);
		if (parentEntityInterface == null)
		{
			node.setParentIdentifier("parentId");
			node.setParentObjectName("");
		}
		else
		{
			node.setParentIdentifier(parentEntityInterface.getId().toString());
			node.setParentObjectName(parentEntityInterface.getName());
		}
		treeData.add(node);
		if (entityInterface.getAssociationCollection() != null && entityInterface.getAssociationCollection().isEmpty())
		{
			return treeData;
		}
		else
		{
			Iterator iterAssociations = entityInterface.getAssociationCollection().iterator();
			while (iterAssociations.hasNext())
			{
				AssociationInterface assInterface = (AssociationInterface) iterAssociations.next();
				EntityInterface parentEntity = assInterface.getEntity();
				EntityInterface childEntity = assInterface.getTargetEntity();
				if (parentEntityInterface != null)
				{
					if (childEntity.getId().equals(parentEntityInterface.getId()))
					{
						continue;
					}
				}
				treeData = generateTreeData(parentEntity, childEntity, treeData);
			}
		}
		return treeData;
			}
	/**
	 * This method gets the Entities List From Database according to the parameters passed to it.
	 * @param tableName name of the table
	 * @param selectColumnNames select column names list 
	 * @param whereColumnNames whereColumnNames list
	 * @param whereColumnCondition whereColumnCondition list 
	 * @param whereColumnValue whereColumnValue list
	 * @return List EntitiesListFromDatabase
	 */
	private List<List<String>> getEntitiesListFromDatabase(String tableName, String[] selectColumnNames, String[] whereColumnNames,
			String[] whereColumnCondition, Object[] whereColumnValue)
			{
		List entityList = new ArrayList();
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDao.openSession(null);
			entityList = jdbcDao.retrieve(tableName, selectColumnNames, whereColumnNames, whereColumnCondition, whereColumnValue, null);
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		return (List<List<String>>) entityList;
			}
	/**
	 * Creates ParticipantEntity : DUmmy data
	 * @return 
	 */
	private List<EntityInterface> createParticipantEntity()
	{
		List<EntityInterface> eList = new ArrayList<EntityInterface>();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		EntityInterface e = factory.createEntity();
		e.setName("edu.wustl.catissuecore.domain.Participant");
		e.setDescription("This is a participant entity");
		e.setId(new Long(2));
		eList.add(e);
		EntityInterface e1 = factory.createEntity();
		e1.setName("edu.wustl.catissuecore.domain.SpecimenCollectionGroup");
		e1.setId(new Long(14));
		e1.setDescription("This is a specimen collection group entity");
		//eList.add(e1);
		return eList;
	}
	/**
	 * 
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	/*
	 private List<EntityInterface>createPreDefinedEntities() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 {
	 EntityManagerInterface entityManager = EntityManager.getInstance();
	 List<EntityInterface> preDefinedEntities = new ArrayList<EntityInterface>();
	 DomainObjectFactory factory = DomainObjectFactory.getInstance();
	 EntityInterface user = factory.createEntity();		
	 user.setName("Query_User");
	 EntityInterface study = factory.createEntity();
	 study.setName("Query_Study");
	 AssociationInterface association = factory.createAssociation();
	 association.setTargetEntity(study);
	 association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
	 association.setName("primaryInvestigator");
	 association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator",Cardinality.ONE, Cardinality.ONE));
	 association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO,Cardinality.MANY));
	 user.addAbstractAttribute(association);	
	 EntityInterface experiment = factory.createEntity();
	 experiment.setName("Query_Experiment");		
	 entityManager.persistEntity(experiment);
	 AssociationInterface association1 = factory.createAssociation();
	 association1.setTargetEntity(experiment);
	 association1.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
	 association1.setName("primaryInvestigator1");
	 association1.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator1",	Cardinality.ONE, Cardinality.ONE));
	 association1.setTargetRole(getRole(AssociationType.ASSOCIATION, "experiment", Cardinality.ZERO,Cardinality.MANY));
	 user.addAbstractAttribute(association1);
	 entityManager.persistEntity(user);
	 EntityInterface study_child = factory.createEntity();
	 study_child.setName("Query_Study_child");
	 entityManager.persistEntity(study_child);
	 AssociationInterface association2 = factory.createAssociation();
	 association2.setTargetEntity(study_child);
	 association2.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
	 association2.setName("primaryInvestigator2");
	 association2.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator2",Cardinality.ONE, Cardinality.ONE));
	 association2.setTargetRole(getRole(AssociationType.ASSOCIATION, "study_child", Cardinality.ZERO,Cardinality.MANY));
	 study.addAbstractAttribute(association2);	
	 entityManager.persistEntity(study);
	 preDefinedEntities.add(user);
	 preDefinedEntities.add(study);
	 preDefinedEntities.add(experiment);
	 preDefinedEntities.add(study_child);
	 return preDefinedEntities;
	 }
	 *//**
	 * @param associationType associationType
	 * @param name name
	 * @param minCard  minCard
	 * @param maxCard maxCard
	 * @return  RoleInterface
	 */
	/*
	 private RoleInterface getRole(AssociationType associationType, String name,
	 Cardinality minCard, Cardinality maxCard)
	 {
	 RoleInterface role = DomainObjectFactory.getInstance().createRole();
	 role.setAssociationsType(associationType);
	 role.setName(name);
	 role.setMinimumCardinality(minCard);
	 role.setMaximumCardinality(maxCard);
	 return role;
	 }
	 */
}
