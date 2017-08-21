package com.krishagni.catissueplus.core.de.domain;

import java.util.HashMap;
import java.util.Map;

public class ReportSpec {
	private String type;
	
	private Map<String, Object> params = new HashMap<String, Object>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}		
}