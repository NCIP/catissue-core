
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;  
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.administrative.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.CollectionProtocolRegistrationFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.ParticipantFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.CollectionProtocolRegistrationServiceImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.notification.events.RegisterParticipantEvent;
import com.krishagni.catissueplus.core.notification.repository.CPStudyMappingDao;
import com.krishagni.catissueplus.core.notification.services.CatissueNotificationService;
import com.krishagni.catissueplus.core.notification.services.impl.CatissueNotificationServiceImpl;
import com.krishagni.catissueplus.core.services.testdata.CaTissueNotificationTestData;
import com.krishagni.catissueplus.core.services.testdata.CprTestData;

import com.krishagni.catissueplus.core.administrative.domain.Site;

public class CaTissueNotificationTest {

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

	private CollectionProtocolRegistrationService cprSvc;

	private CollectionProtocolRegistrationFactory registrationFactory;

	private ParticipantFactory participantFactory;

	@Mock
	private CPStudyMappingDao cpStudyMappingdao;

	private CatissueNotificationService catNotifSvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getCPStudyMappingDao()).thenReturn(cpStudyMappingdao);
		catNotifSvc = new CatissueNotificationServiceImpl();
		((CatissueNotificationServiceImpl) catNotifSvc).setDaoFactory(daoFactory);

		when(daoFactory.getCprDao()).thenReturn(registrationDao);
		when(daoFactory.getCollectionProtocolDao()).thenReturn(collectionProtocolDao);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		when(daoFactory.getUserDao()).thenReturn(userDao);
		when(daoFactory.getParticipantDao()).thenReturn(participantDao);

		Site site = new Site();
		when(siteDao.getSite(anyString())).thenReturn(site);
		cprSvc = new CollectionProtocolRegistrationServiceImpl();
		((CatissueNotificationServiceImpl) catNotifSvc).setCprSvc(cprSvc);
		((CollectionProtocolRegistrationServiceImpl) cprSvc).setDaoFactory(daoFactory);
		registrationFactory = new CollectionProtocolRegistrationFactoryImpl();
		((CollectionProtocolRegistrationFactoryImpl) registrationFactory).setDaoFactory(daoFactory);
		((CollectionProtocolRegistrationServiceImpl) cprSvc).setRegistrationFactory(registrationFactory);

		participantFactory = new ParticipantFactoryImpl();
		((ParticipantFactoryImpl) participantFactory).setDaoFactory(daoFactory);
		((CollectionProtocolRegistrationFactoryImpl) registrationFactory).setParticipantFactory(participantFactory);
		when(registrationDao.isPpidUniqueForProtocol(anyLong(), anyString())).thenReturn(true);
		when(registrationDao.getCprByPpId(anyLong(), anyString()))
				.thenReturn(CaTissueNotificationTestData.getCprToReturn());
		when(userDao.getUser(anyString())).thenReturn(CprTestData.getUser());

		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
	}

	@Test
	public void testForRegisterParticipant() {

		when(cpStudyMappingdao.getMappedCPId(anyString(), anyString())).thenReturn(1L);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		RegisterParticipantEvent event = CaTissueNotificationTestData.getRegisterParticipantEvent();

		RegistrationCreatedEvent response = catNotifSvc.registerParticipant(event);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
//		assertEquals(EventStatus.OK, response.getStatus());
//		assertNotNull(actualResult);

	}

	@Test
	public void testUpdateParticipantRegistration() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		RegisterParticipantEvent event = CaTissueNotificationTestData.getRegisterParticipantEvent();
		RegistrationUpdatedEvent response = catNotifSvc.updateParticipantRegistartion(event);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);
	}

	@Test
	public void testForRegistrationWithBadRequeset() {

		when(cpStudyMappingdao.getMappedCPId(anyString(), anyString())).thenReturn(null);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		RegisterParticipantEvent event = CaTissueNotificationTestData.getRegisterParticipantEvent();
		RegistrationCreatedEvent response = catNotifSvc.registerParticipant(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());

	}

	@Test
	public void testUpdateRegistrationWithBadRequest() {
		when(cpStudyMappingdao.getMappedCPId(anyString(), anyString())).thenReturn(null);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		RegisterParticipantEvent event = CaTissueNotificationTestData.getRegisterParticipantEventWithNullPpId();
		RegistrationUpdatedEvent response = catNotifSvc.updateParticipantRegistartion(event);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testUpdateRegistrationWithServerError() {
		when(cpStudyMappingdao.getMappedCPId(anyString(), anyString())).thenReturn(null);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		RegistrationUpdatedEvent response = catNotifSvc.updateParticipantRegistartion(null);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForRegistrationWithServerError() {
		when(cpStudyMappingdao.getMappedCPId(anyString(), anyString())).thenReturn(1L);
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		RegistrationCreatedEvent response = catNotifSvc.registerParticipant(null);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());

	}

}