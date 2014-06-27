
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateBiohazardEvent extends RequestEvent {

	private BiohazardDetails biohazardDetails;

	private Long id;

	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
