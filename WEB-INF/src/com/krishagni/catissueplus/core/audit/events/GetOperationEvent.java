
package com.krishagni.catissueplus.core.audit.events;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GetOperationEvent extends ResponseEvent {

	Map<String,String> operationMap = new HashMap<String, String>();
	
	public Map<String, String> getOperationMap() {
		return operationMap;
	}

	
	public void setOperationMap(Map<String, String> operationMap) {
		this.operationMap = operationMap;
	}

	public static GetOperationEvent ok(Map<String,String> operationMap) {
		GetOperationEvent eventTypeDetailsEvent = new GetOperationEvent();
		eventTypeDetailsEvent.setOperationMap(operationMap);
		eventTypeDetailsEvent.setStatus(EventStatus.OK);
		return eventTypeDetailsEvent;
	}

	public static GetOperationEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		GetOperationEvent resp = new GetOperationEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
