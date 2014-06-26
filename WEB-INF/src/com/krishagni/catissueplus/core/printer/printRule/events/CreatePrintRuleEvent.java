
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreatePrintRuleEvent extends RequestEvent {

	private Object details;

	public CreatePrintRuleEvent(Object details) {
		this.details = details;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

}
