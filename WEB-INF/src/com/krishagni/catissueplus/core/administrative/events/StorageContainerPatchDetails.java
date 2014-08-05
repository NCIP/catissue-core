
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;

public class StorageContainerPatchDetails {

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

	private List<String> modifiedAttributes = new ArrayList<String>();

	public boolean isNameModified() {
		return modifiedAttributes.contains("name");
	}

	public boolean isBarcodeModified() {
		return modifiedAttributes.contains("barcode");
	}

	public boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}

	public boolean isTempratureModified() {
		return modifiedAttributes.contains("tempratureInCentigrade");
	}

	public boolean isSiteNameModified() {
		return modifiedAttributes.contains("siteName");
	}

	public boolean isParentContainerNameModified() {
		return modifiedAttributes.contains("parentContainerName");
	}

	public boolean isCreatedByModified() {
		return modifiedAttributes.contains("createdBy");
	}

	public boolean isHoldsCPTitlesModified() {
		return modifiedAttributes.contains("holdsCPTitles");
	}

	public boolean isHoldsSpecimenTypesModified() {
		return modifiedAttributes.contains("holdsSpecimenTypes");
	}

	public boolean isCommentsModified() {
		return modifiedAttributes.contains("comments");
	}

	public boolean isOneDimensionCapacityModified() {
		return modifiedAttributes.contains("oneDimensionCapacity");
	}

	public boolean isTwoDimensionCapacityModified() {
		return modifiedAttributes.contains("twoDimensionCapacity");
	}

	public boolean isTwoDimentionLabelingSchemeModified() {
		return modifiedAttributes.contains("twoDimentionLabelingScheme");
	}

	public boolean isOneDimentionLabelingSchemeModified() {
		return modifiedAttributes.contains("oneDimentionLabelingScheme");
	}

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

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
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

}
