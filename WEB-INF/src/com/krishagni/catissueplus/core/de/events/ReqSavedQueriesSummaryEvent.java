package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSavedQueriesSummaryEvent extends RequestEvent {
	private String searchString;
	
	private int startAt;
	
	private int maxRecords;
	
	private boolean countReq;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

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
