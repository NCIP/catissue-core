package com.krishagni.catissueplus.core.importer.services.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.krishagni.catissueplus.core.common.service.TemplateService;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaBuilder;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaFactory;


public class ObjectSchemaFactoryImpl implements ObjectSchemaFactory {
	private static final String COLLECTION_PROTOCOL_EXTENSION = "CollectionProtocolExtension";
	
	private static final String PARTICIPANT_EXTENSION = "ParticipantExtension";
	
	private static final String SITE_EXTENSION = "SiteExtension";
	
	private static Map<String, String> customExtensionForms = new HashMap<String, String>();
	
	static {
		customExtensionForms.put("collectionProtocol", COLLECTION_PROTOCOL_EXTENSION);
		customExtensionForms.put("participant", PARTICIPANT_EXTENSION);
		customExtensionForms.put("site", SITE_EXTENSION);
	}
	
	private TemplateService templateService;
	
	private Map<String, ObjectSchema> schemaMap = new HashMap<String, ObjectSchema>();
	
	private Map<String, ObjectSchemaBuilder> schemaBuilders = new HashMap<String, ObjectSchemaBuilder>();
	
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	public void setSchemaMap(Map<String, ObjectSchema> schemaMap) {
		this.schemaMap = schemaMap;
	}
	
	public void setSchemaBuilders(Map<String, ObjectSchemaBuilder> schemaBuilders) {
		this.schemaBuilders = schemaBuilders;
	}
	
	public void setSchemaResources(List<String> schemaResources) {
		for (String schemaResource : schemaResources) {
			ObjectSchema schema = parseSchema(schemaResource);
			schemaMap.put(schema.getName(), schema);
		}
	}
	
	@Override
	public ObjectSchema getSchema(String name) {
		return getSchema(name, Collections.<String, Object>emptyMap());
	}
	
	@Override
	public ObjectSchema getSchema(String name, Map<String, Object> params) {
		ObjectSchema schema = schemaMap.get(name);
		if (schema != null) {
			addExtensionRecord(schema, name);
			return schema;
		}
		
		ObjectSchemaBuilder builder = schemaBuilders.get(name);
		if (builder != null) {
			schema = builder.getObjectSchema(params);
		}
		
		return schema;
	}

	@Override
	public void registerSchema(String name, ObjectSchema schema) {
		schemaMap.put(name, schema);
	}
	
	private void addExtensionRecord(ObjectSchema schema, String name) {
		String entityType = customExtensionForms.get(name);
		if (entityType == null) {
			return;
		}

		ObjectSchema extension = null;
		ObjectSchemaBuilder builder = schemaBuilders.get("extensions");
		if (builder != null) {
			extension = builder.getObjectSchema(entityType);
		}
		
		if (extension != null) {
			schema.getRecord().getSubRecords().add(extension.getRecord());
		}
	}
	
	private ObjectSchema parseSchema(String schemaResource) {
		InputStream in = null;
		try {
			in = preprocessSchema(schemaResource);
			return ObjectSchema.parseSchema(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	private InputStream preprocessSchema(String schemaResource) {
		String template = templateService.render(schemaResource, new HashMap<String, Object>());
		return new ByteArrayInputStream( template.getBytes() );
	}
}
