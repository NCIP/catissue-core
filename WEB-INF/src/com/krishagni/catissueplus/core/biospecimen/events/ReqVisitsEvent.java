
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqVisitsEvent extends RequestEvent {
	private Long cprId;
	
	private Long cpId;
	
	private String ppid;
	
	private boolean includeStats;

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

	public boolean isIncludeStats() {
		return includeStats;
	}

	public void setIncludeStats(boolean includeStats) {
		this.includeStats = includeStats;
	}

}
