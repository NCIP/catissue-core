
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PrintRuleCreatedEvent extends ResponseEvent {

	private Object details;

	public Object getPrintRuleDetails() {
		return details;
	}

	public void setPrintRuleDetails(Object details) {
		this.details = details;
	}

	public static PrintRuleCreatedEvent ok(Object details) {
		PrintRuleCreatedEvent event = new PrintRuleCreatedEvent();
		event.setPrintRuleDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static PrintRuleCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		PrintRuleCreatedEvent resp = new PrintRuleCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static PrintRuleCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PrintRuleCreatedEvent resp = new PrintRuleCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
