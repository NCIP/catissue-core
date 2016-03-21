package com.krishagni.catissueplus.core.common;

public interface EntityCrudListener<T, U> {
	public void onRead(T output, U entity);

	public void onSave(T input, T output, U entity);

	public void onDelete(T input, T output, U entity);
}
