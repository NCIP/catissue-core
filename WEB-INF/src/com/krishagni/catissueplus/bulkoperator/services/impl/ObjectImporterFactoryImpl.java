package com.krishagni.catissueplus.bulkoperator.services.impl;

import java.util.Map;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.bulkoperator.services.ObjectImporterFactory;

public class ObjectImporterFactoryImpl implements ObjectImporterFactory {
	private Map<String, ObjectImporter> importersMap;
	
	public void setImportersMap(Map<String, ObjectImporter> dispatcherMap) {
		this.importersMap = dispatcherMap;
	}

	@Override
	public ObjectImporter getImporter(String simpleObjectName) {
		return importersMap.get(simpleObjectName);
	}
}