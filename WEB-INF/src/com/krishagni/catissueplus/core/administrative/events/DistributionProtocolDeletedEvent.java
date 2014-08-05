
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolDeletedEvent extends ResponseEvent {

	private long id;

	private String title;

	private static final String SUCCESS = "success";

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

	public static DistributionProtocolDeletedEvent notFound(Long id) {
		DistributionProtocolDeletedEvent resp = new DistributionProtocolDeletedEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static DistributionProtocolDeletedEvent notFound(String title) {
		DistributionProtocolDeletedEvent resp = new DistributionProtocolDeletedEvent();
		resp.setTitle(title);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static DistributionProtocolDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DistributionProtocolDeletedEvent resp = new DistributionProtocolDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static DistributionProtocolDeletedEvent ok() {
		DistributionProtocolDeletedEvent event = new DistributionProtocolDeletedEvent();
		event.setStatus(EventStatus.OK);
		event.setMessage(SUCCESS);
		return event;
	}

}
