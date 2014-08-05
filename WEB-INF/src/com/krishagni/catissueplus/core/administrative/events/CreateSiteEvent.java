
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateSiteEvent extends RequestEvent {

	private SiteDetails siteDetails;

	public SiteDetails getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(SiteDetails siteDetails) {
		this.siteDetails = siteDetails;
	}

}
