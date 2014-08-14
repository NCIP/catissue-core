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

import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.administrative.services.impl.PermissibleValueServiceImpl;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.ParticipantFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.ParticipantServiceImpl;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.services.testdata.ParticipantTestData;
import com.krishagni.catissueplus.core.services.testdata.PermissibleValueTestData;

public class ParticipantTest {

	private final String SITE = "site";

	private final String DEATH_DATE = "death date";

	@Mock
	private DaoFactory daoFactory;

	@Mock
	private ParticipantDao participantDao;

	@Mock
	private SiteDao siteDao;
	
	@Mock
	PermissibleValueDao pvDao;

	@Mock
	CommonValidator commonValidator;
	
	PermissibleValuesManager pvManager;
	
	private PermissibleValueService pvService;

	private ParticipantFactory participantFactory;

	private ParticipantService participantService;

	private final String SSN = "social security number";

	private final String PMI = "participant medical identifier";

	private final String BIRTH_DATE = "birth date";

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
		when(daoFactory.getParticipantDao()).thenReturn(participantDao);
		participantService = new ParticipantServiceImpl();
		((ParticipantServiceImpl) participantService).setDaoFactory(daoFactory);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		participantFactory = new ParticipantFactoryImpl();
		((ParticipantFactoryImpl) participantFactory).setDaoFactory(daoFactory);
		when(siteDao.getSite(anyString())).thenReturn(
				ParticipantTestData.getSite("siteName"));
		((ParticipantServiceImpl) participantService)
				.setParticipantFactory(participantFactory);
		when(participantDao.isSsnUnique(anyString())).thenReturn(true);
	}

	@Test
	public void testForSuccessfulParticipantCreation() {

		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateEvent();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		 assertEquals(EventStatus.OK, response.getStatus());
		 ParticipantDetail createdParticipant =
		 response.getParticipantDetail();
		 assertNotNull(createdParticipant);
		 assertEquals(reqEvent.getParticipantDetail().getFirstName(),
		 createdParticipant.getFirstName());

	}

	@Test
	public void testParticipantCreationInvalidSSN() {

		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateEventInvalidSSN();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		 assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		 assertEquals(1, response.getErroneousFields().length);
		 assertEquals(SSN, response.getErroneousFields()[0].getFieldName());
		 assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message(),
		 response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testParticipantCreationFutureBirthaAndPastDeathDate() {

		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateWithFutureDate();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(BIRTH_DATE,
				response.getErroneousFields()[0].getFieldName());
		assertEquals(ParticipantErrorCode.CONSTRAINT_VIOLATION.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testParticipantCreationPastDeathDate() {

		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreatEventPastDeathDate();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DEATH_DATE,
				response.getErroneousFields()[0].getFieldName());
		assertEquals(ParticipantErrorCode.CONSTRAINT_VIOLATION.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testParticipantCreationInvalidDeathDate() {

		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateInvalidDeathDate();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(1, response.getErroneousFields().length);
		assertEquals(DEATH_DATE,
				response.getErroneousFields()[0].getFieldName());
		assertEquals(ParticipantErrorCode.CONSTRAINT_VIOLATION.message(),
				response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testParticipantCreationInvalidSite() {

		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateEventInvalidSite();
		when(siteDao.getSite(anyString())).thenReturn(null);
		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		 assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		 assertEquals(1, response.getErroneousFields().length);
		 assertEquals(SITE, response.getErroneousFields()[0].getFieldName());
		 assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message(),
		 response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testParticipantCreationEmptyMrn() {

		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateEmptyMrn();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		 assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		 assertEquals(ParticipantErrorCode.MISSING_ATTR_VALUE.message(),
		 response.getErroneousFields()[0].getErrorMessage());
	}

	@Test
	public void testParticipantCreationInvalidGender() {

		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateInvalidGender();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
//		System.out.println(response.getMessage());
//		 assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		 assertEquals("Attribute value is invalid : gender",
//		 response.getMessage());
	}

	@Test
	public void testParticipantCreationInvalidRace() {

		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateInvalidRace();

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
//		 assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
//		 assertEquals("Attribute value is invalid : race",
//		 response.getMessage());
	}

	@Test
	public void testParticipantCreationDuplicateSsn() {
		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateDuplicateSsn();

		when(participantDao.isSsnUnique(anyString())).thenReturn(false);

		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		 assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		 assertEquals(ParticipantErrorCode.DUPLICATE_SSN.message(),
		 response.getErroneousFields()[0].getErrorMessage());
		 assertEquals(SSN, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testParticipantCreationWithServerErr() {

		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		CreateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantCreateEvent();

		doThrow(new RuntimeException()).when(participantDao).saveOrUpdate(
				any(Participant.class));
		ParticipantCreatedEvent response = participantService
				.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		 assertEquals(EventStatus.INTERNAL_SERVER_ERROR,response.getStatus());
	}

	@Test
	public void testForSuccessfulParticipantUpdate() {

		when(participantDao.isSsnUnique(anyString())).thenReturn(true);
		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		when(participantDao.getParticipant(anyLong())).thenReturn(
				ParticipantTestData.getUpdateParticipant());
		UpdateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantUpdateEvent();

		ParticipantUpdatedEvent response = participantService
				.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		 assertEquals(EventStatus.OK, response.getStatus());
		 ParticipantDetail createdParticipant =
		 response.getParticipantDetail();
		 assertNotNull(createdParticipant);
		 assertEquals(reqEvent.getParticipantDetail().getFirstName(),
		 createdParticipant.getFirstName());
	}

	@Test
	public void testParticipantUpdateInvalidSsn() {

		when(participantDao.getParticipant(anyLong())).thenReturn(
				ParticipantTestData.getUpdateParticipant());
		UpdateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantUpdateInvalidSsn();

		ParticipantUpdatedEvent response = participantService
				.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message(),
				response.getErroneousFields()[0].getErrorMessage());
		assertEquals(SSN, response.getErroneousFields()[0].getFieldName());
	}

	@Test
	public void testParticipantUpdateNewSsn() {
		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		when(participantDao.getParticipant(anyLong())).thenReturn(
				ParticipantTestData.getUpdatedParticipantWithNewSsn());

		UpdateParticipantEvent reqEvent = ParticipantTestData
				.getUpdateParticipantNewSsn();

		ParticipantUpdatedEvent response = participantService
				.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		 assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testParticipantUpdateInvaliParticipant() {

		when(participantDao.getParticipant(anyLong())).thenReturn(null);
		UpdateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantUpdateEvent();

		ParticipantUpdatedEvent response = participantService
				.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testParticipantUpdateWithServerErr() {

		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				true);
		when(participantDao.getParticipant(anyLong())).thenReturn(
				ParticipantTestData.getUpdateParticipant());
		UpdateParticipantEvent reqEvent = ParticipantTestData
				.getParticipantUpdateEvent();

		doThrow(new RuntimeException()).when(participantDao).saveOrUpdate(
				any(Participant.class));
		ParticipantUpdatedEvent response = participantService
				.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		 assertEquals(EventStatus.INTERNAL_SERVER_ERROR,response.getStatus());
	}

	@Test
	public void testGetParticipant() {

		when(participantDao.getParticipant(anyLong())).thenReturn(
				ParticipantTestData.getUpdateParticipant());

		ReqParticipantDetailEvent reqEvent = ParticipantTestData
				.getRequestParticipantEvent();

		ParticipantDetailEvent response = participantService
				.getParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetail retrievedParticipant = response
				.getParticipantDetail();
		assertNotNull(retrievedParticipant);
	}

	@Test
	public void testSuccessfulParticipantDelete() {
		DeleteParticipantEvent event = ParticipantTestData
				.getParticipantDeleteEvent();

		when(participantDao.getParticipant(anyLong())).thenReturn(
				ParticipantTestData.getParticipantToDelete());
		ParticipantDeletedEvent response = participantService.delete(event);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.OK, response.getStatus());
	}

	@Test
	public void testParticipantDeleteNotFound() {
		DeleteParticipantEvent event = ParticipantTestData
				.getParticipantDeleteEvent();

		when(participantDao.getParticipant(anyLong())).thenReturn(null);

		ParticipantDeletedEvent response = participantService.delete(event);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testParticipantDeleteServerError() {
		DeleteParticipantEvent event = ParticipantTestData
				.getParticipantDeleteEvent();

		doThrow(new RuntimeException()).when(participantDao).getParticipant(
				anyLong());

		ParticipantDeletedEvent response = participantService.delete(event);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testDeleteParticipantWithoutChild() {
		DeleteParticipantEvent event = ParticipantTestData
				.getParticipantDeleteEventWithoutChildren();

		when(participantDao.getParticipant(anyLong())).thenReturn(
				ParticipantTestData.getParticipantToDelete());

		ParticipantDeletedEvent response = participantService.delete(event);
		assertNotNull("response cannot be null", response);
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
	}

	@Test
	public void testCreateParticipantDuplicateMrn() {
		CreateParticipantEvent event = ParticipantTestData
				.getParticipantCreateEventDuplicateMrn();

		when(participantDao.isPmiUnique(anyString(), anyString())).thenReturn(
				false);
		when(siteDao.getSite("siteName")).thenReturn(
				ParticipantTestData.getSite("siteName"));
		when(siteDao.getSite("newSiteName")).thenReturn(
				ParticipantTestData.getSite("newSiteName"));

		ParticipantCreatedEvent response = participantService
				.createParticipant(event);
		assertNotNull("response cannot be null", response);
		 assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		 assertEquals(1, response.getErroneousFields().length);
		 assertEquals(ParticipantErrorCode.DUPLICATE_PMI.message(),
		 response.getErroneousFields()[0].getErrorMessage());
		 assertEquals(PMI, response.getErroneousFields()[0].getFieldName());
	}

}
