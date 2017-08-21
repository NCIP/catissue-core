
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;

public interface DistributionProtocolFactory {
	public DistributionProtocol createDistributionProtocol(DistributionProtocolDetail distributionProtocolDetail);
	
}
