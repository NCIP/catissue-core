
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;

public class EquipmentDetails {

	private String deviceName;

	private String deviceSerialNumber;

	private String manufacturerName;

	private String siteName;

	private Long id;

	private String displayName;

	private String softwareVersion;

	private String equipmentId;

	private String activityStatus;

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

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
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

	public static EquipmentDetails fromDomain(Equipment equipment) {
		EquipmentDetails equipmentDto = new EquipmentDetails();
		equipmentDto.setDeviceName(equipment.getDeviceName());
		equipmentDto.setDeviceSerialNumber(equipment.getDeviceSerialNumber());
		equipmentDto.setDisplayName(equipment.getDisplayName());
		equipmentDto.setEquipmentId(equipment.getEquipmentId());
		equipmentDto.setId(equipment.getId());
		equipmentDto.setManufacturerName(equipment.getManufacturerName());
		equipmentDto.setSiteName(equipment.getSite().getName());
		equipmentDto.setSoftwareVersion(equipment.getSoftwareVersion());
		equipmentDto.setActivityStatus(equipment.getActivityStatus());
		return equipmentDto;
	}

}
