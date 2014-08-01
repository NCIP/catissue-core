package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ReqAllStorageContainersEvent extends ResponseEvent {

	private int maxResults;

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
