
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GotBiohazardEvent extends ResponseEvent {

	private Long id;

	private String name;

	private BiohazardDetails details;

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

	public BiohazardDetails getDetails() {
		return details;
	}

	public void setDetails(BiohazardDetails details) {
		this.details = details;
	}

	public static GotBiohazardEvent ok(BiohazardDetails biohazardDetails) {
		GotBiohazardEvent event = new GotBiohazardEvent();
		event.setDetails(biohazardDetails);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static GotBiohazardEvent notFound(Long id) {
		GotBiohazardEvent resp = new GotBiohazardEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static GotBiohazardEvent notFound(String name) {
		GotBiohazardEvent resp = new GotBiohazardEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
