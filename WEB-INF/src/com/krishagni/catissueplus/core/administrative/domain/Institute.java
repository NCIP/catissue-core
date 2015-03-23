package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

//import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.dependency.InstituteDependencyChecker;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;
@Configurable
@Entity
@Audited
public class Institute extends BaseEntity {
	private String name;

	private String activityStatus;

	private Set<Department> departments = new HashSet<Department>();

	@Autowired
	private InstituteDependencyChecker dependencyChecker;
	
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

	@NotAudited
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
		
		updateActivityStatus(other.getActivityStatus());
	}

	public Map<String,List> delete(Boolean close) {
		if (!close) {
			Map<String, List> dependencies = dependencyChecker.getDependencies(this);
			if (!dependencies.isEmpty()) {
				return dependencies;
			}
		}
		
		String activityStatus = close ? Status.ACTIVITY_STATUS_CLOSED.getStatus() 
				: Status.ACTIVITY_STATUS_DISABLED.getStatus();
		this.setActivityStatus(activityStatus);
		
		return Collections.<String, List>emptyMap();
	}
	
	private void updateActivityStatus(String newActivityStatus) {
		if (activityStatus.equals(newActivityStatus)) {
			return;
		}
		
		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(newActivityStatus)) {
			Map<String, List> dependencies = dependencyChecker.getDependencies(this);
			if (!dependencies.isEmpty()) {
				throw new OpenSpecimenException(ErrorType.USER_ERROR, InstituteErrorCode.REF_ENTITY_FOUND);
			}
		}
		
		setActivityStatus(newActivityStatus);
	}
}
