
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.RegisteredParticipantsEvent;
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
	
	public static AllDistributionProtocolsEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		
		AllDistributionProtocolsEvent resp = new AllDistributionProtocolsEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}