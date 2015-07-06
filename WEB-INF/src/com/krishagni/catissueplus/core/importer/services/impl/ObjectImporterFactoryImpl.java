package com.krishagni.catissueplus.core.importer.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.importer.services.ObjectImporter;
import com.krishagni.catissueplus.core.importer.services.ObjectImporterFactory;

public class ObjectImporterFactoryImpl implements ObjectImporterFactory {
	
	private Map<String, ObjectImporter<?, ?>> importersMap = new HashMap<String, ObjectImporter<?, ?>>();
	
	public void setImportersMap(Map<String, ObjectImporter<?, ?>> importersMap) {
		this.importersMap = importersMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, U> ObjectImporter<T, U> getImporter(String objectType) {
		return (ObjectImporter<T, U>)importersMap.get(objectType);
	}
	
	@Override
	public <T, U> void registerImporter(String objectType, ObjectImporter<T, U> importer) {
		importersMap.put(objectType, importer);
	}
}
