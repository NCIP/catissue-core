
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DomainRegisteredEvent extends ResponseEvent {

	private DomainDetails domainDetails;

	public DomainDetails getDomainDetails() {
		return domainDetails;
	}

	public void setDomainDetails(DomainDetails domainDetails) {
		this.domainDetails = domainDetails;
	}

	public static DomainRegisteredEvent ok(DomainDetails details) {
		DomainRegisteredEvent event = new DomainRegisteredEvent();
		event.setDomainDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static DomainRegisteredEvent invalidRequest(String message, ErroneousField... erroneousField) {
		DomainRegisteredEvent resp = new DomainRegisteredEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static DomainRegisteredEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DomainRegisteredEvent resp = new DomainRegisteredEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
