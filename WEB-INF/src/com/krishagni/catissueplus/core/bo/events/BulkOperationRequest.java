package com.krishagni.catissueplus.core.bo.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

import edu.wustl.common.beans.SessionDataBean;

public class BulkOperationRequest extends RequestEvent {
	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public BulkOperationRequest() {
		
	}
	
	public BulkOperationRequest(Object obj, SessionDataBean sessionDataBean) {
		this.object = obj;
		setSessionDataBean(sessionDataBean);
	}
}
