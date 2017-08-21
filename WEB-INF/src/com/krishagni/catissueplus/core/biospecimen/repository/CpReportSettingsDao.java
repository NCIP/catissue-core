package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.biospecimen.domain.CpReportSettings;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CpReportSettingsDao extends Dao<CpReportSettings> {
	public CpReportSettings getByCp(Long cpId);

	public CpReportSettings getByCp(String cpShortTitle);
}
