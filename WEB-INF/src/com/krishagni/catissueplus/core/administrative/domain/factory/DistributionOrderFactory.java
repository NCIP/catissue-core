package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.Status;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;

public interface DistributionOrderFactory {
	public DistributionOrder createDistributionOrder(DistributionOrderDetail detail, Status status);
	
}
