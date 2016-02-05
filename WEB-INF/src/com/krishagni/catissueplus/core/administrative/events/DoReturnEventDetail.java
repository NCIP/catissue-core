package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;
import java.util.Map;

public class DoReturnEventDetail {
	private DistributionOrderSummary order;

	private List<Map<String, Object>> valueMapList;

	public DistributionOrderSummary getOrder() {
		return order;
	}

	public void setOrder(DistributionOrderSummary order) {
		this.order = order;
	}

	public List<Map<String, Object>> getValueMapList() {
		return valueMapList;
	}

	public void setValueMapList(List<Map<String, Object>> valueMapList) {
		this.valueMapList = valueMapList;
	}

}
