
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Collections;
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
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

@Configurable
public class Site {

	@Autowired
	private SiteDependencyChecker siteDependencyChecker;
	
	private Long id;

	private String name;

	private String code;

	private String type;

	private String activityStatus;

	private String address;
    
	private Set<User> coordinators = new HashSet<User>();

	private Set<Visit> visits = new HashSet<Visit>();

	private Set<StorageContainer> storageContainers = new HashSet<StorageContainer>();
	
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
		setCode(other.getCode());
		setType(other.getType());
		updateActivityStatus(other.getActivityStatus());
		setActivityStatus(other.getActivityStatus());
		CollectionUpdater.update(this.getCoordinators(), other.getCoordinators());
	}

	public Map<String, List> delete(boolean close) {
		if (!close) {
			Map<String, List> dependencies = siteDependencyChecker.getDependencies(this);
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
		
		if (newActivityStatus.equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			Map<String, List> dependencies = siteDependencyChecker.getDependencies(this);
			if (!dependencies.isEmpty()) {
				OpenSpecimenException.userError(SiteErrorCode.REF_ENTITY_FOUND);
			}
		}
	}

}
