package com.krishagni.catissueplus.core.importer.services;

public interface ObjectImporterFactory {
	public <T> ObjectImporter<T> getImporter(String objectType);
	
	public <T> void registerImporter(String objectType, ObjectImporter<T> importer);
}
