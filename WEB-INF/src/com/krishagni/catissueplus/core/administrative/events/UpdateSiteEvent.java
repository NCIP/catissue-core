
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.biospecimen.events.SiteDetails;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateSiteEvent extends RequestEvent {

	private SiteDetails siteDetails;

	public UpdateSiteEvent(SiteDetails details, Long id) {
		this.siteDetails = details;
		this.siteDetails.setId(id);
	}

	public SiteDetails getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(SiteDetails siteDetails) {
		this.siteDetails = siteDetails;
	}

}
