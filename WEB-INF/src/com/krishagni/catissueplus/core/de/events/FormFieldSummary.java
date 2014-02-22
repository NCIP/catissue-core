package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class FormFieldSummary {
	private String name;
	
	private String caption;
	
	private String dataType;
	
	private List<String> pvs;

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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<String> getPvs() {
		return pvs;
	}

	public void setPvs(List<String> pvs) {
		this.pvs = pvs;
	}
}
