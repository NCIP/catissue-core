
package com.krishagni.catissueplus.core.common.events;

import edu.wustl.common.beans.SessionDataBean;

public class RequestEvent<T> {
	
	private SessionDataBean sessionDataBean;
	
	private T payload;
	
	public RequestEvent() {		
	}
	
	public RequestEvent(T payload) {
		this.payload = payload;
	}
	
	public RequestEvent(SessionDataBean sdb, T payload) {
		this.sessionDataBean = sdb;
		this.payload = payload;
	}

	public SessionDataBean getSessionDataBean() {
		return sessionDataBean;
	}

	public void setSessionDataBean(SessionDataBean sessionDataBean) {
		this.sessionDataBean = sessionDataBean;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}
}
