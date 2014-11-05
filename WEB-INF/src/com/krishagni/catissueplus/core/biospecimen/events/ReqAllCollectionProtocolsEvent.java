
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqAllCollectionProtocolsEvent extends RequestEvent {

	private boolean chkPrivileges = Boolean.FALSE;
	
	private boolean includePi = false;
	
	private boolean includeStats = false;

	public boolean isChkPrivileges() {
		return chkPrivileges;
	}

	public void setChkPrivileges(boolean chkPrivileges) {
		this.chkPrivileges = chkPrivileges;
	}

	public boolean isIncludePi() {
		return includePi;
	}

	public void setIncludePi(boolean includePi) {
		this.includePi = includePi;
	}

	public boolean isIncludeStats() {
		return includeStats;
	}

	public void setIncludeStats(boolean includeStats) {
		this.includeStats = includeStats;
	}

}
