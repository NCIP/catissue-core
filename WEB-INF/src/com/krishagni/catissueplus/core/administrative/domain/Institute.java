package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.util.Status;

public class Institute {

	private Long id;

	private String name;

	private String activityStatus;

	private Set<Department> departments = new HashSet<Department>();

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

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	
	public void update(Institute other) {		
		setName(other.getName());
		
		CollectionUpdater.update(this.getDepartments(), other.getDepartments());
		for (Department department : this.getDepartments()) {
			department.setInstitute(this);
		}
		
		setActivityStatus(other.getActivityStatus());
	}

	public void delete(Boolean isClosed) {
		if (isClosed) {
			this.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.getStatus());
		} else {
			this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		}
	}
}