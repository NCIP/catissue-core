
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolPatchDetails;

public interface DistributionProtocolFactory {

	public DistributionProtocol create(DistributionProtocolDetails distributionProtocolDetails);

	public DistributionProtocol patch(DistributionProtocol distributionProtocol, DistributionProtocolPatchDetails details);

}
