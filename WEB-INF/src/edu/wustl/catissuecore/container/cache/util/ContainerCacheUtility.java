package edu.wustl.catissuecore.container.cache.util;
import edu.wustl.catissuecore.container.cache.ContainerCacheKey;
import edu.wustl.catissuecore.container.cache.ContainerNameIdKey;
import edu.wustl.catissuecore.container.cache.ContainerPositionDetails;
import edu.wustl.catissuecore.container.cache.Position;


/**
 * 
 */

/**
 * @author ashish_gupta
 *
 */
public class ContainerCacheUtility
{
//	/**
//	 * @param containerId
//	 * @param containerName
//	 * @return
//	 */
//	public static ContainerNameIdKey getNameIdKeyObject(Long containerId, String containerName)
//	{
//		ContainerNameIdKey nameIdKey = new ContainerNameIdKey();
//		nameIdKey.setContainerId(containerId);
//		nameIdKey.setContainerName(containerName);
//		
//		return nameIdKey;
//	}
	/**
	 * @param cpId
	 * @param userId
	 * @param spClass
	 * @return
	 */
	public static ContainerCacheKey getContainerCacheKey(Long cpId, Long userId, String spClass)
	{
		ContainerCacheKey containerCacheKey = new ContainerCacheKey();
		containerCacheKey.setCpId(cpId);
		containerCacheKey.setSpecimenClass(spClass);
		containerCacheKey.setUserId(userId);
		
		return containerCacheKey;
	}
	/**
	 * @param xPosition
	 * @param yPosition
	 * @return
	 */
	public static Position getPosition(int xPosition, int yPosition)
	{
		Position pos = new Position();
		pos.setPosition1(xPosition);
		pos.setPosition2(yPosition);
		
		return pos;
	}
	/**
	 * @param position
	 * @param containerNameIdKey
	 * @return
	 */
	public static ContainerPositionDetails getContainerPositionDetails(Position position, ContainerNameIdKey containerNameIdKey)
	{
		ContainerPositionDetails containerPositionDetails = new ContainerPositionDetails();
		containerPositionDetails.setContainerId(containerNameIdKey.getContainerId().longValue());	
		containerPositionDetails.setContainerName(containerNameIdKey.getContainerName());
		containerPositionDetails.setXPosition(position.getPosition1());
		containerPositionDetails.setYPosition(position.getPosition2());
		
		return containerPositionDetails;
	}
}
