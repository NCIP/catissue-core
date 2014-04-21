package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class FormFieldSummary {
	private String name;
	
	private String caption;
	
	private String type;
	
	private List<String> pvs;
	
	private List<FormFieldSummary> subFields;

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
}
