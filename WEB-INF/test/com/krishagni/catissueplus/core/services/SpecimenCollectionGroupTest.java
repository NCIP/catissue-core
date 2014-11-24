
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.SpecimenCollectionGroupFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.SpecimenCollGroupServiceImpl;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;
import com.krishagni.catissueplus.core.services.testdata.ScgTestData;

public class SpecimenCollectionGroupTest {

	private final String INVALID_ATTR_VALUE = "Attribute value is invalid";
	
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
		when(siteDao.getSite(anyString())).thenReturn(ScgTestData.getSite("siteName"));
	}

	@Test
	public void testSuccessfullScgCreation() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.OK, response.getStatus());
	}
	
	@Test
	public void testSuccessfullScgCreationWithPPIDAndCollectionPointLabelAndCpTitle() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpeByCollectionPointLabel(anyLong(), anyString());
		doReturn(ScgTestData.getCpe().getCollectionProtocol()).when(collectionProtocolDao).getCollectionProtocol(anyString());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCprByPpId(anyLong(), anyString());
		
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setCprId(null);
		event.getScgDetail().setCpeId(null);
		event.getScgDetail().setPpid("default_default");
		event.getScgDetail().setCpTitle("default_cp_title");
		event.getScgDetail().setCollectionPointLabel("Visit1");
		
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testScgCreationInvalidCpeExpectBadRequest() {
		doReturn(null).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("collection protocol event" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgCreationCprIdInvalidExpectBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(null).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("collection protocol registration" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testInvalidCpTitleBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpeByCollectionPointLabel(anyLong(), anyString());
		doReturn(null).when(collectionProtocolDao).getCollectionProtocol(anyString());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCprByPpId(anyLong(), anyString());
		
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setCprId(null);
		event.getScgDetail().setCpeId(null);
		event.getScgDetail().setPpid("default_default");
		event.getScgDetail().setCpTitle("default_cp_title");
		event.getScgDetail().setCollectionPointLabel("Visit1");
		
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("collection protocol title" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testInvalidCollectionPointLabelExpectBadRequest() {
		doReturn(null).when(collectionProtocolDao).getCpeByCollectionPointLabel(anyLong(), anyString());
		doReturn(ScgTestData.getCpe().getCollectionProtocol()).when(collectionProtocolDao).getCollectionProtocol(anyString());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCprByPpId(anyLong(), anyString());
		
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setCprId(null);
		event.getScgDetail().setCpeId(null);
		event.getScgDetail().setPpid("default_default");
		event.getScgDetail().setCpTitle("default_cp_title");
		event.getScgDetail().setCollectionPointLabel("Visit1");
		
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("collection point label" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgCreationInvalidCpTitleExpectBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpeByCollectionPointLabel(anyLong(), anyString());
		doReturn(null).when(collectionProtocolDao).getCollectionProtocol(anyString());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCprByPpId(anyLong(), anyString());
		
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setCprId(null);
		event.getScgDetail().setCpeId(null);
		event.getScgDetail().setPpid("default_default");
		event.getScgDetail().setCpTitle("default_cp_title");
		event.getScgDetail().setCollectionPointLabel("Visit1");
		
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("collection protocol title" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgCreationInvalidPPIDExpectBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpeByCollectionPointLabel(anyLong(), anyString());
		doReturn(ScgTestData.getCpe().getCollectionProtocol()).when(collectionProtocolDao).getCollectionProtocol(anyString());
		doReturn(null).when(cprDao).getCprByPpId(anyLong(), anyString());
		
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setCprId(null);
		event.getScgDetail().setCpeId(null);
		event.getScgDetail().setPpid("default_default");
		event.getScgDetail().setCpTitle("default_cp_title");
		event.getScgDetail().setCollectionPointLabel("Visit1");
		
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("participant protocol id" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgCreationInvalidSiteExpectBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(null).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("site name" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgCreationInvalidCollectorNameExpectBadRequest() {
		String collectorName = "collector@catissue.org";
		String receiverName = "receiver@catissue.org";
		
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(null).when(userDao).getUserByLoginNameAndDomainName(eq(collectorName), anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(eq(receiverName), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setCollectorName(collectorName);
		event.getScgDetail().setReceiverName(receiverName);
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("collector name" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testScgCreationInvalidReceiverNameExpectBadRequest() {
		String collectorName = "collector@catissue.org";
		String receiverName = "receiver@catissue.org";
		
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(eq(collectorName), anyString());
		doReturn(null).when(userDao).getUserByLoginNameAndDomainName(eq(receiverName), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setCollectorName(collectorName);
		event.getScgDetail().setReceiverName(receiverName);
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("receiver name" , response.getErroneousFields()[0].getFieldName());
		assertEquals(INVALID_ATTR_VALUE, response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testMismatchCprCpeExpectBadRequest() {
		CollectionProtocolEvent cpe = ScgTestData.getCpe();
		cpe.getCollectionProtocol().setId(3131L);
		doReturn(cpe).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("registraion and event point refering to different protocols." , response.getErroneousFields()[0].getFieldName());
		assertEquals("registraion and event point refering to different protocols." , response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testCreateScgWithDuplicateNameExpectBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(new SpecimenCollectionGroup()).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("name" , response.getErroneousFields()[0].getFieldName());
		assertEquals("specimen collection group with same name already exists.", response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testCreateScgWithDuplicateBarocdeExpectBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(new SpecimenCollectionGroup()).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("barcode" , response.getErroneousFields()[0].getFieldName());
		assertEquals("Specimen Collection Group with same barcode already exists.", response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testCreateScgNullNameExpectBadRequest() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		event.getScgDetail().setName("");
		ScgCreatedEvent response = service.createScg(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("name" , response.getErroneousFields()[0].getFieldName());
		assertEquals("Required attribute is either empty or null", response.getErroneousFields()[0].getErrorMessage());
	}
	
	@Test
	public void testCreateScgServerErrorOnSaveOrUpdateExpectServerError() {
		doReturn(ScgTestData.getCpe()).when(collectionProtocolDao).getCpe(anyLong());
		doReturn(ScgTestData.getCpr(1L)).when(cprDao).getCpr(anyLong());
		doReturn(ScgTestData.getSite("default-site")).when(siteDao).getSite(anyString());
		doReturn(ScgTestData.getUser()).when(userDao).getUserByLoginNameAndDomainName(anyString(), anyString());
		doReturn(null).when(scgDao).getScgByName(anyString());
		doReturn(null).when(scgDao).getScgByBarcode(anyString());
		
		doThrow(new RuntimeException()).when(scgDao).saveOrUpdate(any(SpecimenCollectionGroup.class));
		
		CreateScgEvent event = ScgTestData.getCreateScgEvent();
		ScgCreatedEvent response = service.createScg(event);
		assertNotNull("Response cannot be null",response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}
	
//	private void printResponse(ResponseEvent resp) {
//		System.out.println("Message: " + resp.getMessage());
//		
//		if (resp.getErroneousFields() != null) {
//			for (ErroneousField field : resp.getErroneousFields()) {
//				System.out.println("Error Detail Field: " + field.getFieldName() + " Message: " + field.getErrorMessage());
//			}
//		}
//		
//		if (resp.getException() != null) {
//			resp.getException().printStackTrace();
//		}
//		
//		System.out.println("Operation Status: " + resp.getStatus());
//	}
	
//	@Test
//	public void testScgCreateServerErr() {
//		doThrow(new RuntimeException()).when(scgDao).saveOrUpdate(any(SpecimenCollectionGroup.class));
//		CreateScgEvent event = ScgTestData.getCreateScgEventServerErr();
//		
//		ScgCreatedEvent response = service.createScg(event);
//		assertNotNull("Response cannot be null",response);
//		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
//	}
//	
//	@Test
//	public void testScgCreateDuplicateName() {
//		when(scgDao.isNameUnique(anyString())).thenReturn(false);
//		CreateScgEvent event = ScgTestData.getCreateScgEvent();
//		
//		ScgCreatedEvent response = service.createScg(event);
//		assertNotNull("Response cannot be null",response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals("name", response.getErroneousFields()[0].getFieldName());
//		assertEquals(ScgErrorCode.DUPLICATE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
//	}
//	
//	@Test
//	public void testScgCreateDuplicateBarcode() {
//		when(scgDao.isBarcodeUnique(anyString())).thenReturn(false);
//		CreateScgEvent event = ScgTestData.getCreateScgEvent();
//		
//		ScgCreatedEvent response = service.createScg(event);
//		assertNotNull("Response cannot be null",response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals("barcode", response.getErroneousFields()[0].getFieldName());
//		assertEquals(ScgErrorCode.DUPLICATE_BARCODE.message(), response.getErroneousFields()[0].getErrorMessage());
//	}
//	
//	@Test
//	public void testSuccessfullScgUpdation() {
//		UpdateScgEvent event = ScgTestData.getUpdateScgEvent();
//		ScgUpdatedEvent response = service.updateScg(event);
//		assertNotNull("Response cannot be null",response);
//		assertEquals(EventStatus.OK, response.getStatus());
//	}
//	
//	@Test
//	public void testScgUpdateServerErr() {
//		doThrow(new RuntimeException()).when(scgDao).saveOrUpdate(any(SpecimenCollectionGroup.class));
//		UpdateScgEvent event = ScgTestData.getupdateScgEventServerErr();
//		
//		ScgUpdatedEvent response = service.updateScg(event);
//		assertNotNull("Response cannot be null",response);
//		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
//	}
//
//	@Test
//	public void testScgUpdateDuplicateName() {
//		when(scgDao.isNameUnique(anyString())).thenReturn(false);
//		UpdateScgEvent event = ScgTestData.getUpdateScgEvent();
//		
//		ScgUpdatedEvent response = service.updateScg(event);
//		assertNotNull("Response cannot be null",response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals("name", response.getErroneousFields()[0].getFieldName());
//		assertEquals(ScgErrorCode.DUPLICATE_NAME.message(), response.getErroneousFields()[0].getErrorMessage());
//	}
//	
//	@Test
//	public void testScgUpdateDuplicateBarcode() {
//		when(scgDao.isBarcodeUnique(anyString())).thenReturn(false);
//		UpdateScgEvent event = ScgTestData.getUpdateScgEvent();
//		
//		ScgUpdatedEvent response = service.updateScg(event);
//		assertNotNull("Response cannot be null",response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals("barcode", response.getErroneousFields()[0].getFieldName());
//		assertEquals(ScgErrorCode.DUPLICATE_BARCODE.message(), response.getErroneousFields()[0].getErrorMessage());
//	}
//	
//	
//	@Test
//	public void testForSuccessfulSCGReportUpdate() {
//		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEvent();
//		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
//		assertNotNull("response cannot be null", response);
//		assertEquals(EventStatus.OK, response.getStatus());
//	}
//	
//	@Test
//	public void testForSuccessfulSCGGetwithReport() {
//		GetScgReportEvent reqEvent = new GetScgReportEvent();
//		reqEvent.setId(1l);
//		ScgReportUpdatedEvent response = service.getScgReport(reqEvent);
//		assertNotNull("response cannot be null", response);
//		assertEquals(EventStatus.OK, response.getStatus());
//	}
//	
//	@Test
//	public void testForSuccessfulSCGGetwithReportWithInvalidScg() {
//		when(scgDao.getscg(anyLong())).thenReturn(null);
//		GetScgReportEvent reqEvent = new GetScgReportEvent();
//		reqEvent.setId(1l);
//		ScgReportUpdatedEvent response = service.getScgReport(reqEvent);
//		assertNotNull("response cannot be null", response);
//		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
//	}
//
//	@Test
//	public void testForUserUpdateWithNullValues() {
//		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEventWithNullValues();
//
//		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
//		assertNotNull("response cannot be null", response);
//		assertEquals(ScgTestData.SCG_REPORTS, response.getErroneousFields()[0].getFieldName());
//		assertEquals(ScgErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//	}
//	
//	@Test
//	public void testForInvalidScgUpdate() {
//		when(scgDao.getscg(anyLong())).thenReturn(null);
//		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEvent();
//
//		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
//		assertNotNull("response cannot be null", response);
//		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
//	}
//
//	@Test
//	public void testUserUpdateWithServerErr() {
//		when(userDao.getUser(anyLong())).thenReturn(UserTestData.getUser(1L));
//		UpdateScgReportEvent reqEvent = ScgTestData.getUpdateScgReportEvent();
//
//		doThrow(new RuntimeException()).when(scgDao).saveOrUpdate(any(SpecimenCollectionGroup.class));
//		ScgReportUpdatedEvent response = service.updateScgReport(reqEvent);
//		assertNotNull("response cannot be null", response);
//		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
//	}
}
