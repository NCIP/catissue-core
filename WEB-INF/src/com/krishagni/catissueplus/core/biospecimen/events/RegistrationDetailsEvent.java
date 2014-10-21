package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RegistrationDetailsEvent extends ResponseEvent {
	private List<CollectionProtocolRegistrationDetail> cprDetails = new ArrayList<CollectionProtocolRegistrationDetail>();

	public List<CollectionProtocolRegistrationDetail> getCprDetails() {
		return cprDetails;
	}

	public void setCprDetails(List<CollectionProtocolRegistrationDetail> cprDetails) {
		this.cprDetails = cprDetails;
	}
	
	public static RegistrationDetailsEvent ok(List<CollectionProtocolRegistrationDetail> cprDetails) {
		RegistrationDetailsEvent res = new RegistrationDetailsEvent();
		res.setCprDetails(cprDetails);
		res.setStatus(EventStatus.OK);
		return res;
	}
	
	public static RegistrationDetailsEvent serverError(Throwable e) {
		RegistrationDetailsEvent res = new RegistrationDetailsEvent();
		res.setException(e);
		res.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		res.setMessage(e == null ? null : e.getMessage());
		return res;
	}
}
