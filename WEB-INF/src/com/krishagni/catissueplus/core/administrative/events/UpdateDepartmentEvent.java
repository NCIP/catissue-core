package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;


public class UpdateDepartmentEvent extends RequestEvent {

	private DepartmentDetails departmentDetails;

	public DepartmentDetails getDepartmentDetails() {
		return departmentDetails;
	}

	public void setDepartmentDetails(DepartmentDetails departmentDetails) {
		this.departmentDetails = departmentDetails;
	}
}
