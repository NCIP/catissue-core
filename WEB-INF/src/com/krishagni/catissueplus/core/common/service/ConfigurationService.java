package com.krishagni.catissueplus.core.common.service;

public interface ConfigurationService {
	public Integer getIntSetting(String module, String name, Integer ... defValue);
	
	public String getStrSetting(String module, String name, String ... defValue);
	
	public Double getFloatSetting(String module, String name, Double ... defValue);
	
	public void saveSetting(String module, String name, String setting);
	
	public void reload();
	
	public void registerChangeListener(String module, ConfigChangeListener callback);
}
