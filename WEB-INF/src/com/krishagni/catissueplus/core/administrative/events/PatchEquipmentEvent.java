
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchEquipmentEvent extends RequestEvent {

	private Long id;

	private EquipmentPatchDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EquipmentPatchDetails getDetails() {
		return details;
	}

	public void setDetails(EquipmentPatchDetails details) {
		this.details = details;
	}

}
