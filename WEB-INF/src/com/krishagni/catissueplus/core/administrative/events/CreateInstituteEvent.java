
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateInstituteEvent extends RequestEvent {

	private InstituteDetails instituteDetails;

	public InstituteDetails getInstituteDetails() {
		return instituteDetails;
	}

	public void setInstituteDetails(InstituteDetails instituteDetails) {
		this.instituteDetails = instituteDetails;
	}
}
