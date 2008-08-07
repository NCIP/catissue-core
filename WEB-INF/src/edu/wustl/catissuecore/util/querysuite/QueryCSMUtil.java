/**
 * 
 */
package edu.wustl.catissuecore.util.querysuite;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryCsmBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.util.global.ApplicationProperties;


/**
 * @author supriya_dankh
 * This class contains all the methods required for CSM integration in query.
 */
public abstract class QueryCSMUtil
{
	
	/**This method will check if main objects for all the dependent objects are present in query or not.
	 * If yes then will create map of entity as key and main entity list as value.
	 * If not then will set error message in session.
	 * @param query
	 * @param session
	 * @param uniqueIdNodesMap
	 * @return
	 */
	public static Map<EntityInterface, List<EntityInterface>> setMainObjectErrorMessage(
			IQuery query, HttpSession session, Map<String, OutputTreeDataNode> uniqueIdNodesMap)
	{
		int status;
		Map<EntityInterface ,List<EntityInterface>> mainEntityMap = getMainEntitiesForAllQueryNodes(uniqueIdNodesMap);
		List<Long> queryDeEntities = getAllDEEntities(query);
		String errorMessg = getErrorMessage(mainEntityMap, uniqueIdNodesMap);
		if(!errorMessg.equals(""))
		{
			session.setAttribute(Constants.NO_MAIN_OBJECT_IN_QUERY, errorMessg);
			return null;
		}
		else
			session.setAttribute(Constants.MAIN_ENTITY_MAP, mainEntityMap);
		return mainEntityMap;
	}
	
	
	
	/**
	 * This method will return error message for main object.
	 * @param mainEntityMap main entity map.
	 * @param uniqueIdNodesMap contains all the entities in the query 
	 * @return error message if main entity of node not present.
	 */
	private static String getErrorMessage(Map<EntityInterface, List<EntityInterface>> mainEntityMap, 
			Map<String, OutputTreeDataNode> uniqueIdNodesMap)
	{ 
		String errorMsg = ""; 
		//iterate through the uniqueIdNodesMap and check if main entities of all the nodes are present
		for (Iterator idMapIterator = uniqueIdNodesMap.entrySet().iterator(); idMapIterator.hasNext();)
		{	
			Map.Entry<String, OutputTreeDataNode> IdmapValue = (Map.Entry<String, OutputTreeDataNode>) idMapIterator.next();
			// get the node
			OutputTreeDataNode node = IdmapValue.getValue();
			//get the entity
			EntityInterface mapEntity = node.getOutputEntity().getDynamicExtensionsEntity();
			// get the main entities list for the entity
			List<EntityInterface> mainEntityList = (List<EntityInterface>)mainEntityMap.get(mapEntity);
			//mainEntityList is null if the entity itself is main entity;
			EntityInterface mainEntity = null;
			if (mainEntityList != null)
			{
				//check if main entity of the dependent entity is present in the query
				mainEntity = getMainEntity(mainEntityList, node);
				if (mainEntity == null) // if main entity is not present
				{
					//set the error message 
					String mainEntityNames = "";
					for (EntityInterface entity : mainEntityList)
					{
						//get the names of all the main entities for the dependent entity
						String name = entity.getName();
						name = name.substring(name.lastIndexOf(".")+1, name.length()); //TODO: use Utility Method for getting className
						mainEntityNames = mainEntityNames + name + " or ";
					}
					mainEntityNames = mainEntityNames.substring(0, mainEntityNames.lastIndexOf("r")-1);
					String message = ApplicationProperties.getValue("query.mainObjectError");
					String entityName = mapEntity.getName();
					entityName = entityName.substring(entityName.lastIndexOf(".")+1, entityName.length());//TODO: use Utility Method for getting className
					Object[] arguments = new Object[]{entityName, mainEntityNames};
					errorMsg = MessageFormat.format(message, arguments);
					break;
				}
			}
		}
		return errorMsg;
	}

	
	/**
	 * @param query
	 * @return
	 */
	private static List<Long> getAllDEEntities(IQuery query)
	{
		Set<IQueryEntity> queryEntities = query.getConstraints().getQueryEntities();
		List<Long> queryDeEntities = new ArrayList<Long>();
		for(IQueryEntity queryEntity : queryEntities)
		{
			queryDeEntities.add(queryEntity.getDynamicExtensionsEntity().getId());
		}
		return queryDeEntities;
	}

	/**
	 * This method will return map of a entity as value and list of all the main entities of this particular entity as value. 
	 * @param uniqueIdNodesMap Map that will all nodes present in query as node id as key and node as value. 
	 * @return mainEntityMap Map of all main entities present in query.
	 */
	private static Map<EntityInterface, List<EntityInterface>> getMainEntitiesForAllQueryNodes(
			Map<String, OutputTreeDataNode> uniqueIdNodesMap)
	{
		Map<EntityInterface, List<EntityInterface>> mainEntityMap = new HashMap<EntityInterface, List<EntityInterface>>();

		for (OutputTreeDataNode queryNode : uniqueIdNodesMap.values())
		{
			List<EntityInterface> mainEntityList = new ArrayList<EntityInterface>();
			EntityInterface dynamicExtensionsEntity = queryNode.getOutputEntity()
					.getDynamicExtensionsEntity();
			mainEntityList = getAllMainEntities(dynamicExtensionsEntity, mainEntityList);
			EntityInterface tempDynamicExtensionsEntity = dynamicExtensionsEntity;
			List<EntityInterface> tempMainEntityList;
	
			while(true)
			{
				tempMainEntityList = new ArrayList<EntityInterface>();
				EntityInterface parentEntity = tempDynamicExtensionsEntity.getParentEntity();
				if(parentEntity == null)
					break;
				else
				{
					tempMainEntityList = getAllMainEntities(parentEntity, tempMainEntityList);
					for(EntityInterface tempMainEntity : tempMainEntityList)
					{
						if(!(tempMainEntity.equals(parentEntity)))
						{
							mainEntityList.add(tempMainEntity);
						}
					}
					tempDynamicExtensionsEntity = parentEntity;
				}
			}
			if(mainEntityList.size() != 1)
			{
				List<EntityInterface> temperoryList = mainEntityList;
				mainEntityList = new ArrayList<EntityInterface>();
				for(EntityInterface temperoryEntity : temperoryList)
				{
					if(!(temperoryEntity.equals(dynamicExtensionsEntity)))
						mainEntityList.add(temperoryEntity);
				}
			}
			
			if(!(mainEntityList!=null && mainEntityList.size()==1 &&mainEntityList.get(0).equals(dynamicExtensionsEntity)))
			{
				tempMainEntityList = new ArrayList<EntityInterface>();
				for(EntityInterface mainEntity : mainEntityList)
				{
					if(mainEntity.isAbstract())
					{
						tempMainEntityList.addAll(QueryCsmBizLogic.getMainEntityList(mainEntity, dynamicExtensionsEntity));
					}
				}
				mainEntityList.addAll(tempMainEntityList);
			    mainEntityMap.put(dynamicExtensionsEntity, mainEntityList);
			}
		}
		return mainEntityMap;
	}

	/**This is a recursive method that will create list of all main entities (Entities for which entity passed to it is having containment association ) 
	 * @param entity
	 * @param mainEntityList
	 */
	private static List<EntityInterface> getAllMainEntities(EntityInterface entity,
			List<EntityInterface> mainEntityList) 
	{
		try
		{
			List<AssociationInterface> associationList = getIncomingContainmentAssociations(entity);
			if (associationList.size() != 0)
			{
				for (AssociationInterface assocoation : associationList)
				{
					mainEntityList = getAllMainEntities(assocoation.getEntity(), mainEntityList);
				}
			}
			else
				mainEntityList.add(entity);
		}
		catch (DynamicExtensionsSystemException deExeption)
		{
			deExeption.printStackTrace();
		}
		return mainEntityList;
	}
	
	/**
	 * This method will create queryResultObjectDataBean for a node passed to it.
	 * @param node node for which QueryResultObjectDataBean is to be created.
	 * @param mainEntityMap main entity map. 
	 * @return queryResultObjectDataBean.
	 */
	public static  QueryResultObjectDataBean getQueryResulObjectDataBean(
			OutputTreeDataNode node, Map<EntityInterface, List<EntityInterface>> mainEntityMap)
	{   
		QueryResultObjectDataBean queryResultObjectDataBean = new QueryResultObjectDataBean();
		boolean readDeniedObject = false;
		if(node!=null)
		{
		EntityInterface dynamicExtensionsEntity = node.getOutputEntity()
				.getDynamicExtensionsEntity();
		String entityName;
		queryResultObjectDataBean
				.setPrivilegeType(edu.wustl.common.querysuite.security.utility.Utility
						.getPrivilegeType(dynamicExtensionsEntity));
		queryResultObjectDataBean.setEntity(dynamicExtensionsEntity);

		List<EntityInterface> mainEntityList = mainEntityMap.get(dynamicExtensionsEntity);
		if (mainEntityList != null)
		{
			//
			EntityInterface mainEntity = getMainEntity(mainEntityList, node);
			queryResultObjectDataBean.setMainEntity(mainEntity);
			entityName = mainEntity.getName();
		}
		else
		{
			entityName = dynamicExtensionsEntity.getName();
		}
		
		queryResultObjectDataBean.setCsmEntityName(entityName);
		setEntityName(queryResultObjectDataBean, entityName);
		readDeniedObject = isReadDeniedObject(queryResultObjectDataBean.getCsmEntityName());
		queryResultObjectDataBean.setReadDeniedObject(readDeniedObject);
		}
		return queryResultObjectDataBean;
	}

	/**If main entity is inherited from an entity (e.g. Fluid Specimen is inherited from Specimen) and present in INHERITED_ENTITY_NAMES  
	 * then csmEntityName of queryResultObjectDataBean will be set to it's parent entity name.(as Sql for getting CP ids id retrived 
	 * according to parent entity name from entityCPSqlMap in Variables class).
	 * @param queryResultObjectDataBean
	 * @param name
	 */
	private static void setEntityName(QueryResultObjectDataBean queryResultObjectDataBean, String name)
	{
		boolean presentInArray = QueryModuleUtil.isPresentInArray(name,
				Constants.INHERITED_ENTITY_NAMES);
		
		if (presentInArray)
		{
			EntityInterface parentEntity = queryResultObjectDataBean.getEntity().getParentEntity();
			if (parentEntity != null)
			{
				queryResultObjectDataBean.setCsmEntityName(parentEntity.getName());
			}
		}
	}
	
	/**This method will check if for an entity read denied has to checked or not. All theses entities are present in 
	 * Variables.queryReadDeniedObjectList list.
	 * @param name
	 * @return
	 */
	private static boolean isReadDeniedObject(String entityName)
	{  
		if (edu.wustl.common.util.global.Variables.queryReadDeniedObjectList.contains(entityName))
			return true;
		else
			return false;
	}

	
	/**
	 * Searches for main entity in parent hierarchy or child hierarchy
	 * @param mainEntityList - list of all main Entities
	 * @param node - current node
	 * @return - main Entiy if found in parent or child hierarchy. Returns null if not found
	 */
	private static EntityInterface getMainEntity(List<EntityInterface> mainEntityList, 
			 OutputTreeDataNode node)
	{
		 //check if node itself is main entity
		EntityInterface entity = null;
		 	
		 	
		// check if main entity is present in parent hierarchy
		if (node.getParent() != null)
			entity = getMainEntityFromParentHierarchy(mainEntityList, node.getParent()); 
		if (entity != null)
			return entity;	
		 	
		 //check if main entity is present in child hierarchy
		 	
		entity = getMainEntityFromChildHierarchy(mainEntityList, node);
			
		return entity;
	}
		 
	 /**
	  * To check whether the given Entity in OutputTreeDataNode is mainEntity or not 
	  * @param mainEntityList the list of main entities.
	  * @param node the OutputTreeDataNode
	  * @return The reference to entity in the OutputTreeDataNode, if its present in the mainEntityList.
	  */
	 private static EntityInterface isMainEntity(List<EntityInterface> mainEntityList, OutputTreeDataNode node)
	 {
		 EntityInterface dynamicExtensionsEntity = node.getOutputEntity()
					.getDynamicExtensionsEntity();
		 if (mainEntityList.contains(dynamicExtensionsEntity))
			 return dynamicExtensionsEntity;
		 else 
			 return null;
	 }
		 
	 /**
	  * recursively checks in parent hierarchy for main entity
	  * @param mainEntityList
	  * @param node
	  * @return main Entiy if found in parent Hierarchy
	  */
	 private static EntityInterface  getMainEntityFromParentHierarchy(
		 		List<EntityInterface> mainEntityList, OutputTreeDataNode node)
	 {
		 
		EntityInterface entity = isMainEntity(mainEntityList, node);
	 	if (entity == null)
	 	{
	 		if (node.getParent() != null)
	 		 	return getMainEntityFromParentHierarchy(mainEntityList, node.getParent());
	 	}
	 	return entity;
	 }
	 
	 /**
	  * recursively checks in child hierarchy for main entity
	  * @param mainEntityList
	  * @param node
	  * @return main Entiy if found in child Hierarchy
	  */
	 private static EntityInterface getMainEntityFromChildHierarchy(
	 		List<EntityInterface> mainEntityList, OutputTreeDataNode node)
	 {
		EntityInterface entity = isMainEntity(mainEntityList, node);
	 	if (entity == null)
	 	{
		 	List<OutputTreeDataNode> children = node.getChildren();
		 	
		 	for (OutputTreeDataNode childNode : children)
			{
				entity = getMainEntityFromChildHierarchy(mainEntityList, childNode);
				if (entity != null)
				{
		 			return entity;	
		 		}
			}
		}
		return entity;	
	 }
		
	

	/**This method will internally call  getIncomingAssociationIds of DE which will return all incoming associations 
	 * for entities passed.This method will filter out all incoming containment associations and return list of them.
	 * @param entity
	 */
	public static List<AssociationInterface> getIncomingContainmentAssociations(EntityInterface entity) throws DynamicExtensionsSystemException
	{   
		EntityManagerInterface entityManager = EntityManager.getInstance();
		ArrayList<Long> allIds = (ArrayList<Long>)entityManager.getIncomingAssociationIds(entity);
		List<AssociationInterface> list = new ArrayList<AssociationInterface>();
		EntityCache cache = EntityCache.getInstance();
		for (Long id: allIds)
		{
			AssociationInterface associationById = cache.getAssociationById(id);
			
			RoleInterface targetRole = associationById.getTargetRole();
			if (associationById!=null && targetRole.getAssociationsType().getValue().equals(Constants.CONTAINTMENT_ASSOCIATION))
				list.add(associationById);
		}
		return list;
	}	
		
	/**
	 * @param queryResultObjectDataBean
	 * @param columnIndex
	 * @param selectSql 
	 * @param entityIdIndexMap 
	 * @param uniqueIdNodesMap2 
	 * @param defineViewEntityList 
	 */
	public static String updateEntityIdIndexMap(QueryResultObjectDataBean queryResultObjectDataBean,
			int columnIndex, String selectSql, List<EntityInterface> defineViewNodeList, 
			Map<EntityInterface, Integer> entityIdIndexMap, Map<String, OutputTreeDataNode> uniqueIdNodesMap)
	{ 
		List<String> selectSqlColumnList = getListOfSelectedColumns(selectSql);
		if (defineViewNodeList != null)
		{
			//Map<String, OutputTreeDataNode> uniqueIdNodesMap = QueryModuleUtil.uniqueIdNodesMap;
			Set<String> keySet = uniqueIdNodesMap.keySet();
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();)
			{
				String key = "";
				Object nextObject = iterator.next();
				if (nextObject instanceof String)
				{
					key = (String) nextObject;
					OutputTreeDataNode outputTreeDataNode = uniqueIdNodesMap.get(key);
					Map sqlIndexMap = putIdColumnsInSql(columnIndex, selectSql, entityIdIndexMap,
							selectSqlColumnList, outputTreeDataNode);
					selectSql = (String) sqlIndexMap.get(Constants.SQL);
					columnIndex = (Integer) sqlIndexMap.get(Constants.ID_COLUMN_ID);
				}
			}
		}
		else
		{
			OutputTreeDataNode outputTreeDataNode = getMatchingEntityNode(queryResultObjectDataBean
					.getMainEntity(),uniqueIdNodesMap);
			Map sqlIndexMap = putIdColumnsInSql(columnIndex, selectSql, entityIdIndexMap,
					selectSqlColumnList, outputTreeDataNode);
			selectSql = (String) sqlIndexMap.get(Constants.SQL);
			columnIndex = (Integer) sqlIndexMap.get(Constants.ID_COLUMN_ID);
		}
		if (queryResultObjectDataBean != null)
		{
			queryResultObjectDataBean.setEntityIdIndexMap(entityIdIndexMap);
			if (entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity()) != null)
			{
				queryResultObjectDataBean.setMainEntityIdentifierColumnId(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity()));
			}
		}
		return selectSql;
	}

	/**
	 * To add the Id columns of MainEntities in the SQL if its not present. 
	 * It will also populate entityIdIndexMap passes it. 
	 * @param columnIndex
	 * @param selectSql
	 * @param entityIdIndexMap
	 * @param selectSqlColumnList
	 * @param outputTreeDataNode
	 * @return The modified SQL string.
	 */
	private static Map putIdColumnsInSql(int columnIndex, String selectSql,
			Map<EntityInterface, Integer> entityIdIndexMap,
			List<String> selectSqlColumnList,
			OutputTreeDataNode outputTreeDataNode) {
		Map sqlIndexMap = new HashMap();
		if (outputTreeDataNode != null)
		{
			List<QueryOutputTreeAttributeMetadata> attributes = outputTreeDataNode
					.getAttributes();
			for (QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
			{
				AttributeInterface attribute = attributeMetaData.getAttribute();
				String sqlColumnName = attributeMetaData.getColumnName().trim();
				if (attribute.getName().equals(Constants.ID))
				{
					int index = selectSqlColumnList.indexOf(sqlColumnName);
					
					if (index>=0)
					{
						entityIdIndexMap.put(attribute.getEntity(), index);
						break;
					}
					else
					{
						if(selectSql.equals(""))
						{
							selectSql += sqlColumnName;
						}
						else
						{
							selectSql += ", " + sqlColumnName;
						}
						entityIdIndexMap.put(attribute.getEntity(), columnIndex);
						columnIndex++;
						break;
					}
				}
			}
		}
		sqlIndexMap.put(Constants.SQL,selectSql);
		sqlIndexMap.put(Constants.ID_COLUMN_ID, columnIndex);
		return sqlIndexMap;
	}
	/**
	 * TO the list of selectColumn Names in the selectSql.
	 * @param selectSql the Select part of SQL.
	 * @return The list of selectColumn Names in the selectSql.
	 */
	private static List<String> getListOfSelectedColumns(String selectSql) {
		String[] selectSqlColumnArray = selectSql.split(","); 
		List<String> selectSqlColumnList = new ArrayList<String>();
		for (int i = 0; i < selectSqlColumnArray.length; i++)
		{
			selectSqlColumnList.add(selectSqlColumnArray[i].trim());
		}
		return selectSqlColumnList;
	}
 
	/**This method will return node corresponding to an entity from query.
	 * @param entity
	 * @param uniqueIdNodesMap 
	 * @return outputTreeDataNode
	 */
	private static OutputTreeDataNode getMatchingEntityNode(EntityInterface entity, Map<String, OutputTreeDataNode> uniqueIdNodesMap)
	{
		Iterator<OutputTreeDataNode> iterator = uniqueIdNodesMap.values().iterator(); 
		while (iterator.hasNext())
		{
			OutputTreeDataNode outputTreeDataNode = (OutputTreeDataNode) iterator.next();
			if(outputTreeDataNode.getOutputEntity().getDynamicExtensionsEntity().equals(entity))
				return outputTreeDataNode;
		}
		return null;
	}
} 