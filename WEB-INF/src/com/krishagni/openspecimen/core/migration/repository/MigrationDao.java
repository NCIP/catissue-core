package com.krishagni.openspecimen.core.migration.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.openspecimen.core.migration.domain.Migration;

public interface MigrationDao extends Dao<Migration> {
	public Migration getMigrationInfo(String name, String fromVersion, String toVersion);
}
