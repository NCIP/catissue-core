package com.krishagni.catissueplus.core.importer.events;

import java.util.HashMap;
import java.util.Map;

public class ImportObjectDetail<T> {
	private T object;
	
	private boolean create;
	
	private Map<String, Object> params = new HashMap<String, Object>();

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
	
	public Map<String, Object> getParams() {
		return params;
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
