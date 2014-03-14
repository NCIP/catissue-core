
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateRegistrationEvent extends RequestEvent {

	private Long cpId;

	private CollectionProtocolRegistrationDetail cprDetail;

	public CollectionProtocolRegistrationDetail getCprDetail() {
		return cprDetail;
	}

	public void setCprDetail(CollectionProtocolRegistrationDetail cprDetail) {
		this.cprDetail = cprDetail;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

}
