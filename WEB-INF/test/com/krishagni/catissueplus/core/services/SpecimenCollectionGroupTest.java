
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
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.SpecimenCollectionGroupFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.GetScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.SpecimenCollGroupServiceImpl;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.ParticipantTestData;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;
import com.krishagni.catissueplus.core.services.testdata.ScgTestData;
import com.krishagni.catissueplus.core.services.testdata.UserTestData;

public class SpecimenCollectionGroupTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	private SpecimenCollectionGroupDao scgDao;

	@Mock
	private CollectionProtocolDao collectionProtocolDao;

	@Mock
	private CollectionProtocolRegistrationDao cprDao;

	@Mock
	private SiteDao siteDao;
	@Mock
	private UserDao userDao;
	
	@Mock
	private PermissibleValueDao pvDao;

	@Mock
	private SpecimenCollGroupService service;

	@Mock
	private SpecimenCollectionGroupFactory factory;
	
	@Mock
	private CommonValidator commonValidator;
	
	private PermissibleValuesManager pvManager;
	
	private PermissibleValueService pvService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getScgDao()).thenReturn(scgDao);
		when(daoFactory.getCollectionProtocolDao()).thenReturn(collectionProtocolDao);
		when(daoFactory.getCprDao()).thenReturn(cprDao);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);
		service = new SpecimenCollGroupServiceImpl();
		((SpecimenCollGroupServiceImpl) service).setDaoFactory(daoFactory);
		factory = new SpecimenCollectionGroupFactoryImpl();
		((SpecimenCollectionGroupFactoryImpl)factory).setDaoFactory(daoFactory);
		((SpecimenCollGroupServiceImpl) service).setScgFactory(factory);
		pvService = new PermissibleValueServiceImpl();
		
		((PermissibleValueServiceImpl) pvService).setDaoFactory(daoFactory);
		pvManager = new PermissibleValuesManagerImpl();
		((PermissibleValuesManagerImpl) pvManager).setPermissibleValueSvc(pvService);
		CommonValidator.setPvManager(pvManager);
		when(pvDao.getAllValuesByAttribute(anyString())).thenReturn(PermissibleValueTestData.getPvValues());
		when(siteDao.getSite(anyString())).thenReturn(ParticipantTestData.getSite("siteName"));
		when(cprDao.getCpr(anyLong())).thenReturn(ScgTestData.getCpr(1l));
		when(collectionProtocolDao.getCpe(anyLong())).thenReturn(ScgTestData.getCpe());
		when(userDao.getUser(anyString())).thenReturn(ScgTestData.getUser());
		when(userDao.getUserByLoginNameAndDomainName(anyString(), anyString())).thenReturn(ScgTestData.getUser());
		when(scgDao.isBarcodeUnique(anyString())).thenReturn(true);
		when(scgDao.isNameUnique(anyString())).thenReturn(true);
		when(scgDao.getscg(anyLong())).thenReturn(ScgTestData.getScgToReturn());
	}

	@Test
	public void testSuccessfullScgCreation() {
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testScgCreateServerErr() {
		doThrow(new RuntimeException()).when(scgDao).saveOrUpdate(any(SpecimenCollectionGroup.class));
		CreateScgEvent event = ScgTestData.getCreateScgEventServerErr();
		
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
	@Test
	public void testScgCreateDuplicateName() {
		when(scgDao.isNameUnique(anyString())).thenReturn(false);
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("name", response.getErroneousFields()[0].getFieldName());
		assertEquals(ScgErrorCode.DUPLICATE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgCreateDuplicateBarcode() {
		when(scgDao.isBarcodeUnique(anyString())).thenReturn(false);
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("barcode", response.getErroneousFields()[0].getFieldName());
		assertEquals(ScgErrorCode.DUPLICATE_BARCODE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testSuccessfullScgUpdation() {
		UpdateScgEvent event = ScgTestData.getUpdateScgEvent();
		ScgUpdatedEvent response = service.updateScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testScgUpdateServerErr() {
		doThrow(new RuntimeException()).when(scgDao).saveOrUpdate(any(SpecimenCollectionGroup.class));
		UpdateScgEvent event = ScgTestData.getupdateScgEventServerErr();
		
		ScgUpdatedEvent response = service.updateScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testScgUpdateDuplicateName() {
		when(scgDao.isNameUnique(anyString())).thenReturn(false);
		UpdateScgEvent event = ScgTestData.getUpdateScgEvent();
		
		ScgUpdatedEvent response = service.updateScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("name", response.getErroneousFields()[0].getFieldName());
		assertEquals(ScgErrorCode.DUPLICATE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgUpdateDuplicateBarcode() {
		when(scgDao.isBarcodeUnique(anyString())).thenReturn(false);
		UpdateScgEvent event = ScgTestData.getUpdateScgEvent();
		
		ScgUpdatedEvent response = service.updateScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("barcode", response.getErroneousFields()[0].getFieldName());
		assertEquals(ScgErrorCode.DUPLICATE_BARCODE.message(), response.getErroneousFields()[0].getErrorMessage());
	}
	
	
	@Test
	public void testForSuccessfulSCGReportUpdate() {
		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEvent();
		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testForSuccessfulSCGGetwithReport() {
		GetScgReportEvent reqEvent = new GetScgReportEvent();
		reqEvent.setId(1l);
		ScgReportUpdatedEvent response = service.getScgReport(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testForSuccessfulSCGGetwithReportWithInvalidScg() {
		when(scgDao.getscg(anyLong())).thenReturn(null);
		GetScgReportEvent reqEvent = new GetScgReportEvent();
		reqEvent.setId(1l);
		ScgReportUpdatedEvent response = service.getScgReport(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testForUserUpdateWithNullValues() {
		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEventWithNullValues();

		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(ScgTestData.SCG_REPORTS, response.getErroneousFields()[0].getFieldName());
		assertEquals(ScgErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	}
	
	@Test
	public void testForInvalidScgUpdate() {
		when(scgDao.getscg(anyLong())).thenReturn(null);
		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEvent();

		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testUserUpdateWithServerErr() {
		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEvent();

		doThrow(new RuntimeException()).when(scgDao).saveOrUpdate(any(SpecimenCollectionGroup.class));
		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
}
