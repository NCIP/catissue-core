package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ExecuteQueryEvent extends RequestEvent {
	
	private String drivingForm;

	private String aql;
	
	private boolean wideRows = false;

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
}
