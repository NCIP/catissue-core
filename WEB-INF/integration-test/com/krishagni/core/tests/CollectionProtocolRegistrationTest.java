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
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.events.VisitsEvent;
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

	private static final String VISIT_STATUS = "Collection Status";

	private static final String CPE = "No event identification";
	
	private static final String CPL = "collection point label";

	private static final String CPR = "No Collection Protocol Registration Identification Specified";
	
	private static final String CP_TITLE = "collection protocol title";
	
	private static final String SITE = "site name";

	private static final String CPR_CPE = "registraion and event point refering to different protocols.";

	private static final String SCG_REPORTS = "scg reports";
	
	private static final String VISIT_DATE = "visit date";
	
	@After
	public void after() {
		TableViewer.logTables(ctx, "RBAC_ROLES", "RBAC_RESOURCES", "RBAC_OPERATIONS", "RBAC_PERMISSIONS", 
				"RBAC_ROLE_ACCESS_LIST", "RBAC_ROLE_ACCESS_LIST_OPS", "RBAC_SUBJECT_ROLES");
		
	}
	
	/*
	 * Register Participant API Tests
	 */
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.registerParticipant.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.registerParticipant.expected.xml", 
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
		Assert.assertNotNull(detail);
		Assert.assertNotNull(detail.getParticipant());
		Assert.assertEquals(true, resp.getCprDetail().getId() > new Long(0));
		Assert.assertEquals(cpr.getPpid(), detail.getPpid());
		Assert.assertEquals(cpr.getCpId(), detail.getCpId());
		Assert.assertEquals("default-cp", detail.getCpTitle());
		Assert.assertNotNull(detail.getConsentDetails());
		Assert.assertNotNull(detail.getConsentDetails().getConsentTierResponses());
		Assert.assertEquals((int)3, detail.getConsentDetails().getConsentTierResponses().size());
		
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
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.registerParticipantToSameCp.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.createAndRegisterParticipant.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.createAndRegisterParticipant.expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createAndRegisterParticipant() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipantWithPmi();
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		
		Assert.assertNotNull(resp);
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolRegistrationDetail detail = resp.getCprDetail();
		Assert.assertNotNull(detail);
		Assert.assertNotNull(detail.getParticipant());
		Assert.assertEquals(true, resp.getCprDetail().getId() > new Long(0));
		Assert.assertEquals(cpr.getPpid(), detail.getPpid());
		Assert.assertEquals(cpr.getCpId(), detail.getCpId());
		Assert.assertEquals("default-cp", detail.getCpTitle());
		Assert.assertNotNull(detail.getConsentDetails());
		Assert.assertNotNull(detail.getConsentDetails().getConsentTierResponses());
		Assert.assertEquals((int)3, detail.getConsentDetails().getConsentTierResponses().size());
		
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.createAndRegisterDeadParticipant.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.createAndRegisterDeadParticipant.expected.xml", 
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
		
		Assert.assertNotNull(resp);
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolRegistrationDetail detail = resp.getCprDetail();
		Assert.assertNotNull(detail);
		Assert.assertNotNull(detail.getParticipant());
		Assert.assertEquals(true, resp.getCprDetail().getId() > new Long(0));
		Assert.assertEquals(cpr.getPpid(), detail.getPpid());
		Assert.assertEquals(cpr.getCpId(), detail.getCpId());
		Assert.assertEquals("default-cp", detail.getCpTitle());
		Assert.assertNotNull(detail.getConsentDetails());
		Assert.assertNotNull(detail.getConsentDetails().getConsentTierResponses());
		Assert.assertEquals((int)3, detail.getConsentDetails().getConsentTierResponses().size());
		
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.registerParticipantToDisabledCp.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.registerParticipantToDisabledCp.expected.xml", 
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	//@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.generic.expected.xml", 
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
	
	/*
	 * Get Visits API Tests
	 */
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.getVisits.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void getVisits() {
		ReqVisitsEvent req = CprTestData.getReqVisitsEvent();
		VisitsEvent resp = cprSvc.getVisits(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals("Mismatch in Visist!", (int)6, resp.getVisits().size());
		
		for (VisitSummary visit : resp.getVisits()) {
			Long visitId = visit.getId();
			Assert.assertNotNull(visitId);
			Assert.assertEquals(visitId, visit.getEventId());
			Assert.assertEquals(new String("scg-"+ visitId), visit.getName());
			Assert.assertEquals(new String("Visit"+ visitId), visit.getLabel());
			Assert.assertEquals((int) (visit.getEventId().intValue() * 10) , visit.getCalendarPoint());
			Assert.assertEquals("Complete", visit.getStatus());
			
			Date registrationDate = CprTestData.getDate(31,1,2001);
			Calendar cal = Calendar.getInstance();
			cal.setTime(registrationDate);
			cal.add(Calendar.DAY_OF_YEAR, visit.getCalendarPoint());
			Assert.assertEquals(cal.getTime(), visit.getAnticipatedVisitDate());
			
			if (visitId < 5L) {
				assertPlannedVisitsStats(visit);
			} else {
				assertUnplannedVisitsStats(visit);
			}
		}
	}
	
	private void assertPlannedVisitsStats(VisitSummary visit) {
		Assert.assertEquals((int)1, visit.getAnticipatedSpecimens());
		Assert.assertEquals((int)1, visit.getCollectedSpecimens());
		Assert.assertEquals((int)0, visit.getUncollectedSpecimens());
		
		Assert.assertEquals((int)0, visit.getUnplannedSpecimens());
	}
	
	private void assertUnplannedVisitsStats(VisitSummary visit) {
		Assert.assertEquals((int)0, visit.getAnticipatedSpecimens());
		Assert.assertEquals((int)0, visit.getCollectedSpecimens());
		Assert.assertEquals((int)0, visit.getUncollectedSpecimens());
		
		Assert.assertEquals((int)1, visit.getUnplannedSpecimens());
	}
	
	@Test
	public void getVisitsForNonExistingCpr() {
		ReqVisitsEvent req = CprTestData.getReqVisitsEvent();
		VisitsEvent resp = cprSvc.getVisits(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(0), new Integer(resp.getVisits().size()));
	}
	
	@Test 
	@DatabaseSetup("CollectionProtocolRegistrationTest.getVisitsForInactiveCpr.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void getVisitsForInactiveCpr() {
		ReqVisitsEvent req = CprTestData.getReqVisitsEvent();
		VisitsEvent resp = cprSvc.getVisits(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(0), new Integer(resp.getVisits().size()));
	}
	
	//TODO: Check with VP whether disabled to be shown 
	//@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.getVisitsSomeVisitsDisabled.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void getVisitsSomeVisitsDisabled() {
		ReqVisitsEvent req = CprTestData.getReqVisitsEvent();
		VisitsEvent resp = cprSvc.getVisits(req);
		req.setIncludeStats(false);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals("Mismatch in Visist!", (int)6, resp.getVisits().size());
		
		for (VisitSummary visit : resp.getVisits()) {
			Long visitId = visit.getId();
			Assert.assertNotNull(visitId);
			Assert.assertEquals(visitId, visit.getEventId());
			Assert.assertEquals(new String("scg-"+ visitId), visit.getName());
			Assert.assertEquals(new String("Visit"+ visitId), visit.getLabel());
			Assert.assertEquals((int) (visit.getEventId().intValue() * 10) , visit.getCalendarPoint());
			Assert.assertEquals("Complete", visit.getStatus());
			
			Date registrationDate = CprTestData.getDate(31,1,2001);
			Calendar cal = Calendar.getInstance();
			cal.setTime(registrationDate);
			cal.add(Calendar.DAY_OF_YEAR, visit.getCalendarPoint());
			Assert.assertEquals(cal.getTime(), visit.getAnticipatedVisitDate());
		}
	}
	
	/*
	 * Get Registraiton API Tests
	 */
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.getRegistration.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void getRegistration() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCprId(1L);
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccess());
		CollectionProtocolRegistrationDetail cpr = resp.getCpr();
		Assert.assertNotNull(cpr);
		Assert.assertNotNull(cpr.getConsentDetails());
		Assert.assertNotNull(cpr.getParticipant());
		Assert.assertNotNull(cpr.getParticipant().getRace());
		
		Assert.assertEquals(new Long(1), cpr.getId());
		Assert.assertEquals(new Long(1), cpr.getCpId());
		Assert.assertEquals("default-cp", cpr.getCpTitle());
		Assert.assertEquals("default-gen-ppid-1", cpr.getPpid());
		Assert.assertEquals("www.exampleurl.com", cpr.getConsentDetails().getConsentDocumentUrl());
		Assert.assertEquals((int)3, cpr.getConsentDetails().getConsentTierResponses().size());
		Assert.assertEquals("admin@admin.com", cpr.getConsentDetails().getWitnessName());
		Assert.assertEquals(CprTestData.getDate(31,1,2001), cpr.getRegistrationDate());
		Assert.assertEquals("Active", cpr.getActivityStatus());
		
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
		Assert.assertEquals("Falero", participant.getLastName());
		Assert.assertEquals("Guava", participant.getFirstName());
		Assert.assertEquals((int)1, participant.getRace().size());
		Assert.assertEquals("Asian", participant.getRace().iterator().next());
		Assert.assertEquals(CprTestData.getDate(21,10,2012), participant.getBirthDate());
		Assert.assertEquals("Active" , participant.getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.getRegistration.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void getRegistrationByPpid() {
		ReqRegistrationEvent req = CprTestData.getReqRegistrationEvent();
		req.setCpId(1L);
		req.setPpid("default-gen-ppid-1");
		
		RegistrationEvent resp = cprSvc.getRegistration(req);
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccess());
		CollectionProtocolRegistrationDetail cpr = resp.getCpr();
		Assert.assertNotNull(cpr);
		Assert.assertNotNull(cpr.getConsentDetails());
		Assert.assertNotNull(cpr.getParticipant());
		Assert.assertNotNull(cpr.getParticipant().getRace());
		
		Assert.assertEquals(new Long(1), cpr.getId());
		Assert.assertEquals(new Long(1), cpr.getCpId());
		Assert.assertEquals("default-cp", cpr.getCpTitle());
		Assert.assertEquals("default-gen-ppid-1", cpr.getPpid());
		Assert.assertEquals("www.exampleurl.com", cpr.getConsentDetails().getConsentDocumentUrl());
		Assert.assertEquals((int)3, cpr.getConsentDetails().getConsentTierResponses().size());
		Assert.assertEquals("admin@admin.com", cpr.getConsentDetails().getWitnessName());
		Assert.assertEquals(CprTestData.getDate(31,1,2001), cpr.getRegistrationDate());
		Assert.assertEquals("Active", cpr.getActivityStatus());
		
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
		Assert.assertEquals("Falero", participant.getLastName());
		Assert.assertEquals("Guava", participant.getFirstName());
		Assert.assertEquals((int)1, participant.getRace().size());
		Assert.assertEquals("Asian", participant.getRace().iterator().next());
		Assert.assertEquals(CprTestData.getDate(21,10,2012), participant.getBirthDate());
		Assert.assertEquals("Active" , participant.getActivityStatus());
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.getDisabledRegistration.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.getDisabledRegistration.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.getRegistration.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.getRegistration.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
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
	@DatabaseSetup("CollectionProtocolRegistrationTest.getRegistrationCpidAndPpidMismatch.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
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
	
	/*
	 * Add Visit API Tests
	 */
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.addVisitsTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.addVisitsTest.expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addVisitsTest() {
		AddVisitEvent req = CprTestData.getAddVisitEvent();
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertNotNull(resp.getVisit());
		Assert.assertEquals((Long)1L, resp.getVisit().getCpeId());
		Assert.assertEquals((Long)1L, resp.getVisit().getCprId());
		Assert.assertEquals("test-pathology", resp.getVisit().getSurgicalPathologyNumber());
		Assert.assertEquals("test-daiagnosis", resp.getVisit().getClinicalDiagnosis());
		Assert.assertEquals("Completed", resp.getVisit().getVisitStatus());
		Assert.assertEquals("SITE1", resp.getVisit().getVisitSite());
		Assert.assertEquals("Active", resp.getVisit().getActivityStatus());
		Assert.assertEquals("test-status", resp.getVisit().getClinicalStatus());
	}
	
	/*
	 * This test Checks for the error: Cpr not found, Cpe not found, Site Not Found
	 */
	@Test
	public void addVisitsMissingCprCpeSite() {
		AddVisitEvent req = CprTestData.getAddVisitEvent();
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, CPE));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, CPR));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, SITE));
		
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.addVisitsTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void addVisitsVisitDatePriorToRegistrationDate() {
		AddVisitEvent req = CprTestData.getAddVisitEvent();
		req.getVisit().setVisitDate(CprTestData.getDate(11, 6, 2000));
		
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_VISIT_DATE, VISIT_DATE));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.addVisitsInvalidCprCpe.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void addVisitsInvalidCprCpe() {
		AddVisitEvent req = CprTestData.getAddVisitEvent();
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_CPR_CPE, CPR_CPE));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.addVisitsTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void addVisitsMissingSiteName() {
		AddVisitEvent req = CprTestData.getAddVisitEvent();
		req.getVisit().setVisitSite(null);
		req.getVisit().setCprId(null);
		req.getVisit().setCpTitle("invalid-serach-term");
		req.getVisit().setPpid("invalid-search-term");
		
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Missing site name error was not found!", 
				true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.MISSING_ATTR_VALUE, "site name"));
		Assert.assertEquals("Invalid cp-title error was expected, but not found!", 
				true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "collection protocol title"));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.addVisitsTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.generic.teardown.xml")
	public void addVisitsTestInvalidPpid() {
		AddVisitEvent req = CprTestData.getAddVisitEvent();
		req.getVisit().setCprId(null);
		req.getVisit().setCpeId(null);
		
		req.getVisit().setCpTitle("default-cp");
		req.getVisit().setPpid("invalid-ppid");
		req.getVisit().setEventLabel("invalid-cpl");
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "participant protocol id"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, CPL));
	}
}

