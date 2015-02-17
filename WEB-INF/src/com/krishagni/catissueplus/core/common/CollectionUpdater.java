package com.krishagni.catissueplus.core.common;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

public class CollectionUpdater {

	public static <T> void update(Collection<T> oldColl, Collection<T> newColl) {
		oldColl.addAll(newColl);
		CollectionUtils.retainAll(oldColl, newColl);
	}
}
