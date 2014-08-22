
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SubRegistrationDetailsEvent extends ResponseEvent {

	private List<RegistrationInfo> registrationInfo;

	public List<RegistrationInfo> getRegistrationInfo() {
		return registrationInfo;
	}

	public void setRegistrationInfo(List<RegistrationInfo> registrationInfo) {
		this.registrationInfo = registrationInfo;
	}

	public static SubRegistrationDetailsEvent ok(List<RegistrationInfo> registrationsInfo) {
		SubRegistrationDetailsEvent event = new SubRegistrationDetailsEvent();
		event.setRegistrationInfo(registrationsInfo);
		event.setStatus(EventStatus.OK);
		return event;
	}
}
