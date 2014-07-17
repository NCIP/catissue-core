
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.factory.EquipmentErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.EquipmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDetails;
import com.krishagni.catissueplus.core.administrative.events.EquipmentPatchDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class EquipmentFactoryImpl implements EquipmentFactory {

	private static final String DISPLAY_NAME = "display name";

	private static final String DEVICE_NAME = "device name";

	private static final String SITE_NAME = "site name";

	private static final String EQUIPMENT_ID = "equipment id";

	private static final String ACTIVITY_STATUS = "activity status";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Equipment create(EquipmentDetails details) {
		Equipment equipment = new Equipment();
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		setDeviceName(equipment, details.getDeviceName(), exceptionHandler);
		setDeviceSerialNumber(equipment, details.getDeviceSerialNumber());
		setDisplayName(equipment, details.getDisplayName(), exceptionHandler);
		setEquipmentId(equipment, details.getEquipmentId(), exceptionHandler);
		setManufacturerName(equipment, details.getManufacturerName());
		setSiteName(equipment, details.getSiteName(), exceptionHandler);
		setSoftwareVersion(equipment, details.getSoftwareVersion());
		setActivityStatus(equipment, details.getActivityStatus(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();

		return equipment;
	}

	private void setActivityStatus(Equipment equipment, String activityStatus, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
		}
		equipment.setActivityStatus(activityStatus);
	}

	private void setSoftwareVersion(Equipment equipment, String softwareVersion) {
		equipment.setSoftwareVersion(softwareVersion);

	}

	private void setSiteName(Equipment equipment, String siteName, ObjectCreationException exceptionHandler) {
		if (isBlank(siteName)) {
			exceptionHandler.addError(EquipmentErrorCode.MISSING_ATTR_VALUE, SITE_NAME);
			return;
		}

		Site site = daoFactory.getSiteDao().getSite(siteName);
		if (!(site == null)) {
			equipment.setSite(site);
		}
		else {
			exceptionHandler.addError(EquipmentErrorCode.INVALID_ATTR_VALUE, SITE_NAME);
		}
	}

	private void setManufacturerName(Equipment equipment, String manufacturerName) {
		equipment.setManufacturerName(manufacturerName);
	}

	private void setEquipmentId(Equipment equipment, String equipmentId, ObjectCreationException exceptionHandler) {
		if (isBlank(equipmentId)) {
			exceptionHandler.addError(EquipmentErrorCode.MISSING_ATTR_VALUE, EQUIPMENT_ID);
			return;
		}
		equipment.setEquipmentId(equipmentId);
	}

	private void setDisplayName(Equipment equipment, String displayName, ObjectCreationException exceptionHandler) {
		if (isBlank(displayName)) {
			exceptionHandler.addError(EquipmentErrorCode.MISSING_ATTR_VALUE, DISPLAY_NAME);
			return;
		}
		equipment.setDisplayName(displayName);

	}

	private void setDeviceSerialNumber(Equipment equipment, String deviceSerialNumber) {
		equipment.setDeviceSerialNumber(deviceSerialNumber);

	}

	private void setDeviceName(Equipment equipment, String deviceName, ObjectCreationException exceptionHandler) {
		if (isBlank(deviceName)) {
			exceptionHandler.addError(EquipmentErrorCode.MISSING_ATTR_VALUE, DEVICE_NAME);
			return;
		}
		equipment.setDeviceName(deviceName);

	}

	@Override
	public Equipment patch(Equipment equipment, EquipmentPatchDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		if (details.isDeviceNameModified()) {
			setDeviceName(equipment, details.getDeviceName(), exceptionHandler);
		}

		if (details.isDeviceSerialNumberModified()) {
			setDeviceSerialNumber(equipment, details.getDeviceSerialNumber());
		}

		if (details.isDisplayNameModified()) {
			setDisplayName(equipment, details.getDisplayName(), exceptionHandler);
		}

		if (details.isEquipmentIdModified()) {
			setEquipmentId(equipment, details.getEquipmentId(), exceptionHandler);
		}

		if (details.isManufacturerNameModified()) {
			setManufacturerName(equipment, details.getManufacturerName());
		}

		if (details.isSiteNameModified()) {
			setSiteName(equipment, details.getSiteName(), exceptionHandler);
		}

		if (details.isSoftwareVersionModified()) {
			setSoftwareVersion(equipment, details.getSoftwareVersion());
		}

		if (details.isActivityStatusModified()) {
			setActivityStatus(equipment, details.getActivityStatus(), exceptionHandler);
		}

		exceptionHandler.checkErrorAndThrow();
		return equipment;
	}

}
