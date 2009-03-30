package edu.wustl.catissuecore.container.cache;

import java.util.HashMap;
import java.util.Map;

public class ContainerNameIdKeyVsContCacheMap implements IContainerMap 
{

	private Map<IContainerCacheKey, ContainerCache> containerNameIdKeyVsContCacheMap = new HashMap<IContainerCacheKey, ContainerCache>();
	private static ContainerNameIdKeyVsContCacheMap wrapperContainerNameIdKeyVsContCacheMap;
	
	private ContainerNameIdKeyVsContCacheMap()
	{
		
	}
	
	public static IContainerMap getInstance()
	{
		if(wrapperContainerNameIdKeyVsContCacheMap==null)
		{
			wrapperContainerNameIdKeyVsContCacheMap = new ContainerNameIdKeyVsContCacheMap();
		}
		return wrapperContainerNameIdKeyVsContCacheMap;
	}
	
	public void addContainerCacheValue(IContainerCacheKey key,ContainerCache value) 
	{
		containerNameIdKeyVsContCacheMap.put(key, value);
	}

	public Object getContainerCacheValue(IContainerCacheKey key) 
	{
		return containerNameIdKeyVsContCacheMap.get(key);
	}
	
	public void removeContainerCacheValue(IContainerCacheKey key,ContainerCache value)
	{
		containerNameIdKeyVsContCacheMap.remove(key);
	}

}
