
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GotImageEvent extends ResponseEvent {

	private Long id;

	private String eqpImageId;

	private ImageDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEqpImageId() {
		return eqpImageId;
	}

	public void setEqpImageId(String eqpImageId) {
		this.eqpImageId = eqpImageId;
	}

	public ImageDetails getDetails() {
		return details;
	}

	public void setDetails(ImageDetails details) {
		this.details = details;
	}

	public static GotImageEvent notFound(Long id) {
		GotImageEvent resp = new GotImageEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static GotImageEvent notFound(String eqpImageId) {
		GotImageEvent resp = new GotImageEvent();
		resp.setEqpImageId(eqpImageId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static GotImageEvent ok(ImageDetails details) {
		GotImageEvent event = new GotImageEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}
}
