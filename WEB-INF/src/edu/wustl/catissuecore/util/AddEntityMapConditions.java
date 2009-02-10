/**
 * 
 */

package edu.wustl.catissuecore.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author suhas_khot
 * This class adds default entityMapConditions to the forms/Entities. 
 */
public final class AddEntityMapConditions
{
	
	/*
	 * create singleton object
	 */
	private static AddEntityMapConditions addEntityMapCond= new AddEntityMapConditions();
	/*
	 * Private constructor
	 */
	private AddEntityMapConditions()
	{
		
	}
	
	/*
	 * returns single object
	 */
	public static AddEntityMapConditions getInstance()
	{
		return addEntityMapCond;
	}
	
	
	/*
	 * @param args command line inputs
	 * @throws DAOException if fails to get Object from database
	 * @throws DynamicExtensionsSystemException fails to get container for all entities
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException, DAOException
	{
		Long typeId = (Long) Utility.getObjectIdentifier(Constants.COLLECTION_PROTOCOL,
				AbstractMetadata.class.getName(), Constants.NAME);
		Map<Long, Long> entityIdsVsContId = Utility.getAllContainers();
		Collection<Long> containerIdColl = (Collection) entityIdsVsContId.values();
		Long cpId = Long.valueOf(-1);
		associateFormsToCP(cpId, typeId, containerIdColl);
	}

	/**
	 * @param cpId stores Id of Collection Protocol 
	 * @param typeId stores Id of Collection Protocol object
	 * @param entityIds entityIds collection
	 * @throws DAOException if it fails to retrieve entityMapObject from database
	 */
	private static void associateFormsToCP(Long cpId, Long typeId, Collection<Long> containerIds)
			throws DAOException
	{
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		for (Long containerId : containerIds)
		{
			if (containerId != null)
			{
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(),
						Constants.CONTAINERID, containerId);
				if (entityMapList != null && !entityMapList.isEmpty())
				{
					EntityMap entityMap = entityMapList.get(0);
					Utility.editConditions(entityMap, cpId, typeId);
					annotation.updateEntityMap(entityMap);
				}
			}
		}
	}

}
