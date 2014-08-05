
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SiteDeletedEvent extends ResponseEvent {

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

	public static SiteDeletedEvent notFound(Long siteId) {
		SiteDeletedEvent response = new SiteDeletedEvent();
		response.setId(siteId);
		response.setStatus(EventStatus.NOT_FOUND);
		return response;
	}

	public static SiteDeletedEvent notFound(String name) {
		SiteDeletedEvent response = new SiteDeletedEvent();
		response.setName(name);
		response.setStatus(EventStatus.NOT_FOUND);
		return response;
	}

	public static SiteDeletedEvent ok() {
		SiteDeletedEvent response = new SiteDeletedEvent();
		response.setStatus(EventStatus.OK);
		response.setMessage(SUCCESS);
		return response;
	}

	public static SiteDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SiteDeletedEvent resp = new SiteDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
