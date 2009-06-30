
package edu.wustl.catissuecore.container.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.container.cache.domaininterface.IContainerCacheManager;
import edu.wustl.catissuecore.container.cache.util.ContainerCacheUtility;

/**
 * @author ashish_gupta
 *
 */
public class ContainerCacheManagerImpl implements IContainerCacheManager
{

	private static final long serialVersionUID = 1L;

	IContainerMap containerCacheMap;
	IContainerMap containerNameIdKeyVsContCacheMap;
	IContainerMap storageTypeMap;
	IContainerMap specimenArrayTypeMap;

	public void initialize()
	{
		containerCacheMap = ContainerCacheMap.getInstance();
		containerNameIdKeyVsContCacheMap = ContainerNameIdKeyVsContCacheMap.getInstance();
		storageTypeMap = StorageTypeMap.getInstance();
		specimenArrayTypeMap = SpecimenArrayTypeMap.getInstance();
	}

	/**
	 * This method adds a freed up position to the master map for the given container id.
	 */
	public void addPosition(Long containerId, int xPosition, int yPosition)
	{
		ContainerNameIdKey nameIdkey = new ContainerNameIdKey(containerId, null);
		ContainerCache containerCache = (ContainerCache) containerNameIdKeyVsContCacheMap
				.getContainerCacheValue(nameIdkey);//containerNameIdKeyVsContCacheMap.get(nameIdkey);
		if (containerCache != null)
		{
			containerCache.addPosition(xPosition, yPosition);
		}
	}

	/**
	 * This method adds a freed up position to the master map for the given container name.
	 */
	public void addPosition(String containerName, int xPosition, int yPosition)
	{
		ContainerNameIdKey nameIdKey = new ContainerNameIdKey(null, containerName);
		ContainerCache containerCache = (ContainerCache) containerNameIdKeyVsContCacheMap
				.getContainerCacheValue(nameIdKey);//containerNameIdKeyVsContCacheMap.get(nameIdkey);
		if (containerCache != null)
		{
			containerCache.addPosition(xPosition, yPosition);
		}
	}

	/**
	 * This method returns the ContainerCache object for the given key
	 */
	public List<ContainerCache> getContainerCacheObjects(Long cpId, Long userId, String spClass)
	{
		ContainerCacheKey containerCacheKey = ContainerCacheUtility.getContainerCacheKey(cpId,
				userId, spClass);
		List<ContainerCache> containerCacheList = (List) containerCacheMap
				.getContainerCacheValue(containerCacheKey); //containerCacheMap.get(containerCacheKey);
		return containerCacheList;
	}

	/**
	 * This method returns true if the given position is present in the container cache i.e. it is free
	 */
	public boolean isPositionFree(Long containerId, int xPosition, int yPosition)
	{
		Position pos = ContainerCacheUtility.getPosition(xPosition, yPosition);
		ContainerNameIdKey nameIdKey = new ContainerNameIdKey(containerId, null);
		ContainerCache containerCache = (ContainerCache) containerNameIdKeyVsContCacheMap
				.getContainerCacheValue(nameIdKey);//containerNameIdKeyVsContCacheMap.get(nameIdKey);
		Collection<Position> freePositions = containerCache.getFreePositions();
		boolean isPositionFree = freePositions.contains(pos);

		return isPositionFree;
	}

	/**
	 * This method returns true if the given position is present in the container cache i.e. it is free
	 */
	public boolean isPositionFree(String containerName, int xPosition, int yPosition)
	{
		Position pos = ContainerCacheUtility.getPosition(xPosition, yPosition);
		ContainerNameIdKey nameIdKey = new ContainerNameIdKey(null, containerName);
		ContainerCache containerCache = (ContainerCache) containerNameIdKeyVsContCacheMap
				.getContainerCacheValue(nameIdKey);//containerNameIdKeyVsContCacheMap.get(nameIdKey);
		Collection<Position> freePositions = containerCache.getFreePositions();
		boolean isPositionFree = freePositions.contains(pos);

		return isPositionFree;
	}

	/**
	 * This method loads the container cache from the db.
	 */
	public void loadCache()
	{

	}

	/**
	 * This method removes a position for the given container id.
	 */
	public void removePosition(Long containerId, int xPosition, int yPosition)
	{
		ContainerNameIdKey nameIdKey = new ContainerNameIdKey(containerId, null);
		ContainerCache containerCache = (ContainerCache) containerNameIdKeyVsContCacheMap
				.getContainerCacheValue(nameIdKey);//containerNameIdKeyVsContCacheMap.get(nameIdkey);
		if (containerCache != null)
		{
			containerCache.removePosition(xPosition, yPosition);
		}

	}

	/**
	 * This method removes a position for the given container name.
	 */
	public void removePosition(String containerName, int xPosition, int yPosition)
	{
		ContainerNameIdKey nameIdKey = new ContainerNameIdKey(null, containerName);
		ContainerCache containerCache = (ContainerCache) containerNameIdKeyVsContCacheMap
				.getContainerCacheValue(nameIdKey);//containerNameIdKeyVsContCacheMap.get(nameIdkey);
		if (containerCache != null)
		{
			containerCache.removePosition(xPosition, yPosition);
		}

	}

	/**
	 * This method returns the requested number of free positions from one or multiple containers.
	 */
	public List<ContainerPositionDetails> getContainerFreePositions(Long cpId,
			String specimenClass, Long userId, int count)
	{
		ContainerCacheKey containerCacheKey = ContainerCacheUtility.getContainerCacheKey(cpId,
				userId, specimenClass);
		List<ContainerCache> containerCacheList = (List) containerCacheMap
				.getContainerCacheValue(containerCacheKey);//containerCacheMap.get(containerCacheKey);

		List<ContainerPositionDetails> containerPositionDetailsList = new ArrayList<ContainerPositionDetails>();

		int posCounter = count;
		for (int i = 0; i < containerCacheList.size(); i++)
		{
			ContainerCache containerCache = containerCacheList.get(i);
			List<Position> freePositions = containerCache.getFreePositions();

			for (int j = 0; j < freePositions.size(); j++)
			{
				if (posCounter != 0)
				{
					Position position = freePositions.get(j);

					ContainerNameIdKey containerNameIdKey = containerCache.getContainerNameIdKey();

					ContainerPositionDetails containerPositionDetails = ContainerCacheUtility
							.getContainerPositionDetails(position, containerNameIdKey);

					containerPositionDetailsList.add(containerPositionDetails);
					posCounter--;
				}
			}
		}

		return containerPositionDetailsList;
	}

	/**
	 *This method returns the next free position for the given key from the cache 
	 */
	public ContainerPositionDetails getNextFreePosition(Long cpId, String specimenClass, Long userId)
	{
		ContainerCacheKey containerCacheKey = ContainerCacheUtility.getContainerCacheKey(cpId,
				userId, specimenClass);
		List<ContainerCache> containerCacheList = (List) containerCacheMap
				.getContainerCacheValue(containerCacheKey);//containerCacheMap.get(containerCacheKey);

		for (ContainerCache containerCache : containerCacheList)
		{
			if (containerCache.getFreePositions().size() > 0)
			{
				Position pos = containerCache.getFreePositions().get(0);

				ContainerPositionDetails containerPositionDetails = ContainerCacheUtility
						.getContainerPositionDetails(pos, containerCache.getContainerNameIdKey());

				return containerPositionDetails;
			}
		}
		return null;
	}

	public void removeStorageContainer(ContainerNameIdKey nameIdKey)
	{
		ContainerCache containerCache = (ContainerCache) containerNameIdKeyVsContCacheMap
				.getContainerCacheValue(nameIdKey);
		if (containerCache != null)
		{
			containerCache.removeAllCorrespondingContainerKeys();
		}
	}

	public void removeStorageContainer(Long containerId)
	{
		ContainerNameIdKey nameIdKey = new ContainerNameIdKey(containerId, null);
		removeStorageContainer(nameIdKey);
	}

	public void removeStorageContainer(String containerName)
	{
		ContainerNameIdKey nameIdKey = new ContainerNameIdKey(null, containerName);
		removeStorageContainer(nameIdKey);
	}

	/*public void updateStoragePositions()
	{
	//		int xOld = oldContainer.getCapacity().getOneDimensionCapacity().intValue();
	//		int xNew = currentContainer.getCapacity().getOneDimensionCapacity().intValue();
	//		int yOld = oldContainer.getCapacity().getTwoDimensionCapacity().intValue();
	//		int yNew = currentContainer.getCapacity().getTwoDimensionCapacity().intValue();		
	}
	
	public void addStorageContainerInMaps(IContainerCacheKey key, ContainerCache value) throws Exception
	{
			
		key.getCorrespondingContainerMap().addContainerCacheValue(key, value);
	}*/

}
