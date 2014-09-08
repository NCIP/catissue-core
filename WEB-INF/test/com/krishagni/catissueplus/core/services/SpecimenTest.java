
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

import com.krishagni.catissueplus.core.administrative.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.ContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.SpecimenFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.SpecimenServiceImpl;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;
import com.krishagni.catissueplus.core.services.testdata.SpecimenTestData;

public class SpecimenTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	private SpecimenDao specimenDao;

	@Mock
	private SpecimenCollectionGroupDao scgDao;

	@Mock
	private CollectionProtocolDao collectionProtocolDao;

	@Mock
	private ContainerDao containerDao;

	@Mock
	private UserDao userDao;

	@Mock
	private SpecimenService service;

	@Mock
	private SpecimenFactory factory;
	
	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;
	
	PermissibleValuesManager pvManager;
	
	private PermissibleValueService pvService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(daoFactory.getScgDao()).thenReturn(scgDao);
		when(daoFactory.getCollectionProtocolDao()).thenReturn(collectionProtocolDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);
		when(daoFactory.getSpecimenDao()).thenReturn(specimenDao);
		when(daoFactory.getContainerDao()).thenReturn(containerDao);
		service = new SpecimenServiceImpl();
		((SpecimenServiceImpl) service).setDaoFactory(daoFactory);
		factory = new SpecimenFactoryImpl();
		((SpecimenFactoryImpl) factory).setDaoFactory(daoFactory);
		((SpecimenServiceImpl) service).setSpecimenFactory(factory);
		when(collectionProtocolDao.getSpecimenRequirement(anyLong())).thenReturn(SpecimenTestData.getRequirement());
		when(userDao.getUser(anyString())).thenReturn(SpecimenTestData.getUser());
		when(scgDao.getscg(anyLong())).thenReturn(SpecimenTestData.getScgToReturn());
		
		when(daoFactory.getPermissibleValueDao()).thenReturn(pvDao);
		pvService = new PermissibleValueServiceImpl();
		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvManager = new PermissibleValuesManagerImpl();
		((PermissibleValuesManagerImpl) pvManager).setPermissibleValueSvc(pvService);
		CommonValidator.setPvManager(pvManager);
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());
		when(containerDao.getContainer(anyString())).thenReturn(SpecimenTestData.getContainerToReturn());

		when(specimenDao.isBarcodeUnique(anyString())).thenReturn(true);
		when(specimenDao.isLabelUnique(anyString())).thenReturn(true);
		when(specimenDao.getSpecimen(anyLong())).thenReturn(SpecimenTestData.getSpecimenToReturn());
	}

	@Test
	public void testSuccessfullSpecimenCreation() {
		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSuccessfullChildSpecimenCreation() {
		CreateSpecimenEvent event = SpecimenTestData.getCreateChildSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSpecimenCreateWithVirtualContainer() {

		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSpecimenCreateInvaliContainer() {
		when(containerDao.getContainer(anyString())).thenReturn(null);

		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());

		assertEquals(1, response.getErroneousFields().length);
		assertEquals("container name", response.getErroneousFields()[0].getFieldName());
		assertEquals(SpecimenErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSpecimenCreateEmptyLabel() {
		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEventEmptyLabel();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("label", response.getErroneousFields()[0].getFieldName());
		assertEquals(SpecimenErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void tesChildCreationInvalidParent() {
		when(specimenDao.getSpecimen(anyLong())).thenReturn(null);
		CreateSpecimenEvent event = SpecimenTestData.getCreateChildEventInvalidParent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("parent specimen", response.getErroneousFields()[0].getFieldName());
		assertEquals(SpecimenErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void tesChildCreationInvalidParentData() {
		when(specimenDao.getSpecimen(anyLong())).thenReturn(null);
		CreateSpecimenEvent event = SpecimenTestData.getCreateChildEventInvalidParentData();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("parent", response.getErroneousFields()[0].getFieldName());
		assertEquals(SpecimenErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testSpecimenCreationServerErr() {
		doThrow(new RuntimeException()).when(specimenDao).saveOrUpdate(any(Specimen.class));
		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSpecimenCreateInvalidScg() {
		when(scgDao.getscg(anyLong())).thenReturn(null);

		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);

	}

	@Test
	public void testSpecimenCreateInvalidRequirement() {
		when(collectionProtocolDao.getSpecimenRequirement(anyLong())).thenReturn(null);
		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(SpecimenErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		assertEquals("specimen requirement", response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testSpecimenCreationDuplicateLabelBarcode() {
		when(specimenDao.isBarcodeUnique(anyString())).thenReturn(false);
		when(specimenDao.isLabelUnique(anyString())).thenReturn(false);
		CreateSpecimenEvent event = SpecimenTestData.getCreateSpecimenEvent();
		SpecimenCreatedEvent response = service.createSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(2, response.getErroneousFields().length);

		assertEquals("barcode", response.getErroneousFields()[0].getFieldName());
		assertEquals(SpecimenErrorCode.DUPLICATE_BARCODE.message(), response.getErroneousFields()[0].getErrorMessage());

		assertEquals("label", response.getErroneousFields()[1].getFieldName());
		assertEquals(SpecimenErrorCode.DUPLICATE_LABEL.message(), response.getErroneousFields()[1].getErrorMessage());
	}

	@Test
	public void testSuccessfullSpecimenUpdation() {
		UpdateSpecimenEvent event = SpecimenTestData.getUpdateSpecimenEvent();
		SpecimenUpdatedEvent response = service.updateSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testSpecimenUpdateDuplicateLabelBarcode() {
		when(specimenDao.isBarcodeUnique(anyString())).thenReturn(false);
		when(specimenDao.isLabelUnique(anyString())).thenReturn(false);
		UpdateSpecimenEvent event = SpecimenTestData.getUpdateSpecimenEvent();
		SpecimenUpdatedEvent response = service.updateSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(2, response.getErroneousFields().length);

		assertEquals("barcode", response.getErroneousFields()[0].getFieldName());
		assertEquals(SpecimenErrorCode.DUPLICATE_BARCODE.message(), response.getErroneousFields()[0].getErrorMessage());

		assertEquals("label", response.getErroneousFields()[1].getFieldName());
		assertEquals(SpecimenErrorCode.DUPLICATE_LABEL.message(), response.getErroneousFields()[1].getErrorMessage());
	}

	@Test
	public void testSpecimenUpdationServerErr() {
		doThrow(new RuntimeException()).when(specimenDao).saveOrUpdate(any(Specimen.class));
		UpdateSpecimenEvent event = SpecimenTestData.getUpdateSpecimenEvent();
		SpecimenUpdatedEvent response = service.updateSpecimen(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testSuccessfullAliquotCreation() {
		CreateAliquotEvent event = SpecimenTestData.getCreateAliquotEvent();
		AliquotCreatedEvent response = service.createAliquot(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testCreateAliquotInsufficientSpecimenCount() {
		CreateAliquotEvent event = SpecimenTestData.getCreateAliquotToTestInsufficientSpecimenCount();
		AliquotCreatedEvent response = service.createAliquot(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(SpecimenErrorCode.INSUFFICIENT_SPECIMEN_QTY.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testCreateAliquotCountGreterThanZero() {
		CreateAliquotEvent event = SpecimenTestData.getCreateAliquotWithCountZero();
		AliquotCreatedEvent response = service.createAliquot(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(SpecimenErrorCode.INVALID_ALIQUOT_COUNT.message(), response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testCreateAliquotWithFullContainer() {
		CreateAliquotEvent event = SpecimenTestData.getCreateAliquotWithFullContainer();
		AliquotCreatedEvent response = service.createAliquot(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(SpecimenErrorCode.CONTAINER_FULL.message(), response.getErroneousFields()[0].getErrorMessage());

	}

}
