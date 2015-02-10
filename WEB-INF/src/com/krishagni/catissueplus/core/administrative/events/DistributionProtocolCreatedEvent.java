
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolCreatedEvent extends ResponseEvent {

	private DistributionProtocolDetail protocol;

	public DistributionProtocolDetail getProtocol() {
		return protocol;
	}

	public void setProtocol(DistributionProtocolDetail details) {
		this.protocol = details;
	}

	public static DistributionProtocolCreatedEvent ok(DistributionProtocolDetail protocolDetails) {
		DistributionProtocolCreatedEvent createdEvent = new DistributionProtocolCreatedEvent();
		createdEvent.setProtocol(protocolDetails);
		createdEvent.setStatus(EventStatus.OK);

		return createdEvent;
	}

	public static DistributionProtocolCreatedEvent badRequest(Exception e) {
		DistributionProtocolCreatedEvent resp = new DistributionProtocolCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		resp.setMessage(e.getMessage());
		
		if (e instanceof ObjectCreationException) {
			resp.setErroneousFields(((ObjectCreationException)e).getErroneousFields());
		}
		return resp;
	}

	public static DistributionProtocolCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		DistributionProtocolCreatedEvent resp = new DistributionProtocolCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
