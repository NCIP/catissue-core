
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.common.util.Status;

public class DepartmentDetails {

	private Long id;

	private String name;

	private String instituteName;

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

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static DepartmentDetails fromDomain(Department department) {
		DepartmentDetails details = new DepartmentDetails();
		details.setId(department.getId());
		details.setName(department.getName());
		//details.setInstituteName(department.getInstitute().getName());
		//details.setActivityStatus(department.getActivityStatus());
		return details;
	}

}
