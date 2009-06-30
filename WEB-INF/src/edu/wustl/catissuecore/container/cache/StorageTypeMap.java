
package edu.wustl.catissuecore.container.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageTypeMap implements IContainerMap
{

	private Map<IContainerCacheKey, List<ContainerCache>> storageTypeMap = new HashMap<IContainerCacheKey, List<ContainerCache>>();
	private static StorageTypeMap wrapperStorageTypeMap;

	private StorageTypeMap()
	{

	}

	public static IContainerMap getInstance()
	{
		if (wrapperStorageTypeMap == null)
		{
			wrapperStorageTypeMap = new StorageTypeMap();
		}
		return wrapperStorageTypeMap;
	}

	public void addContainerCacheValue(IContainerCacheKey key, ContainerCache value)
	{
		List<ContainerCache> valueList = storageTypeMap.get(key);

		if (valueList == null)
		{
			valueList = new ArrayList<ContainerCache>();
			storageTypeMap.put(key, valueList);
		}
		valueList.add(value);
	}

	public Object getContainerCacheValue(IContainerCacheKey key)
	{
		return storageTypeMap.get(key);
	}

	public void removeContainerCacheValue(IContainerCacheKey key, ContainerCache value)
	{
		List<ContainerCache> valueList = storageTypeMap.get(key);
		if (valueList != null)
		{
			valueList.remove(value);
		}
	}

}
