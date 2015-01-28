package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class CpeEvent extends ResponseEvent {
	private Long cpeId;
	
	private CollectionProtocolEventDetail cpe;

	public Long getCpeId() {
		return cpeId;
	}

	public void setCpeId(Long cpeId) {
		this.cpeId = cpeId;
	}

	public CollectionProtocolEventDetail getCpe() {
		return cpe;
	}

	public void setCpe(CollectionProtocolEventDetail cpe) {
		this.cpe = cpe;
	}
	
	public static CpeEvent ok(CollectionProtocolEventDetail cpe) {
		CpeEvent resp = new CpeEvent();
		resp.setCpe(cpe);
		resp.setCpeId(cpe.getId());
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static CpeEvent notFound(Long cpeId) {
		CpeEvent resp = new CpeEvent();
		resp.setCpeId(cpeId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
	
	public static CpeEvent serverError(Exception e) {
		CpeEvent resp = new CpeEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
