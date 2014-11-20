package com.krishagni.catissueplus.bulkoperator.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqJobsDetailEvent extends RequestEvent {
	private int startAt;
	
	private int maxRecords;

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
}
