
package edu.wustl.catissuecore.annotations.xmi;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.dao.exception.DAOException;

/**
 * 
 * @author falguni_sachde
 *
 */
public class DECategoryIntegration
{

	/**
	 * @param rootCategoryList
	 */
	public static void categoryIntegration(List<HashMap> rootCategoryList)
	{
		try
		{
			for (HashMap rootCategoryMap : rootCategoryList)
			{

				CategoryInterface rootCategory = (CategoryInterface) rootCategoryMap.keySet().iterator().next();
				boolean isEdited = ((Boolean) rootCategoryMap.get(rootCategoryMap.keySet().iterator().next())).booleanValue();
				//System.out.println(isEdited+"isEdited ................rootcategoryid ="+rootCategory.getRootCategoryElement().getId());

				if (!isEdited)
				{
					//FROM ROOT CATEGORY ENTITY ID 
					EntityManagerInterface entityManager = EntityManager.getInstance();
					Container objContainer = (Container) entityManager.getContainerByEntityIdentifier(rootCategory.getRootCategoryElement().getId());

					//This container id of rootcategory entity's container ,we requires to make entry in entitymap table
					Long rootCategoryContainerId = objContainer.getId();
					Container objEntityContainer = (Container) entityManager.getContainerByEntityIdentifier(rootCategory.getRootCategoryElement()
							.getEntity().getId());

				
					DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
					List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(), "containerId", objEntityContainer.getId());

					for (EntityMap objEntityMap : entityMapList)
					{
						//create new entitymap object
						EntityMap entityMap = getEntityMap(rootCategoryContainerId, objEntityMap.getStaticEntityId());

						Collection<FormContext> existingFormContexts = Utility.getFormContexts(objEntityMap.getId());

						Set<FormContext> newFormContexts = new HashSet<FormContext>(Utility.getFormContexts(entityMap.getId()));
						for (FormContext existingFormContext : existingFormContexts)
						{
							FormContext newFormContext = new FormContext();
							newFormContext.setIsInfiniteEntry(existingFormContext.getIsInfiniteEntry());
							newFormContext.setNoOfEntries(existingFormContext.getNoOfEntries());
							newFormContext.setStudyFormLabel(existingFormContext.getStudyFormLabel());
							newFormContext.setEntityMap(entityMap);
							Collection<EntityMapCondition> existingEntityMapConditions = Utility.getEntityMapConditions(existingFormContext.getId());
							Collection<EntityMapCondition> newEntityMapConditions = Utility.getEntityMapConditions(newFormContext.getId());


							for (EntityMapCondition existingEntityMapCondition : existingEntityMapConditions)
							{
							if(existingEntityMapConditions.isEmpty())

							{
								EntityMapCondition newEntityMapCondition = new EntityMapCondition();
								newEntityMapCondition.setStaticRecordId(existingEntityMapCondition.getStaticRecordId());
								newEntityMapCondition.setTypeId(existingEntityMapCondition.getTypeId());
								newEntityMapConditions.add(newEntityMapCondition);

								EntityMapCondition objNewEntityMapCondition = new EntityMapCondition();
								Long typeId = (Long) Utility.getObjectIdentifier(Constants.COLLECTION_PROTOCOL,
										AbstractMetadata.class.getName(), Constants.NAME);
								objNewEntityMapCondition.setStaticRecordId(Long.valueOf(Constants.DEFAULT_CONDITION));
								objNewEntityMapCondition.setTypeId(typeId);
								objNewEntityMapCondition.setFormContext(newFormContext);
								newEntityMapConditions.add(objNewEntityMapCondition);
							}
							else
							{
								for (EntityMapCondition objEntityMapCondition : existingEntityMapConditions)
								{
									EntityMapCondition objNewEntityMapCondition = new EntityMapCondition();
									objNewEntityMapCondition.setStaticRecordId(objEntityMapCondition.getStaticRecordId());
									objNewEntityMapCondition.setTypeId(objEntityMapCondition.getTypeId());
									objNewEntityMapCondition.setFormContext(newFormContext);
									newEntityMapConditions.add(objNewEntityMapCondition);
								}
							}
							}
							newFormContext.setEntityMapConditionCollection(newEntityMapConditions);
							newFormContexts.add(newFormContext);

						}
						entityMap.setFormContextCollection(newFormContexts);
						AnnotationBizLogic annotation = new AnnotationBizLogic();
						annotation.insert(entityMap, Constants.HIBERNATE_DAO);

					}
				}
			}

		}
		catch (Exception ex)
		{
			System.out.println("Exception: " + ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @param containerId
	 * @param staticEntityId
	 * @return
	 * @throws DAOException
	 * @throws DynamicExtensionsSystemException
	 */
	private static EntityMap getEntityMap(Long containerId, Long staticEntityId) throws DAOException, DynamicExtensionsSystemException
	{

		EntityMap entityMap = new EntityMap();
		entityMap.setContainerId(containerId);
		entityMap.setCreatedBy("");
		entityMap.setCreatedDate(new Date());
		entityMap.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);
		entityMap.setStaticEntityId(staticEntityId);

		return entityMap;
	}

}
