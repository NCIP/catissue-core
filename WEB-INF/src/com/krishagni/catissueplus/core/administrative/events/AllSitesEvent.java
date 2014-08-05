
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllSitesEvent extends ResponseEvent {

	private List<SiteDetails> sites;

	public void setSites(List<SiteDetails> sites) {
		this.sites = sites;
	}

	public List<SiteDetails> getSites() {
		return sites;
	}

	public static AllSitesEvent ok(List<SiteDetails> sites) {
		AllSitesEvent resp = new AllSitesEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSites(sites);
		return resp;
	}
}
