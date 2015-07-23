package com.krishagni.openspecimen.core.migration.services.impl;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.openspecimen.core.migration.domain.Migration;
import com.krishagni.openspecimen.core.migration.events.MigrationDetail;
import com.krishagni.openspecimen.core.migration.services.MigrationService;

public class MigrationServiceImpl implements MigrationService {
	
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public MigrationDetail getMigration(String name, String versionFrom, String versionTo) {
		Migration migration = daoFactory.getMigrationDao().getMigrationInfo(name, versionFrom, versionTo);
		return MigrationDetail.from(migration);
	}

	@Override
	@PlusTransactional
	public MigrationDetail saveMigration(MigrationDetail detail) {
		Migration migration = null;
		
		if (detail.getId() != null) {
			migration = daoFactory.getMigrationDao().getById(detail.getId());
		} else {
			migration = new Migration();
		}
		
		migration.setName(detail.getName());
		migration.setVersionFrom(detail.getVersionFrom());
		migration.setVersionTo(detail.getVersionTo());
		migration.setStatus(detail.getStatus());
		migration.setDate(detail.getDate());
		
		daoFactory.getMigrationDao().saveOrUpdate(migration, true);
		return MigrationDetail.from(migration);
	}
}
