
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ImageUpdatedEvent extends ResponseEvent {

	private ImageDetails details;

	private Long id;

	public ImageDetails getDetails() {
		return details;
	}

	public void setDetails(ImageDetails details) {
		this.details = details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static ImageUpdatedEvent notFound(Long id) {
		ImageUpdatedEvent resp = new ImageUpdatedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static ImageUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		ImageUpdatedEvent resp = new ImageUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static ImageUpdatedEvent ok(ImageDetails details) {
		ImageUpdatedEvent event = new ImageUpdatedEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static ImageUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ImageUpdatedEvent resp = new ImageUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
