package com.krishagni.catissueplus.core.de.events;

import java.util.HashSet;
import java.util.Set;


public class ExecuteQueryEventOp  {
	
	private Long cpId;
	
	private String drivingForm;

	private String aql;
	
	private boolean wideRows = false;
	
	private Long savedQueryId;
	
	private String runType;
	
	private String indexOf;
	
	private boolean forceUseEmail = false;
	
	private Set<String> additionalRecipients = new HashSet<String>();

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

	public boolean isWideRows() {
		return wideRows;
	}

	public void setWideRows(boolean wideRows) {
		this.wideRows = wideRows;
	}

	public Long getSavedQueryId() {
		return savedQueryId;
	}

	public void setSavedQueryId(Long savedQueryId) {
		this.savedQueryId = savedQueryId;
	}

	public String getRunType() {
		return runType;
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

	public boolean isForceUseEmail() {
		return forceUseEmail;
	}

	public void setForceUseEmail(boolean forceUseEmail) {
		this.forceUseEmail = forceUseEmail;
	}

	public Set<String> getAdditionalRecipients() {
		return additionalRecipients;
	}

	public void setAdditionalRecipients(Set<String> additionalRecipients) {
		this.additionalRecipients = additionalRecipients;
	}
	
}
