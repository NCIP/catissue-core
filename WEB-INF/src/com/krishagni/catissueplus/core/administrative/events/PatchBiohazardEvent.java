
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchBiohazardEvent extends RequestEvent {

	private Long id;

	private String name;

	private BiohazardPatchDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BiohazardPatchDetails getDetails() {
		return details;
	}

	public void setDetails(BiohazardPatchDetails details) {
		this.details = details;
	}

}
