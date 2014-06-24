
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolPatchedEvent extends ResponseEvent {

	private DistributionProtocolDetails details = new DistributionProtocolDetails();

	private long id;

	private String title;

	public DistributionProtocolDetails getDetails() {
		return details;
	}

	public void setDetails(DistributionProtocolDetails details) {
		this.details = details;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static DistributionProtocolPatchedEvent notFound(Long id) {
		DistributionProtocolPatchedEvent resp = new DistributionProtocolPatchedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static DistributionProtocolPatchedEvent notFound(String title) {
		DistributionProtocolPatchedEvent resp = new DistributionProtocolPatchedEvent();
		resp.setTitle(title);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static DistributionProtocolPatchedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DistributionProtocolPatchedEvent resp = new DistributionProtocolPatchedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static DistributionProtocolPatchedEvent ok(DistributionProtocolDetails details) {
		DistributionProtocolPatchedEvent event = new DistributionProtocolPatchedEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);

		return event;
	}

	public static DistributionProtocolPatchedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		DistributionProtocolPatchedEvent resp = new DistributionProtocolPatchedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

}
