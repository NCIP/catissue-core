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
		CollectionProtocolDetail detail = resp.getCp();
		
		Assert.assertEquals("default-cp", detail.getTitle());
		Assert.assertEquals("default-short-title", detail.getShortTitle());
		
		Assert.assertNotNull(resp.getCp().getPrincipalInvestigator());
		UserSummary pi = resp.getCp().getPrincipalInvestigator();
		Assert.assertEquals("ADMIN", pi.getFirstName());
		Assert.assertEquals("ADMIN", pi.getLastName());
		Assert.assertEquals("admin@admin.com", pi.getLoginName());
	}
	
	@Test
	public void getCollectionProtocolThatDoesntExists() {
		ReqCollectionProtocolEvent req = CpTestData.getReqCollectionProtocolEvent();
		CollectionProtocolDetailEvent resp = cpSvc.getCollectionProtocol(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
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
		
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertEquals((int)3, resp.getDiagnoses().size());
		Assert.assertEquals(true, resp.getDiagnoses().contains("clinical-daignosis-1"));
		Assert.assertEquals(true, resp.getDiagnoses().contains("clinical-daignosis-2"));
		Assert.assertEquals(true, resp.getDiagnoses().contains("clinical-daignosis-3"));
	}
	
	@Test
	public void getClinicalDaignosisCpDoesntExists() {
		ReqClinicalDiagnosesEvent req = CpTestData.getReqClinicalDiagnosesEvent();
		ClinicalDiagnosesEvent resp = cpSvc.getDiagnoses(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
	}
	
	
}
