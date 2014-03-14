package com.krishagni.catissueplus.core.common;

import java.util.HashSet;
import java.util.Set;


public class SetUpdater<T> {
	
	public static <T> SetUpdater<T> newInstance() {
		return new SetUpdater<T>();		
	}
	
	public void update(Set<T> oldColl,Set<T> newColl)
	{
		oldColl = oldColl==null?new HashSet<T>():oldColl;
		newColl = newColl==null?new HashSet<T>():newColl;
		Set<T> addedObjects = new HashSet<T>();
		for (T object : newColl) {
			if (!oldColl.contains(object)) {
				addedObjects.add(object);
			}
		}
		Set<T> deletedObjects = new HashSet<T>();
		for (T object : oldColl) {
			if (!newColl.contains(object)) {
				deletedObjects.add(object);
			}
		}

		oldColl.removeAll(deletedObjects);
		oldColl.addAll(addedObjects);
	}
}
