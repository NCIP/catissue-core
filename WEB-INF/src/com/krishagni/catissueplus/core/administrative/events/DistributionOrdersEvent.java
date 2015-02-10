package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionOrdersEvent extends ResponseEvent {
	private List<DistributionOrderDetail> orders = new ArrayList<DistributionOrderDetail>();

	public List<DistributionOrderDetail> getOrders() {
		return orders;
	}

	public void setOrders(List<DistributionOrderDetail> orders) {
		this.orders = orders;
	}
	
	public static DistributionOrdersEvent ok(List<DistributionOrderDetail> orders) {
		DistributionOrdersEvent resp = new DistributionOrdersEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOrders(orders);
		return resp;
	}
	
	public static DistributionOrdersEvent serverError(Exception e) {
		DistributionOrdersEvent resp = new DistributionOrdersEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		resp.setMessage(e.getMessage());
		return resp;		
	}
}
