
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateDistributionProtocolEvent extends RequestEvent {

	private DistributionProtocolDetail protocol;

	public DistributionProtocolDetail getProtocol() {
		return protocol;
	}

	public void setProtocol(DistributionProtocolDetail distributionProtocolDetails) {
		this.protocol = distributionProtocolDetails;
	}

}
