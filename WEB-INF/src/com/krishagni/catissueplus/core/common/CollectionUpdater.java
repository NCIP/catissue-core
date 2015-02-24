package com.krishagni.catissueplus.core.common;

import java.util.Collection;


public class CollectionUpdater {		
	public static <T> void update(Collection<T> oldColl, Collection<T> newColl) {
		oldColl.addAll(newColl);
		oldColl.retainAll(newColl);
	}
}
