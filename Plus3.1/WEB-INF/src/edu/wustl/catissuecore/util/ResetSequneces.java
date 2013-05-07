package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chitra_garg
 *
 * This class is used to reset the  sequences of DE UI controls.
 *
 */
public class ResetSequneces {

	/**
	 * logger
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(ResetSequneces.class);
	/**
	 * method is used to reset the sequences
	 */
	public void resetSequences()
	{


		try{
			final List<EntityGroupInterface> entityGroupList = new ArrayList<EntityGroupInterface>();
			entityGroupList.addAll(getAnnotationGroups());
			final List<Long> containerIdList= new ArrayList<Long>();
		for (EntityGroupInterface group : entityGroupList)
		{
			int count=1;
				final Collection<EntityInterface>  entityColl =group.getEntityCollection();


				for (EntityInterface entity : entityColl) {
					final Collection<ContainerInterface>  entityContainers  = entity.getContainerCollection();

					for (ContainerInterface containerInterface : entityContainers) {

						final Collection<ControlInterface> controls=containerInterface.getControlCollection();
							for (ControlInterface controlInterface : controls) {
								if(!containerIdList.contains(controlInterface.getId()))
								{
									controlInterface.setSequenceNumber(count);
									containerIdList.add(controlInterface.getId());
									count=count+1;

								}

							}
						}

					}

				EntityGroupManager.getInstance().persistEntityGroup(group);
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}
	/**
	 * returns the EntityGroupInterface collection
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private Collection < EntityGroupInterface > getAnnotationGroups()
	throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		final Collection < EntityGroupInterface >  groupInterfaces=EntityCache.getInstance().getEntityGroups();
		final List<EntityGroupInterface> groups = new ArrayList<EntityGroupInterface>();

		 for (EntityGroupInterface entityGroupInterface : groupInterfaces) {
			if(!entityGroupInterface.getIsSystemGenerated()){
				groups.add(entityGroupInterface);
			}
		}

		return groups ;

	}
}
