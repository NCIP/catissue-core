
package edu.wustl.catissuecore.container.cache;

/**
 * 
 */

/**
 * @author ashish_gupta
 *
 */
public class ContainerNameIdKey implements IContainerCacheKey
{

	Long containerId;
	String containerName;
	private static ContainerNameIdKeyVsContCacheMap containerNameIdKeyVsContCacheMap = (ContainerNameIdKeyVsContCacheMap) ContainerNameIdKeyVsContCacheMap
			.getInstance();

	public ContainerNameIdKey(Long containerId, String containerName)
	{
		this.containerId = containerId;
		this.containerName = containerName;
	}

	/**
	 * @return the containerId
	 */
	public Long getContainerId()
	{
		return containerId;
	}

	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(Long containerId)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean returnValue = false;
		if (obj instanceof ContainerNameIdKey)
		{
			ContainerNameIdKey key = (ContainerNameIdKey) obj;
			if ((key.getContainerId() != null && (key.getContainerId().intValue() == containerId
					.intValue() || (key.getContainerName() != null && key.getContainerName()
					.equalsIgnoreCase(containerName))))
					|| (key.getContainerName() != null && key.getContainerName().equalsIgnoreCase(
							containerName)))
			{
				returnValue = true;
			}
		}
		return returnValue;
	}

	public IContainerMap getCorrespondingContainerMap()
	{
		return containerNameIdKeyVsContCacheMap;
	}

}
