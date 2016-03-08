package com.krishagni.catissueplus.core.common.service;

import java.util.Map;

public interface ObjectStateParamsResolver {
	public String getObjectName();

	public Map<String, Object> resolve(String key, Object value);
}
