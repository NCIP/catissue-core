
package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.administrative.domain.factory.ImageErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ImageFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateImageEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GetImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GotImageEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDetails;
import com.krishagni.catissueplus.core.administrative.events.ImageUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchImageEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateImageEvent;
import com.krishagni.catissueplus.core.administrative.services.ImageService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class ImageServiceImpl implements ImageService {

	private static final String EQP_IMG_ID = "equipment image id";

	private ImageFactory imageFactory;

	private DaoFactory daoFactory;

	public void setImageFactory(ImageFactory imageFactory) {
		this.imageFactory = imageFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public GotImageEvent getImage(GetImageEvent event) {
		Image image;
		if (event.getId() != null) {
			image = daoFactory.getImageDao().getImage(event.getId());
			if (image == null) {
				return GotImageEvent.notFound(event.getId());
			}
		}
		else {
			image = daoFactory.getImageDao().getImage(event.getEqpImageId());
			if (image == null) {
				return GotImageEvent.notFound(event.getEqpImageId());
			}
		}

		ImageDetails details = ImageDetails.fromDomain(image);
		return GotImageEvent.ok(details);
	}

	@Override
	@PlusTransactional
	public ImageCreatedEvent createImage(CreateImageEvent reqEvent) {
		try {
			Image image = imageFactory.create(reqEvent.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueEqpImgId(image.getEqpImageId(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getImageDao().saveOrUpdate(image);
			return ImageCreatedEvent.ok(ImageDetails.fromDomain(image));
		}
		catch (ObjectCreationException ex) {
			return ImageCreatedEvent.invalidRequest(ex.getMessage(), ex.getErroneousFields());
		}
		catch (Exception ex) {
			return ImageCreatedEvent.serverError(ex);
		}
	}

	private void ensureUniqueEqpImgId(String eqpImageId, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getImageDao().isUniqueEquipmentImageId(eqpImageId)) {
			exceptionHandler.addError(ImageErrorCode.DUPLICATE_EQUIPMENT_IMAGE_ID, EQP_IMG_ID);
		}
	}

	@Override
	@PlusTransactional
	public ImageUpdatedEvent updateImage(UpdateImageEvent event) {
		try {
			Long id = event.getId();
			Image oldImage = daoFactory.getImageDao().getImage(id);
			if (oldImage == null) {
				return ImageUpdatedEvent.notFound(id);
			}

			Image image = imageFactory.create(event.getDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkEquipmentImageId(oldImage.getEqpImageId(), image.getEqpImageId(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			oldImage.update(image);
			daoFactory.getImageDao().saveOrUpdate(oldImage);

			return ImageUpdatedEvent.ok(ImageDetails.fromDomain(oldImage));

		}
		catch (ObjectCreationException ex) {
			return ImageUpdatedEvent.invalidRequest(ex.getMessage(), ex.getErroneousFields());
		}
		catch (Exception ex) {
			return ImageUpdatedEvent.serverError(ex);
		}

	}

	@Override
	@PlusTransactional
	public ImageUpdatedEvent patchImage(PatchImageEvent event) {
		try {
			Long id = event.getId();
			Image oldImage = daoFactory.getImageDao().getImage(id);
			if (oldImage == null) {
				return ImageUpdatedEvent.notFound(id);
			}
			String oldEqpImageId = oldImage.getEqpImageId();
			Image image = imageFactory.patch(oldImage, event.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkEquipmentImageId(oldEqpImageId, event.getDetails().getEqpImageId(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getImageDao().saveOrUpdate(image);
			return ImageUpdatedEvent.ok(ImageDetails.fromDomain(image));
		}
		catch (ObjectCreationException exception) {
			return ImageUpdatedEvent.invalidRequest(SiteErrorCode.ERRORS.message(), exception.getErroneousFields());
		}
		catch (Exception e) {
			return ImageUpdatedEvent.serverError(e);
		}
	}

	private void checkEquipmentImageId(String oldEqpImageId, String eqpImageId, ObjectCreationException exceptionHandler) {
		if (!oldEqpImageId.equals(eqpImageId)) {
			ensureUniqueEqpImgId(eqpImageId, exceptionHandler);
		}

	}

	@Override
	@PlusTransactional
	public ImageDeletedEvent deleteImage(DeleteImageEvent reqEvent) {
		try {
			Image image;
			if (reqEvent.getId() != null) {
				image = daoFactory.getImageDao().getImage(reqEvent.getId());
				if (image == null) {
					return ImageDeletedEvent.notFound(reqEvent.getId());
				}
			}
			else {
				image = daoFactory.getImageDao().getImage(reqEvent.getEqpImageId());
				if (image == null) {
					return ImageDeletedEvent.notFound(reqEvent.getEqpImageId());
				}
			}

			image.delete();
			daoFactory.getImageDao().saveOrUpdate(image);
			return ImageDeletedEvent.ok();
		}
		catch (Exception e) {
			return ImageDeletedEvent.serverError(e);
		}
	}

}