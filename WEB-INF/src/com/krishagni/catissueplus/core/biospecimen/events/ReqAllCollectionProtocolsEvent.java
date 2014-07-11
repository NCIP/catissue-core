
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqAllCollectionProtocolsEvent extends RequestEvent {

	private boolean chkPrivileges=Boolean.FALSE;

	public boolean isChkPrivileges() {
		return chkPrivileges;
	}

	public void setChkPrivileges(boolean chkPrivileges) {
		this.chkPrivileges = chkPrivileges;
	}

}
