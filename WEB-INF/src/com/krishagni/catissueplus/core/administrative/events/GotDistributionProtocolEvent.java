
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GotDistributionProtocolEvent extends ResponseEvent {

	private Long id;

	private String title;

	private DistributionProtocolDetails details;

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

	public static GotDistributionProtocolEvent notFound(Long id) {
		GotDistributionProtocolEvent resp = new GotDistributionProtocolEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static GotDistributionProtocolEvent notFound(String title) {
		GotDistributionProtocolEvent resp = new GotDistributionProtocolEvent();
		resp.setTitle(title);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static GotDistributionProtocolEvent ok(DistributionProtocolDetails details) {
		GotDistributionProtocolEvent event = new GotDistributionProtocolEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
