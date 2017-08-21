package com.krishagni.catissueplus.core.importer.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.importer.domain.ImportJob;

public interface ImportJobDao extends Dao<ImportJob> {
	public List<ImportJob> getImportJobs(ListImportJobsCriteria crit);
}
