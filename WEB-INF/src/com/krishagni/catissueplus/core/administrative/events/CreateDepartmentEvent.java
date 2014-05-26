
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateDepartmentEvent extends RequestEvent {

	DepartmentDetails departmentDetails;

	public DepartmentDetails getDepartmentDetails() {
		return departmentDetails;
	}

	public void setDepartmentDetails(DepartmentDetails departmentDetails) {
		this.departmentDetails = departmentDetails;
	}

}
