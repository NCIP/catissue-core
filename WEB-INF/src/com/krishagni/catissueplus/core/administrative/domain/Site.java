
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.util.Status;

public class Site {

	private Long id;

	private String name;

	private String code;

	private String type;

	private String activityStatus;

	private String address;
    
	private Set<User> coordinators = new HashSet<User>();

	private Set<Visit> scgCollection = new HashSet<Visit>();

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

	public void setCoordinators(Set<User> coordinatorCollection) {
		this.coordinators = coordinatorCollection;
	}

	public Set<Visit> getScgCollection() {
		return scgCollection;
	}

	public void setScgCollection(Set<Visit> scgCollection) {
		this.scgCollection = scgCollection;
	}

	public Set<StorageContainer> getStorageContainers() {
		return storageContainers;
	}

	public void setStorageContainers(Set<StorageContainer> storageContainerCollection) {
		this.storageContainers = storageContainerCollection;
	}

	public void update(Site other) {
		setName(other.getName());
		setCode(other.getCode());
		setType(other.getType());
		setActivityStatus(other.getActivityStatus());
		CollectionUpdater.update(this.getCoordinators(), other.getCoordinators());
	}

	public void delete(boolean close) {
		String activityStatus = close ? Status.ACTIVITY_STATUS_CLOSED.getStatus()  
				: Status.ACTIVITY_STATUS_DISABLED.getStatus();
		this.setActivityStatus(activityStatus);
	}

}
