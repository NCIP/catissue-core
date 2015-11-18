package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ShipmentListCriteria extends AbstractListCriteria<ShipmentListCriteria> {
	private String name;
	
	private String institute;
	
	public String name() {
		return name;
	}
	
	public ShipmentListCriteria name(String name) {
		this.name = name;
		return self();
	}
	
	public String institute() {
		return institute;
	}
	
	public ShipmentListCriteria institute(String institute) {
		this.institute = institute;
		return self();
	}
	
	@Override
	public ShipmentListCriteria self() {
		return this;
	}

}
