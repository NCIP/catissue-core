
package edu.wustl.catissuecore.annotations.xmi;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
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
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

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

						Collection<FormContext> formContextList = objEntityMap.getFormContextCollection();

						Collection<FormContext> formContextNewList = entityMap.getFormContextCollection();
						for (FormContext objFormContext : formContextList)
						{
							FormContext objNewFormContext = new FormContext();
							objNewFormContext.setIsInfiniteEntry(objFormContext.getIsInfiniteEntry());
							objNewFormContext.setNoOfEntries(objFormContext.getNoOfEntries());
							objNewFormContext.setStudyFormLabel(objFormContext.getStudyFormLabel());
							objNewFormContext.setEntityMap(entityMap);
							Collection<EntityMapCondition> entityMapConditionList = objFormContext.getEntityMapConditionCollection();
							Collection<EntityMapCondition> entityMapConditionNewList = objNewFormContext.getEntityMapConditionCollection();

							for (EntityMapCondition objEntityMapCondition : entityMapConditionList)
							{
								EntityMapCondition objNewEntityMapCondition = new EntityMapCondition();
								objNewEntityMapCondition.setStaticRecordId(objEntityMapCondition.getStaticRecordId());
								objNewEntityMapCondition.setTypeId(objEntityMapCondition.getTypeId());
								entityMapConditionNewList.add(objNewEntityMapCondition);

							}
							objNewFormContext.setEntityMapConditionCollection(entityMapConditionNewList);
							formContextNewList.add(objNewFormContext);

						}
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
