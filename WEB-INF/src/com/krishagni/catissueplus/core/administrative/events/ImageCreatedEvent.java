
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ImageCreatedEvent extends ResponseEvent {

	private ImageDetails details;

	public ImageDetails getDetails() {
		return details;
	}

	public void setDetails(ImageDetails details) {
		this.details = details;
	}

	public static ImageCreatedEvent ok(ImageDetails details) {
		ImageCreatedEvent event = new ImageCreatedEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static ImageCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		ImageCreatedEvent resp = new ImageCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static ImageCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ImageCreatedEvent resp = new ImageCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
