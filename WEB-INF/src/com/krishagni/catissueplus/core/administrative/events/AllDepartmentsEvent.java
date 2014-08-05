
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllDepartmentsEvent extends ResponseEvent {

	private List<DepartmentDetails> departments;

	public List<DepartmentDetails> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentDetails> departments) {
		this.departments = departments;
	}

	public static AllDepartmentsEvent ok(List<DepartmentDetails> departments) {
		AllDepartmentsEvent resp = new AllDepartmentsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setDepartments(departments);
		return resp;
	}
}
