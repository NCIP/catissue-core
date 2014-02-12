
package com.krishagni.catissueplus.core.events.registration;

import com.krishagni.catissueplus.core.events.RequestEvent;

public class ReqRegistrationSummaryEvent extends RequestEvent {

	private Long cpId;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

}
