package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class InstituteGotEvent extends ResponseEvent {
	
	private Long id;
	
	private String name;

	private InstituteDetails details;
	
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

	public InstituteDetails getDetails() {
		return details;
	}

	public void setDetails(InstituteDetails details) {
		this.details = details;
	}

	public static InstituteGotEvent ok(InstituteDetails details) {
		InstituteGotEvent event = new InstituteGotEvent();
		event.setStatus(EventStatus.OK);
		event.setDetails(details);
		return event;
	}

	public static InstituteGotEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		InstituteGotEvent resp = new InstituteGotEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static InstituteGotEvent notFound(Long id) {
		InstituteGotEvent resp = new InstituteGotEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setId(id);
		return resp;
	}

	public static InstituteGotEvent notFound(String name) {
		InstituteGotEvent resp = new InstituteGotEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
