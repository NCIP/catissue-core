
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.common.util.Status;

public class DepartmentDetails {

	private Long id;

	private String name;

	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static DepartmentDetails fromDepartment(Department department) {
		DepartmentDetails details = new DepartmentDetails();
		details.setId(department.getId());
		details.setName(department.getName());
		details.setActivityStatus(department.getActivityStatus());
		return details;
	}
	
	public static List<DepartmentDetails> fromDepartments(Set<Department> departments) {
		List<DepartmentDetails> departmentsDetails = new ArrayList<DepartmentDetails>();
		for(Department department : departments) {
			departmentsDetails.add(fromDepartment(department));
		}
		return departmentsDetails;
	}

}