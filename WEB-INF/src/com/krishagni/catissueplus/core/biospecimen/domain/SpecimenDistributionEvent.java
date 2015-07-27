package com.krishagni.catissueplus.core.biospecimen.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.common.util.NumUtil;

public class SpecimenDistributionEvent extends SpecimenEvent {
	private BigDecimal quantity;

	
	public SpecimenDistributionEvent(Specimen specimen) {
		super(specimen);
	}

	public BigDecimal getQuantity() {
		loadRecordIfNotLoaded();
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
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
			this.quantity = NumUtil.numberToBigDecimal(number);
		}
	}

	@Override
	public String getFormName() {
		return "SpecimenDistributedEvent";
	}
}