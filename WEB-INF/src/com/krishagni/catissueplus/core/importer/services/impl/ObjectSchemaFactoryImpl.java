package com.krishagni.catissueplus.core.importer.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaFactory;

public class ObjectSchemaFactoryImpl implements ObjectSchemaFactory {
	
	private Map<String, ObjectSchema> schemaMap = new HashMap<String, ObjectSchema>();

	public void setSchemaMap(Map<String, ObjectSchema> schemaMap) {
		this.schemaMap = schemaMap;
	}
	
	@Override
	public ObjectSchema getSchema(String name) {
		return schemaMap.get(name);
	}

	@Override
	public void registerSchema(String name, ObjectSchema schema) {
		schemaMap.put(name, schema);
	}
}
