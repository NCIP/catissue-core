
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.EquipmentErrorCode;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.util.Status;

public class Equipment {

	private String deviceName;

	private String deviceSerialNumber;

	private String manufacturerName;

	private Site site;

	private Long id;

	private String displayName;

	private String softwareVersion;

	private String equipmentId;

	private String activityStatus;

	private Set<Image> images = new HashSet<Image>();

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}

	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public void update(Equipment equipment) {
		this.deviceName = equipment.getDeviceName();
		this.manufacturerName = equipment.getManufacturerName();
		this.deviceSerialNumber = equipment.getDeviceSerialNumber();
		this.displayName = equipment.getDisplayName();
		this.equipmentId = equipment.getEquipmentId();
		this.softwareVersion = equipment.getSoftwareVersion();
		this.site = equipment.getSite();
		this.activityStatus = equipment.getActivityStatus();
	}

	public void delete() {
		if (!this.getImages().isEmpty()) {
			throw new CatissueException(EquipmentErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

}
