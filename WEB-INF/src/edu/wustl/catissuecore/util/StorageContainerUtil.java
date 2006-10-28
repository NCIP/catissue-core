package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class StorageContainerUtil
{

	/**
	 * This functions updates the storage container map when a new position is to be added in map
	 * @param storageContainer - Storage container whose position is freed
	 * @param continerMap - map of storage container
	 * @param x - x position which is updated
	 * @param y - y position which is updated
	 */
	public static synchronized void insertSinglePositionInContainerMap(StorageContainer storageContainer, Map containerMap, int x, int y)
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), (storageContainer.getId()));
		TreeMap storageContainerMap = (TreeMap) containerMap.get(storageContainerId);
		Integer xObj = new Integer(x);

		NameValueBean nvb = new NameValueBean(xObj, xObj);
		if (storageContainerMap == null)
		{
			storageContainerMap = new TreeMap();
			containerMap.put(storageContainerId, storageContainerMap);

		}
		List yPosList = (List) storageContainerMap.get(nvb);
		if (yPosList == null || yPosList.size() == 0)
		{
			yPosList = new ArrayList();
			yPosList.add(new NameValueBean(new Integer(y), new Integer(y)));
		}
		else
		{
			Collections.sort(yPosList);
			int size = yPosList.size();
			for (int i = 0; i < size; i++)
			{
				NameValueBean yPosNvb = (NameValueBean) yPosList.get(i);
				if (Integer.parseInt(yPosNvb.getName()) == y)
					break;
				if (Integer.parseInt(yPosNvb.getName()) > y)
				{
					yPosList.add(i, new NameValueBean(new Integer(y), new Integer(y)));
				}
			}
		}
		storageContainerMap.put(nvb, yPosList);
	}

	/**
	 * This functions updates the storage container map when a new position is to be deleted in map
	 * @param storageContainer - Storage container whose position is occupied
	 * @param continerMap - map of storage container
	 * @param x - x position which is updated
	 * @param y - y position which is updated
	 */
	public static synchronized void deleteSinglePositionInContainerMap(StorageContainer storageContainer, Map continersMap, int x, int y)
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), storageContainer.getId());
		TreeMap storageContainerMap = (TreeMap) continersMap.get(storageContainerId);

		Integer xObj = new Integer(x);

		NameValueBean nvb = new NameValueBean(xObj, xObj);

		List yPosList = (List) storageContainerMap.get(nvb);
		if (yPosList != null)
		{

			//Logger.out.debug("deleteSinglePositionInContainerMap method :----yPosList :" + yPosList);
			for (int i = 0; i < yPosList.size(); i++)
			{
				NameValueBean yPosnvb = (NameValueBean) yPosList.get(i);
				if (yPosnvb.getValue().equals("" + y))
				{
					//Logger.out.debug("Removing value:" + y);
					yPosList.remove(i);
					break;
				}

				//yPosList.remove(new NameValueBean(new Integer(y), new Integer(y)));

			}
		}
		//Logger.out.debug("deleteSinglePositionInContainerMap method after deleting :----yPosList :" + yPosList);

		if (yPosList == null || yPosList.size() == 0)
		{
			storageContainerMap.remove(nvb);
		}
		if (storageContainerMap == null || storageContainerMap.size() == 0)
		{
			continersMap.remove(storageContainerId);

		}

	}

	/**
	 * This functions updates the storage container map when a new container is added
	 * @param storageContainer - Storage container which is added
	 * @param continerMap - map of storage container
	 * @param x - x position of parent storage container
	 * @param y - y position of parent storage container
	 */
	public static synchronized void addStorageContainerInContainerMap(StorageContainer storageContainer, Map continersMap) throws DAOException
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), storageContainer.getId());
		TreeMap availabelPositionsMap = (TreeMap) getAvailablePositionMapForNewContainer(storageContainer);
		if (availabelPositionsMap != null && availabelPositionsMap.size() != 0)
		{
			continersMap.put(storageContainerId, availabelPositionsMap);
		}

		if (storageContainer.getParent() != null)
		{
			Container parentContainer = storageContainer.getParent();
			int x = storageContainer.getPositionDimensionOne().intValue();
			int y = storageContainer.getPositionDimensionTwo().intValue();
			deleteSinglePositionInContainerMap((StorageContainer) parentContainer, continersMap, x, y);
		}

	}

	/**
	 * This functions updates the storage container map when a new container is deleted or disabled
	 * @param storageContainer - Storage container which is deleted or disabled
	 * @param continerMap - map of storage container
	 * @param x - x position of parent storage container
	 * @param y - y position of parent storage container
	 */
	public static synchronized void removeStorageContainerInContainerMap(StorageContainer storageContainer, Map continersMap) throws DAOException
	{
		NameValueBean storageContainerId = new NameValueBean(storageContainer.getName(), storageContainer.getId());
		continersMap.remove(storageContainerId);

		if (storageContainer.getParent() != null)
		{
			Container parentContainer = storageContainer.getParent();
			int x = storageContainer.getPositionDimensionOne().intValue();
			int y = storageContainer.getPositionDimensionTwo().intValue();
			insertSinglePositionInContainerMap((StorageContainer) parentContainer, continersMap, x, y);
		}

	}

	/**
	 * This functions returns a map of containers from the cache.
	 * @return Returns a map of allocated containers vs. their respective free locations.
	 * @throws DAOException
	 */
	public static Map getContainerMapFromCache() throws Exception
	{
		// TODO if map is null
		// TODO move all code to common utility

		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		Map containerMap = (TreeMap) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_STORAGE_CONTAINERS);
		return containerMap;
	}

	/**
	 * This functions returns a list of disabled containers from the cache.
	 * @return Returns a list of disabled.
	 * @throws DAOException
	 */
	public static List getListOfDisabledContainersFromCache() throws Exception
	{
		// TODO if map is null
		// TODO move all code to common utility

		// getting instance of catissueCoreCacheManager and getting participantMap from cache
		CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
		List disabledconts = (ArrayList) catissueCoreCacheManager.getObjectFromCache(Constants.MAP_OF_DISABLED_CONTAINERS);
		return disabledconts;
	}

	/**
	 * This functions returns a map of available rows vs. available columns.
	 * @param container The container.
	 * @return Returns a map of available rows vs. available columns.
	 * @throws DAOException
	 */

	public static Map getAvailablePositionMapForNewContainer(StorageContainer container) throws DAOException
	{
		Map map = new TreeMap();

		if (!container.isFull().booleanValue())
		{
			final int dimX = container.getCapacity().getOneDimensionCapacity().intValue() + 1;
			final int dimY = container.getCapacity().getTwoDimensionCapacity().intValue() + 1;

			for (int x = 1; x < dimX; x++)
			{

				List list = new ArrayList();

				for (int y = 1; y < dimY; y++)
				{
					list.add(new NameValueBean(new Integer(y), new Integer(y)));

				}

				if (!list.isEmpty())
				{
					Integer xObj = new Integer(x);
					NameValueBean nvb = new NameValueBean(xObj, xObj);
					map.put(nvb, list);

				}
			}
		}

		return map;
	}


}
