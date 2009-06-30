
package edu.wustl.catissuecore.container.cache;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ContainerCache
{

	List<Position> freePositions;

	ContainerNameIdKey containerNameIdKey;

	Set<IContainerCacheKey> correspondingContainerKeySet = new HashSet<IContainerCacheKey>();

	/**
	 * 
	 */
	public ContainerCache()
	{

	}

	public ContainerCache(ContainerNameIdKey containerNameIdKey, List<Position> freePositions)
	{
		this.containerNameIdKey = containerNameIdKey;
		this.freePositions = freePositions;
	}

	/**
	 * @return the containerNameIdKey
	 */
	public ContainerNameIdKey getContainerNameIdKey()
	{
		return containerNameIdKey;
	}

	/**
	 * @param containerNameIdKey the containerNameIdKey to set
	 */
	public void setContainerNameIdKey(ContainerNameIdKey containerNameIdKey)
	{
		this.containerNameIdKey = containerNameIdKey;
	}

	/**
	 * @return the availablePos
	 */
	public List<Position> getFreePositions()
	{
		return freePositions;
	}

	/**
	 * @param availablePos the availablePos to set
	 */
	public void setFreePositions(List<Position> freePositions)
	{
		this.freePositions = freePositions;
	}

	public void addPosition(int pos1, int pos2)
	{
		Position pos = new Position();
		pos.setPosition1(pos1);
		pos.setPosition2(pos2);

		freePositions.add(pos);
	}

	public void removePosition(int pos1, int pos2)
	{
		Position pos = new Position();
		pos.setPosition1(pos1);
		pos.setPosition2(pos2);

		freePositions.remove(pos);
	}

	public Set<IContainerCacheKey> getCorrespondingContainerKeys()
	{
		return correspondingContainerKeySet;
	}

	public void addCorrespondingContainerKey(IContainerCacheKey correspondingContainerKey)
	{
		correspondingContainerKeySet.add(correspondingContainerKey);
		correspondingContainerKey.getCorrespondingContainerMap().addContainerCacheValue(
				correspondingContainerKey, this);
	}

	public void removeCorrespondingContainerKey(IContainerCacheKey correspondingContainerKey)
	{
		if (correspondingContainerKeySet.contains(correspondingContainerKey))
		{
			correspondingContainerKey.getCorrespondingContainerMap().removeContainerCacheValue(
					correspondingContainerKey, this);
			correspondingContainerKeySet.remove(correspondingContainerKey);
		}

	}

	public void removeAllCorrespondingContainerKeys()
	{
		Iterator<IContainerCacheKey> iterator = correspondingContainerKeySet.iterator();
		while (iterator.hasNext())
		{
			IContainerCacheKey cacheKey = iterator.next();
			cacheKey.getCorrespondingContainerMap().removeContainerCacheValue(cacheKey, this);
			iterator.remove();
			//correspondingContainerKeySet.remove(cacheKey);
		}
	}
}
