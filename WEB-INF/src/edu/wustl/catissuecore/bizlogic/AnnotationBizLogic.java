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
	/**
	 * logger object
	 */
	private static Logger logger = Logger.getCommonLogger(AnnotationBizLogic.class);

	/**
	 *  public constructor
	 */
	public AnnotationBizLogic()
	{
		this.setAppName(DynamicExtensionDAO.getInstance().getAppName());
	}

	/**
	 * @param staticEntityId.
	 * @return List of all dynamic entities id from a given static entity
	 * eg: returns all dynamic entity id from a Participant,Specimen etc
	 */
	public List getListOfDynamicEntitiesIds(long staticEntityId) throws BizLogicException
	{
		List<EntityMap> dynamicList = new ArrayList<EntityMap>();

		final List list = new ArrayList();
		dynamicList = this.retrieve(EntityMap.class.getName(), "staticEntityId", new Long(
				staticEntityId));
		if (dynamicList != null && !dynamicList.isEmpty())
		{
			for (final EntityMap entityMap : dynamicList)
			{
				list.add(entityMap.getContainerId());
			}
		}

		return list;
	}

	/**
	 * @param staticEntityId.
	 * @return List of all dynamic entities Objects from a given static entity
	 * eg: returns all dynamic entity objects from a Participant,Specimen etc
	 * @throws DynamicExtensionsApplicationException
	 */
	public List getListOfDynamicEntities(long staticEntityId) throws BizLogicException
	{
		List dynamicList = new ArrayList();

		dynamicList = this.retrieve(EntityMap.class.getName(), "staticEntityId", new Long(
				staticEntityId));

		return dynamicList;
	}

	/**
	 * @param staticEntityId : staticEntityId.
	 * @param  typeId : typeId
	 * @param staticRecordId :staticRecordId
	 * @return List of all dynamic entities id from a given static entity based on its protocol linkage
	 * eg: returns all dynamic entity id from a Participant,Specimen etc which is linked
	   to Protocol 1,Protocol 2 etc
	 */
	public List getListOfDynamicEntitiesIds(long staticEntityId, long typeId, long staticRecordId)
	{
		List dynamicList = new ArrayList();

		final String[] selectColumnName = {"containerId"};
		final String[] whereColumnName = {"staticEntityId", "typeId", "staticRecordId"};
		final String[] whereColumnCondition = {"=", "=", "="};
		final Object[] whereColumnValue = {new Long(staticEntityId), new Long(typeId),
				new Long(staticRecordId)};
		final String joinCondition = Constants.AND_JOIN_CONDITION;

		try
		{
			dynamicList = this.retrieve(EntityMap.class.getName(), selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		}
		catch (final BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return dynamicList;
	}

	/**
	 * Updates the Entity Record object in database.
	 * @param entityRecord : entityRecord
	 * @throws BizLogicException :BizLogicException
	 * 
	 */
	public void updateEntityRecord(EntityMapRecord entityRecord) throws BizLogicException
	{
		this.update(entityRecord);

	}

	/**
	 * @param entityRecord : entityRecord.
	 * Inserts a new EntityRecord record in Database
	 * @throws BizLogicException : BizLogicException
	 */
	public void insertEntityRecord(EntityMapRecord entityRecord) throws BizLogicException
	{
		this.insert(entityRecord);
		final Long entityMapId = entityRecord.getFormContext().getEntityMap().getId();
		final Long staticEntityRecordId = entityRecord.getStaticEntityRecordId();
		final Long dynExtRecordId = entityRecord.getDynamicEntityRecordId();
		this
				.associateRecords(entityMapId, new Long(staticEntityRecordId), new Long(
						dynExtRecordId));
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
		final DefaultBizLogic bizLogic = new CatissueDefaultBizLogic();
		bizLogic.setAppName(DynamicExtensionDAO.getInstance().getAppName());
		final Object object = bizLogic.retrieve(EntityMap.class.getName(), entityMapId);
		final EntityManagerInterface entityManager = EntityManager.getInstance();
		if (object != null)
		{
			try
			{
				final EntityMap entityMap = (EntityMap) object;
				Long dynamicEntityId = entityManager.getEntityIdByContainerId(entityMap
						.getContainerId());
				final Long rootContainerId = entityMap.getContainerId();
				final Long containerId = entityManager.isCategory(rootContainerId);
				if (containerId != null)
				{
					final Long entityId = entityManager
							.getEntityIdByCategorEntityId(dynamicEntityId);
					if (entityId != null)
					{
						dynamicEntityId = entityId;
					}

				}
				//root category entity id .take that entity from cache

				EntityInterface dynamicEntity = EntityCache.getInstance().getEntityById(
						dynamicEntityId);
				final EntityInterface staticEntity = EntityCache.getInstance().getEntityById(
						entityMap.getStaticEntityId());

				final Collection<AssociationInterface> associationCollection = staticEntity
						.getAssociationCollection();
				do
				{
					AssociationInterface associationInterface = null;
					for (final AssociationInterface association : associationCollection)
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
			catch (final DynamicExtensionsSystemException exception)
			{
				logger.debug(exception.getMessage(), exception);
				throw new BizLogicException(null, null, exception.getMessage());
			}
		}
	}

	/**
	 * 
	 * @param entityMap
	 * Updates the Entity Map object in database
	 */
	public void updateEntityMap(EntityMap entityMap) throws BizLogicException
	{

		this.update(entityMap);

	}

	/**
	 * 
	 * @param entityMap
	 * Inserts a new EntityMap record in Database
	 */
	public void insertEntityMap(EntityMap entityMap) throws BizLogicException
	{
		final Long staticEntityId = entityMap.getStaticEntityId();
		final Long dynamicEntityId = entityMap.getContainerId();
		final Long deAssociationID = AnnotationUtil.addAssociation(staticEntityId, dynamicEntityId,
				false);
		if (deAssociationID != null)
		{
			this.insert(entityMap);
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

		final String[] selectColumnName = {"staticEntityId"};
		final String[] whereColumnName = {"containerId"};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {new Long(dynamicEntityContainerId)};
		final String joinCondition = null;

		try
		{
			dynamicList = this.retrieve(EntityMap.class.getName(), selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		}
		catch (final BizLogicException e)
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
			dynamicList = this.retrieve(EntityMap.class.getName(), "containerId", new Long(
					dynamicEntityContainerId));
		}
		catch (final BizLogicException e)
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
			map = (EntityMap) this.retrieve(EntityMap.class.getName(), entityMapId);
		}
		catch (final BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return map;
	}

	public List getEntityMapRecordList(List entityMapids, long staticRecordId)
	{
		final List dynamicList = new ArrayList();

		final String[] selectColumnName = null;
		final String[] whereColumnName = {"staticEntityRecordId", "formContext.entityMap.id"};
		final String[] whereColumnCondition = {"=", "="};
		final String joinCondition = Constants.AND_JOIN_CONDITION;

		final Iterator iter = entityMapids.iterator();
		while (iter.hasNext())
		{
			final Long entityMapId = (Long) iter.next();
			if (entityMapId != null)
			{
				final Object[] whereColumnValue = {new Long(staticRecordId), entityMapId};
				try
				{
					final List list = this.retrieve(EntityMapRecord.class.getName(),
							selectColumnName, whereColumnName, whereColumnCondition,
							whereColumnValue, joinCondition);
					if (list != null)
					{
						dynamicList.addAll(list);
					}
				}
				catch (final BizLogicException e)
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

			final String[] selectColumnName = null;
			final String[] whereColumnName = {"formContext.entityMap.id", "dynamicEntityRecordId"};
			final String[] whereColumnCondition = {"=", "="};
			final Object[] whereColumnValue = {new Long(entityMapId),
					new Long(dynamicEntityRecordId)};
			final String joinCondition = Constants.AND_JOIN_CONDITION;

			dynamicList = this.retrieve(EntityMapRecord.class.getName(), selectColumnName,
					whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

			if (dynamicList != null && !dynamicList.isEmpty())
			{

				final EntityMapRecord entityRecord = (EntityMapRecord) dynamicList.get(0);
				entityRecord.setLinkStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
				this.update(entityRecord);

			}
		}
		catch (final BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param containerId : containerId
	 * @param recordIdList : recordIdList
	 * @throws BizLogicException : BizLogicException
	 */
	public void deleteAnnotationRecords(Long containerId, List<Long> recordIdList)
			throws BizLogicException
	{
		final EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		try
		{
			entityManagerInterface.deleteRecords(containerId, recordIdList);
		}
		catch (final Exception e)
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
	@Override
	protected void delete(Object obj, DAO dao)
	{
		try
		{
			dao.delete(obj);
		}
		catch (final DAOException e)
		{
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
	public List getAnnotationIdsBasedOnCondition(List dynEntitiesList, List cpIdList)
			throws BizLogicException
	{
		final List dynEntitiesIdList = new ArrayList();
		if (dynEntitiesList != null && !dynEntitiesList.isEmpty())
		{
			final Iterator dynEntitiesIterator = dynEntitiesList.iterator();
			while (dynEntitiesIterator.hasNext())
			{
				final EntityMap entityMap = (EntityMap) dynEntitiesIterator.next();

				final Collection<FormContext> formContexts = AppUtility.getFormContexts(entityMap
						.getId());
				final Iterator<FormContext> formContextIter = formContexts.iterator();
				while (formContextIter.hasNext())
				{
					final FormContext formContext = formContextIter.next();

					final Collection<EntityMapCondition> entityMapConditions = AppUtility
							.getEntityMapConditions(formContext.getId());
					if ((formContext.getNoOfEntries() == null || formContext.getNoOfEntries()
							.equals(""))
							&& (formContext.getStudyFormLabel() == null || formContext
									.getStudyFormLabel().equals("")))
					{
						if (entityMapConditions != null && !entityMapConditions.isEmpty())
						{
							final boolean check = this.checkStaticRecId(entityMapConditions,
									cpIdList);
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
	 * @return boolean
	 * @throws CacheException
	 */
	private boolean checkStaticRecId(Collection entityMapConditionCollection, List cpIdList)
	{
		final Iterator entityMapCondIterator = entityMapConditionCollection.iterator();
		try
		{
			final CatissueCoreCacheManager cache = CatissueCoreCacheManager.getInstance();
			if (cpIdList != null && !cpIdList.isEmpty())
			{
				while (entityMapCondIterator.hasNext())
				{
					final EntityMapCondition entityMapCond = (EntityMapCondition) entityMapCondIterator
							.next();
					if (entityMapCond.getTypeId().toString().equals(
							cache.getObjectFromCache(
									AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID).toString())
							&& cpIdList.contains(entityMapCond.getStaticRecordId()))
					{
						return true;
					}
				}
			}
		}
		catch (final Exception e)
		{
			logger.debug(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * @param entityMapId
	 * @return EntityMap object for its given id
	 */
	public List getEntityMapOnContainer(long containerId)
	{
		List dynamicList = new ArrayList();

		try
		{
			dynamicList = this.retrieve(EntityMap.class.getName(), "containerId", new Long(
					containerId));
		}
		catch (final BizLogicException e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		return dynamicList;
	}

	/**
	 * @param entityMapCondition
	 */
	public void insertEntityMapCondition(EntityMapCondition entityMapCondition)
	{
		try
		{
			this.insert(entityMapCondition);
		}
		catch (final Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	//Function added by Preeti :  to get all entitymap entries for a dynamic entity container
	public Collection getEntityMapsForContainer(Long deContainerId) throws BizLogicException
	{
		final List entityMaps = this.retrieve(EntityMap.class.getName(), "containerId",
				deContainerId);
		return entityMaps;
	}
}