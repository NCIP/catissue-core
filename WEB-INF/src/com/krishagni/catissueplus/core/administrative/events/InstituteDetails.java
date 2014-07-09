
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.common.util.Status;

public class InstituteDetails {

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

	public static InstituteDetails fromDomain(Institute institute) {
		InstituteDetails instituteDetails = new InstituteDetails();
		instituteDetails.setId(institute.getId());
		instituteDetails.setName(institute.getName());
		instituteDetails.setActivityStatus(institute.getActivityStatus());
		return instituteDetails;
	}

}
