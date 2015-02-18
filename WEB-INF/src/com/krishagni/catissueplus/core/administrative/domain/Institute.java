package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
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
		CollectionUpdater.update(this.getDepartmentCollection(), institute.getDepartmentCollection());
		for (Department department : this.getDepartmentCollection()) {
			department.setInstitute(this);
		}
	}

	public void delete() {
		if (!this.getDepartmentCollection().isEmpty()) {
			throw OpenSpecimenException.userError(InstituteErrorCode.REF_ENTITY_FOUND);
		}
		
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
}