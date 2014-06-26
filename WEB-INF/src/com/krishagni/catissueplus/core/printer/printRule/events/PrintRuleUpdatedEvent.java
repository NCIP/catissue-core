
package com.krishagni.catissueplus.core.printer.printRule.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PrintRuleUpdatedEvent extends ResponseEvent {

	private Object printRuleDetails;

	public Object getPrintRuleDetails() {
		return printRuleDetails;
	}

	public void setPrintRuleDetails(Object printRuleDetails) {
		this.printRuleDetails = printRuleDetails;
	}

	public static PrintRuleUpdatedEvent ok(SpecimenPrintRuleDetails details) {
		PrintRuleUpdatedEvent event = new PrintRuleUpdatedEvent();
		event.setPrintRuleDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static PrintRuleUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		PrintRuleUpdatedEvent resp = new PrintRuleUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static PrintRuleUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PrintRuleUpdatedEvent resp = new PrintRuleUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static PrintRuleUpdatedEvent notFound() {
		PrintRuleUpdatedEvent resp = new PrintRuleUpdatedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
