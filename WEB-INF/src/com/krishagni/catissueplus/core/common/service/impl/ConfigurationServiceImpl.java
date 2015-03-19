package com.krishagni.catissueplus.core.common.service.impl;

import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {
	
	@Override
	public Integer getIntSetting(String module, String name, Integer... defValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStrSetting(String module, String name,	String... defValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getFloatSetting(String module, String name, Double... defValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSetting(String module, String name, String setting) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerChangeListener(String module, ConfigChangeListener callback) {
		// TODO Auto-generated method stub

	}

}
