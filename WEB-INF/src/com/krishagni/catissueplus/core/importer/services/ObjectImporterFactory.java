package com.krishagni.catissueplus.core.importer.services;

public interface ObjectImporterFactory {
	public <T, U> ObjectImporter<T, U> getImporter(String objectType);
	
	public <T, U> void registerImporter(String objectType, ObjectImporter<T, U> importer);
}
