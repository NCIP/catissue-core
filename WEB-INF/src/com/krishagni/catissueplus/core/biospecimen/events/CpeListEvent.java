package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CpeListEvent extends ResponseEvent {
	private Long cpId;
	
	private List<CollectionProtocolEventDetail> events;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public List<CollectionProtocolEventDetail> getEvents() {
		return events;
	}

	public void setEvents(List<CollectionProtocolEventDetail> events) {
		this.events = events;
	}
	
	public static CpeListEvent ok(Long cpId, List<CollectionProtocolEventDetail> events) {
		CpeListEvent resp = new CpeListEvent();
		resp.setStatus(EventStatus.OK);
		resp.setCpId(cpId);
		resp.setEvents(events);
		return resp;
	}
	
	public static CpeListEvent notFound(Long cpId) {
		CpeListEvent resp = new CpeListEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setCpId(cpId);
		return resp;		
	}
	
	public static CpeListEvent serverError(Long cpId, Exception e) {
		CpeListEvent resp = new CpeListEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setCpId(cpId);
		return resp;
	}
}
