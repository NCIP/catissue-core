package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSpecificationDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSpecificationListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DistributionOrderDao extends Dao<DistributionOrder> {
	public List<DistributionOrderSummary> getOrders(DistributionOrderListCriteria criteria);
	
	public DistributionOrder getOrder(String name);
	
	public List<DistributionOrderSpecificationDetails> getOrderSpecifications(DistributionOrderSpecificationListCriteria listCrit);
	
}
