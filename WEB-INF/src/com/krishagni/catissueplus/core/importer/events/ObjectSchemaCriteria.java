package com.krishagni.catissueplus.core.importer.events;

import java.util.Map;

public class ObjectSchemaCriteria {
	private String objectType;
	
	private Map<String, Object> params;
	
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
