
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ImageDeletedEvent extends ResponseEvent {

	private static final String SUCCESS = "success";

	private Long id;

	private String eqpImageId;

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

	public static ImageDeletedEvent notFound(Long siteId) {
		ImageDeletedEvent response = new ImageDeletedEvent();
		response.setId(siteId);
		response.setStatus(EventStatus.NOT_FOUND);
		return response;
	}

	public static ImageDeletedEvent notFound(String eqpImageId) {
		ImageDeletedEvent response = new ImageDeletedEvent();
		response.setEqpImageId(eqpImageId);
		response.setStatus(EventStatus.NOT_FOUND);
		return response;
	}

	public static ImageDeletedEvent ok() {
		ImageDeletedEvent response = new ImageDeletedEvent();
		response.setStatus(EventStatus.OK);
		response.setMessage(SUCCESS);
		return response;
	}

	public static ImageDeletedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		ImageDeletedEvent resp = new ImageDeletedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
