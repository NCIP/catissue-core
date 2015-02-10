
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateDistributionProtocolEvent extends RequestEvent {

	private DistributionProtocolDetail protocol;

	private Long id;

	private String title;

	public DistributionProtocolDetail getProtocol() {
		return protocol;
	}

	public void setProtocol(DistributionProtocolDetail details) {
		this.protocol = details;
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
