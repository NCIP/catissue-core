package com.krishagni.catissueplus.bulkoperator.services.impl;

import java.util.Map;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.bulkoperator.services.ObjectImporterFactory;

public class BulkOperationDispatcherImpl implements ObjectImporterFactory {
	private Map<String, ObjectImporter> dispatcherMap;
	
	public void setDispatcherMap(
			Map<String, ObjectImporter> dispatcherMap) {
		this.dispatcherMap = dispatcherMap;
	}

	@Override
	public ObjectImporter getImporter(String simpleObjectName) {
		return dispatcherMap.get(simpleObjectName);
	}

}
