
package edu.wustl.catissuecore.upgrade;

import java.util.Collection;

import edu.common.dynamicextensions.DEIntegration.DEIntegration;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsCacheException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;

public class IntegrateDEData
{

	/**
	 * Associate records.
	 *
	 * @param containerId the container id
	 * @param staticEntityRecId the static entity rec id
	 * @param dynaEntityRecId the dyna entity rec id
	 *
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsCacheException
	 */
	public void associateRecords(Long containerId, Long staticEntityRecId, Long dynaEntityRecId) throws ApplicationException, DynamicExtensionsCacheException

	{
		EntityGroupInterface catissueEntityGroup = EntityCache.getInstance().getEntityGroupByName(
				"Catissue Suite");
		EntityInterface sppStaticEntity = catissueEntityGroup
				.getEntityByName("edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry");

		EntityManagerInterface entityManager = EntityManager.getInstance();
		try
		{
			DEIntegration integration = new DEIntegration();
			if (integration.isCategory(containerId))
			{
				//Then take container id of its root entity and then associate the record.
				containerId = entityManager.getCategoryRootContainerId(containerId);
			}
			Long dynamicEntityId = entityManager.getEntityIdByContainerId(containerId);
			EntityInterface dynamicEntity = EntityCache.getInstance()
					.getEntityById(dynamicEntityId);
			EntityInterface staticEntity = EntityCache.getInstance().getEntityById(
					Long.valueOf(sppStaticEntity.getId()));
			Collection<AssociationInterface> asntCollection = staticEntity
					.getAssociationCollection();
			AssociationInterface asntInterface = null;
			for (AssociationInterface association : asntCollection)
			{
				if (association.getTargetEntity().equals(dynamicEntity))
				{
					asntInterface = association;
					break;
				}
			}
			if (asntInterface != null)
			{
				entityManager.associateEntityRecords(asntInterface, staticEntityRecId,
						dynaEntityRecId);
			}
		}
		catch (DynamicExtensionsSystemException e)
		{
			throw new ApplicationException(ErrorKey.getErrorKey("de.common.error"), e,
					"Can not associate static and dynamic records");
		}
	}
}
