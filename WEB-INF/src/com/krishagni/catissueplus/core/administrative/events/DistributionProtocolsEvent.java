
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.RegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolsEvent extends ResponseEvent {

	private List<DistributionProtocolDetail> protocols;

	public List<DistributionProtocolDetail> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<DistributionProtocolDetail> protocols) {
		this.protocols = protocols;
	}

	public static DistributionProtocolsEvent ok(List<DistributionProtocolDetail> distributionProtocols) {
		DistributionProtocolsEvent resp = new DistributionProtocolsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setProtocols(distributionProtocols);
		return resp;
	}
	
	public static DistributionProtocolsEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		
		DistributionProtocolsEvent resp = new DistributionProtocolsEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}