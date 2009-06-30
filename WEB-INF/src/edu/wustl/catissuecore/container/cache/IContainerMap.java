
package edu.wustl.catissuecore.container.cache;

public interface IContainerMap
{

	public void addContainerCacheValue(IContainerCacheKey key, ContainerCache value);

	public Object getContainerCacheValue(IContainerCacheKey key);

	public void removeContainerCacheValue(IContainerCacheKey key, ContainerCache value);
}
