package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateDistributionOrderEvent extends RequestEvent {
	private DistributionOrderDetail order;

	public DistributionOrderDetail getOrder() {
		return order;
	}

	public void setOrder(DistributionOrderDetail detail) {
		this.order = detail;
	}
}
