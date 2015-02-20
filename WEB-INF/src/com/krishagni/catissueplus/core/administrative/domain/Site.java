
package com.krishagni.catissueplus.core.administrative.domain;

import static com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode.REF_ENTITY_FOUND;

import java.util.HashSet;
import java.util.Set;

import krishagni.catissueplus.util.CommonUtil;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class Site {

	private Long id;

	private String name;

	private String code; // TODO: Need to map in hbm

	private String type;

	private String activityStatus;

	private String address; // TODO: Need to map in hbm
    
	private Set<User> coordinatorCollection = new HashSet<User>();

	private Set<Visit> scgCollection = new HashSet<Visit>();

	private Set<StorageContainer> storageContainerCollection = new HashSet<StorageContainer>();

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

	public Set<User> getCoordinatorCollection() {
		return coordinatorCollection;
	}

	public void setCoordinatorCollection(Set<User> coordinatorCollection) {
		this.coordinatorCollection = coordinatorCollection;
	}

	public Set<Visit> getScgCollection() {
		return scgCollection;
	}

	public void setScgCollection(Set<Visit> scgCollection) {
		this.scgCollection = scgCollection;
	}

	public Set<StorageContainer> getStorageContainerCollection() {
		return storageContainerCollection;
	}

	public void setStorageContainerCollection(Set<StorageContainer> storageContainerCollection) {
		this.storageContainerCollection = storageContainerCollection;
	}

	public void update(Site site) {
		if (site.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			this.setName(CommonUtil.appendTimestamp(site.getName()));
		}
		else {
			this.setName(site.getName());
		}
		
		this.setCode(site.getCode());
		this.setType(site.getType());
		this.setActivityStatus(site.getActivityStatus());
		CollectionUpdater.update(this.getCoordinatorCollection(), site.getCoordinatorCollection());
	}

	public void delete() {
		if (!this.getScgCollection().isEmpty() || !this.getStorageContainerCollection().isEmpty()) {
			throw OpenSpecimenException.userError(REF_ENTITY_FOUND);
		}
		
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
}
