package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqQueryAuditLogsEvent extends RequestEvent {
	public static enum Type {
		ALL,
		LAST_24
	};
	
	private Long savedQueryId;
	
	private int startAt;
	
	private int maxRecords;
	
	private Type type;
	
	private boolean countReq;

	public Long getSavedQueryId() {
		return savedQueryId;
	}

	public void setSavedQueryId(Long savedQueryId) {
		this.savedQueryId = savedQueryId;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isCountReq() {
		return countReq;
	}

	public void setCountReq(boolean countReq) {
		this.countReq = countReq;
	}
}
