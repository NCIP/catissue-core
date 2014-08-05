
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.domain.factory.EquipmentErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.EquipmentFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.impl.EquipmentFactoryImpl;
import com.krishagni.catissueplus.core.administrative.events.AllEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.GetEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.GotEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.repository.EquipmentDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.services.EquipmentService;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.EquipmentServiceImpl;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.EquipmentTestData;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;

public class EquipmentTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	EquipmentDao equipmentDao;

	@Mock
	SiteDao siteDao;

	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;

	PermissibleValuesManager pvManager;

	private PermissibleValueService pvService;

	private EquipmentFactory equipmentFactory;

	private EquipmentService equipmentService;

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

		when(daoFactory.getEquipmentDao()).thenReturn(equipmentDao);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);

		equipmentService = new EquipmentServiceImpl();
		equipmentFactory = new EquipmentFactoryImpl();

		((EquipmentServiceImpl) equipmentService).setDaoFactory(daoFactory);
		((EquipmentServiceImpl) equipmentService).setEquipmentFactory(equipmentFactory);
		((EquipmentFactoryImpl) equipmentFactory).setDaoFactory(daoFactory);

		when(daoFactory.getEquipmentDao().getEquipment(anyLong())).thenReturn(EquipmentTestData.getEquipment());
		when(daoFactory.getEquipmentDao().getEquipment(anyString())).thenReturn(EquipmentTestData.getEquipment());
		when(daoFactory.getSiteDao().getSite(anyString())).thenReturn(EquipmentTestData.getSite());
		when(daoFactory.getEquipmentDao().isUniqueDeviceName(anyString())).thenReturn(true);
		when(daoFactory.getEquipmentDao().isUniqueDisplayName(anyString())).thenReturn(true);
		when(daoFactory.getEquipmentDao().isUniqueEquipmentId(anyString())).thenReturn(true);
	}

	@Test
	public void testForSuccessfulEquipmentCreation() {
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEvent();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(reqEvent.getDetails().getDisplayName(), response.getDetails().getDisplayName());
	}

	@Test
	public void testForEquipmentCreationWithEmptyDisplayName() {
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEventWithEmptyDisplayName();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DISPLAY_NAME, response.getErroneousFields()[0].getFieldName());

	}

	@Test
	public void testForEquipmentCreationWithEmptyDeviceName() {
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEventWithEmptyDeviceName();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DEVICE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentCreationWithEmptyEquipmentId() {
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEventWithEmptyEquipmentId();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.EQUIPMENT_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentCreationWithEmptySiteName() {
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEventWithEmptySiteName();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentCreateWithDuplicateDisplayName() {

		when(daoFactory.getEquipmentDao().isUniqueDisplayName(anyString())).thenReturn(Boolean.FALSE);
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEvent();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DISPLAY_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentCreationWithInvalidSiteName() {
		when(daoFactory.getSiteDao().getSite(anyString())).thenReturn(null);
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEvent();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentCreationWithServerError() {
		doThrow(new RuntimeException()).when(equipmentDao).saveOrUpdate(any(Equipment.class));
		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEquipmentEvent();
		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulEquipmentUpdation() {
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getDetails());
	}

	@Test
	public void testForEquipmentUpdationWithNullOldObject() {
		when(daoFactory.getEquipmentDao().getEquipment(anyLong())).thenReturn(null);
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());

	}

	@Test
	public void testForEquipmentUpdateWithDuplicateDisplayName() {

		when(daoFactory.getEquipmentDao().isUniqueDisplayName(anyString())).thenReturn(Boolean.FALSE);
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DISPLAY_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentUpdateWithDuplicateEquipmentId() {

		when(daoFactory.getEquipmentDao().isUniqueEquipmentId(anyString())).thenReturn(Boolean.FALSE);
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.EQUIPMENT_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentUpdateWithDuplicateDeviceName() {

		when(daoFactory.getEquipmentDao().isUniqueDeviceName(anyString())).thenReturn(Boolean.FALSE);
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DEVICE_NAME, response.getErroneousFields()[0].getFieldName());

	}

	@Test
	public void testForEquipmentPatchWithEmptyDisplayName() {
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEventWithEmptyDisplayName();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DISPLAY_NAME, response.getErroneousFields()[0].getFieldName());

	}

	@Test
	public void testForEquipmentUpdationWithEmptyDeviceName() {
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEventWithEmptyDeviceName();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DEVICE_NAME, response.getErroneousFields()[0].getFieldName());

	}

	@Test
	public void testForEquipmentUpdationWithEmptyEquipmentId() {
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEventWithEmptyEquipmentId();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.EQUIPMENT_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentUpdationWithEmptySiteName() {
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEventWithEmptySiteName();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentUpdationWithInvalidSiteName() {
		when(daoFactory.getSiteDao().getSite(anyString())).thenReturn(null);
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentUpdationWithServerError() {

		doThrow(new RuntimeException()).when(equipmentDao).saveOrUpdate(any(Equipment.class));
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulEquipmentPatch() {
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForEquipmentPatchWithNullOldObject() {
		when(daoFactory.getEquipmentDao().getEquipment(anyLong())).thenReturn(null);
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testForEquipmentUpdationWithEmptyDisplayName() {
		UpdateEquipmentEvent reqEvent = EquipmentTestData.getUpdateEquipmentEventWithEmptyDisplayName();
		EquipmentUpdatedEvent response = equipmentService.updateEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DISPLAY_NAME, response.getErroneousFields()[0].getFieldName());

	}

	@Test
	public void testForEquipmentPatchWithEmptyDeviceName() {
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEventWithEmptyDeviceName();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DEVICE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentPatchWithEmptyEquipmentId() {
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEventWithEmptyEquipmentId();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.EQUIPMENT_ID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentPatchWithEmptySiteName() {
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEventWithEmptySiteName();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentPatchWithInvalidSiteName() {
		when(daoFactory.getSiteDao().getSite(anyString())).thenReturn(null);
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.SITE_NAME, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForEquipmentPatchWithDuplicateDisplayName() {

		when(daoFactory.getEquipmentDao().isUniqueDisplayName(anyString())).thenReturn(Boolean.FALSE);
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEventDisplayName();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DISPLAY_NAME, response.getErroneousFields()[0].getFieldName());
		assertEquals(EquipmentErrorCode.DUPLICATE_DISPLAY_NAME.message(),
				response.getErroneousFields()[0].getErrorMessage());

	}

	@Test
	public void testForEquipmentPatchWithDuplicateEquipmentId() {

		when(daoFactory.getEquipmentDao().isUniqueEquipmentId(anyString())).thenReturn(Boolean.FALSE);
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEventWithDiffEqpId();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.EQUIPMENT_ID, response.getErroneousFields()[0].getFieldName());
		assertEquals(EquipmentErrorCode.DUPLICATE_EQUIPMENT_ID.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testForEquipmentPatchWithDuplicateDeviceName() {

		when(daoFactory.getEquipmentDao().isUniqueDeviceName(anyString())).thenReturn(Boolean.FALSE);
		PatchEquipmentEvent reqEvent = EquipmentTestData.getEmptyPatchData();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(EquipmentTestData.DEVICE_NAME, response.getErroneousFields()[0].getFieldName());

	}

	@Test
	public void testForEquipmentPatchWithServerError() {

		doThrow(new RuntimeException()).when(equipmentDao).saveOrUpdate(any(Equipment.class));
		PatchEquipmentEvent reqEvent = EquipmentTestData.getPatchEquipmentEvent();
		EquipmentUpdatedEvent response = equipmentService.patchEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulEquipmentDeletion() {
		DeleteEquipmentEvent reqEvent = EquipmentTestData.getDeleteEquipmentEvent();
		EquipmentDeletedEvent response = equipmentService.deleteEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForEquipmentDeletionWithNullObject() {
		when(daoFactory.getEquipmentDao().getEquipment(anyLong())).thenReturn(null);
		DeleteEquipmentEvent reqEvent = EquipmentTestData.getDeleteEquipmentEvent();
		EquipmentDeletedEvent response = equipmentService.deleteEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testForSuccessfulEquipmentDeleteWithName() {
		when(equipmentDao.getEquipment(anyString())).thenReturn(EquipmentTestData.getEquipment());
		DeleteEquipmentEvent reqEvent = EquipmentTestData.getDeleteEquipmentEventForName();
		EquipmentDeletedEvent response = equipmentService.deleteEquipment(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testForInvalidEquipmentDeleteWithName() {
		when(equipmentDao.getEquipment(anyString())).thenReturn(null);
		DeleteEquipmentEvent reqEvent = EquipmentTestData.getDeleteEquipmentEventForName();
		EquipmentDeletedEvent response = equipmentService.deleteEquipment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getDisplayName());
	}

	@Test
	public void testForEquipmentDeletionWithActiveChildren() {
		when(daoFactory.getEquipmentDao().getEquipment(anyLong())).thenReturn(
				EquipmentTestData.getEquipmentWithImageCollection());
		DeleteEquipmentEvent reqEvent = EquipmentTestData.getDeleteEquipmentEvent();
		EquipmentDeletedEvent response = equipmentService.deleteEquipment(reqEvent);

		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());

	}

	@Test
	public void testGetAllEquipments() {
		when(equipmentDao.getAllEquipments(eq(1000))).thenReturn(EquipmentTestData.getEquipments());
		ReqAllEquipmentEvent reqEvent = EquipmentTestData.getAllEquipmentsEvent();
		AllEquipmentEvent response = equipmentService.getAllEquipments(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(2, response.getEquipments().size());
	}

	@Test
	public void testGetEquipmentById() {
		when(equipmentDao.getEquipment(anyLong())).thenReturn(EquipmentTestData.getEquipment());
		GetEquipmentEvent reqEvent = EquipmentTestData.getEquipmentEvent();
		GotEquipmentEvent response = equipmentService.getEquipment(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testGetEquipmentWithWrongId() {
		when(equipmentDao.getEquipment(anyLong())).thenReturn(null);
		GetEquipmentEvent reqEvent = EquipmentTestData.getEquipmentEvent();
		GotEquipmentEvent response = equipmentService.getEquipment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getId());
	}

	@Test
	public void testGetEquipmentByName() {
		when(equipmentDao.getEquipment(anyString())).thenReturn(EquipmentTestData.getEquipment());
		GetEquipmentEvent reqEvent = EquipmentTestData.getEquipmentEventForName();
		GotEquipmentEvent response = equipmentService.getEquipment(reqEvent);
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(response.getDetails());
	}

	@Test
	public void testGetEquipmentWithWrongName() {
		when(equipmentDao.getEquipment(anyString())).thenReturn(null);
		GetEquipmentEvent reqEvent = EquipmentTestData.getEquipmentEventForName();
		GotEquipmentEvent response = equipmentService.getEquipment(reqEvent);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
		assertNotNull(response.getDisplayName());
	}

	//	@Test
	//	public void testBiohazardCreationWithInvalidActivityStatus() {
	//		CreateEquipmentEvent reqEvent = EquipmentTestData.getCreateEventWithInvalidActivityStatus();
	//		EquipmentCreatedEvent response = equipmentService.createEquipment(reqEvent);
	//
	//		assertNotNull("response cannot be null", response);
	//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	//	}
}
