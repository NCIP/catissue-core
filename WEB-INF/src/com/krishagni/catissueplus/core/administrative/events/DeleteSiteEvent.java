
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeleteSiteEvent extends RequestEvent {

	private SiteDetails details;

	private Long id;

	public SiteDetails getDetails() {
		return details;
	}

	public void setDetails(SiteDetails details) {
		this.details = details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
