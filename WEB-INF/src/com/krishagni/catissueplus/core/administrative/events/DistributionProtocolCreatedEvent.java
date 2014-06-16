
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionProtocolCreatedEvent extends ResponseEvent {

	private DistributionProtocolDetails details;

	public DistributionProtocolDetails getDetails() {
		return details;
	}

	public void setDetails(DistributionProtocolDetails details) {
		this.details = details;
	}

	public static DistributionProtocolCreatedEvent ok(DistributionProtocolDetails protocolDetails) {
		DistributionProtocolCreatedEvent createdEvent = new DistributionProtocolCreatedEvent();
		createdEvent.setDetails(protocolDetails);
		createdEvent.setStatus(EventStatus.OK);

		return createdEvent;
	}

	public static DistributionProtocolCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		DistributionProtocolCreatedEvent resp = new DistributionProtocolCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
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
