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
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author supriya_dankh
 * This class contains all the methods required for CSM integration in query.
 */
public abstract class AbstractQueryCSMUtil
{
	/**
	 * Logger object used to display exception messages.
	 */
	private static org.apache.log4j.Logger logger =Logger.getLogger(AbstractQueryCSMUtil.class);

	/**This method will check if main objects for all the dependent objects are present in query or not.
	 * If yes then will create map of entity as key and main entity list as value.
	 * If not then will set error message in session.
	 * @param query The query
	 * @param session The session object
	 * @param queryDetailsObj The QueryDetails object
	 * @return mainEntityMap A map containing the main entities of the query nodes
	 */
	public static Map<EntityInterface, List<EntityInterface>> setMainObjectErrorMessage
		(IQuery query, HttpSession session,QueryDetails queryDetailsObj)
	{
		Map<EntityInterface, List<EntityInterface>> nullValueMap = null;
		Map<EntityInterface, List<EntityInterface>> mainEntityMap =
			getMainEntitiesForAllQueryNodes(queryDetailsObj);
		queryDetailsObj.setMainEntityMap(mainEntityMap);

		String errorMessg = getErrorMessage(queryDetailsObj);
		if ("".equals(errorMessg))
		{
			session.setAttribute(Constants.MAIN_ENTITY_MAP, mainEntityMap);
		}
		else
		{
			session.setAttribute(Constants.NO_MAIN_OBJECT_IN_QUERY, errorMessg);
			mainEntityMap = nullValueMap;
		}
		return mainEntityMap;
	}

	/**
	 * This method will return error message for main object.
	 * @param queryDetailsObj contains group of fields in this Object
	 * @return error message if main entity of node not present.
	 */
	private static String getErrorMessage(QueryDetails queryDetailsObj)
	{
		String errorMsg = "";
		Object[] arguments;
		//iterate through the uniqueIdNodesMap and check if main entities of all the nodes are present
		for (Iterator idMapIterator = queryDetailsObj.getUniqueIdNodesMap().entrySet().iterator();
						idMapIterator.hasNext();)
		{
			Map.Entry<String, OutputTreeDataNode> idMapValue =
				(Map.Entry<String, OutputTreeDataNode>) idMapIterator.next();
			// get the node
			OutputTreeDataNode node = idMapValue.getValue();
			//get the entity
			EntityInterface mapEntity = node.getOutputEntity().getDynamicExtensionsEntity();
			// get the main entities list for the entity
			List<EntityInterface> mainEntityList =
				(List<EntityInterface>) queryDetailsObj.getMainEntityMap().get(mapEntity);
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
					StringBuffer tmpMainEntityName = new StringBuffer();
					for (EntityInterface entity : mainEntityList)
					{
						//get the names of all the main entities for the dependent entity
						String name = entity.getName();
						name = name.substring(name.lastIndexOf('.') + 1, name.length()); //TODO: use Utility Method for getting className
						tmpMainEntityName = tmpMainEntityName.append(mainEntityNames)
								.append(name).append(" or ");
					}
					mainEntityNames = tmpMainEntityName.toString();
					mainEntityNames = mainEntityNames
							.substring(0, mainEntityNames.lastIndexOf('r') - 1);
					String message = ApplicationProperties.getValue("query.mainObjectError");
					String entityName = mapEntity.getName();
					entityName = entityName.substring(entityName.lastIndexOf('.') + 1,
					entityName.length());//TODO: use Utility Method for getting className
					arguments = new Object[]{entityName, mainEntityNames};
					errorMsg = MessageFormat.format(message, arguments);
					break;
				}
			}
		}
		return errorMsg;
	}

	/**
	 * This method will return map of a entity as value and list of all the main entities of this
	 * particular entity as value.
	 * @param queryDetailsObj The QueryDetails object
	 * @return mainEntityMap Map of all main entities present in query.
	 */
	private static Map<EntityInterface, List<EntityInterface>> getMainEntitiesForAllQueryNodes
		(QueryDetails queryDetailsObj)
	{
		Map<EntityInterface, List<EntityInterface>> mainEntityMap =
			new HashMap<EntityInterface, List<EntityInterface>>();
		for (OutputTreeDataNode queryNode : queryDetailsObj.getUniqueIdNodesMap().values())
		{
			populateMainEntityMap(mainEntityMap, queryNode);
		}
		return mainEntityMap;
	}

	/**
	 * This method will populate the mainEntityMap which will contain main entities for query nodes.
	 * @param mainEntityMap Map of all main entities present in query
	 * @param queryNode Single query node
	 */
	private static void populateMainEntityMap(Map<EntityInterface,
			List<EntityInterface>> mainEntityMap, OutputTreeDataNode queryNode)
	{
		List<EntityInterface> mainEntityList = new ArrayList<EntityInterface>();
		EntityInterface dynamicExtnEntity = queryNode.getOutputEntity().getDynamicExtensionsEntity();
		mainEntityList = getAllMainEntities(dynamicExtnEntity, mainEntityList);
		EntityInterface tempEntity = dynamicExtnEntity;
		List<EntityInterface> tmpMainEntityList;

		getTemperoryMainEntityList(mainEntityList, tempEntity);
		if (mainEntityList.size() != 1)
		{
			List<EntityInterface> temperoryList = mainEntityList;
			mainEntityList = new ArrayList<EntityInterface>();
			for (EntityInterface temperoryEntity : temperoryList)
			{
				if (!(temperoryEntity.equals(dynamicExtnEntity)))
				{
					mainEntityList.add(temperoryEntity);
				}
			}
		}

		if (!(mainEntityList != null && mainEntityList.size() == 1 &&
				mainEntityList.get(0).equals(dynamicExtnEntity)))
		{
			tmpMainEntityList = new ArrayList<EntityInterface>();
			for (EntityInterface mainEntity : mainEntityList)
			{
				if (mainEntity.isAbstract())
				{
					tmpMainEntityList.addAll
					(QueryCsmBizLogic.getMainEntityList(mainEntity, dynamicExtnEntity));
				}
			}
			mainEntityList.addAll(tmpMainEntityList);
			mainEntityMap.put(dynamicExtnEntity, mainEntityList);
		}
	}

	/**
	 * To populate the mainEntityList temporarily.
	 * @param mainEntityList List of main entities
	 * @param dynamicExtnEntity Dynamic extensions entity
	 */
	private static void getTemperoryMainEntityList(List<EntityInterface> mainEntityList,
			EntityInterface dynamicExtnEntity)
	{
		List<EntityInterface> tmpMainEntityList;
		while (true)
		{
			tmpMainEntityList = new ArrayList<EntityInterface>();
			EntityInterface parentEntity = dynamicExtnEntity.getParentEntity();
			if (parentEntity == null)
			{
				break;
			}
			else
			{
				tmpMainEntityList = getAllMainEntities(parentEntity, tmpMainEntityList);
				for (EntityInterface tempMainEntity : tmpMainEntityList)
				{
					if (!(tempMainEntity.equals(parentEntity)))
					{
						mainEntityList.add(tempMainEntity);
					}
				}
				dynamicExtnEntity = parentEntity;
			}
		}
	}

	/**This is a recursive method that will create list of all main entities
	 * (Entities for which entity passed to it is having containment association).
	 * @param entity The Entity
	 * @param mainEntityList The list of main entities
	 * @return mainEntityList The list of main entities
	 */
	private static List<EntityInterface> getAllMainEntities(EntityInterface entity,
			List<EntityInterface> mainEntityList)
	{
		try
		{
			List<AssociationInterface> associationList = getIncomingContainmentAssociations(entity);
			if (associationList.isEmpty())
			{
				mainEntityList.add(entity);
			}
			else
			{
				for (AssociationInterface assocoation : associationList)
				{
					mainEntityList = getAllMainEntities
					(assocoation.getEntity(), mainEntityList);
				}
			}
		}
		catch (DynamicExtensionsSystemException deExeption)
		{
			logger.error("Exception in retrieving main entities "+deExeption);
		}
		return mainEntityList;
	}

	/**
	 * This method will create queryResultObjectDataBean for a node passed to it.
	 * @param node node for which QueryResultObjectDataBean is to be created.
	 * @param queryDetailsObj The QueryDetails object
	 * @return queryResultObjectDataBean.
	 */
	public static QueryResultObjectDataBean getQueryResulObjectDataBean
		(OutputTreeDataNode node, QueryDetails queryDetailsObj)
	{
		QueryResultObjectDataBean queryResultObjectDataBean = new QueryResultObjectDataBean();
		boolean readDeniedObject = false;
		if (node != null)
		{
			EntityInterface dynamicExtnEntity = node.getOutputEntity().getDynamicExtensionsEntity();
			String entityName;
			queryResultObjectDataBean.setPrivilegeType
			(edu.wustl.common.querysuite.security.utility.Utility.getPrivilegeType(dynamicExtnEntity));
			queryResultObjectDataBean.setEntity(dynamicExtnEntity);

			List<EntityInterface> mainEntityList =
				queryDetailsObj.getMainEntityMap().get(dynamicExtnEntity);
			if (mainEntityList == null)
			{
				entityName = dynamicExtnEntity.getName();
			}
			else
			{
				EntityInterface mainEntity = getMainEntity(mainEntityList, node);
				queryResultObjectDataBean.setMainEntity(mainEntity);
				entityName = mainEntity.getName();
			}

			queryResultObjectDataBean.setCsmEntityName(entityName);
			setEntityName(queryResultObjectDataBean, entityName);
			readDeniedObject = isReadDeniedObject(queryResultObjectDataBean.getCsmEntityName());
			queryResultObjectDataBean.setReadDeniedObject(readDeniedObject);
		}
		return queryResultObjectDataBean;
	}

	/**If main entity is inherited from an entity (e.g. Fluid Specimen is inherited from Specimen)
	 * and present in INHERITED_ENTITY_NAMES then csmEntityName of queryResultObjectDataBean
	 * will be set to it's parent entity name.(as Sql for getting CP ids is retrieved
	 * according to parent entity name from entityCPSqlMap in Variables class).
	 * @param queryResultObjectDataBean QueryResultObjectDataBean
	 * @param name Name of the entity
	 */
	private static void setEntityName(QueryResultObjectDataBean queryResultObjectDataBean, String name)
	{
		boolean presentInArray = QueryModuleUtil.isPresentInArray(name, Constants.INHERITED_ENTITY_NAMES);

		if (presentInArray)
		{
			EntityInterface parentEntity = queryResultObjectDataBean.getEntity().getParentEntity();
			if (parentEntity != null)
			{
				queryResultObjectDataBean.setCsmEntityName(parentEntity.getName());
			}
		}
	}

	/**This method will check if for an entity read denied has to checked or not.
	 * All theses entities are present in Variables.queryReadDeniedObjectList list.
	 * @param entityName The name of the entity
	 * @return <CODE>true</CODE> if entity is read denied
	 * <CODE>false</CODE> otherwise
	 */
	private static boolean isReadDeniedObject(String entityName)
	{
		boolean isReadDenied = false;
		if (edu.wustl.common.util.global.Variables.queryReadDeniedObjectList.contains(entityName))
		{
			isReadDenied = true;
		}
		return isReadDenied;
	}

	/**
	 * Searches for main entity in parent hierarchy or child hierarchy.
	 * @param mainEntityList - list of all main Entities
	 * @param node Current node
	 * @return  main Entity if found in parent or child hierarchy. Returns null if not found
	 */
	private static EntityInterface getMainEntity(List<EntityInterface> mainEntityList, OutputTreeDataNode node)
	{
		//check if node itself is main entity
		EntityInterface entity = null;

		// check if main entity is present in parent hierarchy
		if (node.getParent() != null)
		{
			entity = getMainEntityFromParentHierarchy(mainEntityList, node.getParent());
		}

		//check if main entity is present in child hierarchy
		if(entity == null)
		{
			entity = getMainEntityFromChildHierarchy(mainEntityList, node);
		}
		return entity;
	}

	/**
	 * To check whether the given Entity in OutputTreeDataNode is mainEntity or not.
	 * @param mainEntityList the list of main entities.
	 * @param node the OutputTreeDataNode
	 * @return The reference to entity in the OutputTreeDataNode, if its present in the mainEntityList.
	 */
	private static EntityInterface isMainEntity(List<EntityInterface> mainEntityList, OutputTreeDataNode node)
	{
		EntityInterface nullValue = null;
		EntityInterface dynamicExtnEntity = node.getOutputEntity().getDynamicExtensionsEntity();
		if (!mainEntityList.contains(dynamicExtnEntity))
		{
			dynamicExtnEntity = nullValue;
		}
		return dynamicExtnEntity;
	}

	/**
	 * Recursively checks in parent hierarchy for main entity.
	 * @param mainEntityList List of main entities
	 * @param node Output tree data node
	 * @return main Entity if found in parent Hierarchy
	 */
	private static EntityInterface getMainEntityFromParentHierarchy
	(List<EntityInterface> mainEntityList, OutputTreeDataNode node)
	{
		EntityInterface entity = isMainEntity(mainEntityList, node);
		if (entity == null && node.getParent() != null)
		{
			return getMainEntityFromParentHierarchy(mainEntityList, node.getParent());
		}
		return entity;
	}

	/**
	 * Recursively checks in child hierarchy for main entity.
	 * @param mainEntityList List of main entities
	 * @param node Output tree data node
	 * @return main Entity if found in child Hierarchy
	 */
	private static EntityInterface getMainEntityFromChildHierarchy
	(List<EntityInterface> mainEntityList, OutputTreeDataNode node)
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

	/**This method will internally call getIncomingAssociationIds of DE which will return
	 * all incoming associations for entities passed.
	 * This method will filter out all incoming containment associations and return list of them.
	 * @param entity The entity
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 * @return list List of incoming containment associations
	 */
	public static List<AssociationInterface> getIncomingContainmentAssociations
	(EntityInterface entity) throws DynamicExtensionsSystemException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		ArrayList<Long> allIds = (ArrayList<Long>) entityManager.getIncomingAssociationIds(entity);
		List<AssociationInterface> list = new ArrayList<AssociationInterface>();
		EntityCache cache = EntityCache.getInstance();
		for (Long id : allIds)
		{
			AssociationInterface associationById = cache.getAssociationById(id);

			RoleInterface targetRole = associationById.getTargetRole();
			if (associationById != null && targetRole.getAssociationsType().getValue().equals
					(Constants.CONTAINTMENT_ASSOCIATION))
			{
				list.add(associationById);
			}
		}
		return list;
	}

	/**
	 * @param queryResultObjectDataBean QueryResultObjectDataBean object
	 * @param columnIndex Index of the column
	 * @param selectSql The select query formed
	 * @param entityIdIndexMap Map with key->entity and value->index
	 * @param queryDetailsObj QueryDetails object containing query details
	 * @param defineViewNodeList The list containing the nodes in define view
	 * @return selectSql The select query formed
	 */
	public static String updateEntityIdIndexMap(QueryResultObjectDataBean queryResultObjectDataBean,
			int columnIndex, String selectSql, List<EntityInterface> defineViewNodeList,
			Map<EntityInterface, Integer> entityIdIndexMap, QueryDetails queryDetailsObj)
	{
		List<String> selectSqlColList = getListOfSelectedColumns(selectSql);
		if (defineViewNodeList == null)
		{
			OutputTreeDataNode outputTreeDataNode = getMatchingEntityNode
				(queryResultObjectDataBean.getMainEntity(), queryDetailsObj);
			Map sqlIndexMap = putIdColumnsInSql
			(columnIndex, selectSql, entityIdIndexMap, selectSqlColList, outputTreeDataNode);
			selectSql = (String) sqlIndexMap.get(Constants.SQL);
			columnIndex = (Integer) sqlIndexMap.get(Constants.ID_COLUMN_ID);
		}
		else
		{
			//Map<String, OutputTreeDataNode> uniqueIdNodesMap = QueryModuleUtil.uniqueIdNodesMap;
			Set<String> keySet = queryDetailsObj.getUniqueIdNodesMap().keySet();
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();)
			{
				String key = "";
				Object nextObject = iterator.next();
				if (nextObject instanceof String)
				{
					key = (String) nextObject;
					OutputTreeDataNode outputTreeDataNode =
						queryDetailsObj.getUniqueIdNodesMap().get(key);
					Map sqlIndexMap = putIdColumnsInSql(columnIndex, selectSql,
							entityIdIndexMap, selectSqlColList, outputTreeDataNode);
					selectSql = (String) sqlIndexMap.get(Constants.SQL);
					columnIndex = (Integer) sqlIndexMap.get(Constants.ID_COLUMN_ID);
				}
			}
		}
		if (queryResultObjectDataBean != null)
		{
			queryResultObjectDataBean.setEntityIdIndexMap(entityIdIndexMap);
			if (entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity()) != null)
			{
				queryResultObjectDataBean.setMainEntityIdentifierColumnId
				(entityIdIndexMap.get(queryResultObjectDataBean.getMainEntity()));
			}
		}
		return selectSql;
	}

	/**
	 * To add the Id columns of MainEntities in the SQL if its not present.
	 * It will also populate entityIdIndexMap passes it.
	 * @param columnIndex column index
	 * @param selectSql The select query
	 * @param entityIdIndexMap Map with key->entity and value->index
	 * @param selectSqlColumnList The list of columns in select query
	 * @param outputTreeDataNode OutputTreeDataNode object
	 * @return The modified SQL string.
	 */
	private static Map putIdColumnsInSql(int columnIndex, String selectSql,
			Map<EntityInterface, Integer> entityIdIndexMap,
			List<String> selectSqlColumnList, OutputTreeDataNode outputTreeDataNode)
	{
		Map sqlIndexMap = new HashMap();
		if (outputTreeDataNode != null)
		{
			List<QueryOutputTreeAttributeMetadata> attributes = outputTreeDataNode.getAttributes();
			for (QueryOutputTreeAttributeMetadata attributeMetaData : attributes)
			{
				AttributeInterface attribute = attributeMetaData.getAttribute();
				String sqlColumnName = attributeMetaData.getColumnName().trim();
				if (attribute.getName().equals(Constants.ID))
				{
					int index = selectSqlColumnList.indexOf(sqlColumnName);

					if (index >= 0)
					{
						entityIdIndexMap.put(attribute.getEntity(), index);
						break;
					}
					else
					{
						selectSql = getSelectSQLString(selectSql, sqlColumnName);
						entityIdIndexMap.put(attribute.getEntity(), columnIndex);
						columnIndex++;
						break;
					}
				}
			}
		}
		sqlIndexMap.put(Constants.SQL, selectSql);
		sqlIndexMap.put(Constants.ID_COLUMN_ID, columnIndex);
		return sqlIndexMap;
	}

	/**
	 * @param selectSql The select query.
	 * @param sqlColumnName Column name
	 * @return selectSql The select query formed
	 */
	private static String getSelectSQLString(String selectSql, String sqlColumnName)
	{
		if ("".equals(selectSql))
		{
			selectSql += sqlColumnName;
		}
		else
		{
			selectSql += ", " + sqlColumnName;
		}
		return selectSql;
	}

	/**
	 * To get the list of selectColumn Names in the selectSql.
	 * @param selectSql the Select part of SQL.
	 * @return The list of selectColumn Names in the selectSql.
	 */
	private static List<String> getListOfSelectedColumns(String selectSql)
	{
		String[] selectSqlColumnArray = selectSql.split(",");
		List<String> selectSqlColumnList = new ArrayList<String>();
		for (int i = 0; i < selectSqlColumnArray.length; i++)
		{
			selectSqlColumnList.add(selectSqlColumnArray[i].trim());
		}
		return selectSqlColumnList;
	}

	/**
	 * This method will return node corresponding to an entity from query.
	 * @param entity The entity
	 * @param queryDetailsObj QueryDetails object
	 * @return tempOutputTreeDataNode Node corresponding to an entity from query
	 */
	private static OutputTreeDataNode getMatchingEntityNode
			(EntityInterface entity, QueryDetails queryDetailsObj)
	{
		Iterator<OutputTreeDataNode> iterator = queryDetailsObj.getUniqueIdNodesMap().values().iterator();
		OutputTreeDataNode tempOutputTreeDataNode = null;
		while (iterator.hasNext())
		{
			OutputTreeDataNode outputTreeDataNode = (OutputTreeDataNode) iterator.next();
			if (outputTreeDataNode.getOutputEntity().getDynamicExtensionsEntity().equals(entity))
			{
				tempOutputTreeDataNode = outputTreeDataNode;
				break;
			}
		}
		return tempOutputTreeDataNode;
	}
}