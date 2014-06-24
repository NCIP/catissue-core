
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolUpdatedEvent extends ResponseEvent {

	private DistributionProtocolDetails details;

	private Long id;

	private String title;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DistributionProtocolDetails getDetails() {
		return details;
	}

	public void setDetails(DistributionProtocolDetails details) {
		this.details = details;
	}

	public static DistributionProtocolUpdatedEvent notFound(Long id) {
		DistributionProtocolUpdatedEvent resp = new DistributionProtocolUpdatedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static DistributionProtocolUpdatedEvent notFound(String title) {
		DistributionProtocolUpdatedEvent resp = new DistributionProtocolUpdatedEvent();
		resp.setTitle(title);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static DistributionProtocolUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DistributionProtocolUpdatedEvent resp = new DistributionProtocolUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static DistributionProtocolUpdatedEvent ok(DistributionProtocolDetails details) {
		DistributionProtocolUpdatedEvent event = new DistributionProtocolUpdatedEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);

		return event;
	}

	public static DistributionProtocolUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		DistributionProtocolUpdatedEvent resp = new DistributionProtocolUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}
}
