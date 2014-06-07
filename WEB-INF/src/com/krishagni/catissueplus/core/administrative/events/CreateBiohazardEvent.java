
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateBiohazardEvent extends RequestEvent{

	private BiohazardDetails biohazardDetails;

	public BiohazardDetails getBiohazardDetails() {
		return biohazardDetails;
	}

	public void setBiohazardDetails(BiohazardDetails biohazardDetails) {
		this.biohazardDetails = biohazardDetails;
	}

}
