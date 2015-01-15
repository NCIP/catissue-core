package com.krishagni.core.tests;

import javax.annotation.Resource;

import java.util.Calendar;
import java.util.Date;

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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.events.VisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CprTestData;
import com.krishagni.core.tests.testdata.VisitsTestData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class VisitsTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private CollectionProtocolRegistrationService cprSvc;
	
	@Autowired
	private ApplicationContext ctx;
	
	private static final String VISIT_DATE = "visit date";
	
	private static final String VISIT_STATUS = "Collection Status";

	/*
	 * Add Visit API Tests
	 */
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-test-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/add-visits-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addVisitsTest() {
		AddVisitEvent req = VisitsTestData.getAddVisitEvent();
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccess());
		Assert.assertNotNull(resp.getVisit());
		Assert.assertEquals((Long)1L, resp.getVisit().getEventId());
		Assert.assertEquals((Long)1L, resp.getVisit().getCprId());
		Assert.assertEquals("test-pathology", resp.getVisit().getSurgicalPathologyNumber());
		Assert.assertEquals("test-daiagnosis", resp.getVisit().getClinicalDiagnosis());
		Assert.assertEquals("Completed", resp.getVisit().getStatus());
		Assert.assertEquals("SITE1", resp.getVisit().getSite());
		Assert.assertEquals("Active", resp.getVisit().getActivityStatus());
		Assert.assertEquals("test-status", resp.getVisit().getClinicalStatus());
	}
	
	/*
	 * This test Checks for the error: Cpr not found, Cpe not found, Site Not Found
	 */
	@Test
	public void addVisitsMissingCprCpeSite() {
		AddVisitEvent req = VisitsTestData.getAddVisitEvent();
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid CPE Errorcode was not found!", true,
				TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "cpeId"));
		Assert.assertEquals("Invalid CPR Errorcode was not found!", true, 
				TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "cprId"));
		Assert.assertEquals("Invalid SITE Errorcode was not found!", true, 
				TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "site"));
		
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-invalid-cpr-cpe-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void addVisitsInvalidCprCpe() {
		AddVisitEvent req = VisitsTestData.getAddVisitEvent();
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_CPR_CPE, "cpr, cpe"));
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-test-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void addVisitsMissingSiteName() {
		AddVisitEvent req = VisitsTestData.getAddVisitEvent();
		req.getVisit().setSite(null);
		req.getVisit().setCprId(null);
		req.getVisit().setCpTitle("invalid-serach-term");
		req.getVisit().setPpid("invalid-search-term");
		
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Missing site name error was not found!", 
				true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.MISSING_ATTR_VALUE, "site"));
		Assert.assertEquals("Invalid cp-title error was expected, but not found!", 
				true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "collectionProtocol"));
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-test-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void addVisitsTestInvalidPpid() {
		AddVisitEvent req = VisitsTestData.getAddVisitEvent();
		req.getVisit().setCprId(null);
		req.getVisit().setEventId(null);
		
		req.getVisit().setCpTitle("default-cp");
		req.getVisit().setPpid("invalid-ppid");
		req.getVisit().setEventLabel("invalid-cpl");
		VisitAddedEvent resp = cprSvc.addVisit(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "ppid"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, ScgErrorCode.INVALID_ATTR_VALUE, "eventLabel"));
	}

	/*
	 * Get Visits API Tests
	 */
	@Test
	@DatabaseSetup("cp-test/visits-test/get-visits-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getVisits() {
		ReqVisitsEvent req = VisitsTestData.getReqVisitsEvent();
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
		ReqVisitsEvent req = VisitsTestData.getReqVisitsEvent();
		VisitsEvent resp = cprSvc.getVisits(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(0), new Integer(resp.getVisits().size()));
	}
	
	@Test 
	@DatabaseSetup("cp-test/visits-test/get-visits-for-inactive-cpr-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getVisitsForInactiveCpr() {
		ReqVisitsEvent req = VisitsTestData.getReqVisitsEvent();
		VisitsEvent resp = cprSvc.getVisits(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(0), new Integer(resp.getVisits().size()));
	}
	
	//TODO: Check with VP whether disabled to be shown 
	//@Test
	@DatabaseSetup("cp-test/visits-test/get-visits-some-visits-disabled-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getVisitsSomeVisitsDisabled() {
		ReqVisitsEvent req = VisitsTestData.getReqVisitsEvent();
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
}