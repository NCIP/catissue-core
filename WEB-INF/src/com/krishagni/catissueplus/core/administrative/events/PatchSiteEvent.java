
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.biospecimen.events.SiteDetails;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchSiteEvent extends RequestEvent{

	private Long siteId;

	private SiteDetails details;

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public SiteDetails getSiteDetails() {
		return details;
	}

	public void setSiteDetails(SiteDetails details) {
		this.details = details;
	}

}
