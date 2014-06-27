
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class BiohazardUpdatedEvent extends ResponseEvent {

	private Long id;

	private String name;

	private BiohazardDetails biohazardDetails;

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

	public BiohazardDetails getBiohazardDetails() {
		return biohazardDetails;
	}

	public void setBiohazardDetails(BiohazardDetails biohazardDetails) {
		this.biohazardDetails = biohazardDetails;
	}

	public static BiohazardUpdatedEvent notFound(Long biohazardId) {
		BiohazardUpdatedEvent resp = new BiohazardUpdatedEvent();
		resp.setId(biohazardId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static BiohazardUpdatedEvent notFound(String name) {
		BiohazardUpdatedEvent resp = new BiohazardUpdatedEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static BiohazardUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		BiohazardUpdatedEvent resp = new BiohazardUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static BiohazardUpdatedEvent ok(BiohazardDetails details) {
		BiohazardUpdatedEvent event = new BiohazardUpdatedEvent();
		event.setBiohazardDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static BiohazardUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		BiohazardUpdatedEvent resp = new BiohazardUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
