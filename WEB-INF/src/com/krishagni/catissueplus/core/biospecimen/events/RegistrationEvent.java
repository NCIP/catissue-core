package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RegistrationEvent extends ResponseEvent {
	private Long cprId;
	
	private Long cpId;
	
	private String ppid;
	
	private CollectionProtocolRegistrationDetail cpr;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public CollectionProtocolRegistrationDetail getCpr() {
		return cpr;
	}

	public void setCpr(CollectionProtocolRegistrationDetail cpr) {
		this.cpr = cpr;
	}
	
	
	public static RegistrationEvent ok(CollectionProtocolRegistrationDetail cpr) {
		RegistrationEvent resp = new RegistrationEvent();
		resp.setCpr(cpr);
		resp.setCpId(cpr.getCpId());
		resp.setPpid(cpr.getPpid());
		resp.setCprId(cpr.getId());
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static RegistrationEvent notFound(Long cpId, String ppid) {
		RegistrationEvent resp = new RegistrationEvent();
		resp.setCpId(cpId);
		resp.setPpid(ppid);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static RegistrationEvent notFound(Long cprId) {
		RegistrationEvent resp = new RegistrationEvent();
		resp.setCprId(cprId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static RegistrationEvent invalidRequest() {
		RegistrationEvent resp = new RegistrationEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		return resp;
	}
	
	public static RegistrationEvent serverError(Exception e) {
		RegistrationEvent resp = new RegistrationEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;		
	}
}
