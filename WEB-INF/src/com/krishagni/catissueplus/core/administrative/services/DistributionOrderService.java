package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.AllDistributionOrdersEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionOrderEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDistributionOrdersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionOrderEvent;

public interface DistributionOrderService {
	public DistributionOrderCreatedEvent createDistribution(CreateDistributionOrderEvent req);
	
	public DistributionOrderUpdatedEvent updateDistribution(UpdateDistributionOrderEvent req);
	
	public AllDistributionOrdersEvent getAllDistributionOrders(ReqAllDistributionOrdersEvent req);
}
