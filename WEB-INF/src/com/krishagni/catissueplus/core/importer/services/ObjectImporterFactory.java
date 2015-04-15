package com.krishagni.catissueplus.core.importer.services;

public interface ObjectImporterFactory {
	public ObjectImporter getImporter(String objectType);
	
	public void registerImporter(String objectType, ObjectImporter importer);
}
