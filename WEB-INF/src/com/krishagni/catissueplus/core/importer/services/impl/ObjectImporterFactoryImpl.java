package com.krishagni.catissueplus.core.importer.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.importer.services.ObjectImporter;
import com.krishagni.catissueplus.core.importer.services.ObjectImporterFactory;

public class ObjectImporterFactoryImpl implements ObjectImporterFactory {
	
	private Map<String, ObjectImporter<?>> importersMap = new HashMap<String, ObjectImporter<?>>();
	
	
	public void setImportersMap(Map<String, ObjectImporter<?>> importersMap) {
		this.importersMap = importersMap;
	}

	@Override
	public ObjectImporter<?> getImporter(String objectType) {
		return importersMap.get(objectType);
	}
	
	@Override
	public void registerImporter(String objectType, ObjectImporter<?> importer) {
		importersMap.put(objectType, importer);
	}
}
