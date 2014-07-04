
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
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.CollectionProtocolRegistrationFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.ParticipantFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.CollectionProtocolRegistrationServiceImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.CprTestData;

import com.krishagni.catissueplus.core.administrative.domain.Site;

public class CollectionProtocolRegTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	private CollectionProtocolRegistrationDao registrationDao;

	@Mock
	private SiteDao siteDao;

	@Mock
	private UserDao userDao;

	@Mock
	private CollectionProtocolDao collectionProtocolDao;

	@Mock
	private ParticipantDao participantDao;

	private CollectionProtocolRegistrationService registrationSvc;

	private CollectionProtocolRegistrationFactory registrationFactory;

	private ParticipantFactory participantFactory;

	private final String PPID = "participant protocol identifier";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getCprDao()).thenReturn(registrationDao);
		when(daoFactory.getCollectionProtocolDao()).thenReturn(collectionProtocolDao);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);
		when(daoFactory.getParticipantDao()).thenReturn(participantDao);

		Site site = new Site();
		when(siteDao.getSite(anyString())).thenReturn(site);
		registrationSvc = new CollectionProtocolRegistrationServiceImpl();
		((CollectionProtocolRegistrationServiceImpl) registrationSvc).setDaoFactory(daoFactory);
		registrationFactory = new CollectionProtocolRegistrationFactoryImpl();
		((CollectionProtocolRegistrationFactoryImpl) registrationFactory).setDaoFactory(daoFactory);
		((CollectionProtocolRegistrationServiceImpl) registrationSvc).setRegistrationFactory(registrationFactory);
		participantFactory = new ParticipantFactoryImpl();
		((ParticipantFactoryImpl) participantFactory).setDaoFactory(daoFactory);

		when(registrationDao.isPpidUniqueForProtocol(anyLong(), anyString())).thenReturn(true);
		when(userDao.getUser(anyString())).thenReturn(CprTestData.getUser());

		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
	}

	@Test
	public void testForSuccessfulRegistrationCreation() {

		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
//		assertEquals(EventStatus.OK, response.getStatus());
//		assertNotNull(actualResult);

	}

	@Test
	public void testForCprCreationInvalidParticipant() {

		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		when(participantDao.getParticipant(anyLong())).thenReturn(null);

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
//		assertNotNull("participant", response.getErroneousFields()[0].getErrorMessage());

	}

	@Test
	public void testForRegistrationDuplicatePpid() {
		when(registrationDao.isPpidUniqueForProtocol(anyLong(), anyString())).thenReturn(false);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEventDuplicatePpid();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals(ParticipantErrorCode.DUPLICATE_PPID.message(), response.getErroneousFields()[0].getErrorMessage());
//		assertEquals(PPID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForRegistrationWithServerError() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());

		doThrow(new RuntimeException()).when(registrationDao).saveOrUpdate(any(CollectionProtocolRegistration.class));

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulRegistrationWithConsents() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCpToReturnWithConsents());

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
//		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
//
//		assertEquals(EventStatus.OK, response.getStatus());
//		assertNotNull(actualResult);

	}

	@Test
	public void testForSuccessfulRegistrationWithEmptyConsents() {

		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCpToReturnWithEmptyConsents());

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
//		assertNotNull("Response cannot be null", response);
//		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
//
//		assertEquals(EventStatus.OK, response.getStatus());
//		assertNotNull(actualResult);
	}

	@Test
	public void testRegistrationCreationEmptyPPID() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCollectionProtocol());

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEventEmptyPpid();
		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
//		assertNotNull("Response cannot be null", response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals(ParticipantErrorCode.MISSING_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
//		assertEquals(PPID, response.getErroneousFields()[0].getFieldName());

	}

	//	@Test
	//	public void testRegistrationCreationNullCP() {
	//		CreateRegistrationEvent reqEvent = new CreateRegistrationEvent();
	//		reqEvent.setSessionDataBean(getSessionDataBean());
	//		CollectionProtocolRegistrationDetail details = getRegistrationDetails(null);
	//		details.setRegistrationDate(null);
	//		details.setPpid("");
	//		reqEvent.setCprDetail(details);
	//
	//		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
	//		assertNotNull("Response cannot be null", response);
	//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	//		assertEquals(ParticipantErrorCode.MISSING_ATTR_VALUE.message() + " : collection protocol", response.getMessage());
	//
	//	}

	@Test
	public void testGetScgListServerError() {
		ReqSpecimenCollGroupSummaryEvent event = CprTestData.getScgListEvent();
		doThrow(new RuntimeException()).when(registrationDao).getScgList(anyLong());
		AllSpecimenCollGroupsSummaryEvent response = registrationSvc.getSpecimenCollGroupsList(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testGetScgList() {
		ReqSpecimenCollGroupSummaryEvent event = CprTestData.getScgListEvent();
		when(registrationDao.getScgList(anyLong())).thenReturn(CprTestData.getSCGSummaryList());
		AllSpecimenCollGroupsSummaryEvent response = registrationSvc.getSpecimenCollGroupsList(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testRegistrationCreationInvalidCP() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(null);
		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
//		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		assertEquals(1, response.getErroneousFields().length);
//		assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message(), response.getErroneousFields()[0].getErrorMessage());
//		assertEquals("collection protocol", response.getErroneousFields()[0].getFieldName());

	}

	@Test
	public void testSuccessfulRegistrationDelete() {
		DeleteRegistrationEvent event = CprTestData.getCprDeleteEvent();
		when(registrationDao.getCpr(anyLong())).thenReturn(CprTestData.getCprToReturn());
		RegistrationDeletedEvent response = registrationSvc.delete(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testRegistrationDeleteWithActiveChildren() {
		DeleteRegistrationEvent event = CprTestData.getDeleteCprWithActiveChildren();
		when(registrationDao.getCpr(anyLong())).thenReturn(CprTestData.getCprToReturn());
		RegistrationDeletedEvent response = registrationSvc.delete(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testRegistrationDeleteNotFound() {
		DeleteRegistrationEvent event = CprTestData.getCprDeleteEvent();
		when(registrationDao.getCpr(anyLong())).thenReturn(null);
		RegistrationDeletedEvent response = registrationSvc.delete(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testRegistrationDeleteServerError() {
		DeleteRegistrationEvent event = CprTestData.getCprDeleteEvent();

		doThrow(new RuntimeException()).when(registrationDao).getCpr(anyLong());
		RegistrationDeletedEvent response = registrationSvc.delete(event);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

}
