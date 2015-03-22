package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public List<Map<String, Object>> getDependencyStat() {
		return dependencyChecker.getDependencyStat(this);
	}
	
	public void delete(Boolean close) {
		String activityStatus = Status.ACTIVITY_STATUS_CLOSED.getStatus();
		if (!close) {
			activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
			List<Map<String, Object>> dependencies = getDependencyStat();
			if (!dependencies.isEmpty()) {
				throw OpenSpecimenException.userError(InstituteErrorCode.REF_ENTITY_FOUND);
			}
		}
		
		this.setActivityStatus(activityStatus);
	}
	
	private void updateActivityStatus(String newActivityStatus) {
		if (activityStatus.equals(newActivityStatus)) {
			return;
		}
		
		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(newActivityStatus)) {
			List<Map<String, Object>> dependencies = dependencyChecker.getDependencyStat(this);
			if (!dependencies.isEmpty()) {
				throw new OpenSpecimenException(ErrorType.USER_ERROR, InstituteErrorCode.REF_ENTITY_FOUND);
			}
		}
		
		setActivityStatus(newActivityStatus);
	}
}
