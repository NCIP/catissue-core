package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DepartmentGotEvent extends ResponseEvent {

	private Long id;
	
	private String name;

	private DepartmentDetails details;

	public DepartmentDetails getDetails() {
		return details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDetails(DepartmentDetails details) {
		this.details = details;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static DepartmentGotEvent ok(DepartmentDetails details) {
		DepartmentGotEvent event = new DepartmentGotEvent();
		event.setStatus(EventStatus.OK);
		event.setDetails(details);
		return event;
	}

	public static DepartmentGotEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DepartmentGotEvent resp = new DepartmentGotEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static DepartmentGotEvent notFound(Long deptId) {
		DepartmentGotEvent resp = new DepartmentGotEvent();
		resp.setId(deptId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static DepartmentGotEvent notFound(String name) {
		DepartmentGotEvent resp = new DepartmentGotEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
