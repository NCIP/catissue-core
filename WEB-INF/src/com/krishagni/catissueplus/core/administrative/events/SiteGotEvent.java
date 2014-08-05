
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SiteGotEvent extends ResponseEvent {

	private Long id;

	private String name;

	private SiteDetails details;

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

	public SiteDetails getDetails() {
		return details;
	}

	public void setDetails(SiteDetails details) {
		this.details = details;
	}

	public static SiteGotEvent ok(SiteDetails details) {
		SiteGotEvent event = new SiteGotEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static SiteGotEvent notFound(Long id) {
		SiteGotEvent resp = new SiteGotEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static SiteGotEvent notFound(String name) {
		SiteGotEvent resp = new SiteGotEvent();
		resp.setName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

}
