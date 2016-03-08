package com.krishagni.catissueplus.core.common.service;


public interface ObjectCopier<T> {
	public T copy(T source);
	
	public interface AttributesCopier<T> {
		public void copy(T source, T target);
	}
}
