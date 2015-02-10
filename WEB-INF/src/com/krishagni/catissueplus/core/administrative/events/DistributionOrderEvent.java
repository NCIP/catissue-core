package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionOrderEvent extends ResponseEvent {
	private Long distirbutionId;
	
	private DistributionOrderDetail order;

	public Long getDistirbutionId() {
		return distirbutionId;
	}

	public void setDistirbutionId(Long distirbutionId) {
		this.distirbutionId = distirbutionId;
	}

	public DistributionOrderDetail getOrder() {
		return order;
	}

	public void setOrder(DistributionOrderDetail order) {
		this.order = order;
	}
	
	public static DistributionOrderEvent ok(DistributionOrderDetail input) {
		DistributionOrderEvent resp = new DistributionOrderEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOrder(input);
		return resp;
	}
	
	public static DistributionOrderEvent notFound(Long distributionId) {
		DistributionOrderEvent resp = new DistributionOrderEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setDistirbutionId(distributionId);
		return resp;
	}
	
	public static DistributionOrderEvent serverError(Exception e) {
		DistributionOrderEvent resp = new DistributionOrderEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}