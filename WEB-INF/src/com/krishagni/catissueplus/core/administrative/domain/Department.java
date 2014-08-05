
package com.krishagni.catissueplus.core.administrative.domain;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.common.util.Status;

public class Department {

	private static final String DEPARTMENT = "department";

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
			reportError(UserErrorCode.REFERENCED_ATTRIBUTE, DEPARTMENT);
		}
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

}
