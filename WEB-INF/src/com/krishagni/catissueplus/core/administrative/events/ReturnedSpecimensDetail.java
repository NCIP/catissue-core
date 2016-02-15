package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;
import java.util.Map;

public class ReturnedSpecimensDetail {
	private Long orderId;

	private String orderName;

	private List<SpecimenReturnDetail> returnedSpecimens;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public List<SpecimenReturnDetail> getReturnedSpecimens() {
		return returnedSpecimens;
	}

	public void setReturnedSpecimens(List<SpecimenReturnDetail> returnedSpecimens) {
		this.returnedSpecimens = returnedSpecimens;
	}
}
