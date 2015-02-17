package com.krishagni.catissueplus.core.administrative.events;

public class UpdateDistributionOrderEvent {
	private Long distributionId;
	
	private DistributionOrderDetail order;
	
	public Long getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(Long distributionId) {
		this.distributionId = distributionId;
	}

	public DistributionOrderDetail getOrder() {
		return order;
	}

	public void setOrder(DistributionOrderDetail detail) {
		this.order = detail;
	}
}
