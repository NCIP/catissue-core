
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.dependency.SiteDependencyChecker;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

@Configurable
public class Site {

	private Long id;

	private String name;
	
	private Institute institute;

	private String code;

	private String type;

	private String activityStatus;

	private String address;
    
	private Set<User> coordinators = new HashSet<User>();

	private Set<Visit> visits = new HashSet<Visit>();

	private Set<StorageContainer> storageContainers = new HashSet<StorageContainer>();

	@Autowired
	private SiteDependencyChecker dependencyChecker;

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

	public String getCode() {
		return this.code; 
	}

	public void setCode(String code) { 
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getAddress() { 
		return address; 
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<User> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(Set<User> coordinators) {
		this.coordinators = coordinators;
	}

	public Set<Visit> getVisits() {
		return visits;
	}

	public void setVisits(Set<Visit> visits) {
		this.visits = visits;
	}

	public Set<StorageContainer> getStorageContainers() {
		return storageContainers;
	}

	public void setStorageContainers(Set<StorageContainer> storageContainers) {
		this.storageContainers = storageContainers;
	}

	public void update(Site other) {
		setName(other.getName());
		setInstitute(other.getInstitute());
		setCode(other.getCode());
		setType(other.getType());
		updateActivityStatus(other.getActivityStatus());
		CollectionUpdater.update(this.getCoordinators(), other.getCoordinators());
	}
	
	public List<Map<String, Object>> getDependencyStat() {
		return dependencyChecker.getDependencyStat(this);
	}

	public void delete(boolean close) {
		String activityStatus = Status.ACTIVITY_STATUS_CLOSED.getStatus();
		if (!close) {
			activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
			List<Map<String, Object>> dependencyStat = getDependencyStat();
			if (!dependencyStat.isEmpty()) {
				throw OpenSpecimenException.userError(SiteErrorCode.REF_ENTITY_FOUND);
			}
		}
		
		this.setActivityStatus(activityStatus);
	}
	
	private void updateActivityStatus(String newActivityStatus) {
		if (activityStatus.equals(newActivityStatus)) {
			return;
		}
		
		if (newActivityStatus.equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			List<Map<String, Object>> dependencies = getDependencyStat();
			if (!dependencies.isEmpty()) {
				throw new OpenSpecimenException(ErrorType.USER_ERROR,SiteErrorCode.REF_ENTITY_FOUND);
			}
		}
		
		this.setActivityStatus(newActivityStatus);
	}

}
