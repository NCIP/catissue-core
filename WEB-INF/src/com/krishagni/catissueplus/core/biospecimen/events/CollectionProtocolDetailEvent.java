package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CollectionProtocolDetailEvent extends ResponseEvent {
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
	
	public static CollectionProtocolDetailEvent ok(CollectionProtocolDetail cp) {
		CollectionProtocolDetailEvent resp = new CollectionProtocolDetailEvent();
		resp.setCp(cp);
		resp.setCpId(cp.getId());
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static CollectionProtocolDetailEvent notFound(Long cpId) {
		CollectionProtocolDetailEvent resp = new CollectionProtocolDetailEvent();
		resp.setCpId(cpId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
}
