package com.krishagni.catissueplus.bulkoperator.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

import edu.wustl.common.beans.SessionDataBean;

public class ImportObjectEvent extends RequestEvent {
	private Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public ImportObjectEvent() {
		
	}
	
	public ImportObjectEvent(Object obj, SessionDataBean sessionDataBean) {
		this.object = obj;
		setSessionDataBean(sessionDataBean);
	}
}
