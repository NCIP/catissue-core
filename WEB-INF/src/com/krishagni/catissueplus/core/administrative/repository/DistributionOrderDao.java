package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DistributionOrderDao extends Dao<DistributionOrder> {
	public DistributionOrder getDistributionOrder(String name);
	
	public List<DistributionOrder> getDistributionOrders(DistributionListCriteria criteria);
}
