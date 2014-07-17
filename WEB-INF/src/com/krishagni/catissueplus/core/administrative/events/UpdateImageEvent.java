
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateImageEvent extends RequestEvent {

	private Long id;

	private ImageDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ImageDetails getDetails() {
		return details;
	}

	public void setDetails(ImageDetails details) {
		this.details = details;
	}

}
