package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AccessCheckedEvent extends ResponseEvent {
	public enum AccessSettings { 
		CAN_ACCESS,
		ACCESS_DENIED,
		SETTING_NOT_FOUND
	}
	
	private AccessSettings accessSettings;
	
	public AccessSettings getAccessSettings() {
		return accessSettings;
	}

	public void setAccessSettings(AccessSettings accessSettings) {
		this.accessSettings = accessSettings;
	}

	public AccessCheckedEvent() {
		
	}
	
	public AccessCheckedEvent(AccessSettings accessSettings) {
		setAccessSettings(accessSettings);
	}
	
	public static AccessCheckedEvent ok() {
		AccessCheckedEvent resp = new AccessCheckedEvent(AccessSettings.CAN_ACCESS);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static AccessCheckedEvent denied() {
		AccessCheckedEvent resp = new AccessCheckedEvent(AccessSettings.ACCESS_DENIED);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static AccessCheckedEvent badRequest(RbacErrorCode error) {
		return errorResp(EventStatus.BAD_REQUEST, error, null);
	}
	
	public static AccessCheckedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static AccessCheckedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		AccessCheckedEvent resp = new AccessCheckedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
