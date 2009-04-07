
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

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.tree.QueryTreeNodeData;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.DAOFactory;
import edu.wustl.dao.exception.DAOException;

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
			
			QueryWhereClause queryWhereClause = new QueryWhereClause(tableName);
			queryWhereClause.addCondition(new EqualClause("SEARCH_CATEGORY_ID",searchedEntityId));
			
			
			List<List<String>> entitiesList = getEntitiesListFromDatabase(tableName, selectColumnNames,queryWhereClause);
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
	private List<List<String>> getEntitiesListFromDatabase(String tableName, String[] selectColumnNames,QueryWhereClause queryWhereClause)
	{
		List entityList = new ArrayList();
		String applicationName = CommonServiceLocator.getInstance().getAppName();
		JDBCDAO  jdbcDAO = null;
		try
		{
			jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
			jdbcDAO.openSession(null);
	
			entityList = jdbcDAO.retrieve(tableName, selectColumnNames, queryWhereClause);
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				jdbcDAO.closeSession();
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (List<List<String>>) entityList;
	}
}
