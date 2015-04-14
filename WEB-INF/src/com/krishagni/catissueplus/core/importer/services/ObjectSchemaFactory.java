package com.krishagni.catissueplus.core.importer.services;

import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;

public interface ObjectSchemaFactory {
	public ObjectSchema getSchema(String name);
	
	public void registerSchema(String name, ObjectSchema schema);
}
