package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AddPvEvent extends RequestEvent {
	
private PermissibleValueDetails pvDetails;
	
	public AddPvEvent(PermissibleValueDetails pvDetails) {
		setDetails(pvDetails);
	}

	public PermissibleValueDetails getDetails() {
		return pvDetails;
	}

	public void setDetails(PermissibleValueDetails pvDetails) {
		this.pvDetails = pvDetails;
	}

}
