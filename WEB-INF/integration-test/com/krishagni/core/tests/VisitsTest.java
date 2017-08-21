package com.krishagni.core.tests;

import java.text.SimpleDateFormat;
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
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
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
	private VisitService visitService;
	
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
		Assert.assertEquals("Complete", resp.getPayload().getStatus());
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
	
	/*
	 * Update Visit API's Test
	 */
	
	public void assertVisitDetail(VisitDetail detail){
		Assert.assertEquals("Error: Event id mismatch", (Long)2L, detail.getEventId());
		Assert.assertEquals("Error: CPR id mismatch", (Long)2L, detail.getCprId());
		Assert.assertEquals("Error: Pathology number mismatch", "updated-pathology", detail.getSurgicalPathologyNumber());
		Assert.assertEquals("Error: Clinical diagnosis mismatch", "updated-daiagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("Error: Status mismatch", "Complete", detail.getStatus());
		Assert.assertEquals("Error: Site mismatch", "SITE2", detail.getSite());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertEquals("Error: Clinical status mismatch", "updated-status", detail.getClinicalStatus());
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/update-visit-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateVisitsTest() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response was found null", resp.getPayload());
		Assert.assertEquals("Error: Name mismatch", "updated-visit", resp.getPayload().getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals("Error: Visit date mismatch", formatter.format(CommonUtils.getDate(21,1,2014)), formatter.format(resp.getPayload().getVisitDate()));
		assertVisitDetail(resp.getPayload());
	}
	
	@Test
	public void updateVisitWhichIsNotPresent() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, VisitErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/update-visit-with-null-name-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateVisitsTestWithNullName() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setName(null);
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response was found null", resp.getPayload());
		Assert.assertEquals("Error: Name mismatch", "default-visit", resp.getPayload().getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals("Error: Visit date mismatch", formatter.format(CommonUtils.getDate(21,1,2014)), formatter.format(resp.getPayload().getVisitDate()));
		assertVisitDetail(resp.getPayload());
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithDuplicateName() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setName("duplicate-visit");
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, VisitErrorCode.DUP_NAME, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/update-visit-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateVisitsTestWithCpTitleAndEventLabel() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setEventId(null);
		input.setCpTitle("default-cp");
		input.setEventLabel("Visit2");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response was found null", resp.getPayload());
		Assert.assertEquals("Error: Name mismatch", "updated-visit", resp.getPayload().getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals("Error: Visit date mismatch", formatter.format(CommonUtils.getDate(21,1,2014)), formatter.format(resp.getPayload().getVisitDate()));
		assertVisitDetail(resp.getPayload());
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidCpId() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setEventId(-1L);
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidCpTitleButValidEventLabel() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setEventId(null);
		input.setCpTitle("invalid-cp");
		input.setEventLabel("Visit2");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidEventLabel() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setEventId(null);
		input.setCpTitle("default-cp");
		input.setEventLabel("invalid-label");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, CpeErrorCode.LABEL_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/update-visit-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateVisitsTestWithCpTitleAndPpId() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setCprId(null);
		input.setCpTitle("default-cp");
		input.setPpid("default-gen-ppid-2");
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response was found null", resp.getPayload());
		Assert.assertEquals("Error: Name mismatch", "updated-visit", resp.getPayload().getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals("Error: Visit date mismatch", formatter.format(CommonUtils.getDate(21,1,2014)), formatter.format(resp.getPayload().getVisitDate()));
		assertVisitDetail(resp.getPayload());
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidCprId() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setCprId(-1L);
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, CprErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidCpTitleButValidPpId() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setCprId(null);
		input.setCpTitle("invalid-title");
		input.setPpid("default-gen-ppid-2");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidPpId() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setCprId(null);
		input.setCpTitle("default-cp");
		input.setPpid("invalid-ppid");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CP_AND_PPID, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-with-cpr-and-cpe-mismatch.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithCprAndCpeMismatch() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setCprId(1L);
		input.setEventId(2L);
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, CprErrorCode.INVALID_CPE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/update-visit-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateVisitsTestWithNullVisitDate() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setVisitDate(null);
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response was found null", resp.getPayload());
		Assert.assertEquals("Error: Name mismatch", "updated-visit", resp.getPayload().getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals("Error: Visit date mismatch", formatter.format(new Date()), formatter.format(resp.getPayload().getVisitDate()));
		assertVisitDetail(resp.getPayload());
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidVisitDate() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setVisitDate(CommonUtils.getDate(21, 1, 2000));
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, VisitErrorCode.INVALID_VISIT_DATE, ErrorType.USER_ERROR);
	}
	
	// TODO: Uncomment this test cases when isValidPv() works fine.
	//@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidVisitStatus() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setStatus("invalid-status");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, VisitErrorCode.INVALID_STATUS, ErrorType.USER_ERROR);
	}
	
	// TODO: Uncomment this test cases when isValidPv() works fine.
	//@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidClinicalDiagnosis() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setClinicalDiagnosis("invalid-diagnosis");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, VisitErrorCode.INVALID_CLINICAL_DIAGNOSIS, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithStatusCompleteAndWithoutSiteName() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setId(2L);
		input.setSite("");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, VisitErrorCode.SITE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidSite(){
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setSite("invalid-site");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/visits-test/update-visit-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateVisitsTestWithNullActivityStatus() {
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setActivityStatus(null);
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response was found null", resp.getPayload());
		Assert.assertEquals("Error: Name mismatch", "updated-visit", resp.getPayload().getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals("Error: Visit date mismatch", formatter.format(CommonUtils.getDate(21,1,2014)), formatter.format(resp.getPayload().getVisitDate()));
		assertVisitDetail(resp.getPayload());
	}
	
	//TODO: uncomment this after isValidPV() works fine
	//@Test
	@DatabaseSetup("cp-test/visits-test/update-visit-initial.xml")
	@DatabaseTearDown("cp-test/registration-test/generic-teardown.xml")
	public void updateVisitsTestWithInvalidActivityStatus(){
		VisitDetail input = VisitsTestData.getUpdateVisitDetail();
		input.setSite("invalid-activity-status");
		
		ResponseEvent<VisitDetail> resp = visitService.addOrUpdateVisit(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Response was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, ActivityStatusErrorCode.INVALID, ErrorType.USER_ERROR);
	}
}