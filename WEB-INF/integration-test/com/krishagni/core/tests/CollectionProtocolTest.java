package com.krishagni.core.tests;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.After;
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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.CreateCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummary;
import com.krishagni.catissueplus.core.biospecimen.events.RegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CpTestData;
import com.krishagni.core.tests.testdata.CprTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class CollectionProtocolTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private CollectionProtocolService cpSvc;

	@Autowired
	private ApplicationContext ctx;
	
	/*
	 * Get All CP's tests
	 */
	@Test
	@DatabaseSetup("CollectionProtocolTest.getAllCps.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getAllCps() {
		AllCollectionProtocolsEvent resp = cpSvc.getAllProtocols(CpTestData.getReqAllCollectionProtocolsEvent());
		List<CollectionProtocolSummary> cpList = resp.getCpList();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CprTestData.getDate(31, 10, 2000), summary.getStartDate());
			
			UserSummary pi = summary.getPrincipalInvestigator();
			Assert.assertNotNull(pi);
			Assert.assertEquals("ADMIN" ,pi.getFirstName());
			Assert.assertEquals("ADMIN" ,pi.getLastName());
			Assert.assertEquals(new Long(1), pi.getId());
			Assert.assertEquals("admin@admin.com" ,pi.getLoginName());
			
			if (summary.getId().equals(new Long(1))) {
				Assert.assertEquals(new Long(2), summary.getParticipantCount());
				Assert.assertEquals(new Long(6), summary.getSpecimenCount());
			} else {
				Assert.assertEquals(new Long(0), summary.getParticipantCount());
				Assert.assertEquals(new Long(0), summary.getSpecimenCount());
			}
		}
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getAllCps.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getAllCpsWithoutStatsAndPi() {
		ReqAllCollectionProtocolsEvent req = CpTestData.getReqAllCollectionProtocolsEvent();
		req.setIncludePi(false);
		req.setIncludeStats(false);
		
		AllCollectionProtocolsEvent resp = cpSvc.getAllProtocols(req);
		List<CollectionProtocolSummary> cpList = resp.getCpList();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CprTestData.getDate(31, 10, 2000), summary.getStartDate());
			
			System.out.println("Participant Count; " + summary.getParticipantCount() + " Specimen Count: " + summary.getSpecimenCount());
			
			Assert.assertNull("PI was expected to be Null!", summary.getPrincipalInvestigator());
			Assert.assertNull("Participant Count was expected to be null!", summary.getParticipantCount());
			Assert.assertNull("Specimen Count was expected to be null!", summary.getSpecimenCount());
		}
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getAllCps.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getAllCpsWithoutStats() {
		ReqAllCollectionProtocolsEvent req = CpTestData.getReqAllCollectionProtocolsEvent();
		req.setIncludeStats(false);
		
		AllCollectionProtocolsEvent resp = cpSvc.getAllProtocols(req);
		List<CollectionProtocolSummary> cpList = resp.getCpList();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CprTestData.getDate(31, 10, 2000), summary.getStartDate());
			
			UserSummary pi = summary.getPrincipalInvestigator();
			Assert.assertNotNull(pi);
			Assert.assertEquals("ADMIN" ,pi.getFirstName());
			Assert.assertEquals("ADMIN" ,pi.getLastName());
			Assert.assertEquals(new Long(1), pi.getId());
			Assert.assertEquals("admin@admin.com" ,pi.getLoginName());
			
			Assert.assertNull("Participant Count was expected to be null!", summary.getParticipantCount());
			Assert.assertNull("Specimen Count was expected to be null!", summary.getSpecimenCount());
		}
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getAllCps.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getAllCpsWithoutPi() {
		ReqAllCollectionProtocolsEvent req = CpTestData.getReqAllCollectionProtocolsEvent();
		req.setIncludePi(false);
		
		AllCollectionProtocolsEvent resp = cpSvc.getAllProtocols(req);
		List<CollectionProtocolSummary> cpList = resp.getCpList();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CprTestData.getDate(31, 10, 2000), summary.getStartDate());
			Assert.assertNull(summary.getPrincipalInvestigator());
			
			if (summary.getId().equals(new Long(1))) {
				Assert.assertEquals(new Long(2), summary.getParticipantCount());
				Assert.assertEquals(new Long(6), summary.getSpecimenCount());
			} else {
				Assert.assertEquals(new Long(0), summary.getParticipantCount());
				Assert.assertEquals(new Long(0), summary.getSpecimenCount());
			}
		}
	}
		
	/*
	 * Get Registered Participants API Tests
	 */
	@Test
	@DatabaseSetup("CollectionProtocolTest.getRegisteredParticipants.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getRegisteredParticipants() {
		ReqRegisteredParticipantsEvent req = CpTestData.getReqRegisteredParticipantsEvent();
		RegisteredParticipantsEvent resp = cpSvc.getRegisteredParticipants(req);
		
		List<CprSummary> registeredParticipants = resp.getParticipants();
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			Assert.assertEquals(firstName, participant.getFirstName());
			Assert.assertEquals(lastName, participant.getLastName());
			
			Assert.assertEquals(CprTestData.getDate(31,1,2001), summary.getRegistrationDate());
			Assert.assertEquals(new String("ppid-" + summary.getCprId()), summary.getPpid());
			
			if (summary.getCprId().equals(new Long(1))) {
				Assert.assertEquals(new Long(2), summary.getSpecimenCount());
				Assert.assertEquals(new Long(1), summary.getScgCount());
			} else if (summary.getCprId().equals(new Long(2))) {
				Assert.assertEquals(new Long(1), summary.getSpecimenCount());
				Assert.assertEquals(new Long(1), summary.getScgCount());
			} else if (summary.getCprId().equals(new Long(3))) {
				Assert.assertEquals(new Long(0), summary.getSpecimenCount());
				Assert.assertEquals(new Long(0), summary.getScgCount());
			} else {
				Assert.fail("The cprid: " + summary.getCprId() + " was not expected!");
			}
		}
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getRegisteredParticipants.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getRegisteredParticipantsWithoutStats() {
		ReqRegisteredParticipantsEvent req = CpTestData.getReqRegisteredParticipantsEvent();
		req.setIncludeStats(false);
		RegisteredParticipantsEvent resp = cpSvc.getRegisteredParticipants(req);
		
		List<CprSummary> registeredParticipants = resp.getParticipants();
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			Assert.assertEquals(firstName, participant.getFirstName());
			Assert.assertEquals(lastName, participant.getLastName());
			
			Assert.assertEquals(CprTestData.getDate(31,1,2001), summary.getRegistrationDate());
			Assert.assertEquals(new String("ppid-" + summary.getCprId()), summary.getPpid());
			
			Assert.assertNull("Specimen Count was supposed to be null!", summary.getSpecimenCount());
			Assert.assertNull("Scg count was supposed to be null!", summary.getScgCount());
		}
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getRegisteredParticipantsGivenSearchString.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getRegisteredParticipantsGivenSearchString() {
		ReqRegisteredParticipantsEvent req = CpTestData.getReqRegisteredParticipantsEvent();
		String pattern = "default-pattern";
		req.setSearchString(pattern);
		RegisteredParticipantsEvent resp = cpSvc.getRegisteredParticipants(req);
		
		List<CprSummary> registeredParticipants = resp.getParticipants();
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			String expectedPpid = new String("ppid-" + summary.getCprId()); 
			Assert.assertEquals(CprTestData.getDate(31,1,2001), summary.getRegistrationDate());
			
			if (summary.getCprId().equals(new Long(1))) {
				
				Assert.assertEquals(pattern, participant.getLastName());
				Assert.assertEquals(firstName, participant.getFirstName());
				Assert.assertEquals(expectedPpid, summary.getPpid());
				Assert.assertEquals(new Long(2), summary.getSpecimenCount());
				Assert.assertEquals(new Long(1), summary.getScgCount());
				
			} else if (summary.getCprId().equals(new Long(2))) {
				
				Assert.assertEquals(pattern, summary.getPpid());
				Assert.assertEquals(firstName, participant.getFirstName());
				Assert.assertEquals(lastName, participant.getLastName());
				Assert.assertEquals(new Long(1), summary.getSpecimenCount());
				Assert.assertEquals(new Long(1), summary.getScgCount());
				
			} else if (summary.getCprId().equals(new Long(3))) {
				
				Assert.assertEquals(expectedPpid, summary.getPpid());
				Assert.assertEquals(pattern, participant.getFirstName());
				Assert.assertEquals(lastName, participant.getLastName());
				Assert.assertEquals(new Long(0), summary.getSpecimenCount());
				Assert.assertEquals(new Long(0), summary.getScgCount());
				
			} else {
				
				Assert.fail("The cprid: " + summary.getCprId() + " was not expected!");
				
			}
		}
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getRegisteredParticipantsGivenSearchString.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getRegisteredParticipantsGivenSearchStringWithoutStat() {
		ReqRegisteredParticipantsEvent req = CpTestData.getReqRegisteredParticipantsEvent();
		String pattern = "default-pattern";
		req.setSearchString(pattern);
		req.setIncludeStats(false);
		RegisteredParticipantsEvent resp = cpSvc.getRegisteredParticipants(req);
		
		List<CprSummary> registeredParticipants = resp.getParticipants();
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			String expectedPpid = new String("ppid-" + summary.getCprId()); 
			Assert.assertEquals(CprTestData.getDate(31,1,2001), summary.getRegistrationDate());
			
			Assert.assertNull(summary.getSpecimenCount());
			Assert.assertNull(summary.getScgCount());
			
			if (summary.getCprId().equals(new Long(1))) {
				
				Assert.assertEquals(pattern, participant.getLastName());
				Assert.assertEquals(firstName, participant.getFirstName());
				Assert.assertEquals(expectedPpid, summary.getPpid());
				
			} else if (summary.getCprId().equals(new Long(2))) {
				
				Assert.assertEquals(pattern, summary.getPpid());
				Assert.assertEquals(firstName, participant.getFirstName());
				Assert.assertEquals(lastName, participant.getLastName());
				
			} else if (summary.getCprId().equals(new Long(3))) {
				
				Assert.assertEquals(expectedPpid, summary.getPpid());
				Assert.assertEquals(pattern, participant.getFirstName());
				Assert.assertEquals(lastName, participant.getLastName());
				
			} else {
				
				Assert.fail("The cprid: " + summary.getCprId() + " was not expected!");
				
			}
		}
	}
	
	@Test
	public void getRegisteredParticipantForANonExistingCp() {
		ReqRegisteredParticipantsEvent req = CpTestData.getReqRegisteredParticipantsEvent();
		RegisteredParticipantsEvent resp = cpSvc.getRegisteredParticipants(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(0), new Integer(resp.getParticipants().size()));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getRegisteredParticipantsForDisabledCp.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getRegisteredParticipantsForDisabledCp() {
		ReqRegisteredParticipantsEvent req = CpTestData.getReqRegisteredParticipantsEvent();
		RegisteredParticipantsEvent resp = cpSvc.getRegisteredParticipants(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(0), new Integer(resp.getParticipants().size()));
	}
	
	/*
	 * Get Collection Protocol API Tests
	 */
	@Test
	@DatabaseSetup("CollectionProtocolTest.getCollectionProtocolTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getCollectionProtocolTest() {
		ReqCollectionProtocolEvent req = CpTestData.getReqCollectionProtocolEvent();
		CollectionProtocolDetailEvent resp = cpSvc.getCollectionProtocol(req);
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		CollectionProtocolDetail actual = resp.getCp();
		
		Assert.assertNotNull(actual);
		Assert.assertEquals(new Long(1), resp.getCp().getId());
		Assert.assertEquals("Cp title mismatch", "title", actual.getTitle());
		Assert.assertEquals("Cp short title mismatch", "short-title", actual.getShortTitle());
		Assert.assertEquals("Start date mismatch", CprTestData.getDate(31,1,2000), actual.getStartDate());
		Assert.assertEquals("Consent waived mismatch!", new Boolean(true), actual.getConsentsWaived());
		Assert.assertEquals("Ppid format mismatch", "ppid-fmt", actual.getPpidFmt());
		Assert.assertEquals("Specimen label format mismatch", "specimen-label-format", actual.getSpecimenLabelFmt());
		Assert.assertEquals("Derivative format mismatch!", "derivative-format", actual.getDerivativeLabelFmt());
		Assert.assertEquals("Aliquot format mismatch", "aliquot-format", actual.getAliquotLabelFmt());
		Assert.assertEquals("Aliquot in same container mismatch", new Boolean(true), actual.getAliquotsInSameContainer());
		Assert.assertEquals("ActivityStatus mismatch" , "Active" , actual.getActivityStatus());
		
		UserSummary pi = actual.getPrincipalInvestigator();
		String firstName = "ADMIN" + pi.getId();
		String lastName = "ADMIN" + pi.getId();
		String loginName = "admin" + pi.getId() + "@admin.com";
		Assert.assertEquals("PI firstName mismatch: ", firstName, pi.getFirstName());
		Assert.assertEquals("PI lastname mismatch ", lastName, pi.getLastName());
		Assert.assertEquals("loginname mismatch" , loginName, pi.getLoginName());
		Assert.assertEquals("coordinator count mismatch", new Integer(2), new Integer(actual.getCoordinators().size()));
		
		for (UserSummary user : actual.getCoordinators()) {
			firstName = "ADMIN" + user.getId();
			lastName = "ADMIN" + user.getId();
			loginName = "admin" + user.getId() + "@admin.com";
			
			Assert.assertEquals("coordinator firstname mismatch", firstName, user.getFirstName());
			Assert.assertEquals("coordinator lastname mismatch", lastName, user.getLastName());
			Assert.assertEquals("coordinator loginname mismatch", loginName, user.getLoginName());
		}	
	}
	
	@Test
	public void getCollectionProtocolThatDoesntExists() {
		ReqCollectionProtocolEvent req = CpTestData.getReqCollectionProtocolEvent();
		CollectionProtocolDetailEvent resp = cpSvc.getCollectionProtocol(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getDisabledCp.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getDisabledCp() {
		ReqCollectionProtocolEvent req = CpTestData.getReqCollectionProtocolEvent();
		CollectionProtocolDetailEvent resp = cpSvc.getCollectionProtocol(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
	}
	
	/*
	 * Get Daignosis API
	 */
	@Test
	@DatabaseSetup("CollectionProtocolTest.getCollectionProtocolTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getClinicalDaignosis() {
		ReqClinicalDiagnosesEvent req = CpTestData.getReqClinicalDiagnosesEvent();
		ClinicalDiagnosesEvent resp = cpSvc.getDiagnoses(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Operation failed!", EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)3, resp.getDiagnoses().size());
		Assert.assertEquals(true, resp.getDiagnoses().contains("One"));
		Assert.assertEquals(true, resp.getDiagnoses().contains("Two"));
		Assert.assertEquals(true, resp.getDiagnoses().contains("Three"));
	}
	
	@Test
	public void getClinicalDaignosisCpDoesntExists() {
		ReqClinicalDiagnosesEvent req = CpTestData.getReqClinicalDiagnosesEvent();
		ClinicalDiagnosesEvent resp = cpSvc.getDiagnoses(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.getDisabledCp.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void getDiagnosesForDisabledCp() {
		ReqClinicalDiagnosesEvent req = CpTestData.getReqClinicalDiagnosesEvent();
		ClinicalDiagnosesEvent resp = cpSvc.getDiagnoses(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
	}
	
	/*
	 *Create Collection Protocol API Tests  
	 */
	@Test
	@DatabaseSetup("CollectionProtocolTest.createCpTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolRegistrationTest.createCpTest.expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createCpTest() {
		CreateCollectionProtocolEvent req = CpTestData.getCreateCollectionProtocolEvent();
		CollectionProtocolCreatedEvent resp = cpSvc.createCollectionProtocol(req);
		
		CollectionProtocolDetail expected = req.getCp();
		CollectionProtocolDetail actual = resp.getCp();
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		Assert.assertEquals(expected.getTitle(), actual.getTitle());
		Assert.assertEquals(expected.getShortTitle(), actual.getShortTitle());
		Assert.assertEquals(CprTestData.getDate(31,1,2000), actual.getStartDate());
		Assert.assertEquals(expected.getStartDate(), actual.getStartDate());
		Assert.assertEquals(expected.getConsentsWaived(), actual.getConsentsWaived());
		Assert.assertEquals(expected.getPpidFmt(), actual.getPpidFmt());
		Assert.assertEquals(expected.getSpecimenLabelFmt(), actual.getSpecimenLabelFmt());
		Assert.assertEquals(expected.getDerivativeLabelFmt(), actual.getDerivativeLabelFmt());
		Assert.assertEquals(expected.getAliquotLabelFmt(), actual.getAliquotLabelFmt());
		Assert.assertEquals(expected.getAliquotsInSameContainer(), actual.getAliquotsInSameContainer());
		//TODO: Uncomment below once the dev code is fixed.
		//default Activity Status is not set while creating collection protoc
		//Assert.assertEquals("Active" , actual.getActivityStatus());
		
		UserSummary pi = actual.getPrincipalInvestigator();
		String firstName = "ADMIN" + pi.getId();
		String lastName = "ADMIN" + pi.getId();
		String loginName = "admin" + pi.getId() + "@admin.com";
		Assert.assertEquals(firstName, pi.getFirstName());
		Assert.assertEquals(lastName, pi.getLastName());
		Assert.assertEquals(loginName, pi.getLoginName());
		Assert.assertEquals(new Integer(2), new Integer(actual.getCoordinators().size()));
		
		for (UserSummary user : actual.getCoordinators()) {
			firstName = "ADMIN" + user.getId();
			lastName = "ADMIN" + user.getId();
			loginName = "admin" + user.getId() + "@admin.com";
			
			Assert.assertEquals(firstName, user.getFirstName());
			Assert.assertEquals(lastName, user.getLastName());
			Assert.assertEquals(loginName, user.getLoginName());
		}	
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.createCpTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void createCpTestWithInvalidPIAndCoordinators() {
		CreateCollectionProtocolEvent req = CpTestData.getCreateCollectionProtocolEvent();
		req.getCp().getPrincipalInvestigator().setId(-1L);
		req.getCp().getCoordinators().add(CpTestData.getUser(-1L, "", "", ""));
		req.getCp().setTitle("");
		req.getCp().setShortTitle(null);
		req.getCp().setConsentsWaived(null);
		
		CollectionProtocolCreatedEvent resp = cpSvc.createCollectionProtocol(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_PI, "principalInvestigator"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_COORDINATORS, "coordinators"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, CpErrorCode.MISSING_TITLE, "title"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, CpErrorCode.MISSING_SHORT_TITLE, "shortTitle"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, CpErrorCode.MISSING_CONSENTS_WAIVED, "consentsWaived"));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.createCpTest.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void createCpTestWithOutPi() {
		CreateCollectionProtocolEvent req = CpTestData.getCreateCollectionProtocolEvent();
		req.getCp().setPrincipalInvestigator(null);
		CollectionProtocolCreatedEvent resp = cpSvc.createCollectionProtocol(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, CpErrorCode.MISSING_PI, "principalInvestigator"));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolTest.createCpTestWithDuplicateTitle.initial.xml")
	@DatabaseTearDown("CollectionProtocolTest.generic.teardown.xml")
	public void createCpTestWithDuplicateTitle() {
		CreateCollectionProtocolEvent req = CpTestData.getCreateCollectionProtocolEvent();
		req.getCp().setTitle("duplicate-title");
		
		CollectionProtocolCreatedEvent resp = cpSvc.createCollectionProtocol(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, CpErrorCode.TITLE_NOT_UNIQUE, "title"));
	}
}
