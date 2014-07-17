
package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.Image;

public class ImageDetails {

	private Long id;

	private String description;

	private String eqpImageId;

	private Date lastUpdateDate;

	private Long quality;

	private String resolution;

	private Date scanDate;

	private String stainName;

	private String status;

	private byte[] thumbnail;

	private Long width;

	private Long height;

	private String imageType;

	private Long equipmentId;

	private Long specimenId;

	private String fullLocUrl;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	//	public Byte[] getThumbnail() {
	//		return thumbnail;
	//	}
	//
	//	public void setThumbnail(Byte[] thumbnail) {
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

	public static ImageDetails fromDomain(Image image) {
		ImageDetails imageDto = new ImageDetails();

		imageDto.setActivityStatus(image.getActivityStatus());
		imageDto.setDescription(image.getDescription());
		imageDto.setEqpImageId(image.getEqpImageId());
		imageDto.setFullLocUrl(image.getFullLocUrl());
		imageDto.setId(image.getId());
		imageDto.setImageType(image.getImageType());
		imageDto.setLastUpdateDate(image.getLastUpdateDate());
		imageDto.setQuality(image.getQuality());
		imageDto.setResolution(image.getResolution());
		imageDto.setScanDate(image.getScanDate());
		imageDto.setSpecimenId(image.getSpecimen().getId());
		imageDto.setStainName(image.getStainName());
		imageDto.setStatus(image.getStatus());
		//	imageDto.setThumbnail(image.getThumbnail());
		imageDto.setWidth(image.getWidth());
		imageDto.setHeight(image.getHeight());
		imageDto.setEquipmentId(image.getEquipment().getId());
		return imageDto;
	}
}
