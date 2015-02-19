package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DistributionOrderService {
	public ResponseEvent<List<DistributionOrderDetail>> getDistributionOrders(RequestEvent<DistributionOrderListCriteria> req);
	
	public ResponseEvent<DistributionOrderDetail> getDistributionOrder(RequestEvent<Long> req);
	
	public ResponseEvent<DistributionOrderDetail> createDistribution(RequestEvent<DistributionOrderDetail> req);
	
	public ResponseEvent<DistributionOrderDetail> updateDistribution(RequestEvent<DistributionOrderDetail> req);
}
