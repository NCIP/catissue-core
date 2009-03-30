package edu.wustl.catissuecore.container.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerCacheMap implements IContainerMap 
{

	private Map<IContainerCacheKey, List<ContainerCache>> containerCacheMap = new HashMap<IContainerCacheKey, List<ContainerCache>>();
	private static ContainerCacheMap wrapperContainerCacheMap;
	/**
	 *
	 */
	private ContainerCacheMap()
	{
		
	}
	
	/**
	 *
	 * @return
	 */
	public static IContainerMap getInstance()
	{
		if(wrapperContainerCacheMap==null)
		{
			wrapperContainerCacheMap = new ContainerCacheMap();
		}
		return wrapperContainerCacheMap;
	}
	/**
	 *
	 */
	public void addContainerCacheValue(IContainerCacheKey key,ContainerCache value) 
	{
		List<ContainerCache> valueList = containerCacheMap.get(key);
		
		if(valueList==null)
		{
			valueList = new ArrayList<ContainerCache>();
			containerCacheMap.put(key, valueList);
		}
		valueList.add(value);
	}
	/**
	 *
	 */
	public Object getContainerCacheValue(IContainerCacheKey key) 
	{
		return containerCacheMap.get(key);
	}
	/**
	 *
	 */
	public void removeContainerCacheValue(IContainerCacheKey key,ContainerCache value)
	{
		List<ContainerCache> valueList = containerCacheMap.get(key);
		if(valueList!=null)
		{
			valueList.remove(value);
		}
	}

}
