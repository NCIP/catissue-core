
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateBiohazardEvent extends RequestEvent {

	private BiohazardDetails biohazardDetails;

	private Long id;

	public BiohazardDetails getBiohazardDetails() {
		return biohazardDetails;
	}

	public void setBiohazardDetails(BiohazardDetails biohazardDetails) {
		this.biohazardDetails = biohazardDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
