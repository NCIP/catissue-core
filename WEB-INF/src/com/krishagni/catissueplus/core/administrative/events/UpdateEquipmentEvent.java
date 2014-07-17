
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateEquipmentEvent extends RequestEvent {

	private Long id;

	private EquipmentDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EquipmentDetails getDetails() {
		return details;
	}

	public void setDetails(EquipmentDetails details) {
		this.details = details;
	}

}
