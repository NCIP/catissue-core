package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class StorageContainerSummary {

	private Long id;

	private String name;

	private String barcode;

	private String activityStatus;

	private String siteName;

	private String parentContainerName;

	private UserInfo createdBy;

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

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getParentContainerName() {
		return parentContainerName;
	}

	public void setParentContainerName(String parentContainerName) {
		this.parentContainerName = parentContainerName;
	}

	public UserInfo getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserInfo createdBy) {
		this.createdBy = createdBy;
	}

	public static StorageContainerSummary fromDomain(
			StorageContainer storageContainer) {
		StorageContainerSummary details = new StorageContainerSummary();
		details.setId(storageContainer.getId());
		details.setActivityStatus(storageContainer.getActivityStatus());
		details.setBarcode(storageContainer.getBarcode());
		details.setCreatedBy(getUserInfo(storageContainer.getCreatedBy()));
		details.setName(storageContainer.getName());
		if (storageContainer.getParentContainer() != null) {
			details.setParentContainerName(storageContainer
					.getParentContainer().getName());
		}
		details.setSiteName(storageContainer.getSite().getName());
		return details;
	}

	private static UserInfo getUserInfo(User user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginName(user.getLoginName());
		if (user.getAuthDomain() != null) {
			userInfo.setDomainName(user.getAuthDomain().getName());
		}
		return userInfo;
	}
}
