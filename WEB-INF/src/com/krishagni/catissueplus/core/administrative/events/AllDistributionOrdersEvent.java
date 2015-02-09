package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllDistributionOrdersEvent extends ResponseEvent {
	private List<DistributionOrderDetail> orders = new ArrayList<DistributionOrderDetail>();

	public List<DistributionOrderDetail> getOrders() {
		return orders;
	}

	public void setOrders(List<DistributionOrderDetail> orders) {
		this.orders = orders;
	}
	
	public static AllDistributionOrdersEvent ok(List<DistributionOrderDetail> orders) {
		AllDistributionOrdersEvent resp = new AllDistributionOrdersEvent();
		resp.setStatus(EventStatus.OK);
		resp.setOrders(orders);
		return resp;
	}
	
	public static AllDistributionOrdersEvent serverError(Exception e) {
		AllDistributionOrdersEvent resp = new AllDistributionOrdersEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		resp.setMessage(e.getMessage());
		return resp;		
	}
}
