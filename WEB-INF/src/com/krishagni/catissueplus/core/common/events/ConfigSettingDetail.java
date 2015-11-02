package com.krishagni.catissueplus.core.common.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.common.domain.ConfigProperty;
import com.krishagni.catissueplus.core.common.domain.ConfigProperty.DataType;
import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.domain.Module;

public class ConfigSettingDetail implements Comparable<ConfigSettingDetail> {
	private String module;
	
	private String name;
	
	private String value;
	
	private DataType type;
	
	private Set<String> allowedValues;
	
	private String displayNameCode;
	
	private String descCode;
	
	private Date activationDate;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public Set<String> getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(Set<String> allowedValues) {
		this.allowedValues = allowedValues;
	}

	public String getDisplayNameCode() {
		return displayNameCode;
	}

	public void setDisplayNameCode(String displayNameCode) {
		this.displayNameCode = displayNameCode;
	}

	public String getDescCode() {
		return descCode;
	}

	public void setDescCode(String descCode) {
		this.descCode = descCode;
	}
	
	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	
	@Override
	public int compareTo(ConfigSettingDetail o) {
		int cmp = module.compareTo(o.module);
		if (cmp == 0) {
			cmp = name.compareTo(o.name);
		}

		return cmp;
	}
	
	public static ConfigSettingDetail from(ConfigSetting setting) {
		ConfigSettingDetail result = new ConfigSettingDetail();
		
		ConfigProperty property = setting.getProperty();
		Module module = property.getModule();
		
		result.setModule(module.getName());
		result.setName(property.getName());
		result.setType(property.getDataType());
		result.setAllowedValues(new HashSet<String>(property.getAllowedValues()));
		result.setDescCode(property.getDescCode());
		result.setDisplayNameCode(property.getDisplayNameCode());
		result.setValue(setting.getValue());
		result.setActivationDate(setting.getActivationDate());
		return result;
	}
	
	public static List<ConfigSettingDetail> from(Collection<ConfigSetting> settings) {
		List<ConfigSettingDetail> result = new ArrayList<ConfigSettingDetail>();
		
		for (ConfigSetting cs :  settings) {
			result.add(from(cs));
		}
		
		Collections.sort(result);
		return result;
	}
}
