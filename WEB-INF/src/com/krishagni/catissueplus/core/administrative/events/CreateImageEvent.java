
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateImageEvent extends RequestEvent {

	private ImageDetails details;

	public ImageDetails getDetails() {
		return details;
	}

	public void setDetails(ImageDetails details) {
		this.details = details;
	}

}
