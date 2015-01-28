
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.common.util.Status;

public class InstituteDetails {

	private Long id;

	private String name;

	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
	
	private List<DepartmentDetails> departments;

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
	
	public List<DepartmentDetails> getDepartments(){
		return departments;
	}
	
	public void setDepartments(List<DepartmentDetails> departments){		
		this.departments = departments;
	}

	public static InstituteDetails fromDomain(Institute institute) {
		InstituteDetails instituteDetails = new InstituteDetails();
		instituteDetails.setId(institute.getId());
		instituteDetails.setName(institute.getName());
		instituteDetails.setActivityStatus(institute.getActivityStatus());
		instituteDetails.setDepartments(DepartmentDetails.fromDepartments(institute.getDepartmentCollection()));
		return instituteDetails;
	}

}