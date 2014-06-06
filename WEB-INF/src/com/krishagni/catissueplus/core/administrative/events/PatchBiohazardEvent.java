
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchBiohazardEvent extends RequestEvent {

	private Long id;

	private BiohazardDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BiohazardDetails getDetails() {
		return details;
	}

	public void setDetails(BiohazardDetails details) {
		this.details = details;
	}

}
