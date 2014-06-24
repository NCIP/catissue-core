
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SiteCreatedEvent extends ResponseEvent {

	private SiteDetails siteDetails;

	public SiteDetails getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(SiteDetails siteDetails) {
		this.siteDetails = siteDetails;
	}

	public static SiteCreatedEvent ok(SiteDetails details) {
		SiteCreatedEvent event = new SiteCreatedEvent();
		event.setSiteDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static SiteCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		SiteCreatedEvent resp = new SiteCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static SiteCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		SiteCreatedEvent resp = new SiteCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
