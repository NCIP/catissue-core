
package edu.wustl.catissuecore.annotations.xmi;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
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
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;

/**
 * @author falguni_sachde
 *
 */
public class DECategoryIntegration
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(DECategoryIntegration.class);

	/**
	 * @param rootCategoryList - rootCategoryList
	 */
	public static void categoryIntegration(List<HashMap> rootCategoryList)
	{
		try
		{
			for (final HashMap rootCategoryMap : rootCategoryList)
			{

				final CategoryInterface rootCategory = (CategoryInterface) rootCategoryMap.keySet()
						.iterator().next();
				final boolean isEdited = ((Boolean) rootCategoryMap.get(rootCategoryMap.keySet()
						.iterator().next())).booleanValue();
				//System.out.println(isEdited+"isEdited ................rootcategoryid ="+rootCategory.getRootCategoryElement().getId());

				if (!isEdited)
				{
					//FROM ROOT CATEGORY ENTITY ID
					final EntityManagerInterface entityManager = EntityManager.getInstance();
					final Container objContainer = (Container) entityManager
							.getContainerByEntityIdentifier(rootCategory.getRootCategoryElement()
									.getId());

					//This container id of rootcategory entity's container ,we requires to make entry in entitymap table
					final Long rootCategoryContainerId = objContainer.getId();
					final Container objEntityContainer = (Container) entityManager
							.getContainerByEntityIdentifier(rootCategory.getRootCategoryElement()
									.getEntity().getId());

					final DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
					final List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class
							.getName(), "containerId", objEntityContainer.getId());

					for (final EntityMap objEntityMap : entityMapList)
					{
						//create new entitymap object
						final EntityMap entityMap = getEntityMap(rootCategoryContainerId,
								objEntityMap.getStaticEntityId());

						final Collection<FormContext> existingFormContexts = AppUtility
								.getFormContexts(objEntityMap.getId());

						final Set<FormContext> newFormContexts = new HashSet<FormContext>(
								AppUtility.getFormContexts(entityMap.getId()));
						for (final FormContext existingFormContext : existingFormContexts)
						{
							final FormContext newFormContext = new FormContext();
							newFormContext.setIsInfiniteEntry(existingFormContext
									.getIsInfiniteEntry());
							newFormContext.setNoOfEntries(existingFormContext.getNoOfEntries());
							newFormContext.setStudyFormLabel(existingFormContext
									.getStudyFormLabel());
							newFormContext.setEntityMap(entityMap);
							final Collection<EntityMapCondition> existingEntityMapConditions = AppUtility
									.getEntityMapConditions(existingFormContext.getId());
							final Collection<EntityMapCondition> newEntityMapConditions = AppUtility
									.getEntityMapConditions(newFormContext.getId());
							if (!existingEntityMapConditions.isEmpty())

							{
								for (final EntityMapCondition objEntityMapCondition : existingEntityMapConditions)
								{
									final EntityMapCondition objNewEntityMapCondition = new EntityMapCondition();
									objNewEntityMapCondition
											.setStaticRecordId(objEntityMapCondition
													.getStaticRecordId());
									objNewEntityMapCondition.setTypeId(objEntityMapCondition
											.getTypeId());
									objNewEntityMapCondition.setFormContext(newFormContext);
									newEntityMapConditions.add(objNewEntityMapCondition);
								}
							}
							newFormContext.setEntityMapConditionCollection(newEntityMapConditions);
							newFormContexts.add(newFormContext);

						}
						entityMap.setFormContextCollection(newFormContexts);
						final AnnotationBizLogic annotation = new AnnotationBizLogic();
						annotation.setAppName(DynamicExtensionDAO.getInstance().getAppName());
						annotation.insert(entityMap, 0);

					}
				}
			}

		}
		catch (final Exception ex)
		{
			logger.debug(ex.getMessage(), ex);
			//System.out.println("Exception: " + ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @param containerId - containerId
	 * @param staticEntityId - staticEntityId
	 * @return - EntityMap
	 * @throws DAOException - DAOException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 */
	private static EntityMap getEntityMap(Long containerId, Long staticEntityId)
			throws DAOException, DynamicExtensionsSystemException
	{

		final EntityMap entityMap = new EntityMap();
		entityMap.setContainerId(containerId);
		entityMap.setCreatedBy("");
		entityMap.setCreatedDate(new Date());
		entityMap.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);
		entityMap.setStaticEntityId(staticEntityId);

		return entityMap;
	}

}
