package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ShippingOrderListCriteria extends AbstractListCriteria<ShippingOrderListCriteria> {
	private String name;
	
	private String institute;
	
	public String name() {
		return name;
	}
	
	public ShippingOrderListCriteria name(String name) {
		this.name = name;
		return self();
	}
	
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
