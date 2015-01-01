
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import krishagni.catissueplus.util.CommonUtil;

import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.util.Status;

public class Site {

	private Long id;

	private String name;

	private String code; // TODO: Need to map in hbm

	private String type;

	private String activityStatus;

	private String address; // TODO: Need to map in hbm
    
	private Set<User> coordinatorCollection = new HashSet<User>();

	private Set<SpecimenCollectionGroup> scgCollection = new HashSet<SpecimenCollectionGroup>();

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

	public Set<SpecimenCollectionGroup> getScgCollection() {
		return scgCollection;
	}

	public void setScgCollection(Set<SpecimenCollectionGroup> scgCollection) {
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
		this.setType(site.getType());
		this.setActivityStatus(site.getActivityStatus());
		SetUpdater.<User> newInstance().update(this.getCoordinatorCollection(), site.getCoordinatorCollection());
	}

	public void delete() {

		if (!this.getScgCollection().isEmpty()) {
			throw new CatissueException(SiteErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		if (!this.getStorageContainerCollection().isEmpty()) {
			throw new CatissueException(SiteErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
}
