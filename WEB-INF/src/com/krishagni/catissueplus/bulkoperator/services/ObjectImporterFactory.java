package com.krishagni.catissueplus.bulkoperator.services;

import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;


public interface ObjectImporterFactory {
	public ObjectImporter getImporter(String simpleObjectName);

}
