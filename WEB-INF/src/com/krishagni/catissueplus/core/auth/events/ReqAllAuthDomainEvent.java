
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqAllAuthDomainEvent extends RequestEvent {

	private int maxResults;

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
