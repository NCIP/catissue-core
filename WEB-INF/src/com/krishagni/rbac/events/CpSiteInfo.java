package com.krishagni.rbac.events;

public class CpSiteInfo {

	private Long cpId;
	
	private Long siteId;
	
	public CpSiteInfo() {
		
	}
	
	public CpSiteInfo(Long cpId, Long siteId) {
		this.cpId = cpId;
		this.siteId = siteId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
}
