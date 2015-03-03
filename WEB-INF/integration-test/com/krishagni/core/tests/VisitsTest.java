package com.krishagni.core.tests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.CpeTestData;
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

	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Add Visit API Tests
	 */
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-test-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/add-visits-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addVisitsTest() {
		VisitDetail input = VisitsTestData.getVisitDetail();
		ResponseEvent<VisitDetail> resp = cprSvc.addVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((Long)1L, resp.getPayload().getEventId());
		Assert.assertEquals((Long)1L, resp.getPayload().getCprId());
		Assert.assertEquals("test-pathology", resp.getPayload().getSurgicalPathologyNumber());
		Assert.assertEquals("test-daiagnosis", resp.getPayload().getClinicalDiagnosis());
		Assert.assertEquals("Completed", resp.getPayload().getStatus());
		Assert.assertEquals("SITE1", resp.getPayload().getSite());
		Assert.assertEquals("Active", resp.getPayload().getActivityStatus());
		Assert.assertEquals("test-status", resp.getPayload().getClinicalStatus());
	}
	
	/*
	 * This test Checks for the error: Cpr not found, Cpe not found, Site Not Found
	 */
	@Test
	public void addVisitsMissingCprCpeSite() {
		VisitDetail input = VisitsTestData.getVisitDetail();
		ResponseEvent<VisitDetail> resp = cprSvc.addVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, CprErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-test-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void addVisitsInvalidCpTitle() {
		VisitDetail input = VisitsTestData.getVisitDetail();
		input.setEventId(null);
		input.setCpTitle("invalid-title");
		input.setEventLabel("invalid-label");
		ResponseEvent<VisitDetail> resp = cprSvc.addVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-invalid-cpr-cpe-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void addVisitsInvalidCprCpe() {
		VisitDetail input = VisitsTestData.getVisitDetail();
		ResponseEvent<VisitDetail> resp = cprSvc.addVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CPE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-test-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void addVisitsMissingSiteName() {
		VisitDetail input = VisitsTestData.getVisitDetail();
		input.setSite(null);
		input.setCprId(null);
		input.setCpTitle("invalid-serach-term");
		input.setPpid("invalid-search-term");
		
		ResponseEvent<VisitDetail> resp = cprSvc.addVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, VisitErrorCode.SITE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/add-visits-test-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void addVisitsTestInvalidPpid() {
		VisitDetail input = VisitsTestData.getVisitDetail();
		input.setCprId(null);
		input.setEventId(null);
		
		input.setCpTitle("default-cp");
		input.setPpid("invalid-ppid");
		input.setEventLabel("invalid-cpl");
		ResponseEvent<VisitDetail> resp = cprSvc.addVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CP_AND_PPID, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, CpeErrorCode.LABEL_NOT_FOUND, ErrorType.USER_ERROR);
	}

	/*
	 * Get Visits API Tests
	 */
	@Test
	@DatabaseSetup("cp-test/visits-test/get-visits-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getVisits() {
		VisitsListCriteria input = new VisitsListCriteria();
		input.cprId(1L);
		input.includeStat(true);
		
		ResponseEvent<List<VisitSummary>> resp = cprSvc.getVisits(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("Mismatch in Visist!", (int)6, resp.getPayload().size());
		
		for (VisitSummary visit : resp.getPayload()) {
			Long visitId = visit.getId();
			Assert.assertNotNull(visitId);
			Assert.assertEquals(visitId, visit.getEventId());
			Assert.assertEquals(new String("scg-"+ visitId), visit.getName());
			Assert.assertEquals(new String("Visit"+ visitId), visit.getEventLabel());
			Assert.assertEquals((int) (visit.getEventId().intValue() * 10) , visit.getEventPoint());
			Assert.assertEquals("Complete", visit.getStatus());
			
			Date registrationDate = CommonUtils.getDate(31,1,2001);
			Calendar cal = Calendar.getInstance();
			cal.setTime(registrationDate);
			cal.add(Calendar.DAY_OF_YEAR, visit.getEventPoint());
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
		VisitsListCriteria input = new VisitsListCriteria();
		input.cprId(1L);
		input.includeStat(true);
		
		ResponseEvent<List<VisitSummary>> resp = cprSvc.getVisits(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(0), new Integer(resp.getPayload().size()));
	}
	
	@Test 
	@DatabaseSetup("cp-test/visits-test/get-visits-for-inactive-cpr-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getVisitsForInactiveCpr() {
		VisitsListCriteria input = new VisitsListCriteria();
		input.cprId(1L);
		input.includeStat(true);
		
		ResponseEvent<List<VisitSummary>> resp = cprSvc.getVisits(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(0), new Integer(resp.getPayload().size()));
	}
	
	//TODO: Check with VP whether disabled to be shown 
	//@Test
	@DatabaseSetup("cp-test/visits-test/get-visits-some-visits-disabled-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void getVisitsSomeVisitsDisabled() {
		VisitsListCriteria input = new VisitsListCriteria();
		input.cprId(1L);
		input.includeStat(true);
		
		ResponseEvent<List<VisitSummary>> resp = cprSvc.getVisits(getRequest(input));
		
		//req.setIncludeStats(false);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("Mismatch in Visist!", (int)6, resp.getPayload().size());
		
		for (VisitSummary visit : resp.getPayload()) {
			Long visitId = visit.getId();
			Assert.assertNotNull(visitId);
			Assert.assertEquals(visitId, visit.getEventId());
			Assert.assertEquals(new String("scg-"+ visitId), visit.getName());
			Assert.assertEquals(new String("Visit"+ visitId), visit.getEventLabel());
			Assert.assertEquals((int) (visit.getEventId().intValue() * 10) , visit.getEventPoint());
			Assert.assertEquals("Complete", visit.getStatus());
			
			Date registrationDate = CommonUtils.getDate(31,1,2001);
			Calendar cal = Calendar.getInstance();
			cal.setTime(registrationDate);
			cal.add(Calendar.DAY_OF_YEAR, visit.getEventPoint());
			Assert.assertEquals(cal.getTime(), visit.getAnticipatedVisitDate());
		}
	}
}