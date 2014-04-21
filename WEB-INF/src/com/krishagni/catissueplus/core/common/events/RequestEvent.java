
package com.krishagni.catissueplus.core.common.events;

import edu.wustl.common.beans.SessionDataBean;

public class RequestEvent {

	private SessionDataBean sessionDataBean;

	public SessionDataBean getSessionDataBean() {
		return sessionDataBean;
	}

	public void setSessionDataBean(SessionDataBean sessionDataBean) {
		this.sessionDataBean = sessionDataBean;
	}
}
