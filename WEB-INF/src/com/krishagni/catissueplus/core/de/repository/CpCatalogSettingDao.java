package com.krishagni.catissueplus.core.de.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.de.domain.CpCatalogSetting;

public interface CpCatalogSettingDao extends Dao<CpCatalogSetting> {
	
	public CpCatalogSetting getByCpId(Long cpId);
	
	public CpCatalogSetting getByCpShortTitle(String shortTitle);

}
