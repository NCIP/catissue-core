package com.krishagni.core.tests;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
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
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	//TODO: add update registration test case!!
	/*
	 * Register Participant API Tests
	 */
	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/register-participant-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipant() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		TestUtils.recordResponse(resp);
//		
//		Assert.assertNotNull(resp);
//		Assert.assertEquals(true, resp.isSuccessful());
//		
//		CollectionProtocolRegistrationDetail detail = resp.getPayload();
//		Assert.assertEquals(input.getCpId(), detail.getCpId());
//		assertRegistrationCreatedEventDetail(detail, input);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	public void registerParticipantWithNullParticipant() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.setParticipant(null);
//
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CprErrorCode.PARTICIPANT_DETAIL_REQUIRED, ErrorType.USER_ERROR);
//	}
//
//	@Test
//	@DatabaseSetup("cp-test/registration-test/register-participant-to-same-cp-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	public void registerParticipantToSameCp() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.getParticipant().setId(1L);
//		input.setCpId(1L);
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CprErrorCode.DUP_REGISTRATION, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/create-and-register-participant-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/create-and-register-participant-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void createAndRegisterParticipant() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetailForCreateAndRegisterParticipantWithPmi();
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		TestUtils.recordResponse(resp);
//		
//		Assert.assertNotNull(resp);
//		Assert.assertEquals(true, resp.isSuccessful());
//		
//		CollectionProtocolRegistrationDetail detail = resp.getPayload();
//		Assert.assertEquals(input.getCpId(), detail.getCpId());
//		assertRegistrationCreatedEventDetail(detail, input);
//		
//		ParticipantDetail participant = detail.getParticipant();
//		Assert.assertEquals(true, (new Long(0) < participant.getId()));
//		Assert.assertEquals("default_first_name", participant.getFirstName());
//		Assert.assertEquals("default_last_name", participant.getLastName());
//		Assert.assertEquals("default_middle_name", participant.getMiddleName());
//		Assert.assertEquals(CommonUtils.getDate(21, 10, 2012), participant.getBirthDate());
//		Assert.assertEquals("MALE", participant.getGender());
//		Assert.assertEquals((int)1, participant.getRaces().size());
//		Assert.assertEquals("Asian", participant.getRaces().iterator().next());
//		Assert.assertEquals("Alive", participant.getVitalStatus());
//		Assert.assertEquals("XX", participant.getSexGenotype());
//		Assert.assertEquals("333-22-4444", participant.getSsn());
//		Assert.assertEquals("Canadian", participant.getEthnicity());
//		Assert.assertEquals("default-empi-id", participant.getEmpi());
//		Assert.assertEquals((int)2, participant.getPmis().size());
//		
//		for (PmiDetail pmi : participant.getPmis()) {
//			if (pmi.getMrn().equals("PMI1")) {
//				Assert.assertEquals("SITE1", pmi.getSiteName());		
//			} else if (pmi.getMrn().equals("PMI2")) {
//				Assert.assertEquals("SITE2", pmi.getSiteName());		
//			} else {
//				Assert.fail("The pmi: " + pmi.getMrn() + " was not expected in the testcase!");
//			}
//		}		
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantDuplicatePPID() {		
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.setPpid("duplicate-ppid");
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CprErrorCode.DUP_PPID, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantInvalidParticipantId() {		
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.getParticipant().setId(-1L);
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantInvalidCpid() {		
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.setCpId(-1L);
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/register-participant-expected.xml",
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED) 
//	public void registerParticipantWithCpTitle() {		
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.setCpId(null);
//		input.setCpTitle("default-cp");
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		TestUtils.recordResponse(resp);
//		
//		Assert.assertNotNull(resp);
//		Assert.assertEquals(true, resp.isSuccessful());
//		
//		CollectionProtocolRegistrationDetail detail = resp.getPayload();
//		Assert.assertEquals(new Long(1), detail.getCpId());
//		assertRegistrationCreatedEventDetail(detail, input);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/register-participant-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	public void registerParticipantWithNullCpIdAndTitle() {		
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.setCpId(null);
//		input.setCpTitle(null);
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		TestUtils.recordResponse(resp);
//		
//		Assert.assertNotNull(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CprErrorCode.CP_REQUIRED, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantEmptyPPID() {		
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.setPpid(null);
//
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CprErrorCode.PPID_REQUIRED, ErrorType.USER_ERROR);
//	}
//
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void createAndRegisterParticipantDuplicateSSN() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetailForCreateAndRegisterParticipant();
//		input.getParticipant().setSsn("123-12-1234");
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.DUP_SSN, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void createAndRegisterParticipantInvalidSSNFormat() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetailForCreateAndRegisterParticipant();
//		input.getParticipant().setSsn("1234-ab-1234");
//
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.INVALID_SSN, ErrorType.USER_ERROR);
//	}
//
//	@Test
//	@DatabaseSetup("cp-test/registration-test/create-and-register-dead-participant-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/create-and-register-dead-participant-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void createAndRegisterDeadParticipant() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetailForCreateAndRegisterParticipantWithPmi();
//		input.getParticipant().setVitalStatus("Death");
//		input.getParticipant().setDeathDate(CommonUtils.getDate(21, 10, 2013));
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		
//		Assert.assertNotNull(resp);
//		Assert.assertEquals(true, resp.isSuccessful());
//		
//		CollectionProtocolRegistrationDetail detail = resp.getPayload();
//		Assert.assertEquals(input.getCpId(), detail.getCpId());
//		assertRegistrationCreatedEventDetail(detail, input);
//
//		ParticipantDetail participant = detail.getParticipant();
//		Assert.assertEquals(true, (new Long(0) < participant.getId()));
//		Assert.assertEquals("default_first_name", participant.getFirstName());
//		Assert.assertEquals("default_last_name", participant.getLastName());
//		Assert.assertEquals("default_middle_name", participant.getMiddleName());
//		Assert.assertEquals(CommonUtils.getDate(21, 10, 2012), participant.getBirthDate());
//		Assert.assertEquals(CommonUtils.getDate(21, 10, 2013), participant.getDeathDate());
//		Assert.assertEquals("MALE", participant.getGender());
//		Assert.assertEquals((int)1, participant.getRaces().size());
//		Assert.assertEquals("Asian", participant.getRaces().iterator().next());
//		Assert.assertEquals("Death", participant.getVitalStatus());
//		Assert.assertEquals("XX", participant.getSexGenotype());
//		Assert.assertEquals("333-22-4444", participant.getSsn());
//		Assert.assertEquals("Canadian", participant.getEthnicity());
//		Assert.assertEquals("default-empi-id", participant.getEmpi());
//		Assert.assertEquals((int)2, participant.getPmis().size());
//		
//		for (PmiDetail pmi : participant.getPmis()) {
//			if (pmi.getMrn().equals("PMI1")) {
//				Assert.assertEquals("SITE1", pmi.getSiteName());		
//			} else if (pmi.getMrn().equals("PMI2")) {
//				Assert.assertEquals("SITE2", pmi.getSiteName());		
//			} else {
//				Assert.fail("The pmi: " + pmi.getMrn() + " was not expected in the testcase!");
//			}
//		}	
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml",
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void createAndRegisterDeadParticipantDeathDatePriorToBirthDate() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetailForCreateAndRegisterParticipant();
//		input.getParticipant().setBirthDate(CommonUtils.getDate(21, 2, 2002));
//		input.getParticipant().setDeathDate(CommonUtils.getDate(21, 2, 1987));
//		input.getParticipant().setVitalStatus("Death");
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.INVALID_DEATH_DATE, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantWithNoRegistrationDate() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//		input.setRegistrationDate(null);
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CprErrorCode.REG_DATE_REQUIRED, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/registration-test/register-participant-to-disabled-cp-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/register-participant-to-disabled-cp-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantToDisabledCp() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetail();
//				
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
//	}
//
//	@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml",
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantWithInvalidBirthDate() {
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetailForCreateAndRegisterParticipant();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date());
//		cal.add(Calendar.DATE, 100);
//		input.getParticipant().setBirthDate(cal.getTime());
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.INVALID_BIRTH_DATE, ErrorType.USER_ERROR);
//	}
//	
//	//TODO: fix this when pvManager is back in place
//	////@Test
//	@DatabaseSetup("cp-test/registration-test/generic-initial.xml")
//	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
//	@ExpectedDatabase(value="cp-test/registration-test/generic-expected.xml", 
//		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
//	public void registerParticipantWithInvalidParticipantDetails() {
//		String string = "asdf-invalid";
//		CollectionProtocolRegistrationDetail input = CprTestData.getCprDetailForCreateAndRegisterParticipant();
//		input.getParticipant().getRaces().clear();
//		input.getParticipant().getRaces().add(string);
//		input.getParticipant().setGender(string);
//		input.getParticipant().setEthnicity(string);
//		input.getParticipant().setSexGenotype(string);
//		
//		char[] bytes = new char[500];
//		Arrays.fill(bytes, 'a');
//		input.getParticipant().setFirstName(new String(bytes));
//		
//		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.createRegistration(getRequest(input));
//		
//		TestUtils.recordResponse(resp);
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.INVALID_RACE, ErrorType.USER_ERROR);
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.INVALID_GENDER, ErrorType.USER_ERROR);
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.INVALID_GENOTYPE, ErrorType.USER_ERROR);
//		TestUtils.checkErrorCode(resp, ParticipantErrorCode.INVALID_ETHNICITY, ErrorType.USER_ERROR);
//	}
//	
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
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCprId(1L);
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		AssertCprDetail(resp.getPayload());
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationByPpid() {
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCpId(1L);
		input.setPpid("default-gen-ppid-1");
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		AssertCprDetail(resp.getPayload());
	}
	
	public void AssertCprDetail(CollectionProtocolRegistrationDetail cpr) {
		Assert.assertNotNull("Error: Collection protocol registration detail found null", cpr);
		Assert.assertNotNull("Error: Consent details found null", cpr.getConsentDetails());
		Assert.assertNotNull("Error: Participants found null", cpr.getParticipant());
		Assert.assertNotNull("Error: Race found null", cpr.getParticipant().getRaces());
		
		Assert.assertEquals("Error: CprId mismatch", new Long(1), cpr.getId());
		Assert.assertEquals("Error: CpId mismatch", new Long(1), cpr.getCpId());
		Assert.assertEquals("Error: Cp title mismatch", "default-cp", cpr.getCpTitle());
		Assert.assertEquals("Error: Ppid mismatch", "default-gen-ppid-1", cpr.getPpid());
		Assert.assertEquals("Error: Comsent document url mismatch", "www.exampleurl.com", cpr.getConsentDetails().getConsentDocumentUrl());
		Assert.assertEquals("Error: Consent tier response count mismatch", (int)3, cpr.getConsentDetails().getConsentTierResponses().size());
		Assert.assertEquals("Error: Witness name mismatch", "admin@admin.com", cpr.getConsentDetails().getWitnessName());
		Assert.assertEquals("Error: Registration date mismatch", CommonUtils.getDate(31,1,2001), cpr.getRegistrationDate());
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
		
		ParticipantDetail participant = cpr.getParticipant();		
		Assert.assertEquals("Error: Participant's last name mismatch", "Falero", participant.getLastName());
		Assert.assertEquals("Error: Participant's first name mismatch", "Guava", participant.getFirstName());
		Assert.assertEquals("Error: Participant's race count mismatch", (int)1, participant.getRaces().size());
		Assert.assertEquals("Asian", participant.getRaces().iterator().next());
		Assert.assertEquals("Error: Birthdate mismatch", CommonUtils.getDate(21,10,2012), participant.getBirthDate());
		Assert.assertEquals("Error: Participant's activity status mismatch", "Active" , participant.getActivityStatus());
	}
	
	@Test
	public void getNonExistingRegistration() {
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCprId(1L);
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void getNonExistingRegistrationByPpid() {
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCpId(1L);
		input.setPpid("default-gen-ppid-1");
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CP_AND_PPID, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-disabled-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getDisabledRegistration() {
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCprId(1L);
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	//TODO: look at this test case, it should be a negative testcase but its not!
	@Test
	@DatabaseSetup("cp-test/registration-test/get-disabled-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getDisabledRegistrationByPpid() {
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCpId(1L);
		input.setPpid("default-gen-ppid-1");
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals(new Long(1) , resp.getPayload().getId());
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationForNonExistingPpid() {
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCpId(1L);
		input.setPpid("non-existing-ppid");
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CP_AND_PPID, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationForNonExistingCpid() {
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCpId(-1L);
		input.setPpid("default-gen-ppid-1");
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CP_AND_PPID, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/registration-test/get-registration-cpid-and-ppid-mismatch-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getRegistrationCpidAndPpidMismatch() {
		/*
		 * ppid and cpid both are valid but they belong to different cp's
		 */
		RegistrationQueryCriteria input = new RegistrationQueryCriteria();
		input.setCpId(2L);
		input.setPpid("default-gen-ppid-1");
		
		ResponseEvent<CollectionProtocolRegistrationDetail> resp = cprSvc.getRegistration(getRequest(input));
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CP_AND_PPID, ErrorType.USER_ERROR);
	}
}
