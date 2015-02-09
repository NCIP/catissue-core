package com.krishagni.catissueplus.core.administrative.events;

public class UpdateDistributionOrderEvent {
	private DistributionOrderDetail detail;

	public DistributionOrderDetail getDetail() {
		return detail;
	}

	public void setDetail(DistributionOrderDetail detail) {
		this.detail = detail;
	}
}
