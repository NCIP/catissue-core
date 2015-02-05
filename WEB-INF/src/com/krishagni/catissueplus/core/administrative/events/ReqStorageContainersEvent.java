package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqStorageContainersEvent extends RequestEvent {
	private boolean onlyFreeContainers;
	
	private int startAt;
	
	private int maxRecords;
	
	private Long parentContainerId;

	public boolean isOnlyFreeContainers() {
		return onlyFreeContainers;
	}

	public void setOnlyFreeContainers(boolean onlyFreeContainers) {
		this.onlyFreeContainers = onlyFreeContainers;
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

	public Long getParentContainerId() {
		return parentContainerId;
	}

	public void setParentContainerId(Long parentContainerId) {
		this.parentContainerId = parentContainerId;
	}
}
