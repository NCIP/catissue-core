package com.krishagni.core.tests;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TableViewer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CprTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class CollectionProtocolRegistrationTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private CollectionProtocolRegistrationService cprSvc;

	@Autowired
	private ApplicationContext ctx;
	
	private final String PPID = "participant protocol identifier";
	
	private final String PARTICIPANT = "participant";
	
	private final String COLLECTION_PROTOCOL = "collection protocol";
	
	private final String BARCODE = "barcode";
	
	private final String SSN = "social security number";
	
	private final String DEATH_DATE = "death date";
	
	private final String REGISTRATION_DATE = "registration date";
	
	private final String BIRTH_DATE = "birth date";

	private final String SEX_GENOTYPE = "sexGenotype";
	
	private final String RACE = "race";
	
	private final String GENDER = "gender";
	
	private final String ETHNICITY = "ethnicity";
	
	private static final String CLINICAL_DIAGNOSIS = "clinical diagnosis";

	private static final String CLINICAL_STATUS = "clinical status";

	private static final String CPE = "No event identification";
	
	private static final String CPL = "collection point label";

	private static final String CPR = "No Collection Protocol Registration Identification Specified";
	
	private static final String CP_TITLE = "collection protocol title";
	
	private static final String SITE = "site name";

	private static final String CPR_CPE = "registraion and event point refering to different protocols.";

	private static final String SCG_REPORTS = "scg reports";
	
	
	@After
	public void after() {
		TableViewer.logTables(ctx, "RBAC_ROLES", "RBAC_RESOURCES", "RBAC_OPERATIONS", "RBAC_PERMISSIONS", 
				"RBAC_ROLE_ACCESS_LIST", "RBAC_ROLE_ACCESS_LIST_OPS", "RBAC_SUBJECT_ROLES");
		
	}
	
	/*
	 * Register Participant API Tests
	 */
	
	@Test
	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/register-participant-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipant() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolRegistrationDetail detail = resp.getCprDetail();
		Assert.assertEquals(cpr.getCpId(), detail.getCpId());
		assertRegistrationCreatedEventDetail(detail, cpr);
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void registerParticipantWithNullParticipant() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setParticipant(null);
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error code participant not found!",
				true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.INVALID_ATTR_VALUE, PARTICIPANT));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/register-participant-to-same-cp-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void registerParticipantToSameCp() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.getParticipant().setId(1L);
		cpr.setCpId(1L);
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error code participant already registered not found!",
				true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.ALREADY_REGISTERED, PARTICIPANT));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/create-and-register-participant-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/create-and-register-participant-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createAndRegisterParticipant() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipantWithPmi();
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolRegistrationDetail detail = resp.getCprDetail();
		Assert.assertEquals(cpr.getCpId(), detail.getCpId());
		assertRegistrationCreatedEventDetail(detail, cpr);
		
		ParticipantDetail participant = detail.getParticipant();
		Assert.assertEquals(true, (new Long(0) < participant.getId()));
		Assert.assertEquals("default_first_name", participant.getFirstName());
		Assert.assertEquals("default_last_name", participant.getLastName());
		Assert.assertEquals("default_middle_name", participant.getMiddleName());
		Assert.assertEquals(CprTestData.getDate(21, 10, 2012), participant.getBirthDate());
		Assert.assertEquals("MALE", participant.getGender());
		Assert.assertEquals((int)1, participant.getRace().size());
		Assert.assertEquals("Asian", participant.getRace().iterator().next());
		Assert.assertEquals("Alive", participant.getVitalStatus());
		Assert.assertEquals("XX", participant.getSexGenotype());
		Assert.assertEquals("333-22-4444", participant.getSsn());
		Assert.assertEquals("Canadian", participant.getEthnicity());
		Assert.assertEquals("default-empi-id", participant.getEmpi());
		Assert.assertEquals((int)2, participant.getPmis().size());
		
		for (ParticipantMedicalIdentifierNumberDetail pmi : participant.getPmis()) {
			if (pmi.getMrn().equals("PMI1")) {
				Assert.assertEquals("SITE1", pmi.getSiteName());		
			} else if (pmi.getMrn().equals("PMI2")) {
				Assert.assertEquals("SITE2", pmi.getSiteName());		
			} else {
				Assert.fail("The pmi: " + pmi.getMrn() + " was not expected in the testcase!");
			}
		}		
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantDuplicatePPID() {		
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setPpid("duplicate-ppid");
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.DUPLICATE_PPID, PPID));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantInvalidParticipantId() {		
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.getParticipant().setId(-1L);
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.INVALID_ATTR_VALUE, PARTICIPANT));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantInvalidCpid() {		
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setCpId(-1L);
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/register-participant-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED) 
	public void registerParticipantWithCpTitle() {		
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setCpId(null);
		cpr.setCpTitle("default-cp");
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolRegistrationDetail detail = resp.getCprDetail();
		Assert.assertEquals(new Long(1), detail.getCpId());
		assertRegistrationCreatedEventDetail(detail, cpr);
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void registerParticipantWithNullCpIdAndTitle() {		
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setCpId(null);
		cpr.setCpTitle(null);
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.MISSING_ATTR_VALUE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantEmptyPPID() {		
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setPpid(null);

		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.MISSING_ATTR_VALUE, PPID));
	}

	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createAndRegisterParticipantDuplicateSSN() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipant();
		cpr.getParticipant().setSsn("123-12-1234");
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.DUPLICATE_SSN, SSN));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createAndRegisterParticipantInvalidSSNFormat() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipant();
		cpr.getParticipant().setSsn("1234-ab-1234");
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.INVALID_ATTR_VALUE, SSN));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/create-and-register-dead-participant-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/create-and-register-dead-participant-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createAndRegisterDeadParticipant() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipantWithPmi();
		cpr.getParticipant().setVitalStatus("Death");
		cpr.getParticipant().setDeathDate(CprTestData.getDate(21, 10, 2013));
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolRegistrationDetail detail = resp.getCprDetail();
		Assert.assertEquals(cpr.getCpId(), detail.getCpId());
		assertRegistrationCreatedEventDetail(detail, cpr);

		ParticipantDetail participant = detail.getParticipant();
		Assert.assertEquals(true, (new Long(0) < participant.getId()));
		Assert.assertEquals("default_first_name", participant.getFirstName());
		Assert.assertEquals("default_last_name", participant.getLastName());
		Assert.assertEquals("default_middle_name", participant.getMiddleName());
		Assert.assertEquals(CprTestData.getDate(21, 10, 2012), participant.getBirthDate());
		Assert.assertEquals(CprTestData.getDate(21, 10, 2013), participant.getDeathDate());
		Assert.assertEquals("MALE", participant.getGender());
		Assert.assertEquals((int)1, participant.getRace().size());
		Assert.assertEquals("Asian", participant.getRace().iterator().next());
		Assert.assertEquals("Death", participant.getVitalStatus());
		Assert.assertEquals("XX", participant.getSexGenotype());
		Assert.assertEquals("333-22-4444", participant.getSsn());
		Assert.assertEquals("Canadian", participant.getEthnicity());
		Assert.assertEquals("default-empi-id", participant.getEmpi());
		Assert.assertEquals((int)2, participant.getPmis().size());
		
		for (ParticipantMedicalIdentifierNumberDetail pmi : participant.getPmis()) {
			if (pmi.getMrn().equals("PMI1")) {
				Assert.assertEquals("SITE1", pmi.getSiteName());		
			} else if (pmi.getMrn().equals("PMI2")) {
				Assert.assertEquals("SITE2", pmi.getSiteName());		
			} else {
				Assert.fail("The pmi: " + pmi.getMrn() + " was not expected in the testcase!");
			}
		}	
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createAndRegisterDeadParticipantDeathDatePriorToBirthDate() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipant();
		cpr.getParticipant().setBirthDate(CprTestData.getDate(21, 2, 2002));
		cpr.getParticipant().setDeathDate(CprTestData.getDate(21, 2, 1987));
		cpr.getParticipant().setVitalStatus("Death");
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.CONSTRAINT_VIOLATION, DEATH_DATE));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantWithNoRegistrationDate() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setRegistrationDate(null);
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.MISSING_ATTR_VALUE, REGISTRATION_DATE));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/register-participant-to-disabled-cp-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/register-participant-to-disabled-cp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantToDisabledCp() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantWithInvalidBirthDate() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipant();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 100);
		cpr.getParticipant().setBirthDate(cal.getTime());
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.CONSTRAINT_VIOLATION, BIRTH_DATE));
	}
	
	//TODO: fix this when pvManager is back in place
	////@Test
	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void registerParticipantWithInvalidParticipantDetails() {
		String string = "asdf-invalid";
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipant();
		cpr.getParticipant().getRace().clear();
		cpr.getParticipant().getRace().add(string);
		cpr.getParticipant().setGender(string);
		cpr.getParticipant().setEthnicity(string);
		cpr.getParticipant().setSexGenotype(string);
		
		char[] bytes = new char[500];
		Arrays.fill(bytes, 'a');
		cpr.getParticipant().setFirstName(new String(bytes));
		
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.CONSTRAINT_VIOLATION, RACE));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.CONSTRAINT_VIOLATION, GENDER));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.CONSTRAINT_VIOLATION, SEX_GENOTYPE));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.CONSTRAINT_VIOLATION, ETHNICITY));
	}
	
	public void assertRegistrationCreatedEventDetail (CollectionProtocolRegistrationDetail detail, CollectionProtocolRegistrationDetail cpr) {
		Assert.assertNotNull("Error: Detail found null", detail);
		Assert.assertNotNull("Error: Participants found null", detail.getParticipant());
		Assert.assertEquals("Error: Invalid id", true, detail.getId() > new Long(0));
		Assert.assertEquals("Error: Ppid mismatch" ,cpr.getPpid(), detail.getPpid());
		Assert.assertEquals("Error: CpTitle mismatch", "default-cp", detail.getCpTitle());
		Assert.assertNotNull("Error: Consent details found null", detail.getConsentDetails());
		Assert.assertNotNull("Error: Consent tier response found null", detail.getConsentDetails().getConsentTierResponses());
		Assert.assertEquals("Error: Consent tier response count mismatch", (int)3, detail.getConsentDetails().getConsentTierResponses().size());
		
		for (ConsentTierResponseDetail statement : detail.getConsentDetails().getConsentTierResponses()) {
			String consent = statement.getConsentStatment();
			String response = statement.getParticipantResponse();
			
			if ("CONSENT1".equals(consent)) {
				Assert.assertEquals("yes", response);
			} else if ("CONSENT2".equals(consent) ) {
				Assert.assertEquals("no", response);
			} else if ("CONSENT3".equals(consent)) {
				Assert.assertEquals("may be", response);
			} else {
				Assert.fail("The consent: " + consent + " was not expected in this test");
			}
		}
	}
	
	
	/*
	 * Get Registraiton API Tests
	 */
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistration() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCprId(1L);
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccess());
		
		AssertCprDetail(resp);
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationByPpid() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCpId(1L);
		req.setPpid("default-gen-ppid-1");
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccess());
		
		AssertCprDetail(resp);
	}
	
	public void AssertCprDetail(RegistrationEvent resp) {
		CollectionProtocolRegistrationDetail cpr = resp.getCpr();
		Assert.assertNotNull("Error: Collection protocol registration detail found null", cpr);
		Assert.assertNotNull("Error: Consent details found null", cpr.getConsentDetails());
		Assert.assertNotNull("Error: Participants found null", cpr.getParticipant());
		Assert.assertNotNull("Error: Race found null", cpr.getParticipant().getRace());
		
		Assert.assertEquals("Error: CprId mismatch", new Long(1), cpr.getId());
		Assert.assertEquals("Error: CpId mismatch", new Long(1), cpr.getCpId());
		Assert.assertEquals("Error: Cp title mismatch", "default-cp", cpr.getCpTitle());
		Assert.assertEquals("Error: Ppid mismatch", "default-gen-ppid-1", cpr.getPpid());
		Assert.assertEquals("Error: Comsent document url mismatch", "www.exampleurl.com", cpr.getConsentDetails().getConsentDocumentUrl());
		Assert.assertEquals("Error: Consent tier response count mismatch", (int)3, cpr.getConsentDetails().getConsentTierResponses().size());
		Assert.assertEquals("Error: Witness name mismatch", "admin@admin.com", cpr.getConsentDetails().getWitnessName());
		Assert.assertEquals("Error: Registration date mismatch", CprTestData.getDate(31,1,2001), cpr.getRegistrationDate());
		Assert.assertEquals("Error: Activity status mismatch", "Active", cpr.getActivityStatus());
		
		for (ConsentTierResponseDetail statement : cpr.getConsentDetails().getConsentTierResponses()) {
			String consent = statement.getConsentStatment();
			String response = statement.getParticipantResponse();
			
			if ("CONSENT1".equals(consent)) {
				Assert.assertEquals("yes", response);
			} else if ("CONSENT2".equals(consent) ) {
				Assert.assertEquals("no", response);
			} else if ("CONSENT3".equals(consent)) {
				Assert.assertEquals("may be", response);
			} else {
				Assert.fail("The consent: " + consent + " was not expected in this test");
			}
		}
		
		ParticipantDetail participant = resp.getCpr().getParticipant();		
		Assert.assertEquals("Error: Participant's last name mismatch", "Falero", participant.getLastName());
		Assert.assertEquals("Error: Participant's first name mismatch", "Guava", participant.getFirstName());
		Assert.assertEquals("Error: Participant's race count mismatch", (int)1, participant.getRace().size());
		Assert.assertEquals("Asian", participant.getRace().iterator().next());
		Assert.assertEquals("Error: Birthdate mismatch", CprTestData.getDate(21,10,2012), participant.getBirthDate());
		Assert.assertEquals("Error: Participant's activity status mismatch", "Active" , participant.getActivityStatus());
	}
	
	@Test
	public void getNonExistingRegistration() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCprId(1L);
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCprId(), resp.getCprId());
		Assert.assertNull(req.getCpId());
		Assert.assertNull(req.getPpid());
	}
	
	@Test
	public void getNonExistingRegistrationByPpid() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCpId(1L);
		req.setPpid("default-gen-ppid-1");
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
		Assert.assertEquals(req.getPpid(), resp.getPpid());
		Assert.assertNull(req.getCprId());
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-disabled-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getDisabledRegistration() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCprId(1L);
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCprId(), resp.getCprId());
		Assert.assertNull(req.getCpId());
		Assert.assertNull(req.getPpid());
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-disabled-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getDisabledRegistrationByPpid() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCpId(1L);
		req.setPpid("default-gen-ppid-1");
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull(resp.getCpr());
		Assert.assertEquals(new Long(1) , resp.getCpr().getId());
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationForNonExistingPpid() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCpId(1L);
		req.setPpid("non-existing-ppid");
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
		Assert.assertEquals(req.getPpid(), resp.getPpid());
		Assert.assertNull(req.getCprId());
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationForNonExistingCpid() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCpId(-1L);
		req.setPpid("default-gen-ppid-1");
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
		Assert.assertEquals(req.getPpid(), resp.getPpid());
		Assert.assertNull(req.getCprId());
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-cpid-and-ppid-mismatch-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationCpidAndPpidMismatch() {
		/*
		 * ppid and cpid both are valid but they belong to different cp's
		 */
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCpId(2L);
		req.setPpid("default-gen-ppid-1");
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
		Assert.assertEquals(req.getPpid(), resp.getPpid());
		Assert.assertNull(req.getCprId());
	}
}
