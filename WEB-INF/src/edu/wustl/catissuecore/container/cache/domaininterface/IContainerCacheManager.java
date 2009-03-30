/**
 * <p>Title: IContainerCacheManager Interface>
 * <p>Description:  An interface for Container Cache management</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author ashish_gupta
 * @version 1.00
 */
package edu.wustl.catissuecore.container.cache.domaininterface;
import java.io.Serializable;
import java.util.List;

import edu.wustl.catissuecore.container.cache.ContainerCache;
import edu.wustl.catissuecore.container.cache.ContainerNameIdKey;
import edu.wustl.catissuecore.container.cache.ContainerPositionDetails;


public interface IContainerCacheManager extends Serializable
{

	/**
	 * @param containerId - The container identifier to which position is to be added.
	 * @param xPosition - The x- position to be added
	 * @param yPosition - The y - position to be added
	 */
	
	
	public void addPosition(Long containerId, int xPosition, int yPosition);

	/**
	 * @param containerName - The container name to which position is to be added.
	 * @param xPosition - The x- position to be added
	 * @param yPosition - The y - position to be added
	 */
	public void addPosition(String containerName, int xPosition, int yPosition);

	/**
	 * @param cpId
	 * @param specimenClass
	 * @param userId
	 * @param count
	 * @return
	 */
	public List<ContainerPositionDetails> getContainerFreePositions(Long cpId, String specimenClass, Long userId,
			int count);

	/**
	 * @return
	 */
	public ContainerPositionDetails getNextFreePosition(Long cpId, String specimenClass, Long userId);

	/**
	 * @param cpId
	 * @param userId
	 * @param spClass
	 * @return
	 */
	public List<ContainerCache> getContainerCacheObjects(Long cpId, Long userId, String spClass);

	/**
	 * @param containerId
	 * @param xPosition
	 * @param yPosition
	 */
	public void removePosition(Long containerId, int xPosition, int yPosition);

	/**
	 * @param containerName
	 * @param xPosition
	 * @param yPosition
	 */
	public void removePosition(String containerName, int xPosition, int yPosition);

	/**
	 * @param conatinerId
	 * @param xPosition
	 * @param yPosition
	 * @return
	 */
	public boolean isPositionFree(Long containerId, int xPosition, int yPosition);

	/**
	 * @param conatinerName
	 * @param xPosition
	 * @param yPosition
	 * @return
	 */
	public boolean isPositionFree(String containerName, int xPosition, int yPosition);

	/**
	 * 
	 */
	public void loadCache();
	/**
	 * 
	 */
	public void initialize();
	
//	public void addContainerToCache(Long cpId, Long userId, String spClass, List<ContainerPositionDetails> containerPositionDetailsList);		
//	public void addStorageContainerInMaps(StorageContainer storageCont, boolean isNew);
//	public void updateStoragePositions();	
// 	public void addStorageContainerInMaps(IContainerCacheKey key, ContainerCache value) throws Exception;

	public void removeStorageContainer(ContainerNameIdKey nameIdKey);
	public void removeStorageContainer(Long containerId);
	public void removeStorageContainer(String containerName);
}
