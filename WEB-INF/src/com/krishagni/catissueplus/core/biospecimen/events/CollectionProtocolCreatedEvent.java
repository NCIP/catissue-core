package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CollectionProtocolCreatedEvent extends ResponseEvent {
	private CollectionProtocolDetail cp;

	public CollectionProtocolDetail getCp() {
		return cp;
	}

	public void setCp(CollectionProtocolDetail cp) {
		this.cp = cp;
	}
	
	public static CollectionProtocolCreatedEvent ok(CollectionProtocolDetail cp) {
		CollectionProtocolCreatedEvent resp = new CollectionProtocolCreatedEvent();
		resp.setCp(cp);
		resp.setStatus(EventStatus.OK);
		return resp;
	}

	public static CollectionProtocolCreatedEvent badRequest(Exception e) {
		CollectionProtocolCreatedEvent resp = new CollectionProtocolCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		resp.setMessage(e.getMessage());
		if (e instanceof ObjectCreationException) {
			resp.setErroneousFields(((ObjectCreationException)e).getErroneousFields());
		}
		
		return resp;
	}	
	
	public static CollectionProtocolCreatedEvent serverError(Exception e) {
		CollectionProtocolCreatedEvent resp = new CollectionProtocolCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		resp.setMessage(e.getMessage());
		return resp;		
	}
}
