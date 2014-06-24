
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchSiteEvent extends RequestEvent {

	private Long siteId;

	private SiteDetails details;

	private String siteName;

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public SiteDetails getSiteDetails() {
		return details;
	}

	public void setSiteDetails(SiteDetails details) {
		this.details = details;
	}

}
