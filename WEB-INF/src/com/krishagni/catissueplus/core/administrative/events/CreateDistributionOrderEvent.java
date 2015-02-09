package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateDistributionOrderEvent extends RequestEvent {
	private DistributionOrderDetail detail;

	public DistributionOrderDetail getDetail() {
		return detail;
	}

	public void setDetail(DistributionOrderDetail detail) {
		this.detail = detail;
	}
}
