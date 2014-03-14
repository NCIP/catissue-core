package com.krishagni.catissueplus.core.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapUpdater<K, V> {
	
	public static <K, V> MapUpdater<K, V> newInstance() {
		return new MapUpdater<K, V>();		
	}
	
	public void update(Map<K, V> oldColl,Map<K,V> newColl)
	{
		oldColl = oldColl==null?new HashMap<K, V>():oldColl;
		newColl = newColl==null?new HashMap<K, V>():newColl;
		Iterator<Entry<K, V>> entries = oldColl.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<K, V> entry = entries.next();
			if (!newColl.containsKey(entry.getKey())) {
				entries.remove();
			}
		}
		oldColl.putAll(newColl);
	}
	
}
