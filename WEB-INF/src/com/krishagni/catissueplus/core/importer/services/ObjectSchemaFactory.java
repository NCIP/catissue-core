package com.krishagni.catissueplus.core.importer.services;

import java.util.Map;

import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;

public interface ObjectSchemaFactory {
	public ObjectSchema getSchema(String name);
	
	public ObjectSchema getSchema(String name, Map<String, String> params);
	
	public void registerSchema(String schemaResource);
	
	public void registerSchema(String name, ObjectSchema schema);

	public ObjectSchemaBuilder getSchemaBuilder(String name);

	public void registerSchemaBuilder(String name, ObjectSchemaBuilder builder);
}
