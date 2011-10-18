/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.deintegration;

import java.util.Collection;
import java.util.Map;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsCacheException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.bizlogic.ActionApplicationBizLogic;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.upgrade.IntegrateDEData;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 *
 * @author shital_lawhale
 *
 */
public class DEIntegration extends edu.common.dynamicextensions.DEIntegration.DEIntegration
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
				entityManager.associateEntityRecords(asntInterface, staticEntityRecId,
						dynaEntityRecId, null);
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

	/**
	 * @param formContext
	 * @param action
	 * @param contextVsRecordIdMap
	 * @throws BizLogicException
	 * @throws ApplicationException
	 * @throws DynamicExtensionsCacheException
	 */
	public void insertAndHookRecordEntry(AbstractFormContext formContext, Action action,
			Map<AbstractFormContext, Long> contextVsRecordIdMap) throws BizLogicException,
			ApplicationException, DynamicExtensionsCacheException
	{

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ActionApplicationBizLogic actionAppBizLogic = (ActionApplicationBizLogic) factory
				.getBizLogic(Constants.ACTION_APP_FORM_ID);
		ActionApplicationRecordEntry actionAppRecordEntry = actionAppBizLogic
				.insertActionApplicationRecordEntry(action);
		IntegrateDEData integrateDEData = new IntegrateDEData();
		integrateDEData.associateRecords(formContext.getContainerId(),
				actionAppRecordEntry.getId(), contextVsRecordIdMap.get(formContext));
		action.setApplicationDefaultValue(actionAppRecordEntry);
		actionAppBizLogic.update(action);
	}

	/**
	 *
	 * @param staticEntityId
	 * @param dynEntContainerId
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws ApplicationException
	 */
	/*public static Long getHookEntityRecordId(String staticEntityId, String dynEntContainerId,
			String recordId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, ApplicationException
	{
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();

		Long containerId = Long.valueOf(dynEntContainerId);
		if (entityManagerInterface.isCategory(containerId) != null)
		{
			containerId = entityManagerInterface.getCategoryRootContainerId(containerId);
		}

		Long entityId = entityManagerInterface.getEntityIdByContainerId(Long.valueOf(containerId));

		String sql1 = "select dbP.name from DYEXTN_DATABASE_PROPERTIES dbP, DYEXTN_COLUMN_PROPERTIES colP, "
				+ "DYEXTN_CONSTRAINTKEY_PROP ckP, dyextn_constraint_properties conP, dyextn_association da, "
				+ "dyextn_attribute dt where dbP.identifier=colP.identifier and colP.CNSTR_KEY_PROP_ID=ckP.identifier and "
				+ "ckP.TGT_CONSTRAINT_KEY_ID=conP.identifier and conP.association_id=da.identifier "
				+ " and da.identifier=dt.identifier and da.target_entity_id= "
				+ entityId
				+ " and dt.entiy_id= " + staticEntityId;

		List list1 = AppUtility.executeSQLQuery(sql1);
		List obj = (List) list1.get(0);
		String whereColumnName = obj.get(0).toString();

		String deTablename = entityManagerInterface.getDynamicTableName(Long.valueOf(containerId));

		String sql = "select " + whereColumnName + " from " + deTablename + " where identifier="
				+ recordId;

		List list = AppUtility.executeSQLQuery(sql);
		Long recordEntryId = null;
		if (list != null && !list.isEmpty())
		{
			List lst = (List) list.get(0);
			if (lst != null && !lst.isEmpty())
			{
				Object o = lst.get(0);
				if (o != null && !"".equals(o.toString()))
				{
					recordEntryId = Long.valueOf(o.toString());
				}
			}
		}

		return recordEntryId;

	}*/
}
