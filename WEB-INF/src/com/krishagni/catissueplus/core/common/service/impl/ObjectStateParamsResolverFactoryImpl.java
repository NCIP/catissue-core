package com.krishagni.catissueplus.core.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolverFactory;

public class ObjectStateParamsResolverFactoryImpl implements ObjectStateParamsResolverFactory {
	private Map<String, ObjectStateParamsResolver> resolvers = new HashMap<>();

	@Override
	public ObjectStateParamsResolver getResolver(String objectName) {
		return resolvers.get(objectName);
	}

	public void setResolvers(List<ObjectStateParamsResolver> resolvers) {
		resolvers.forEach(resolver -> this.resolvers.put(resolver.getObjectName(), resolver));
	}
}
