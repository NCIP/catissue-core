package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.ConfigSetting;

public interface ConfigSettingDao extends Dao<ConfigSetting> {
	
	public List<ConfigSetting> getAllSettings();
	
	public List<ConfigSetting> getAllSettingsByModule(String moduleName);
	
}
