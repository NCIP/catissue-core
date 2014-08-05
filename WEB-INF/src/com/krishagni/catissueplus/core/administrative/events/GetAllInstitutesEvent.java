package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GetAllInstitutesEvent extends ResponseEvent {

	private List<InstituteDetails> details;

	public List<InstituteDetails> getDetails() {
		return details;
	}

	public void setDetails(List<InstituteDetails> details) {
		this.details = details;
	}

	public static GetAllInstitutesEvent ok(List<InstituteDetails> details) {
		GetAllInstitutesEvent event = new GetAllInstitutesEvent();
		event.setDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
