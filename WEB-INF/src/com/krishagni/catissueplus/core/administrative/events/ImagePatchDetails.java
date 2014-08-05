
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImagePatchDetails {

	private String description;

	private String eqpImageId;

	private Date lastUpdateDate;

	private Long quality;

	private String resolution;

	private Date scanDate;

	private String stainName;

	private String status;

	//private Blob thumbnail;

	private Long width;

	private Long height;

	private String imageType;

	private Long equipmentId;

	private Long specimenId;

	private String fullLocUrl;

	private String activityStatus;

	private List<String> modifiedAttributes = new ArrayList<String>();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEqpImageId() {
		return eqpImageId;
	}

	public void setEqpImageId(String eqpImageId) {
		this.eqpImageId = eqpImageId;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getQuality() {
		return quality;
	}

	public void setQuality(Long quality) {
		this.quality = quality;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}

	public String getStainName() {
		return stainName;
	}

	public void setStainName(String stainName) {
		this.stainName = stainName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	//	public Blob getThumbnail() {
	//		return thumbnail;
	//	}
	//
	//	public void setThumbnail(Blob thumbnail) {
	//		this.thumbnail = thumbnail;
	//	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}

	public String getFullLocUrl() {
		return fullLocUrl;
	}

	public void setFullLocUrl(String fullLocUrl) {
		this.fullLocUrl = fullLocUrl;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public boolean isDescriptionModified() {
		return modifiedAttributes.contains("description");
	}

	public boolean isEqpImageIdModified() {
		return modifiedAttributes.contains("eqpImageId");
	}

	public boolean isLastUpdateDateModified() {

		return modifiedAttributes.contains("lastUpdateDate");

	}

	public boolean isQualityModified() {
		return modifiedAttributes.contains("quality");
	}

	public boolean isResolutionModified() {
		return modifiedAttributes.contains("resolution");
	}

	public boolean isScanDateModified() {
		return modifiedAttributes.contains("scanDate");
	}

	public boolean isStainNameModified() {
		return modifiedAttributes.contains("stainName");
	}

	public boolean isStatusModified() {
		return modifiedAttributes.contains("status");
	}

	public boolean isThumbnailModified() {
		return modifiedAttributes.contains("thumbnail");
	}

	public boolean isWidthModified() {
		return modifiedAttributes.contains("width");
	}

	public boolean isHeightModified() {
		return modifiedAttributes.contains("height");
	}

	public boolean isImageTypeModified() {
		return modifiedAttributes.contains("imageType");
	}

	public boolean isEquipmentIdModified() {
		return modifiedAttributes.contains("equipmentId");
	}

	public boolean isSpecimenIdModified() {
		return modifiedAttributes.contains("specimenId");
	}

	public boolean isFullLocUrlModified() {
		return modifiedAttributes.contains("fullLocUrl");
	}

	public boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}

}
