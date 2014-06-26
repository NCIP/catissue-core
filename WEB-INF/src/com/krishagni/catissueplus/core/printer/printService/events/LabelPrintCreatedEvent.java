
package com.krishagni.catissueplus.core.printer.printService.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class LabelPrintCreatedEvent extends ResponseEvent {

	private static final String SUCCESS = "success";
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static LabelPrintCreatedEvent ok() {
		LabelPrintCreatedEvent event = new LabelPrintCreatedEvent();
		event.setStatus(EventStatus.OK);
		event.setMessage(SUCCESS);
		return event;
	}

	public static LabelPrintCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		LabelPrintCreatedEvent resp = new LabelPrintCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static LabelPrintCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		LabelPrintCreatedEvent resp = new LabelPrintCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
	
	public static LabelPrintCreatedEvent notFound() {
		LabelPrintCreatedEvent resp = new LabelPrintCreatedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
