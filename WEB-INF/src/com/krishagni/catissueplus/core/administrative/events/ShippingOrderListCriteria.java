package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ShippingOrderListCriteria extends AbstractListCriteria<ShippingOrderListCriteria> {
	private String institute;
	
	public String institute() {
		return institute;
	}
	
	public ShippingOrderListCriteria institute(String institute) {
		this.institute = institute;
		return self();
	}
	
	@Override
	public ShippingOrderListCriteria self() {
		return this;
	}

}
