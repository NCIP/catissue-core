package com.krishagni.catissueplus.core.importer.repository.impl;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.importer.domain.ImportJob;
import com.krishagni.catissueplus.core.importer.repository.ImportJobDao;

public class ImportJobDaoImpl extends AbstractDao<ImportJob> implements ImportJobDao {
	
	public Class<ImportJob> getType() {
		return ImportJob.class;
	}
	
	@Override
	public ImportJob getById(Long id) {		
		return super.getById(id, null);
	}

	@Override
	public ImportJob getById(Long id, String activeCondition) {
		return getById(id);
	}
}
