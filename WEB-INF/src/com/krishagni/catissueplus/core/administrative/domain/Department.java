
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class Department {
	private Long id;

	private String name;

	private Institute institute;

	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();

	private Set<User> userCollection = new HashSet<User>();

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

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Set<User> getUserCollection() {
		return userCollection;
	}

	public void setUserCollection(Set<User> userCollection) {
		this.userCollection = userCollection;
	}

	public void update(Department department) {
		this.setName(department.getName());
		this.setInstitute(department.getInstitute());
		setActivityStatus(department.getActivityStatus());
	}

	public void delete() {
		if (!this.getUserCollection().isEmpty()) {
			throw OpenSpecimenException.userError(InstituteErrorCode.DEPT_REF_ENTITY_FOUND);
		}
		
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

}
