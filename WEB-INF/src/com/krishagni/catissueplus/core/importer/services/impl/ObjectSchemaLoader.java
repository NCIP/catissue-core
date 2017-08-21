package com.krishagni.catissueplus.core.importer.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.importer.services.ObjectSchemaFactory;


public class ObjectSchemaLoader {
	private ObjectSchemaFactory objectSchemaFactory;
	
	public void setObjectSchemaFactory(ObjectSchemaFactory objectSchemaFactory) {
		this.objectSchemaFactory = objectSchemaFactory;
	}
	
	public void setSchemaResources(List<String> schemaResources) {
		for (String schemaResource : schemaResources) {
			objectSchemaFactory.registerSchema(schemaResource);
		}
	}
}
