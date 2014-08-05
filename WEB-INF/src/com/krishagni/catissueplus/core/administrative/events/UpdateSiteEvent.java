
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateSiteEvent extends RequestEvent {

	private SiteDetails siteDetails;

	private String siteName;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public UpdateSiteEvent(SiteDetails details, Long id) {
		this.siteDetails = details;
		this.siteDetails.setId(id);
	}

	public UpdateSiteEvent(SiteDetails details, String name) {
		this.siteDetails = details;
		setSiteName(name);
	}

	public SiteDetails getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(SiteDetails siteDetails) {
		this.siteDetails = siteDetails;
	}

}
