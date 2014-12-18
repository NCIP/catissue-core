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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.core.common.ApplicationContextConfigurer;
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
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.registerParticipant.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.registerParticipant.teardown.xml")
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
		Assert.assertEquals(true, resp.isSuccess());
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolRegistrationTest.createAndRegisterParticipant.initial.xml")
	@DatabaseTearDown("CollectionProtocolRegistrationTest.createAndRegisterParticipant.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.createAndRegisterParticipant.expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createAndRegisterParticipant() {
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetailForCreateAndRegisterParticipantWithPmi();
		
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCpId(1L);
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccess());
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
	public void registerParticipantDuplicateBarcode() {		
		CollectionProtocolRegistrationDetail cpr = CprTestData.getCprDetail();
		cpr.setBarcode("existing-barcode");
		CreateRegistrationEvent req = new CreateRegistrationEvent();
		req.setCprDetail(cpr);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		
		RegistrationCreatedEvent resp = cprSvc.createRegistration(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.DUPLICATE_BARCODE, BARCODE));
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
	@DatabaseTearDown("CollectionProtocolRegistrationTest.createAndRegisterDeadParticipant.teardown.xml")
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
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccess());
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
	@DatabaseTearDown("CollectionProtocolRegistrationTest.registerParticipantToDisabledCp.teardown.xml")
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
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ParticipantErrorCode.DISABLED_CP, COLLECTION_PROTOCOL));
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
}
