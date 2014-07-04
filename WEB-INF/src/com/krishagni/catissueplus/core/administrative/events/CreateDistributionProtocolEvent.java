
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateDistributionProtocolEvent extends RequestEvent {

	private DistributionProtocolDetails distributionProtocolDetails;

	public DistributionProtocolDetails getDistributionProtocolDetails() {
		return distributionProtocolDetails;
	}

	public void setDistributionProtocolDetails(DistributionProtocolDetails distributionProtocolDetails) {
		this.distributionProtocolDetails = distributionProtocolDetails;
	}

}
