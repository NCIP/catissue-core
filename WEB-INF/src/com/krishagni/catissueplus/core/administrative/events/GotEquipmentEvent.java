
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GotEquipmentEvent extends ResponseEvent {

	private Long id;

	private String displayName;

	private EquipmentDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public EquipmentDetails getDetails() {
		return details;
	}

	public void setDetails(EquipmentDetails details) {
		this.details = details;
	}

	public static GotEquipmentEvent notFound(Long id) {
		GotEquipmentEvent resp = new GotEquipmentEvent();
		resp.setId(id);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static GotEquipmentEvent notFound(String displayName) {
		GotEquipmentEvent resp = new GotEquipmentEvent();
		resp.setDisplayName(displayName);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}

	public static GotEquipmentEvent ok(EquipmentDetails details) {
		GotEquipmentEvent event = new GotEquipmentEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
