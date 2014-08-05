
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BiohazardDeletedEvent extends ResponseEvent {

	private static final String SUCCESS = "success";

	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static BiohazardDeletedEvent notFound(Long id) {
		BiohazardDeletedEvent event = new BiohazardDeletedEvent();
		event.setId(id);
		event.setStatus(EventStatus.NOT_FOUND);
		return event;
	}

	public static BiohazardDeletedEvent notFound(String name) {
		BiohazardDeletedEvent event = new BiohazardDeletedEvent();
		event.setName(name);
		event.setStatus(EventStatus.NOT_FOUND);
		return event;
	}

	public static BiohazardDeletedEvent ok() {
		BiohazardDeletedEvent event = new BiohazardDeletedEvent();
		event.setStatus(EventStatus.OK);
		event.setMessage(SUCCESS);
		return event;
	}

	public static BiohazardDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		BiohazardDeletedEvent resp = new BiohazardDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
