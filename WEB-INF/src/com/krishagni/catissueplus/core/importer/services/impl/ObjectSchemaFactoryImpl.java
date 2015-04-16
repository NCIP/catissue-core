package com.krishagni.catissueplus.core.importer.services.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaFactory;

public class ObjectSchemaFactoryImpl implements ObjectSchemaFactory {
	
	private Map<String, ObjectSchema> schemaMap = new HashMap<String, ObjectSchema>();

	public void setSchemaMap(Map<String, ObjectSchema> schemaMap) {
		this.schemaMap = schemaMap;
	}
	
	public void setSchemaResources(List<String> schemaResources) {
		for (String schemaResource : schemaResources) {
			ObjectSchema schema = parseSchema(schemaResource);
			schemaMap.put(schema.getName(), schema);
		}
	}
	
	@Override
	public ObjectSchema getSchema(String name) {
		return schemaMap.get(name);
	}

	@Override
	public void registerSchema(String name, ObjectSchema schema) {
		schemaMap.put(name, schema);
	}
	
	private ObjectSchema parseSchema(String schemaResource) {
		InputStream in = null;
		try {
			in = Utility.getResourceInputStream(schemaResource);
			return ObjectSchema.parseSchema(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}
