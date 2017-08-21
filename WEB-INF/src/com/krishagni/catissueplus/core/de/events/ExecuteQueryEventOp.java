package com.krishagni.catissueplus.core.de.events;


import org.apache.commons.lang3.StringUtils;

public class ExecuteQueryEventOp  {
	
	private Long cpId;
	
	private String drivingForm;

	private String aql;
	
	private String wideRowMode = "OFF";
	
	private Long savedQueryId;
	
	private String runType = "Data";
	
	private String indexOf;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getDrivingForm() {
		return drivingForm;
	}

	public void setDrivingForm(String drivingForm) {
		this.drivingForm = drivingForm;
	}

	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public String getWideRowMode() {
		return wideRowMode;
	}

	public void setWideRowMode(String wideRowMode) {
		this.wideRowMode = wideRowMode;
	}

	public Long getSavedQueryId() {
		return savedQueryId;
	}

	public void setSavedQueryId(Long savedQueryId) {
		this.savedQueryId = savedQueryId;
	}

	public String getRunType() {
		return StringUtils.isBlank(runType) ? "Data" : runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public String getIndexOf() {
		return indexOf;
	}

	public void setIndexOf(String indexOf) {
		this.indexOf = indexOf;
	}
	
}
