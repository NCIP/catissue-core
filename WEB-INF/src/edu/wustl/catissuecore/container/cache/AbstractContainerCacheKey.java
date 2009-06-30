
package edu.wustl.catissuecore.container.cache;

import java.util.Map;

public abstract class AbstractContainerCacheKey implements IContainerCacheKey
{

	private static Map<IContainerCacheKey, Object> cacheMap;

	public static Map<IContainerCacheKey, Object> getCorrespondingMap()
	{
		return cacheMap;
	}

	public static void setCorrespondingMap(Map<IContainerCacheKey, Object> map)
	{
		if (cacheMap != null)
		{
			cacheMap = map;
		}
	}

	public abstract String getMapName();

}
