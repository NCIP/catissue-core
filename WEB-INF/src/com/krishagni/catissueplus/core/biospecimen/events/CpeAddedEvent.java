package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CpeAddedEvent extends ResponseEvent {
	private CollectionProtocolEventDetail cpe;

	public CollectionProtocolEventDetail getCpe() {
		return cpe;
	}

	public void setCpe(CollectionProtocolEventDetail cpe) {
		this.cpe = cpe;
	}
	
	public static CpeAddedEvent ok(CollectionProtocolEventDetail cpe) {
		CpeAddedEvent resp = new CpeAddedEvent();
		resp.setCpe(cpe);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static CpeAddedEvent badRequest(Exception e) {
		CpeAddedEvent resp = new CpeAddedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		if (e instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)e;
			resp.setErroneousFields(oce.getErroneousFields());
			resp.setMessage(oce.getMessage());
		}
		
		return resp;
	}

	public static CpeAddedEvent serverError(Exception e) {
		CpeAddedEvent resp = new CpeAddedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
