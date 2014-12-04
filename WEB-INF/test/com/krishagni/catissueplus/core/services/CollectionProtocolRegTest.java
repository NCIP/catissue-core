
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.CollectionProtocolRegistrationFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.BulkRegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateBulkRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.CollectionProtocolRegistrationServiceImpl;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.privileges.services.PrivilegeService;
import com.krishagni.catissueplus.core.services.testdata.CprTestData;

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
	
	@Mock
	private ParticipantService participantService;
	
	@Mock
	private PrivilegeService privilegeSvc;
	
	@Mock
	private ParticipantFactory participantFactory;

	private CollectionProtocolRegistrationService registrationSvc;

	private CollectionProtocolRegistrationFactory registrationFactory;

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
		((CollectionProtocolRegistrationFactoryImpl) registrationFactory).setParticipantFactory(participantFactory);
		
		((CollectionProtocolRegistrationServiceImpl) registrationSvc).setRegistrationFactory(registrationFactory);
		((CollectionProtocolRegistrationServiceImpl) registrationSvc).setParticipantService(participantService);
		((CollectionProtocolRegistrationServiceImpl) registrationSvc).setPrivilegeSvc(privilegeSvc);
		
		when(userDao.getUser(anyString())).thenReturn(CprTestData.getUser());
		when(privilegeSvc.hasPrivilege(anyLong(), anyLong(), anyString())).thenReturn(true);
		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
	}

	@Test
	public void testForSuccessfulRegistrationCreation() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
		when(registrationDao.getCprByPpId(anyLong(), anyString())).thenReturn(null);
		when(registrationDao.getCprByBarcode(anyString())).thenReturn(null);
		 
		Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                    return null;
            }
	    })
	    .when(registrationDao)
	    .saveOrUpdate((any(CollectionProtocolRegistration.class)));
		
		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();
		
		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);

	}
	
	@Test
	public void testForSuccessfulCreateParticipantAndRegistration() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
		when(registrationDao.getCprByPpId(anyLong(), anyString())).thenReturn(null);
		when(registrationDao.getCprByBarcode(anyString())).thenReturn(null);
		ParticipantCreatedEvent event = new ParticipantCreatedEvent();
		event.setParticipantDetail(CprTestData.getCprCreateEvent().getCprDetail().getParticipant());
		event.setStatus(EventStatus.OK);
		
		when(participantService.createParticipant(any(CreateParticipantEvent.class))).thenReturn(event);
		when(participantFactory.createParticipant(any(ParticipantDetail.class))).thenReturn(CprTestData.getParticipant());
		
		Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                    return null;
            }
	    })
	    .when(registrationDao)
	    .saveOrUpdate((any(CollectionProtocolRegistration.class)));
		
		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();
		reqEvent.getCprDetail().getParticipant().setId(null);
		
		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
		assertEquals(EventStatus.OK, response.getStatus());
		assertNotNull(actualResult);

	}
	
	@Test
	public void testForCprCreationInvalidParticipant() {

		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		when(participantDao.getParticipant(anyLong())).thenReturn(null);

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertNotNull("participant", response.getErroneousFields()[0].getErrorMessage());

	}

	@Test
	public void testForRegistrationDuplicatePpid() {
		when(registrationDao.getCprByPpId(anyLong(), anyString())).thenReturn(new CollectionProtocolRegistration());
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEventDuplicatePpid();

		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(PPID, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testForRegistrationWithServerError() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
		when(registrationDao.getCprByPpId(anyLong(), anyString())).thenReturn(null);
		when(registrationDao.getCprByBarcode(anyString())).thenReturn(null);
		 
		
		doThrow(new RuntimeException())
	    .when(registrationDao)
	    .saveOrUpdate((any(CollectionProtocolRegistration.class)));
		
		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();
		
		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulRegistrationWithConsents() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCpToReturnWithConsents());
		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
		when(registrationDao.getCprByPpId(anyLong(), anyString())).thenReturn(null);
		when(registrationDao.getCprByBarcode(anyString())).thenReturn(null);
		 
		
		Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                    return null;
            }
	    })
	    .when(registrationDao)
	    .saveOrUpdate((any(CollectionProtocolRegistration.class)));
		
		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();
		
		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		CollectionProtocolRegistrationDetail actualResult = response.getCprDetail();
		assertEquals(EventStatus.OK, response.getStatus());
		assertEquals(3, actualResult.getConsentDetails().getConsenTierStatements().size());
		
		int foundStatements = 0;
		for (ConsentTierDetail ctd : actualResult.getConsentDetails().getConsenTierStatements()) {
			if (ctd.getConsentStatment().equals("statement1") || ctd.getConsentStatment().equals("statement2") 
					|| ctd.getConsentStatment().equals("statement3")) {
				foundStatements++;
			}
		}
		
		assertEquals(3, foundStatements);
	}

	@Test
	public void testRegistrationCreationEmptyPPID() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCollectionProtocol());

		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEventEmptyPpid();
		reqEvent.getCprDetail().setPpid("");
		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(PPID, response.getErroneousFields()[0].getFieldName());
	}


//	@Test
//	public void testGetScgListServerError() {
//		ReqSpecimenCollGroupSummaryEvent event = CprTestData.getScgListEvent();
//		doThrow(new RuntimeException()).when(registrationDao).getScgList(anyLong());
//		AllSpecimenCollGroupsSummaryEvent response = registrationSvc.getSpecimenCollGroupsList(event);
//		assertNotNull("Response cannot be null", response);
//		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
//	}
//
//	@Test
//	public void testGetScgList() {
//		ReqSpecimenCollGroupSummaryEvent event = CprTestData.getScgListEvent();
//		when(registrationDao.getScgList(anyLong())).thenReturn(CprTestData.getSCGSummaryList());
//		AllSpecimenCollGroupsSummaryEvent response = registrationSvc.getSpecimenCollGroupsList(event);
//		assertNotNull("Response cannot be null", response);
//		assertEquals(EventStatus.OK, response.getStatus());
//	}

	@Test
	public void testRegistrationCreationInvalidCP() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(null);
		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
		when(registrationDao.getCprByPpId(anyLong(), anyString())).thenReturn(null);
		when(registrationDao.getCprByBarcode(anyString())).thenReturn(null);
		 
		Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                    return null;
            }
	    })
	    .when(registrationDao)
	    .saveOrUpdate((any(CollectionProtocolRegistration.class)));
		
		CreateRegistrationEvent reqEvent = CprTestData.getCprCreateEvent();
		
		RegistrationCreatedEvent response = registrationSvc.createRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals("Attribute value is invalid", response.getErroneousFields()[0].getErrorMessage());
		assertEquals("collection protocol", response.getErroneousFields()[0].getFieldName());
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


	@Test
	public void testCreateBulkCpr() {
		when(collectionProtocolDao.getCollectionProtocol(anyLong())).thenReturn(CprTestData.getCptoReturn());
		when(participantDao.getParticipant(anyLong())).thenReturn(CprTestData.getParticipant());
		when(registrationDao.getCprByPpId(anyLong(), anyString())).thenReturn(null);
		when(registrationDao.getCprByBarcode(anyString())).thenReturn(null);
		ParticipantCreatedEvent event = new ParticipantCreatedEvent();
		event.setParticipantDetail(CprTestData.getCprCreateEvent().getCprDetail().getParticipant());
		event.setStatus(EventStatus.OK);
		when(privilegeSvc.hasPrivilege(anyLong(), anyLong(), anyString())).thenReturn(true);
		when(participantService.createParticipant(any(CreateParticipantEvent.class))).thenReturn(event);
		when(participantFactory.createParticipant(any(ParticipantDetail.class))).thenReturn(CprTestData.getParticipant());
		
		Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                    return null;
            }
	    })
	    .when(registrationDao)
	    .saveOrUpdate((any(CollectionProtocolRegistration.class)));
		
		CreateBulkRegistrationEvent reqEvent = CprTestData.getCreateBulkRegEvent();
		reqEvent.getParticipantDetails().setId(null);
		
		BulkRegistrationCreatedEvent response = registrationSvc.createBulkRegistration(reqEvent);
		assertNotNull("Response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		
		assertNotNull(response.getParticipantDetails());
		assertNotNull(response.getParticipantDetails().getRegistrationDetails());
		assertEquals(reqEvent.getParticipantDetails().getRegistrationDetails().size(), 
				response.getParticipantDetails().getRegistrationDetails().size());
		
		verify(participantService, times(1)).createParticipant(any(Participant.class));
	}
	
	private void printResponse(ResponseEvent resp) {
	System.out.println("Message: " + resp.getMessage());
	
	if (resp.getErroneousFields() != null) {
		for (ErroneousField field : resp.getErroneousFields()) {
			System.out.println("Error Detail Field: " + field.getFieldName() + " Message: " + field.getErrorMessage());
		}
	}
	
	if (resp.getException() != null) {
		resp.getException().printStackTrace();
	}
	
	System.out.println("Operation Status: " + resp.getStatus());
}
}
