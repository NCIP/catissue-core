package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.ShipmentItem;


public class SpecimenShipmentShippedEvent extends SpecimenEvent {
	
	public SpecimenShipmentShippedEvent(Specimen specimen) {
		super(specimen);
	}

	@Override
	public String getFormName() {
		return "SpecimenShipmentShippedEvent";
	}
	
	@Override
	protected Map<String, Object> getEventAttrs() {
		return null;
	}

	@Override
	protected void setEventAttrs(Map<String, Object> attrValues) {
		
	}
	
	public static SpecimenShipmentShippedEvent createForShipmentItem(ShipmentItem item) {
		SpecimenShipmentShippedEvent event = new SpecimenShipmentShippedEvent(item.getSpecimen());
		event.setId(item.getId());
		return event;
	}
}
