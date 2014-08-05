
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

public class EquipmentPatchDetails {

	private String deviceName;

	private String deviceSerialNumber;

	private String manufacturerName;

	private String siteName;

	private String displayName;

	private String softwareVersion;

	private String equipmentId;

	private String activityStatus;

	private List<String> modifiedAttributes = new ArrayList<String>();

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

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
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

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public boolean isDisplayNameModified() {
		return modifiedAttributes.contains("displayName");
	}

	public boolean isSoftwareVersionModified() {
		return modifiedAttributes.contains("softwareVersion");
	}

	public boolean isDeviceSerialNumberModified() {
		return modifiedAttributes.contains("deviceSerialNumber");
	}

	public boolean isManufacturerNameModified() {
		return modifiedAttributes.contains("manufacturerName");
	}

	public boolean isDeviceNameModified() {
		return modifiedAttributes.contains("deviceName");
	}

	public boolean isSiteNameModified() {
		return modifiedAttributes.contains("siteName");
	}

	public boolean isEquipmentIdModified() {
		return modifiedAttributes.contains("equipmentId");
	}

	public boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}
}
