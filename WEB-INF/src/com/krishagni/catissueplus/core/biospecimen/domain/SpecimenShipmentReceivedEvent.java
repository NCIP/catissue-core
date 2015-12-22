package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.ShipmentItem;


public class SpecimenShipmentReceivedEvent extends SpecimenEvent {
	
	public SpecimenShipmentReceivedEvent(Specimen specimen) {
		super(specimen);
	}
	
	@Override
	public String getFormName() {
		return "SpecimenShipmentReceivedEvent";
	}

	@Override
	protected Map<String, Object> getEventAttrs() {
		return null;
	}

	@Override
	protected void setEventAttrs(Map<String, Object> attrValues) {
		
	}
	
	public static SpecimenShipmentReceivedEvent createForShipmentItem(ShipmentItem item) {
		SpecimenShipmentReceivedEvent event = new SpecimenShipmentReceivedEvent(item.getSpecimen());
		event.setId(item.getId());
		return event;
	}
}
