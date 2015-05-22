package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashMap;
import java.util.Map;

public class SpecimenDistributionEvent extends SpecimenEvent {
	private double quantity;

	
	public SpecimenDistributionEvent(Specimen specimen) {
		super(specimen);
	}

	public double getQuantity() {
		loadRecordIfNotLoaded();
		return quantity;
	}

	public void setQuantity(double quantity) {
		loadRecordIfNotLoaded();
		this.quantity = quantity;
	}

	@Override
	protected Map<String, Object> getEventAttrs() {
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("quantity", quantity);
		return attrs;
	}

	@Override
	protected void setEventAttrs(Map<String, Object> attrValues) {
		Object number = attrValues.get("quantity");
		if (number != null) {
			this.quantity = ((Number)number).doubleValue();
		}
	}

	@Override
	public String getFormName() {
		return "SpecimenDistributedEvent";
	}
}