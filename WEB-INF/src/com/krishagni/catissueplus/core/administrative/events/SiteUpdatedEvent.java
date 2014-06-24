
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SiteUpdatedEvent extends ResponseEvent {

	private SiteDetails siteDetails;

	private Long id;

	private String siteName;

	public Long getId() {
		return id;
	}

	public void setId(Long siteId) {
		this.id = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public SiteDetails getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(SiteDetails siteDetails) {
		this.siteDetails = siteDetails;
	}

	public static SiteUpdatedEvent notFound(Long siteId) {
		SiteUpdatedEvent resp = new SiteUpdatedEvent();
		resp.setId(siteId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static SiteUpdatedEvent notFound(String name) {
		SiteUpdatedEvent resp = new SiteUpdatedEvent();
		resp.setSiteName(name);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static SiteUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		SiteUpdatedEvent resp = new SiteUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static SiteUpdatedEvent ok(SiteDetails details) {
		SiteUpdatedEvent event = new SiteUpdatedEvent();
		event.setSiteDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static SiteUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SiteUpdatedEvent resp = new SiteUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
