package com.krishagni.catissueplus.core.importer.events;

public class ImportObjectDetail<T> {
	private T object;
	
	private boolean create;

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}
}
