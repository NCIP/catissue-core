package com.krishagni.core.tests;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

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
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
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
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Get All CP's tests
	 */
	@Test
	@DatabaseSetup("cp-test/get-all-cps-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getAllCps() {
		CpListCriteria input = new CpListCriteria();
		input.includePi(true);
		input.includeStat(true);
		
		ResponseEvent<List<CollectionProtocolSummary>> resp = cpSvc.getProtocols(getRequest(input));
		List<CollectionProtocolSummary> cpList = resp.getPayload();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CommonUtils.getDate(31, 10, 2000), summary.getStartDate());
			
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
	@DatabaseSetup("cp-test/get-all-cps-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getAllCpsWithoutStatsAndPi() {
		CpListCriteria input = new CpListCriteria();
		input.includePi(false);
		input.includeStat(false);
		
		ResponseEvent<List<CollectionProtocolSummary>> resp = cpSvc.getProtocols(getRequest(input));
		List<CollectionProtocolSummary> cpList = resp.getPayload();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CommonUtils.getDate(31, 10, 2000), summary.getStartDate());
			
			System.out.println("Participant Count; " + summary.getParticipantCount() + " Specimen Count: " + summary.getSpecimenCount());
			
			Assert.assertNull("PI was expected to be Null!", summary.getPrincipalInvestigator());
			Assert.assertNull("Participant Count was expected to be null!", summary.getParticipantCount());
			Assert.assertNull("Specimen Count was expected to be null!", summary.getSpecimenCount());
		}
	}
	
	@Test
	@DatabaseSetup("cp-test/get-all-cps-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getAllCpsWithoutStats() {
		CpListCriteria input = new CpListCriteria();
		input.includePi(true);
		input.includeStat(false);
		
		ResponseEvent<List<CollectionProtocolSummary>> resp = cpSvc.getProtocols(getRequest(input));
		List<CollectionProtocolSummary> cpList = resp.getPayload();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CommonUtils.getDate(31, 10, 2000), summary.getStartDate());
			
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
	@DatabaseSetup("cp-test/get-all-cps-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getAllCpsWithoutPi() {
		CpListCriteria input = new CpListCriteria();
		input.includePi(false);
		input.includeStat(true);
		
		ResponseEvent<List<CollectionProtocolSummary>> resp = cpSvc.getProtocols(getRequest(input));
		List<CollectionProtocolSummary> cpList = resp.getPayload();
		
		Assert.assertEquals(new Integer(2), new Integer(cpList.size()));
		
		for (CollectionProtocolSummary summary : cpList) {
			String title = "cp" + summary.getId();
			String shortTitle = "short-cp" + summary.getId();
			
			Assert.assertEquals(title, summary.getTitle());
			Assert.assertEquals(shortTitle, summary.getShortTitle());
			Assert.assertEquals(CommonUtils.getDate(31, 10, 2000), summary.getStartDate());
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
	@DatabaseSetup("cp-test/get-registered-participants-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getRegisteredParticipants() {
		CprListCriteria input = new CprListCriteria();
		input.cpId(1L);
		input.includeStat(true);
		input.startAt(0);
		input.maxResults(100);
		input.includePhi(true);
		
		ResponseEvent<List<CprSummary>> resp = cpSvc.getRegisteredParticipants(getRequest(input));
		
		List<CprSummary> registeredParticipants = resp.getPayload();
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			Assert.assertEquals(firstName, participant.getFirstName());
			Assert.assertEquals(lastName, participant.getLastName());
			
			Assert.assertEquals(CommonUtils.getDate(31,1,2001), summary.getRegistrationDate());
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
	@DatabaseSetup("cp-test/get-registered-participants-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getRegisteredParticipantsWithoutStats() {
		CprListCriteria input = new CprListCriteria();
		input.cpId(1L);
		input.includeStat(false);
		input.startAt(0);
		input.maxResults(100);
		input.includePhi(true);
		
		ResponseEvent<List<CprSummary>> resp = cpSvc.getRegisteredParticipants(getRequest(input));
		
		List<CprSummary> registeredParticipants = resp.getPayload();
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			Assert.assertEquals(firstName, participant.getFirstName());
			Assert.assertEquals(lastName, participant.getLastName());
			
			Assert.assertEquals(CommonUtils.getDate(31,1,2001), summary.getRegistrationDate());
			Assert.assertEquals(new String("ppid-" + summary.getCprId()), summary.getPpid());
			
			Assert.assertNull("Specimen Count was supposed to be null!", summary.getSpecimenCount());
			Assert.assertNull("Scg count was supposed to be null!", summary.getScgCount());
		}
	}
	
	@Test
	@DatabaseSetup("cp-test/get-registered-participants-given-search-string-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getRegisteredParticipantsGivenSearchString() {
		String pattern = "default-pattern";
		CprListCriteria input = new CprListCriteria();
		input.cpId(1L);
		input.includeStat(true);
		input.startAt(0);
		input.maxResults(100);
		input.includePhi(true);
		input.query(pattern);
		
		ResponseEvent<List<CprSummary>> resp = cpSvc.getRegisteredParticipants(getRequest(input));
		
		List<CprSummary> registeredParticipants = resp.getPayload();
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			String expectedPpid = new String("ppid-" + summary.getCprId()); 
			Assert.assertEquals(CommonUtils.getDate(31,1,2001), summary.getRegistrationDate());
			
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
	@DatabaseSetup("cp-test/get-registered-participants-given-search-string-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getRegisteredParticipantsGivenSearchStringWithoutStat() {
		String pattern = "default-pattern";
		CprListCriteria input = new CprListCriteria();
		input.cpId(1L);
		input.includeStat(false);
		input.startAt(0);
		input.maxResults(100);
		input.includePhi(true);
		input.query(pattern);
		
		ResponseEvent<List<CprSummary>> resp = cpSvc.getRegisteredParticipants(getRequest(input));
		
		List<CprSummary> registeredParticipants = resp.getPayload();
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(3), new Integer(registeredParticipants.size()));
		
		for (CprSummary summary : registeredParticipants) {
			ParticipantSummary participant = summary.getParticipant();
			Assert.assertNotNull("Participant is null!", participant);
			
			Long pid = participant.getId();
			String firstName = "first_name_" + pid + "_pad";
			String lastName = "last_name_" + pid + "_pad";
			String expectedPpid = new String("ppid-" + summary.getCprId()); 
			Assert.assertEquals(CommonUtils.getDate(31,1,2001), summary.getRegistrationDate());
			
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
		CprListCriteria input = new CprListCriteria();
		input.cpId(1L);
		input.includeStat(true);
		input.startAt(0);
		input.maxResults(100);
		input.includePhi(true);
		
		ResponseEvent<List<CprSummary>> resp = cpSvc.getRegisteredParticipants(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(0), new Integer(resp.getPayload().size()));
	}
	
	@Test
	@DatabaseSetup("cp-test/get-registered-participants-for-disabled-cp-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void getRegisteredParticipantsForDisabledCp() {
		CprListCriteria input = new CprListCriteria();
		input.cpId(1L);
		input.includeStat(true);
		input.startAt(0);
		input.maxResults(100);
		input.includePhi(true);
		
		ResponseEvent<List<CprSummary>> resp = cpSvc.getRegisteredParticipants(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(0), new Integer(resp.getPayload().size()));
	}
	
	/*
	 * Get Collection Protocol API Tests
	 */
	//TODO: fix the tests below, api got changed
//	@Test
//	@DatabaseSetup("cp-test/get-cp-test-initial.xml")
//	@DatabaseTearDown("cp-test/generic-teardown.xml")
//	public void getCollectionProtocolTest() {
//		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.getCollectionProtocol(getRequest(1L));
//		
//		Assert.assertEquals(true, resp.isSuccessful());
//		CollectionProtocolDetail actual = resp.getPayload();
//		Assert.assertNotNull(actual);
//		Assert.assertEquals(new Long(1), resp.getPayload().getId());
//		Assert.assertEquals("Cp title mismatch", "title", actual.getTitle());
//		Assert.assertEquals("Cp short title mismatch", "short-title", actual.getShortTitle());
//		Assert.assertEquals("Start date mismatch", CommonUtils.getDate(31,1,2000), actual.getStartDate());
//		Assert.assertEquals("Consent waived mismatch!", new Boolean(true), actual.getConsentsWaived());
//		Assert.assertEquals("Ppid format mismatch", "ppid-fmt", actual.getPpidFmt());
//		Assert.assertEquals("Specimen label format mismatch", "specimen-label-format", actual.getSpecimenLabelFmt());
//		Assert.assertEquals("Derivative format mismatch!", "derivative-format", actual.getDerivativeLabelFmt());
//		Assert.assertEquals("Aliquot format mismatch", "aliquot-format", actual.getAliquotLabelFmt());
//		Assert.assertEquals("Aliquot in same container mismatch", new Boolean(true), actual.getAliquotsInSameContainer());
//		Assert.assertEquals("ActivityStatus mismatch" , "Active" , actual.getActivityStatus());
//		
//		UserSummary pi = actual.getPrincipalInvestigator();
//		String firstName = "ADMIN" + pi.getId();
//		String lastName = "ADMIN" + pi.getId();
//		String loginName = "admin" + pi.getId() + "@admin.com";
//		Assert.assertEquals("PI firstName mismatch: ", firstName, pi.getFirstName());
//		Assert.assertEquals("PI lastname mismatch ", lastName, pi.getLastName());
//		Assert.assertEquals("loginname mismatch" , loginName, pi.getLoginName());
//		Assert.assertEquals("coordinator count mismatch", new Integer(2), new Integer(actual.getCoordinators().size()));
//		
//		for (UserSummary user : actual.getCoordinators()) {
//			firstName = "ADMIN" + user.getId();
//			lastName = "ADMIN" + user.getId();
//			loginName = "admin" + user.getId() + "@admin.com";
//			
//			Assert.assertEquals("coordinator firstname mismatch", firstName, user.getFirstName());
//			Assert.assertEquals("coordinator lastname mismatch", lastName, user.getLastName());
//			Assert.assertEquals("coordinator loginname mismatch", loginName, user.getLoginName());
//		}	
//	}
//	
//	@Test
//	public void getCollectionProtocolThatDoesntExists() {
//		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.getCollectionProtocol(getRequest(1L));
//		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
//	}
//	
//	@Test
//	@DatabaseSetup("cp-test/get-disabled-cp-initial.xml")
//	@DatabaseTearDown("cp-test/generic-teardown.xml")
//	public void getDisabledCp() {
//		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.getCollectionProtocol(getRequest(1L));
//		Assert.assertEquals(false, resp.isSuccessful());
//		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
//	}
	
	/*
	 *Create Collection Protocol API Tests  
	 */
	@Test
	@DatabaseSetup("cp-test/create-cp-test-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/create-cp-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createCpTest() {
		CollectionProtocolDetail input = CpTestData.getCp();
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.createCollectionProtocol(getRequest(input));
		
		CollectionProtocolDetail expected = input;
		CollectionProtocolDetail actual = resp.getPayload();
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		AssertCpDetail(expected, actual);
	}
	
	@Test
	@DatabaseSetup("cp-test/create-cp-test-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/registration-test/create-cp-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createCpWithEmptyActivityStatus() {
		CollectionProtocolDetail input = CpTestData.getCp();
		input.setActivityStatus("");
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.createCollectionProtocol(getRequest(input));
		
		CollectionProtocolDetail expected = input;
		CollectionProtocolDetail actual = resp.getPayload();
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		AssertCpDetail(expected, actual);
	}
	
	public void AssertCpDetail(CollectionProtocolDetail expected, CollectionProtocolDetail actual) {
		
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		Assert.assertEquals(expected.getTitle(), actual.getTitle());
		Assert.assertEquals(expected.getShortTitle(), actual.getShortTitle());
		Assert.assertEquals(CommonUtils.getDate(31,1,2000), actual.getStartDate());
		Assert.assertEquals(expected.getStartDate(), actual.getStartDate());
		Assert.assertEquals(expected.getConsentsWaived(), actual.getConsentsWaived());
		Assert.assertEquals(expected.getPpidFmt(), actual.getPpidFmt());
		Assert.assertEquals(expected.getSpecimenLabelFmt(), actual.getSpecimenLabelFmt());
		Assert.assertEquals(expected.getDerivativeLabelFmt(), actual.getDerivativeLabelFmt());
		Assert.assertEquals(expected.getAliquotLabelFmt(), actual.getAliquotLabelFmt());
		Assert.assertEquals(expected.getAliquotsInSameContainer(), actual.getAliquotsInSameContainer());
		Assert.assertEquals("Active" , actual.getActivityStatus());
		
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
	@DatabaseSetup("cp-test/create-cp-test-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void createCpTestWithInvalidPIAndCoordinators() {
		CollectionProtocolDetail input = CpTestData.getCp();
		input.getPrincipalInvestigator().setId(-1L);
		input.getCoordinators().add(CommonUtils.getUser(-1L, "", "", ""));
		input.setTitle("");
		input.setShortTitle(null);
		input.setConsentsWaived(null);
		
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.createCollectionProtocol(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.PI_NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, CpErrorCode.TITLE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, CpErrorCode.SHORT_TITLE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, CpErrorCode.CONSENTS_WAIVED_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/create-cp-test-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void createCpTestWithOutPi() {
		CollectionProtocolDetail input = CpTestData.getCp();
		input.setPrincipalInvestigator(null);

		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.createCollectionProtocol(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.PI_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/create-cp-test-with-duplicate-title-initial.xml")
	@DatabaseTearDown("cp-test/generic-teardown.xml")
	public void createCpTestWithDuplicateTitle() {
		CollectionProtocolDetail input = CpTestData.getCp();
		input.setTitle("duplicate-title");
		
		ResponseEvent<CollectionProtocolDetail> resp = cpSvc.createCollectionProtocol(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.DUP_TITLE, ErrorType.USER_ERROR);
	}
}
