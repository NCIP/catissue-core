/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.CacheException;
import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.EntityMapRecord;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;
/**
 * @author sandeep_chinta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class AnnotationBizLogic extends CatissueDefaultBizLogic
{
	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(AnnotationBizLogic.class);
	
	public AnnotationBizLogic()
	{
		setAppName(DynamicExtensionDAO.getInstance().getAppName());
	}
	/**
	 * 
	 * @param staticEntityId
	 * @return List of all dynamic entities id from a given static entity
	 * eg: returns all dynamic entity id from a Participant,Specimen etc
	 */
	public List getListOfDynamicEntitiesIds(long staticEntityId)throws BizLogicException
	{
		List<EntityMap> dynamicList = new ArrayList<EntityMap>();

		String[] selectColumnName = {"id"};
		String[] whereColumnName = {"staticEntityId"};
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {new Long(staticEntityId)};
		String joinCondition = null;
		List list = new ArrayList();
			dynamicList = retrieve(EntityMap.class.getName(), "staticEntityId", new Long(
					staticEntityId));
			if (dynamicList != null && !dynamicList.isEmpty())
			{
				for (EntityMap entityMap : dynamicList)
				{
					list.add(entityMap.getContainerId());
				}
			}
		

		return list;
	}

	/**
	 * 
	 * @param staticEntityId
	 * @return List of all dynamic entities Objects from a given static entity
	 * eg: returns all dynamic entity objects from a Participant,Specimen etc
	 * @throws DynamicExtensionsApplicationException
	 */
	public List getListOfDynamicEntities(long staticEntityId) throws BizLogicException
	{
		List dynamicList = new ArrayList();
		
		dynamicList = retrieve(EntityMap.class.getName(), "staticEntityId", new Long(
					staticEntityId));
		
		return dynamicList;
	}

	/**
	 * 
	 * @param staticEntityId
	 * @param typeId
	 * @param staticRecordId
	 * @return List of all dynamic entities id from a given static entity based on its protocol linkage
	 * eg: returns all dynamic entity id from a Participant,Specimen etc which is linked to Protocol 1, Protocol 2 etc
	 */
	public List getListOfDynamicEntitiesIds(long staticEntityId, long typeId, long staticRecordId) 
	{
		List dynamicList = new ArrayList();

		String[] selectColumnName = {"containerId"};
		String[] whereColumnName = {"staticEntityId", "typeId", "staticRecordId"};;
		String[] whereColumnCondition = {"=", "=", "="};
		Object[] whereColumnValue = {new Long(staticEntityId), new Long(typeId),
				new Long(staticRecordId)};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		try
		{
			dynamicList = retrieve(EntityMap.class.getName(), selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);
		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return dynamicList;
	}

	/**
	 * 
	 * @param entityRecord
	 * Updates the Entity Record object in database
	 */
	public void updateEntityRecord(EntityMapRecord entityRecord)throws BizLogicException
	{
			update(entityRecord);

	}

	/**
	 * 
	 * @param entityRecord
	 * Inserts a new EntityRecord record in Database
	 * @throws BizLogicException 
	 */
	public void insertEntityRecord(EntityMapRecord entityRecord) throws BizLogicException
	{
			insert(entityRecord);
			Long entityMapId = entityRecord.getFormContext().getEntityMap().getId();
			Long staticEntityRecordId = entityRecord.getStaticEntityRecordId();
			Long dynExtRecordId = entityRecord.getDynamicEntityRecordId();
			associateRecords(entityMapId, new Long(staticEntityRecordId), new Long(dynExtRecordId));
	}

	/**
	 * @param entityMapId
	 * @param long1
	 * @param long2
	 * @throws BizLogicException 
	 */
	private void associateRecords(Long entityMapId, Long staticEntityRecordId,
			Long dynamicEntityRecordId) throws BizLogicException
	{
		DefaultBizLogic bizLogic=new CatissueDefaultBizLogic();
		bizLogic.setAppName(DynamicExtensionDAO.getInstance().getAppName());
		Object object = bizLogic.retrieve(EntityMap.class.getName(), entityMapId);
		EntityManagerInterface entityManager = EntityManager.getInstance();
		if (object != null)
		{
			try
			{
				EntityMap entityMap = (EntityMap) object;
				Long dynamicEntityId = entityManager.getEntityIdByContainerId(entityMap
						.getContainerId());
				Long rootContainerId = entityMap.getContainerId();
				Long containerId = entityManager.isCategory(rootContainerId);
				if (containerId != null)
				{
					Long entityId = entityManager.getEntityIdByCategorEntityId(dynamicEntityId);
					if (entityId != null)
					{
						dynamicEntityId = entityId;
					}

				}
				//root category entity id .take that entity from cache

				EntityInterface dynamicEntity = EntityCache.getInstance().getEntityById(
						dynamicEntityId);
				EntityInterface staticEntity = EntityCache.getInstance().getEntityById(
						entityMap.getStaticEntityId());

				Collection<AssociationInterface> associationCollection = staticEntity
						.getAssociationCollection();
				do
				{
					AssociationInterface associationInterface = null;
					for (AssociationInterface association : associationCollection)
					{
						if (association.getTargetEntity().equals(dynamicEntity))
						{
							associationInterface = association;
							break;
						}
					}
					entityManager.associateEntityRecords(associationInterface,
							staticEntityRecordId, dynamicEntityRecordId);
					dynamicEntity = dynamicEntity.getParentEntity();
				}
				while (dynamicEntity != null);
			}
			catch (DynamicExtensionsSystemException exception)
			{
				logger.debug(exception.getMessage(), exception);
				throw new BizLogicException(null,null ,exception.getMessage()); 
			}
		}
	}

	/**
	 * 
	 * @param entityMap
	 * Updates the Entity Map object in database
	 */
	public void updateEntityMap(EntityMap entityMap)throws BizLogicException
	{

		update(entityMap);
		
		
	}

	/**
	 * 
	 * @param entityMap
	 * Inserts a new EntityMap record in Database
	 */
	public void insertEntityMap(EntityMap entityMap)throws BizLogicException
	{
			Long staticEntityId = entityMap.getStaticEntityId();
			Long dynamicEntityId = entityMap.getContainerId();
			Long deAssociationID = AnnotationUtil.addAssociation(staticEntityId, dynamicEntityId,
					false);
			if (deAssociationID != null)
			{
				insert(entityMap);
			}
		
	}

	/**
	* 
	* @param dynamicEntityContainerId
	* @return List of Static Entity Id from a given Dynamic Entity Id
	* 
	*/
	public List getListOfStaticEntitiesIds(long dynamicEntityContainerId)
	{
		List dynamicList = new ArrayList();

		String[] selectColumnName = {"staticEntityId"};
		String[] whereColumnName = {"containerId"};;
		String[] whereColumnCondition = {"="};
		Object[] whereColumnValue = {new Long(dynamicEntityContainerId)};
		String joinCondition = null;

		try
		{
			dynamicList = retrieve(EntityMap.class.getName(), selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);
		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return dynamicList;
	}

	/**
	 * 
	 * @param dynamicEntityContainerId
	 * @return List of Static Entity Objects from a given Dynamic Entity Id
	 * 
	 */
	public List getListOfStaticEntities(long dynamicEntityContainerId)
	{
		List dynamicList = new ArrayList();

		try
		{
			dynamicList = retrieve(EntityMap.class.getName(), "containerId", new Long(
					dynamicEntityContainerId));
		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return dynamicList;
	}

	/**
	 * 
	 * @param entityMapId
	 * @return EntityMap object for its given id
	 */
	public EntityMap getEntityMap(long entityMapId)
	{
		EntityMap map = null;

		try
		{
			map = (EntityMap) retrieve(EntityMap.class.getName(), entityMapId);
		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return map;
	}

	public List getEntityMapRecordList(List entityMapids, long staticRecordId)
	{
		List dynamicList = new ArrayList();

		String[] selectColumnName = null;
		String[] whereColumnName = {"staticEntityRecordId", "formContext.entityMap.id"};
		String[] whereColumnCondition = {"=", "="};
		String joinCondition = Constants.AND_JOIN_CONDITION;

		Iterator iter = entityMapids.iterator();
		while (iter.hasNext())
		{
			Long entityMapId = (Long) iter.next();
			if (entityMapId != null)
			{
				Object[] whereColumnValue = {new Long(staticRecordId), entityMapId};
				try
				{
					List list = retrieve(EntityMapRecord.class.getName(), selectColumnName,
							whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
					if (list != null)
					{
						dynamicList.addAll(list);
					}
				}
				catch (BizLogicException e)
				{
					logger.debug(e.getMessage(), e);
					e.printStackTrace();
				}
			}

		}

		return dynamicList;
	}

	public void deleteEntityMapRecord(long entityMapId, long dynamicEntityRecordId)
	{
		try
		{
			List dynamicList = new ArrayList();

			String[] selectColumnName = null;
			String[] whereColumnName = {"formContext.entityMap.id", "dynamicEntityRecordId"};;
			String[] whereColumnCondition = {"=", "="};
			Object[] whereColumnValue = {new Long(entityMapId), new Long(dynamicEntityRecordId)};
			String joinCondition = Constants.AND_JOIN_CONDITION;


			dynamicList = retrieve(EntityMapRecord.class.getName(), selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);


			if (dynamicList != null && !dynamicList.isEmpty())
			{

				EntityMapRecord entityRecord = (EntityMapRecord) dynamicList.get(0);
				entityRecord.setLinkStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
				update(entityRecord);


			}
		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param containerId
	 * @param recordIdList
	 * @throws BizLogicException
	 */
	public void deleteAnnotationRecords(Long containerId, List<Long> recordIdList)
			throws BizLogicException
	{
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		try
		{
			entityManagerInterface.deleteRecords(containerId, recordIdList);
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
			/*   throw new BizLogicException(ApplicationProperties
			           .getValue("app.annotatations.errors.deleteRecord"), e);*/
		}
	}

	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException TODO
	 */
	protected void delete(Object obj, DAO dao)
	{
		try {
			dao.delete(obj);
		} catch (DAOException e) {
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	/**
	 * @param dynEntitiesList
	 * @param cpIdList
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public List getAnnotationIdsBasedOnCondition(List dynEntitiesList, List cpIdList) throws BizLogicException
	{
		List dynEntitiesIdList = new ArrayList();
		if (dynEntitiesList != null && !dynEntitiesList.isEmpty())
		{
			Iterator dynEntitiesIterator = dynEntitiesList.iterator();
			while (dynEntitiesIterator.hasNext())
			{
				EntityMap entityMap = (EntityMap) dynEntitiesIterator.next();
				
				Collection<FormContext> formContexts = AppUtility.getFormContexts(entityMap.getId());
				Iterator<FormContext> formContextIter = formContexts.iterator();
				while (formContextIter.hasNext())
				{
					FormContext formContext = formContextIter.next();
					
					Collection<EntityMapCondition> entityMapConditions = AppUtility.getEntityMapConditions(formContext.getId());
					if ((formContext.getNoOfEntries() == null || formContext.getNoOfEntries()
							.equals(""))
							&& (formContext.getStudyFormLabel() == null || formContext
									.getStudyFormLabel().equals("")))
					{
						if (entityMapConditions != null
								&& !entityMapConditions.isEmpty())
						{
							boolean check = checkStaticRecId(entityMapConditions, cpIdList);
							if (check)
							{
								dynEntitiesIdList.add(entityMap.getContainerId());
							}
						}
						else
						{
							dynEntitiesIdList.add(entityMap.getContainerId());
						}
					}
				}
			}
		}
		
		return dynEntitiesIdList;
	}

	/**
	 * @param entityMapConditionCollection
	 * @param cpIdList
	 * @return
	 * @throws CacheException 
	 */
	private boolean checkStaticRecId(Collection entityMapConditionCollection, List cpIdList)
	{
		Iterator entityMapCondIterator = entityMapConditionCollection.iterator();
		try
		{
			CatissueCoreCacheManager cache = CatissueCoreCacheManager.getInstance();
			if (cpIdList != null && !cpIdList.isEmpty())
				while (entityMapCondIterator.hasNext())
				{
					EntityMapCondition entityMapCond = (EntityMapCondition) entityMapCondIterator
							.next();
					if (entityMapCond.getTypeId().toString().equals(
							cache.getObjectFromCache(
									AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID).toString())
							&& cpIdList.contains(entityMapCond.getStaticRecordId()))
						return true;
				}
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 
	 * @param entityMapId
	 * @return EntityMap object for its given id
	 */
	public List getEntityMapOnContainer(long containerId)
	{
		List dynamicList = new ArrayList();

		try
		{
			dynamicList = retrieve(EntityMap.class.getName(), "containerId", new Long(containerId));
		}
		catch (BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return dynamicList;
	}

	/**
	 * 
	 * @param entityMapCondition
	 */
	public void insertEntityMapCondition(EntityMapCondition entityMapCondition)
	{
		try
		{
			insert(entityMapCondition);
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	//Function added by Preeti :  to get all entitymap entries for a dynamic entity container
	public Collection getEntityMapsForContainer(Long deContainerId) throws BizLogicException
	{
		List entityMaps = retrieve(EntityMap.class.getName(), "containerId", deContainerId);
		return entityMaps;
	}
}