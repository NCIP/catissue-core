package com.krishagni.catissueplus.core.administrative.events;


public interface Mergeable<K, V> {
	public K getMergeKey();
	
	public void merge(V other);
}
