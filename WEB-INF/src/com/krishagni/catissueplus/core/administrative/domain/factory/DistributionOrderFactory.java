package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;

public interface DistributionOrderFactory {
	public DistributionOrder create(DistributionOrderDetail detail);
	
}
