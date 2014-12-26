package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CollectionProtocolEvent extends ResponseEvent {
	public Long cpId;
	
	public CollectionProtocolDetail cp;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public CollectionProtocolDetail getCp() {
		return cp;
	}

	public void setCp(CollectionProtocolDetail cp) {
		this.cp = cp;
	}
	
	public static CollectionProtocolEvent ok(CollectionProtocolDetail cp) {
		CollectionProtocolEvent resp = new CollectionProtocolEvent();
		resp.setCp(cp);
		resp.setCpId(cp.getId());
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static CollectionProtocolEvent notFound(Long cpId) {
		CollectionProtocolEvent resp = new CollectionProtocolEvent();
		resp.setCpId(cpId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
}
