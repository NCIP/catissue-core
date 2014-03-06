
package com.krishagni.catissueplus.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.impl.ParticipantFactoryImpl;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.MedicalRecordNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetails;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.biospecimen.repository.SiteDao;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.biospecimen.services.impl.ParticipantServiceImpl;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.common.beans.SessionDataBean;

public class ParticipantTest {

	@Mock
	private DaoFactory daoFactory;

	@Mock
	private ParticipantDao participantDao;

	@Mock
	private SiteDao siteDao;

	private ParticipantFactory participantFactory;

	ParticipantService participantService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(daoFactory.getParticipantDao()).thenReturn(participantDao);
		participantService = new ParticipantServiceImpl();
		((ParticipantServiceImpl) participantService).setDaoFactory(daoFactory);
		when(daoFactory.getSiteDao()).thenReturn(siteDao);
		participantFactory = new ParticipantFactoryImpl();
		((ParticipantFactoryImpl) participantFactory).setDaoFactory(daoFactory);
		when(siteDao.getSite(anyString())).thenReturn(getSite("siteName"));
		((ParticipantServiceImpl) participantService).setParticipantFactory(participantFactory);
		when(participantDao.isSsnUnique(anyString())).thenReturn(true);
	}

	@Test
	public void testForSuccessfulParticipantCreation() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetails createdParticipantDto = response.getParticipantDto();
		assertEquals(participantDto.getFirstName(), createdParticipantDto.getFirstName());
		assertEquals(participantDto.getActivityStatus(), createdParticipantDto.getActivityStatus());
		assertEquals(participantDto.getGender(), createdParticipantDto.getGender());

	}

	@Test
	public void testParticipantCreationInvalidSSN() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setSsn("22-43-546");

		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("Attribute value is invalid : social security number", response.getMessage());
	}

	@Test
	public void testParticipantCreationFutureBirthDate() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		participantDto.setBirthDate(tomorrow);
		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.CONSTRAINT_VIOLATION.message()+" : birth date", response.getMessage());
	}
	
	@Test
	public void testParticipantCreationPastDeathDate() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterday = calendar.getTime();
		participantDto.setDeathDate(yesterday);
		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.CONSTRAINT_VIOLATION.message()+" : death date", response.getMessage());
	}
	
	@Test
	public void testParticipantCreationInvalidDeathDate() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		participantDto.setDeathDate(tomorrow);
		participantDto.setVitalStatus("Alive");
		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.CONSTRAINT_VIOLATION.message()+" : death date", response.getMessage());
	}
	
	@Test
	public void testParticipantCreationInvalidSite() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		when(siteDao.getSite(anyString())).thenReturn(null);
		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message()+" : site", response.getMessage());
	}
	
	@Test
	public void testParticipantCreationEmptyMrn() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setMrns(getMrnWithEmptyNumber());
		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.INVALID_ATTR_VALUE.message()+" : medical record number", response.getMessage());
	}


	@Test
	public void testParticipantCreationInvalidGender() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setGender("mal");

		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("Attribute value is invalid : gender", response.getMessage());
	}

	@Test
	public void testParticipantCreationInvalidRace() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		Set<String> race = new HashSet<String>();
		race.add("melanin");
		participantDto.setRace(race);

		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("Attribute value is invalid : race", response.getMessage());
	}

	@Test
	public void testParticipantCreationDuplicateSsn() {

		when(participantDao.isSsnUnique(anyString())).thenReturn(false);
		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();

		reqEvent.setParticipantDetails(participantDto);

		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals(ParticipantErrorCode.DUPLICATE_SSN.message() + " : social security number", response.getMessage());
	}

	@Test
	public void testParticipantCreationWithServerErr() {

		CreateParticipantEvent reqEvent = new CreateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();

		reqEvent.setParticipantDetails(participantDto);
		doThrow(new RuntimeException()).when(participantDao).saveOrUpdate(any(Participant.class));
		ParticipantCreatedEvent response = participantService.createParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testForSuccessfulParticipantUpdate() {

		when(participantDao.isSsnUnique(anyString())).thenReturn(true);
		Participant participant = getParticipant(1L);
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		pmi.setMedicalRecordNumber("234r5");
		pmi.setSite(getSite("some test site"));
		pmi.setParticipant(participant);
		participant.getPmiCollection().put(pmi.getSite().getName(), pmi);
		when(participantDao.getParticipant(anyLong())).thenReturn(participant);
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setId(1L);
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		reqEvent.setParticipantDto(participantDto);

		ParticipantUpdatedEvent response = participantService.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetails createdParticipant = response.getParticipantDto();
		assertEquals(participantDto.getFirstName(), createdParticipant.getFirstName());
		assertNotNull(createdParticipant.getMrns());
		assertEquals(1, createdParticipant.getMrns().size());
	}

	@Test
	public void testParticipantUpdateInvalidSsn() {

		Participant participant = getParticipant(1L);
		when(participantDao.getParticipant(anyLong())).thenReturn(participant);
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setId(1L);
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		participantDto.setSsn("222-3-333");
		reqEvent.setParticipantDto(participantDto);

		ParticipantUpdatedEvent response = participantService.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.BAD_REQUEST, response.getStatus());
		assertEquals("Attribute value is invalid : social security number", response.getMessage());
	}

	@Test
	public void testParticipantUpdateNewSsn() {

		Participant participant = getParticipant(1L);
		String newSsn = "111-21-4315";
		participant.setSocialSecurityNumber(newSsn);
		when(participantDao.getParticipant(anyLong())).thenReturn(participant);
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setId(1L);
		participantDto.setActivityStatus("Active");
		participantDto.setFirstName("firstName");
		participantDto.setLastName("lastName");
		participantDto.setGender("male");
		participantDto.setSsn("222-32-1333");
		reqEvent.setParticipantDto(participantDto);

		ParticipantUpdatedEvent response = participantService.updateParticipant(reqEvent);
		System.out.println(response.getMessage());
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetails updatedResult = response.getParticipantDto();
		assertNotSame(newSsn, updatedResult.getSsn());
	}

	@Test
	public void testParticipantUpdateInvaliParticipant() {

		when(participantDao.getParticipant(anyLong())).thenReturn(null);
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setId(1l);
		reqEvent.setParticipantDto(participantDto);

		ParticipantUpdatedEvent response = participantService.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.NOT_FOUND, response.getStatus());
	}

	@Test
	public void testParticipantUpdateWithServerErr() {

		when(participantDao.getParticipant(anyLong())).thenReturn(getParticipant(1l));
		UpdateParticipantEvent reqEvent = new UpdateParticipantEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		ParticipantDetails participantDto = getParticipantDetails();
		participantDto.setId(1l);
		reqEvent.setParticipantDto(participantDto);
		doThrow(new RuntimeException()).when(participantDao).saveOrUpdate(any(Participant.class));
		ParticipantUpdatedEvent response = participantService.updateParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	public void testGetParticipant() {
		Long participantId = 1L;
		Participant participant = getParticipant(participantId);
		when(participantDao.getParticipant(anyLong())).thenReturn(participant);

		ReqParticipantDetailEvent reqEvent = new ReqParticipantDetailEvent();
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setParticipantId(participantId);

		ParticipantDetailsEvent response = participantService.getParticipant(reqEvent);
		assertNotNull("response cannot be null", response);
		System.out.println(response.getMessage());
		assertEquals(EventStatus.OK, response.getStatus());
		ParticipantDetails retrievedParticipant = response.getParticipantDetails();
		assertEquals(participantId, retrievedParticipant.getId());

	}

	private SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	private Participant getParticipant(Long id) {
		Participant participant = new Participant();
		participant.setId(id);
		participant.setFirstName("firstName1");
		participant.setLastName("lastName1");
		return participant;
	}

	private ParticipantDetails getParticipantDetails() {
		ParticipantDetails details = new ParticipantDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setGender("male");
		details.setBirthDate(new Date());
		details.setSsn("123-34-3654");
		details.setVitalStatus("Death");
		
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		details.setDeathDate(tomorrow);
		
		details.setEthnicity("Not Reported");
		Set<String> race = new HashSet<String>();
		race.add("Asian");
		details.setRace(race);
		MedicalRecordNumberDetail medicalRecordNumberDetail = new MedicalRecordNumberDetail();
		medicalRecordNumberDetail.setMrn("234");
		medicalRecordNumberDetail.setSiteName("siteName");
		List<MedicalRecordNumberDetail> mrns = new ArrayList<MedicalRecordNumberDetail>();
		mrns.add(medicalRecordNumberDetail);
		details.setMrns(mrns);
		return details;
	}

	private Site getSite(String string) {
		Site site = new Site();
		site.setName(string);
		return site;
	}
	
	private List<MedicalRecordNumberDetail> getMrnWithEmptyNumber() {
		MedicalRecordNumberDetail medicalRecordNumberDetail = new MedicalRecordNumberDetail();
		medicalRecordNumberDetail.setMrn("");
		medicalRecordNumberDetail.setSiteName("siteName");
		List<MedicalRecordNumberDetail> mrns = new ArrayList<MedicalRecordNumberDetail>();
		mrns.add(medicalRecordNumberDetail);
		return mrns;
	}

}
