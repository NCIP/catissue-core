
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllEquipmentEvent extends ResponseEvent {

	private List<EquipmentDetails> equipments;

	public List<EquipmentDetails> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentDetails> equipments) {
		this.equipments = equipments;
	}

	public static AllEquipmentEvent ok(List<EquipmentDetails> equipments) {
		AllEquipmentEvent resp = new AllEquipmentEvent();
		resp.setStatus(EventStatus.OK);
		resp.setEquipments(equipments);
		return resp;
	}

}
