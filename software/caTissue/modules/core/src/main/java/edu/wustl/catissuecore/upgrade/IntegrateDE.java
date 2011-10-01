/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.upgrade;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 *
 * @author shital_lawhale
 *
 */

public class IntegrateDE extends edu.common.dynamicextensions.DEIntegration.DEIntegration
{

	/**
	 *
	 * @param containerId
	 * @param staticEntityRecId
	 * @param dynaEntityRecId
	 * @param hookEntityId
	 * @param hibernateDAO
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws ApplicationException
	 */
	public void associateRecords(Long containerId, Long staticEntityRecId, Long dynaEntityRecId,
			Long hookEntityId, HibernateDAO hibernateDAO)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException,
			ApplicationException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		try
		{
			if (isCategory(containerId))
			{
				//Then take container id of its root entity and then associate the record.
				containerId = entityManager.getCategoryRootContainerId(containerId);

			}
			Long dynamicEntityId = entityManager.getEntityIdByContainerId(containerId);
			EntityInterface dynamicEntity = EntityCache.getInstance()
					.getEntityById(dynamicEntityId);
			EntityInterface staticEntity = EntityCache.getInstance().getEntityById(
					Long.valueOf(hookEntityId));
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
				entityManager.associateEntityRecordsForAPI(asntInterface, staticEntityRecId,
						dynaEntityRecId);
			}
		}
		catch (DynamicExtensionsSystemException e)
		{
			throw new DAOException(ErrorKey.getErrorKey("de.common.error"), e,
					"Can not associate static and dynamic records");
		}
	}

	/**
	 *
	 * @param containerId
	 * @return whether this entity is simple DE form /category.
	 * @throws DynamicExtensionsSystemException
	 */
	@Override
	public boolean isCategory(Long containerId) throws DynamicExtensionsSystemException
	{
		edu.common.dynamicextensions.DEIntegration.DEIntegration integrate = new edu.common.dynamicextensions.DEIntegration.DEIntegration();
		return integrate.isCategory(containerId);

	}
}
