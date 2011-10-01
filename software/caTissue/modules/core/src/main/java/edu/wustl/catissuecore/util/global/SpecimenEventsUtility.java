
package edu.wustl.catissuecore.util.global;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;

public class SpecimenEventsUtility
{

	public static long getRecordIdentifier(long recordEntry, long contId)
			throws ApplicationException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, SQLException
	{

		final List<Long> recordIds = new ArrayList<Long>();
		long recordId=0;
		final NameValueBean dynamicEntity = getDynamicEntity(contId);
		EntityGroupInterface staticEntityGroup = EntityCache.getInstance().getEntityGroupById(1L);
		EntityInterface recordEntryEntity = staticEntityGroup
				.getEntityByName("edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry");

		Collection recIdList = new AnnotationBizLogic().getDynamicRecordIdFromStaticId(recordEntry,
				contId, recordEntryEntity.getId());
		if (recIdList.iterator().hasNext())
		{
			String recId = recIdList.iterator().next().toString();
			if (dynamicEntity != null
					&& dynamicEntity.getName().equalsIgnoreCase(String.valueOf(contId))
					&& !recordIds.contains(recId))
			{
				recordIds.add(Long.valueOf(recId));
				recordId=Long.valueOf(recId);
			}
		}

		return recordId;
	}

	public static NameValueBean getDynamicEntity(Long containerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean dynamicEntity = null;
		if (containerId != null)
		{
			final EntityManagerInterface entityManager = EntityManager.getInstance();
			String containerName = entityManager.getContainerCaption(containerId);
			dynamicEntity = new NameValueBean(containerId, containerName);
		}
		return dynamicEntity;
	}
}
