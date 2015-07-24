package com.krishagni.openspecimen.core.migration.services.impl;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.openspecimen.core.migration.domain.Migration;
import com.krishagni.openspecimen.core.migration.events.MigrationDetail;
import com.krishagni.openspecimen.core.migration.repository.MigrationDao;
import com.krishagni.openspecimen.core.migration.services.MigrationService;

public class MigrationServiceImpl implements MigrationService {
	
	private MigrationDao migrationDao;
	
	public void setMigrationDao(MigrationDao migrationDao) {
		this.migrationDao = migrationDao;
	}

	@Override
	@PlusTransactional
	public MigrationDetail getMigration(String name, String versionFrom, String versionTo) {
		Migration migration = migrationDao.getMigrationInfo(name, versionFrom, versionTo);
		return MigrationDetail.from(migration);
	}

	@Override
	@PlusTransactional
	public MigrationDetail saveMigration(MigrationDetail detail) {
		Migration migration = null;
		
		if (detail.getId() != null) {
			migration = migrationDao.getById(detail.getId());
		} else {
			migration = new Migration();
		}
		
		migration.setName(detail.getName());
		migration.setVersionFrom(detail.getVersionFrom());
		migration.setVersionTo(detail.getVersionTo());
		migration.setStatus(detail.getStatus());
		migration.setDate(detail.getDate());
		
		migrationDao.saveOrUpdate(migration, true);
		return MigrationDetail.from(migration);
	}
}
