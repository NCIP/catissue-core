
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqAllPrintRulesEvent extends RequestEvent {

	private int maxResults;

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
