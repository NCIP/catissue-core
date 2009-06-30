
package edu.wustl.catissuecore.container.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecimenArrayTypeMap implements IContainerMap
{

	private Map<IContainerCacheKey, List<ContainerCache>> specimenArrayTypeMap = new HashMap<IContainerCacheKey, List<ContainerCache>>();
	private static SpecimenArrayTypeMap wrapperSpecimenArrayTypeMap;

	private SpecimenArrayTypeMap()
	{

	}

	public static IContainerMap getInstance()
	{
		//		if(wrapperSpecimenArrayTypeMap==null)
		//		{
		//			wrapperSpecimenArrayTypeMap = new SpecimenArrayTypeMap();
		//		}
		//		return wrapperSpecimenArrayTypeMap;
		return null;
	}

	public void addContainerCacheValue(IContainerCacheKey key, ContainerCache value)
	{
		List<ContainerCache> valueList = specimenArrayTypeMap.get(key);

		if (valueList == null)
		{
			valueList = new ArrayList<ContainerCache>();
			specimenArrayTypeMap.put(key, valueList);
		}
		valueList.add(value);
	}

	public Object getContainerCacheValue(IContainerCacheKey key)
	{
		return specimenArrayTypeMap.get(key);
	}

	public void removeContainerCacheValue(IContainerCacheKey key, ContainerCache value)
	{
		List<ContainerCache> valueList = specimenArrayTypeMap.get(key);
		if (valueList != null)
		{
			valueList.remove(value);
		}
	}

}
