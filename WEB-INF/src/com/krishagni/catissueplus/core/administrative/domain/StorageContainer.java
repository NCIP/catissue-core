
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.util.Status;

public class StorageContainer {

	private Long id;

	private String name;

	private String barcode;

	private String activityStatus;

	private Double tempratureInCentigrade;

	private Site site;

	private StorageContainer parentContainer;

	private User createdBy;

	private Set<CollectionProtocol> holdsCPs = new HashSet<CollectionProtocol>();

	private Set<String> holdsSpecimenTypes = new HashSet<String>();

	private String comments;

	private Integer oneDimensionCapacity;

	private Integer twoDimensionCapacity;

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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Double getTempratureInCentigrade() {
		return tempratureInCentigrade;
	}

	public void setTempratureInCentigrade(Double tempratureInCentigrade) {
		this.tempratureInCentigrade = tempratureInCentigrade;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public StorageContainer getParentContainer() {
		return parentContainer;
	}

	public void setParentContainer(StorageContainer parentContainer) {
		this.parentContainer = parentContainer;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Set<CollectionProtocol> getHoldsCPs() {
		return holdsCPs;
	}

	public void setHoldsCPs(Set<CollectionProtocol> holdsCPs) {
		this.holdsCPs = holdsCPs;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getOneDimensionCapacity() {
		return oneDimensionCapacity;
	}

	public void setOneDimensionCapacity(Integer oneDimensionCapacity) {
		this.oneDimensionCapacity = oneDimensionCapacity;
	}

	public Integer getTwoDimensionCapacity() {
		return twoDimensionCapacity;
	}

	public void setTwoDimensionCapacity(Integer twoDimensionCapacity) {
		this.twoDimensionCapacity = twoDimensionCapacity;
	}

	public Set<String> getHoldsSpecimenTypes() {
		return holdsSpecimenTypes;
	}

	public void setHoldsSpecimenTypes(Set<String> holdsSpecimenTypes) {
		this.holdsSpecimenTypes = holdsSpecimenTypes;
	}

	public void update(StorageContainer storageContainer) {
		this.setActivityStatus(storageContainer.getActivityStatus());
		this.setBarcode(storageContainer.getBarcode());

		this.setComments(storageContainer.getComments());
		this.setCreatedBy(storageContainer.getCreatedBy());
		this.setName(storageContainer.getName());
		this.setTempratureInCentigrade(storageContainer.getTempratureInCentigrade());
		this.setTwoDimensionCapacity(storageContainer.getTwoDimensionCapacity());
		this.setOneDimensionCapacity(storageContainer.getOneDimensionCapacity());
		this.setParentContainer(storageContainer.getParentContainer());
		this.setSite(storageContainer.getSite());

		SetUpdater.<CollectionProtocol> newInstance().update(this.getHoldsCPs(),
				storageContainer.getHoldsCPs());

		SetUpdater.<String> newInstance().update(this.getHoldsSpecimenTypes(), storageContainer.getHoldsSpecimenTypes());
	}

	public void disable() {
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

	public void patch(StorageContainer storageContainer) {
		if (!this.getActivityStatus().equals(storageContainer.getActivityStatus())) {
			this.setActivityStatus(storageContainer.getActivityStatus());
		}

	}

}
