package com.krishagni.catissueplus.core.audit.events;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class GetObjectNameEvent extends ResponseEvent{

	private Map<String, String> objectNameMap = new HashMap<String, String>();

	
	
	
	public Map<String, String> getObjectNameMap() {
		return objectNameMap;
	}

	
	public void setObjectNameMap(Map<String, String> objectNameMap) {
		this.objectNameMap = objectNameMap;
	}

	public static GetObjectNameEvent ok(Map<String, String> objectNameMap) {
		GetObjectNameEvent objectDetailsEvent = new GetObjectNameEvent();
		objectDetailsEvent.setObjectNameMap(objectNameMap);
		objectDetailsEvent.setStatus(EventStatus.OK);
		return objectDetailsEvent;
	}

	public static GetObjectNameEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		GetObjectNameEvent resp = new GetObjectNameEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}


	
}
