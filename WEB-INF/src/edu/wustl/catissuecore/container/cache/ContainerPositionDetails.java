
package edu.wustl.catissuecore.container.cache;

import edu.wustl.catissuecore.container.cache.domaininterface.IContainerPositionDetails;

/**
 * @author ashish_gupta
 *
 */
public class ContainerPositionDetails implements IContainerPositionDetails
{

	String containerName;
	long containerId;

	//We are keeping x and y positions instead of List<Position> so as to avoid iteration in bizlogic calling the cache.
	int xPosition;
	int yPosition;

	/**
	 * @return the containerId
	 */
	public long getContainerId()
	{
		return containerId;
	}

	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(long containerId)
	{
		this.containerId = containerId;
	}

	/**
	 * @return the containerName
	 */
	public String getContainerName()
	{
		return containerName;
	}

	/**
	 * @param containerName the containerName to set
	 */
	public void setContainerName(String containerName)
	{
		this.containerName = containerName;
	}

	/**
	 * @return the xPosition
	 */
	public int getXPosition()
	{
		return xPosition;
	}

	/**
	 * @param position the xPosition to set
	 */
	public void setXPosition(int position)
	{
		xPosition = position;
	}

	/**
	 * @return the yPosition
	 */
	public int getYPosition()
	{
		return yPosition;
	}

	/**
	 * @param position the yPosition to set
	 */
	public void setYPosition(int position)
	{
		yPosition = position;
	}

}
