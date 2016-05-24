package com.krishagni.catissueplus.core.importer.events;

import java.util.HashMap;
import java.util.Map;

public class ImportObjectDetail<T> {
	private T object;
	
	private boolean create;
	
	private Map<String, String> params = new HashMap<>();

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
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
