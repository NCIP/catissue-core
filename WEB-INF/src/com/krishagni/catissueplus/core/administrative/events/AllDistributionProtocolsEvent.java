
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllDistributionProtocolsEvent extends ResponseEvent {

	private List<DistributionProtocolDetails> protocols;

	public List<DistributionProtocolDetails> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<DistributionProtocolDetails> protocols) {
		this.protocols = protocols;
	}

	public static AllDistributionProtocolsEvent ok(List<DistributionProtocolDetails> distributionProtocols) {
		AllDistributionProtocolsEvent resp = new AllDistributionProtocolsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setProtocols(distributionProtocols);
		return resp;
	}
}