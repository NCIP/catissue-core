
package com.krishagni.catissueplus.core.auth.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllDomainsEvent extends ResponseEvent {

	private List<DomainDetails> domains;

	public List<DomainDetails> getDomains() {
		return domains;
	}

	public void setDomains(List<DomainDetails> domains) {
		this.domains = domains;
	}

	public static AllDomainsEvent ok(List<DomainDetails> domainList) {
		AllDomainsEvent event = new AllDomainsEvent();
		event.setDomains(domainList);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
