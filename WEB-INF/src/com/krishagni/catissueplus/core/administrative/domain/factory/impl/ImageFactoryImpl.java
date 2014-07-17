
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.administrative.domain.factory.ImageErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ImageFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ImageDetails;
import com.krishagni.catissueplus.core.administrative.events.ImagePatchDetails;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class ImageFactoryImpl implements ImageFactory {

	private static final String SPECIMEN_ID = "specimen id";

	private static final String URL = "full loc url";

	private static final String EQUIPMENT_ID = "equipment id";

	private static final String EQP_IMG_ID = "equipment image id";

	private static final String ACTIVITY_STATUS = "activity status";

	private static final String STAIN_NAME = "stain name";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Image create(ImageDetails details) {

		Image image = new Image();
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		setActivityStatus(image, details.getActivityStatus(), exceptionHandler);
		setDescription(image, details.getDescription());
		setEqpImageId(image, details.getEqpImageId(), exceptionHandler);
		setEquipmentId(image, details.getEquipmentId(), exceptionHandler);
		setFullLocUrl(image, details.getFullLocUrl(), exceptionHandler);
		setImageType(image, details.getImageType());
		setLastUpdateDate(image, details.getLastUpdateDate());
		setQuality(image, details.getQuality());
		setResolution(image, details.getResolution());
		setScanDate(image, details.getScanDate());
		setSpecimenId(image, details.getSpecimenId(), exceptionHandler);
		setStainName(image, details.getStainName(), exceptionHandler);
		setStatus(image, details.getStatus());
		//		setThumbnail(image, details.getThumbnail());
		setWidth(image, details.getWidth());
		setHeight(image, details.getHeight());
		exceptionHandler.checkErrorAndThrow();
		return image;
	}

	@Override
	public Image patch(Image image, ImagePatchDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		if (details.isActivityStatusModified()) {
			setActivityStatus(image, details.getActivityStatus(), exceptionHandler);
		}

		if (details.isDescriptionModified()) {
			setDescription(image, details.getDescription());
		}

		if (details.isEqpImageIdModified()) {

			setEqpImageId(image, details.getEqpImageId(), exceptionHandler);
		}

		if (details.isFullLocUrlModified()) {
			setFullLocUrl(image, details.getFullLocUrl(), exceptionHandler);
		}

		if (details.isImageTypeModified()) {
			setImageType(image, details.getImageType());
		}

		if (details.isLastUpdateDateModified()) {
			setLastUpdateDate(image, details.getLastUpdateDate());
		}

		if (details.isQualityModified()) {
			setQuality(image, details.getQuality());
		}

		if (details.isResolutionModified()) {
			setResolution(image, details.getResolution());
		}

		if (details.isScanDateModified()) {
			setScanDate(image, details.getScanDate());
		}
		if (details.isSpecimenIdModified()) {
			setSpecimenId(image, details.getSpecimenId(), exceptionHandler);
		}
		if (details.isStainNameModified()) {
			setStainName(image, details.getStainName(), exceptionHandler);
		}
		if (details.isStatusModified()) {
			setStatus(image, details.getStatus());
		}
		//		if (details.isThumbnailModified()) {
		//			setThumbnail(image, details.getThumbnail());
		//		}
		if (details.isWidthModified()) {
			setWidth(image, details.getWidth());
		}

		if (details.isHeightModified()) {
			setHeight(image, details.getHeight());
		}

		if (details.isEquipmentIdModified()) {
			setEquipmentId(image, details.getEquipmentId(), exceptionHandler);
		}
		exceptionHandler.checkErrorAndThrow();
		return image;
	}

	private void setWidth(Image image, Long width) {
		image.setWidth(width);

	}

	private void setHeight(Image image, Long height) {
		image.setHeight(height);

	}

	//	private void setThumbnail(Image image, Blob thumbnail) {
	//		image.setThumbnail(thumbnail);
	//	}

	private void setStatus(Image image, String status) {
		image.setStatus(status);
	}

	private void setStainName(Image image, String stainName, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(stainName, STAIN_NAME)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, STAIN_NAME);
		}
		image.setStainName(stainName);

	}

	private void setSpecimenId(Image image, Long specimenId, ObjectCreationException exceptionHandler) {
		Specimen specimen = daoFactory.getSpecimenDao().getSpecimen(specimenId);
		if (specimen == null) {
			exceptionHandler.addError(ImageErrorCode.INVALID_ATTR_VALUE, SPECIMEN_ID);
			return;
		}
		image.setSpecimen(specimen);
	}

	private void setScanDate(Image image, Date scanDate) {
		if (scanDate == null) {
			image.setScanDate(new Date());
		}
		else {
			image.setScanDate(scanDate);
		}
	}

	private void setResolution(Image image, String resolution) {
		image.setResolution(resolution);
	}

	private void setQuality(Image image, Long quality) {
		image.setQuality(quality);
	}

	private void setLastUpdateDate(Image image, Date lastUpdateDate) {
		if (lastUpdateDate == null) {
			image.setLastUpdateDate(new Date());
		}
		else {
			image.setLastUpdateDate(lastUpdateDate);
		}
	}

	private void setImageType(Image image, String imageType) {
		image.setImageType(imageType);
	}

	private void setFullLocUrl(Image image, String fullLocUrl, ObjectCreationException exceptionHandler) {
		if (isBlank(fullLocUrl)) {
			exceptionHandler.addError(ImageErrorCode.INVALID_ATTR_VALUE, URL);
			return;
		}
		image.setFullLocUrl(fullLocUrl);
	}

	private void setEquipmentId(Image image, Long equipmentId, ObjectCreationException exceptionHandler) {
		Equipment equipment = daoFactory.getEquipmentDao().getEquipment(equipmentId);
		if (equipment == null) {
			exceptionHandler.addError(ImageErrorCode.INVALID_ATTR_VALUE, EQUIPMENT_ID);
			return;
		}
		image.setEquipment(equipment);

	}

	private void setEqpImageId(Image image, String eqpImageId, ObjectCreationException exceptionHandler) {
		if (isBlank(eqpImageId)) {
			exceptionHandler.addError(ImageErrorCode.MISSING_ATTR_VALUE, EQP_IMG_ID);
			return;
		}
		image.setEqpImageId(eqpImageId);

	}

	private void setDescription(Image image, String description) {
		image.setDescription(description);
	}

	private void setActivityStatus(Image image, String activityStatus, ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(SiteErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
		}
		image.setActivityStatus(activityStatus);

	}

}
