package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSavedQueriesSummaryEvent extends RequestEvent {	
	private int startAt;
	
	private int maxRecords;
	
	private boolean countReq;

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public boolean isCountReq() {
		return countReq;
	}

	public void setCountReq(boolean countReq) {
		this.countReq = countReq;
	}		
}
