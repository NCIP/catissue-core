package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DistributionOrderDao extends Dao<DistributionOrder> {
	public List<DistributionOrderSummary> getOrders(DistributionOrderListCriteria criteria);
	
	public DistributionOrder getOrder(String name);

	public List<DistributionOrder> getOrders(List<String> names);

	public List<DistributionOrderItem> getDistributedOrderItems(List<String> specimenLabels);

	public Map<String, Object> getOrderIds(String key, Object value);


}
