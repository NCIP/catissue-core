
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.administrative.domain.factory.ImageFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.ImageFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.CreateImageEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GetImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GotImageEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchImageEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateImageEvent;
import com.krishagni.catissueplus.core.administrative.repository.EquipmentDao;
import com.krishagni.catissueplus.core.administrative.repository.ImageDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.services.ImageService;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.ImageServiceImpl;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.ImageTestData;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;

public class ImageTest {

	@Mock
	DaoFactory daoFactory;

	@Mock
	SpecimenDao specimenDao;

	@Mock
	EquipmentDao equipmentDao;

	@Mock
	ImageDao imageDao;

	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;

	PermissibleValuesManager pvManager;

	private PermissibleValueService pvService;

	ImageFactory imageFactory;

	ImageService imageService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getPermissibleValueDao()).thenReturn(pvDao);
		pvService = new PermissibleValueServiceImpl();

		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvManager = new PermissibleValuesManagerImpl();
		((PermissibleValuesManagerImpl) pvManager).setPermissibleValueSvc(pvService);
		CommonValidator.setPvManager(pvManager);
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());

		when(daoFactory.getSpecimenDao()).thenReturn(specimenDao);
		when(daoFactory.getEquipmentDao()).thenReturn(equipmentDao);
		when(daoFactory.getImageDao()).thenReturn(imageDao);
		imageService = new ImageServiceImpl();
		imageFactory = new ImageFactoryImpl();
		((ImageServiceImpl) imageService).setDaoFactory(daoFactory);
		((ImageServiceImpl) imageService).setImageFactory(imageFactory);
		((ImageFactoryImpl) imageFactory).setDaoFactory(daoFactory);

		when(specimenDao.getSpecimen(anyLong())).thenReturn(ImageTestData.getSpecimen());
		when(equipmentDao.getEquipment(anyLong())).thenReturn(ImageTestData.getEquipment());
		when(daoFactory.getImageDao().getImage(anyLong())).thenReturn(ImageTestData.getImage());
		when(daoFactory.getImageDao().isUniqueEquipmentImageId(anyString())).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testForSuccessfulImageCreation() {
		CreateImageEvent reqEvent = ImageTestData.getCreateImageEvent();
		ImageCreatedEvent response = imageService.createImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(reqEvent.getDetails().getImageType(), response.getDetails().getImageType());
	}

	@Test
	public void testForSuccessfulImageCreationWithNullScanDateAndUpdationDate() {
		when(daoFactory.getImageDao().getImage(anyLong())).thenReturn(ImageTestData.getImage());
		CreateImageEvent reqEvent = ImageTestData.getCreateImageEventWithNullDates();
		ImageCreatedEvent response = imageService.createImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForServerErrorWhileImageCreation() {
		doThrow(new RuntimeException()).when(imageDao).saveOrUpdate(any(Image.class));
		CreateImageEvent reqEvent = ImageTestData.getCreateImageEvent();
		ImageCreatedEvent response = imageService.createImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForImageCreationWithEmptyEqpImageId() {
		CreateImageEvent reqEvent = ImageTestData.getCreateImageEventWithEmptyEqpImageId();
		ImageCreatedEvent response = imageService.createImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(ImageTestData.EQP_IMG_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForImageCreationWithDuplicateEqpImageId() {

		when(daoFactory.getImageDao().isUniqueEquipmentImageId(anyString())).thenReturn(Boolean.FALSE);
		CreateImageEvent reqEvent = ImageTestData.getCreateImageEvent();
		ImageCreatedEvent response = imageService.createImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(ImageTestData.EQP_IMG_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForImageCreationWithEmptyURL() {
		CreateImageEvent reqEvent = ImageTestData.getCreateImageEventWithEmptyURL();
		ImageCreatedEvent response = imageService.createImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(ImageTestData.URL, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForSuccessfulImageUpdation() {
		UpdateImageEvent reqEvent = ImageTestData.getUpdateImageEvent();
		ImageUpdatedEvent response = imageService.updateImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForImageUpdationWithNullDomainObject() {
		when(daoFactory.getImageDao().getImage(anyLong())).thenReturn(null);
		UpdateImageEvent reqEvent = ImageTestData.getUpdateImageEvent();
		ImageUpdatedEvent response = imageService.updateImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testForSuccessfulImageUpdationWithNullScanDateAndUpdationDate() {
		when(daoFactory.getImageDao().getImage(anyLong())).thenReturn(ImageTestData.getImageWithNullDates());
		UpdateImageEvent reqEvent = ImageTestData.getUpdateImageEvent();
		ImageUpdatedEvent response = imageService.updateImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForImageUpdationWithDuplicateEqpImageId() {
		when(daoFactory.getImageDao().isUniqueEquipmentImageId(anyString())).thenReturn(Boolean.FALSE);
		UpdateImageEvent reqEvent = ImageTestData.getUpdateImageEvent();
		ImageUpdatedEvent response = imageService.updateImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(ImageTestData.EQP_IMG_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForImageUpdationWithEmptyURL() {
		UpdateImageEvent reqEvent = ImageTestData.getUpdateImageEventWithEmptyURL();
		ImageUpdatedEvent response = imageService.updateImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(ImageTestData.URL, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForServerErrorWhileImageUpdation() {
		doThrow(new RuntimeException()).when(imageDao).saveOrUpdate(any(Image.class));
		UpdateImageEvent reqEvent = ImageTestData.getUpdateImageEvent();
		ImageUpdatedEvent response = imageService.updateImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSuccessfullPatchImage() {

		PatchImageEvent reqEvent = ImageTestData.getPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSuccessfullPatchImageWithEmptyData() {

		PatchImageEvent reqEvent = ImageTestData.getEmptyPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForImagePatchWithDuplicateEqpImageId() {
		when(daoFactory.getImageDao().isUniqueEquipmentImageId(anyString())).thenReturn(Boolean.FALSE);
		PatchImageEvent reqEvent = ImageTestData.getPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(ImageTestData.EQP_IMG_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForSuccessfulImagePatchWithNullScanDateAndUpdationDate() {
		when(daoFactory.getImageDao().getImage(anyLong())).thenReturn(ImageTestData.getImageWithNullDates());
		PatchImageEvent reqEvent = ImageTestData.getPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForImagePatchWithEmptyURL() {
		UpdateImageEvent reqEvent = ImageTestData.getUpdateImageEventWithEmptyURL();
		ImageUpdatedEvent response = imageService.updateImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(ImageTestData.URL, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForImagePatchWithNullDomainObject() {
		when(daoFactory.getImageDao().getImage(anyLong())).thenReturn(null);
		PatchImageEvent reqEvent = ImageTestData.getPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testForImagePatchWithNullEquipmentObject() {
		when(equipmentDao.getEquipment(anyLong())).thenReturn(null);
		PatchImageEvent reqEvent = ImageTestData.getPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(response.getErroneousFields().length, 1);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testForImagePatchWithNullSpecimenObject() {
		when(specimenDao.getSpecimen(anyLong())).thenReturn(null);

		PatchImageEvent reqEvent = ImageTestData.getPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(response.getErroneousFields().length, 1);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testForServerErrorWhileImagePatch() {
		doThrow(new RuntimeException()).when(imageDao).saveOrUpdate(any(Image.class));
		PatchImageEvent reqEvent = ImageTestData.getPatchData();
		ImageUpdatedEvent response = imageService.patchImage(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulImageDeletion() {
		DeleteImageEvent reqEvent = ImageTestData.getDeleteImageEvent();
		ImageDeletedEvent response = imageService.deleteImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForImageDeleteWithNullDomainObject() {
		when(daoFactory.getImageDao().getImage(anyLong())).thenReturn(null);
		DeleteImageEvent reqEvent = ImageTestData.getDeleteImageEvent();
		ImageDeletedEvent response = imageService.deleteImage(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testForSuccessfulImageDeleteWithDisplayName() {
		when(imageDao.getImage(anyString())).thenReturn(ImageTestData.getImage());
		DeleteImageEvent reqEvent = ImageTestData.getDeleteImageEventForName();
		ImageDeletedEvent response = imageService.deleteImage(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForInvalidImageDeleteWithImgEqpId() {
		when(imageDao.getImage(anyString())).thenReturn(null);
		DeleteImageEvent reqEvent = ImageTestData.getDeleteImageEventForName();
		ImageDeletedEvent response = imageService.deleteImage(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertEquals(response.getEqpImageId(), reqEvent.getEqpImageId());
	}

	@Test
	public void testForServerErrorWhileImageDelete() {
		doThrow(new RuntimeException()).when(imageDao).saveOrUpdate(any(Image.class));
		DeleteImageEvent reqEvent = ImageTestData.getDeleteImageEvent();
		ImageDeletedEvent response = imageService.deleteImage(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testGetImageById() {
		when(imageDao.getImage(anyLong())).thenReturn(ImageTestData.getImage());
		GetImageEvent reqEvent = ImageTestData.getImageEvent();
		GotImageEvent response = imageService.getImage(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testGetImageWithWrongId() {
		when(imageDao.getImage(anyLong())).thenReturn(null);
		GetImageEvent reqEvent = ImageTestData.getImageEvent();
		GotImageEvent response = imageService.getImage(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testGetImageByName() {
		when(imageDao.getImage(anyString())).thenReturn(ImageTestData.getImage());
		GetImageEvent reqEvent = ImageTestData.getImageEventForName();
		GotImageEvent response = imageService.getImage(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getDetails());
	}

	@Test
	public void testGetImageWithWrongName() {
		when(imageDao.getImage(anyString())).thenReturn(null);
		GetImageEvent reqEvent = ImageTestData.getImageEventForName();
		GotImageEvent response = imageService.getImage(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getEqpImageId());
	}
}
