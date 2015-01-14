
package com.krishagni.catissueplus.core.common.repository;

public interface Dao<T> {
	public void saveOrUpdate(T t);
	
	public void saveOrUpdate(T t, boolean flush);
	
	public void delete(T t);
	
	public T getById(Long id);
	
	public T getById(Long id, String activeCondition);
	
	public void flush();
}
