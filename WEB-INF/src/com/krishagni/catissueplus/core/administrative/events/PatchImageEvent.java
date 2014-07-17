
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchImageEvent extends RequestEvent {

	private Long id;

	private ImagePatchDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ImagePatchDetails getDetails() {
		return details;
	}

	public void setDetails(ImagePatchDetails details) {
		this.details = details;
	}

}
