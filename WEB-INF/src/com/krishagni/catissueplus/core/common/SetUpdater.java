package com.krishagni.catissueplus.core.common;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


public class SetUpdater<T> {
	
	public static <T> SetUpdater<T> newInstance() {
		return new SetUpdater<T>();		
	}
	
	public void update(Set<T> oldColl,Set<T> newColl)
	{
		oldColl.addAll(newColl);
		CollectionUtils.retainAll(oldColl, newColl);
	}
}
