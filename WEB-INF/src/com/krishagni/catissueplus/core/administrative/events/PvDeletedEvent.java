
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PvDeletedEvent extends ResponseEvent {

	private static final String SUCCESS = "success";

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static PvDeletedEvent ok() {
		PvDeletedEvent event = new PvDeletedEvent();
		event.setStatus(EventStatus.OK);
		event.setMessage(SUCCESS);
		return event;
	}

	public static PvDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		PvDeletedEvent resp = new PvDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static PvDeletedEvent notFound(Long id) {
		PvDeletedEvent resp = new PvDeletedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
