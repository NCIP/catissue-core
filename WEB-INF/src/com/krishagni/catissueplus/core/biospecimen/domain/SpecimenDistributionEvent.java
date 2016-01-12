package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;

public class SpecimenDistributionEvent extends SpecimenEvent {
	
	public SpecimenDistributionEvent(Specimen specimen) {
		super(specimen);
	}
	
	@Override
	public String getFormName() {
		return "SpecimenDistributedEvent";
	}
	
	@Override
	protected Map<String, Object> getEventAttrs() {
		return null;
	}

	@Override
	protected void setEventAttrs(Map<String, Object> attrValues) {
		
	}
	
	public static SpecimenDistributionEvent createForDistributionOrderItem(DistributionOrderItem item) {
		SpecimenDistributionEvent event = new SpecimenDistributionEvent(item.getSpecimen());
		event.setId(item.getId());
		return event;
	}
}