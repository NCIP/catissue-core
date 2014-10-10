package com.krishagni.catissueplus.core.de.events;

import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FormFieldSummary {
	private String name;
	
	private String caption;
	
	private String type;
	
	private List<String> pvs;
	
	private List<FormFieldSummary> subFields;
	
	private Properties lookupProps;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getPvs() {
		return pvs;
	}

	public void setPvs(List<String> pvs) {
		this.pvs = pvs;
	}

	public List<FormFieldSummary> getSubFields() {
		return subFields;
	}

	public void setSubFields(List<FormFieldSummary> subFields) {
		this.subFields = subFields;
	}

	public Properties getLookupProps() {
		return lookupProps;
	}

	public void setLookupProps(Properties lookupProps) {
		this.lookupProps = lookupProps;
	}
}
