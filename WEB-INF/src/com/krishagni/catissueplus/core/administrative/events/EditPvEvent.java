
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class EditPvEvent extends RequestEvent {

	private PermissibleValueDetails details;

	public EditPvEvent(PermissibleValueDetails details, Long pvId) {
		setDetails(details);
		this.details.setId(pvId);
	}

	public PermissibleValueDetails getDetails() {
		return details;
	}

	public void setDetails(PermissibleValueDetails details) {
		this.details = details;
	}

}
