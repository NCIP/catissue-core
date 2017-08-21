package com.krishagni.openspecimen.core.migration.services;

import com.krishagni.openspecimen.core.migration.events.MigrationDetail;

public interface MigrationService {
	public MigrationDetail getMigration(String name, String versionFrom, String versionTo);
	
	public MigrationDetail saveMigration(MigrationDetail detail);
}
