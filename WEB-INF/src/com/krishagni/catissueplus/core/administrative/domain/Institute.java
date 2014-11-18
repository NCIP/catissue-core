
package com.krishagni.catissueplus.core.administrative.domain;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.util.Status;

public class Institute {

	private Long id;

	private String name;

	private String activityStatus;

	private Set<Department> departmentCollection;

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

	public Set<Department> getDepartmentCollection() {
		return departmentCollection;
	}

	public void setDepartmentCollection(Set<Department> departmentCollection) {
		this.departmentCollection = departmentCollection;
	}
	
	public void update(Institute institute) {
		setName(institute.getName());
		SetUpdater.<Department> newInstance().update(this.getDepartmentCollection(), institute.getDepartmentCollection());
		for (Department department : this.getDepartmentCollection()) {
			department.setInstitute(this);
		}
	}

	public void delete() {
		if (!this.getDepartmentCollection().isEmpty()) {
			reportError(UserErrorCode.REFERENCED_ATTRIBUTE, INSTITUTE);
		}
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

	private static final String INSTITUTE = "institute";
}
