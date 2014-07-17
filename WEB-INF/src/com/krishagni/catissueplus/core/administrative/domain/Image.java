
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.util.Status;

public class Image {

	private Long id;

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

	private Equipment equipment;

	private Specimen specimen;

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

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
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

	public void update(Image image) {
		this.setActivityStatus(image.getActivityStatus());
		this.setDescription(image.getDescription());
		this.setEqpImageId(image.getEqpImageId());
		this.setEquipment(image.getEquipment());
		this.setFullLocUrl(image.getFullLocUrl());
		this.setImageType(image.getImageType());
		this.setLastUpdateDate(image.getLastUpdateDate());
		this.setQuality(image.getQuality());
		this.setResolution(image.getResolution());
		this.setScanDate(image.getScanDate());
		this.setSpecimen(image.getSpecimen());
		this.setStainName(image.getStainName());
		this.setStatus(image.getStatus());
		//this.setThumbnail(image.getThumbnail());
		this.setWidth(image.getWidth());
		this.setHeight(image.getHeight());

	}

	public void delete() {
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

}
