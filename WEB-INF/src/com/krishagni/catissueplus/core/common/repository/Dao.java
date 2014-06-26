
package com.krishagni.catissueplus.core.common.repository;

public interface Dao<T> {

	public void saveOrUpdate(T t);
	
	public void delete(T t);
}
