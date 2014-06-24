
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateDistributionProtocolEvent extends RequestEvent {

	private DistributionProtocolDetails details;

	private Long id;

	private String title;

	public DistributionProtocolDetails getDetails() {
		return details;
	}

	public void setDetails(DistributionProtocolDetails details) {
		this.details = details;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
