
package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class StorageContainerDetails {

	private Long id;

	private String name;

	private String barcode;

	private String activityStatus;

	private Double tempratureInCentigrade;

	private String siteName;

	private String parentContainerName;

	private UserInfo createdBy;

	private Set<String> holdsCPTitles = new HashSet<String>();

	private Set<String> holdsSpecimenTypes = new HashSet<String>();

	private String comments;

	private Integer oneDimensionCapacity;

	private Integer twoDimensionCapacity;

	private String oneDimentionLabelingScheme;

	private String twoDimentionLabelingScheme;

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

	public Set<String> getHoldsCPTitles() {
		return holdsCPTitles;
	}

	public void setHoldsCPTitles(Set<String> holdsCPTitles) {
		this.holdsCPTitles = holdsCPTitles;
	}

	public Set<String> getHoldsSpecimenTypes() {
		return holdsSpecimenTypes;
	}

	public void setHoldsSpecimenTypes(Set<String> holdsSpecimenTypes) {
		this.holdsSpecimenTypes = holdsSpecimenTypes;
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

	public UserInfo getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserInfo createdBy) {
		this.createdBy = createdBy;
	}

	public String getOneDimentionLabelingScheme() {
		return oneDimentionLabelingScheme;
	}

	public void setOneDimentionLabelingScheme(String oneDimentionLabelingScheme) {
		this.oneDimentionLabelingScheme = oneDimentionLabelingScheme;
	}

	public String getTwoDimentionLabelingScheme() {
		return twoDimentionLabelingScheme;
	}

	public void setTwoDimentionLabelingScheme(String twoDimentionLabelingScheme) {
		this.twoDimentionLabelingScheme = twoDimentionLabelingScheme;
	}

	public static StorageContainerDetails fromDomain(StorageContainer storageContainer) {
		StorageContainerDetails details = new StorageContainerDetails();
		details.setId(storageContainer.getId());
		details.setActivityStatus(storageContainer.getActivityStatus());
		details.setBarcode(storageContainer.getBarcode());
		details.setHoldsCPTitles(getCpTitileCollection(storageContainer.getHoldsCPs()));
		details.setHoldsSpecimenTypes(getHoldsSpecimenTypes(storageContainer.getHoldsSpecimenTypes()));
		details.setComments(storageContainer.getComments());
		details.setCreatedBy(getUserInfo(storageContainer.getCreatedBy()));
		details.setName(storageContainer.getName());
		details.setTempratureInCentigrade(storageContainer.getTempratureInCentigrade());
		details.setTwoDimensionCapacity(storageContainer.getTwoDimensionCapacity());
		details.setOneDimensionCapacity(storageContainer.getOneDimensionCapacity());
		details.setOneDimentionLabelingScheme(storageContainer.getOneDimentionLabelingScheme());
		details.setTwoDimentionLabelingScheme(storageContainer.getTwoDimentionLabelingScheme());
		if (storageContainer.getParentContainer() != null) {
			details.setParentContainerName(storageContainer.getParentContainer().getName());
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

	private static Set<String> getHoldsSpecimenTypes(Set<String> holdsSpecimenTypes) {
		Set<String> specTypes = new HashSet<String>();
		for (String specType : holdsSpecimenTypes) {
			specTypes.add(specType);
		}
		return specTypes;
	}

	private static Set<String> getCpTitileCollection(Collection<CollectionProtocol> collectionProtocols) {
		Set<String> cpTitles = new HashSet<String>();
		for (CollectionProtocol cp : collectionProtocols) {
			cpTitles.add(cp.getTitle());
		}
		return cpTitles;
	}

}
