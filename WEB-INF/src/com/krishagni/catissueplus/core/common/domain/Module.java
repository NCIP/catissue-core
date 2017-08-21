package com.krishagni.catissueplus.core.common.domain;

import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class Module extends BaseEntity {
	private String name;
	
	private String description;
	
	private Set<ConfigProperty> configProps;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ConfigProperty> getConfigProps() {
		return configProps;
	}

	public void setConfigProps(Set<ConfigProperty> configProps) {
		this.configProps = configProps;
	}

}
