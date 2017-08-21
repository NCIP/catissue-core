package com.krishagni.core.tests;

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
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CopyCpeOpDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.CpeTestData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })

@WebAppConfiguration
public class CollectionProtocolEventsTest {
	@Resource 
	private WebApplicationContext webApplicationContext;
	
	@Autowired 
	private CollectionProtocolService cpSvc;
	
	@Autowired 
	private ApplicationContext ctx;
	
	/* 
	 * Get CollectionProtocolEvents API Tests 
	 */
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEvents() {
		ResponseEvent<List<CollectionProtocolEventDetail>> resp = cpSvc.getProtocolEvents(getRequest(1L));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());		
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)4, resp.getPayload().size());
		
		for (CollectionProtocolEventDetail event : resp.getPayload()) {			
			Assert.assertNotNull("was not expected to be null", event.getId());
			Assert.assertEquals("default-clinical-diagnosis", event.getClinicalDiagnosis());
			Assert.assertEquals("default-clinical-status", event.getClinicalStatus());
			Assert.assertEquals("cp1", event.getCollectionProtocol());
			Assert.assertEquals("SITE1", event.getDefaultSite());
			Assert.assertEquals("event-" + event.getId(), event.getEventLabel());
			Assert.assertEquals(event.getId(), event.getEventPoint().doubleValue(), 0);
		}
	}
	
	@Test
	public void getEventsForANonExistingCp() {
		ResponseEvent<List<CollectionProtocolEventDetail>> resp = cpSvc.getProtocolEvents(getRequest(1L));
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Events with given CP was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEventsForDisabledCp() {
		ResponseEvent<List<CollectionProtocolEventDetail>> resp = cpSvc.getProtocolEvents(getRequest(2L));
		TestUtils.recordResponse(resp);
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
		Assert.assertNull(resp.getPayload());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEventsForCpWithNoEvents() {
		ResponseEvent<List<CollectionProtocolEventDetail>> resp = cpSvc.getProtocolEvents(getRequest(3L));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals((int)0, resp.getPayload().size());
	}
	/* 
	 * Get CollectionProtocolEvent API Tests 
	 */
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEvent() {
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.getProtocolEvent(getRequest(1L));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());		
		Assert.assertNotNull(resp.getPayload());
				
		CollectionProtocolEventDetail event = resp.getPayload();
		Assert.assertNotNull("was not expected to be null", event.getId());
		Assert.assertEquals("default-clinical-diagnosis", event.getClinicalDiagnosis());
		Assert.assertEquals("default-clinical-status", event.getClinicalStatus());
		Assert.assertEquals("cp1", event.getCollectionProtocol());
		Assert.assertEquals("SITE1", event.getDefaultSite());
		Assert.assertEquals("event-" + event.getId(), event.getEventLabel());
		Assert.assertEquals(event.getId(), event.getEventPoint().doubleValue(), 0);
		
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEventWithInvalidCPE() {
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.getProtocolEvent(getRequest(-1L));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/* 
	 * Add CollectionProtocolEvents API Tests 
	 */
	
	@Test
	@DatabaseSetup("cp-test/events-test/add-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/add-event-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addEvent() {
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(CpeTestData.getcpEventDetail()));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail.getId());
		Assert.assertNotNull(detail);
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("default-event-label", detail.getEventLabel());
		Assert.assertEquals("default-site", detail.getDefaultSite());
		Assert.assertEquals(1, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("default-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("default-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Active", detail.getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithDuplicateEventLabel() {
		CollectionProtocolEventDetail detail = CpeTestData.getcpEventDetail();
		detail.setEventLabel("duplicate-event-label");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(detail));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		//TODO: invalid label below, please fix it asap
		TestUtils.checkErrorCode(resp, CpeErrorCode.DUP_LABEL, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithoutEventLabel() {
		CollectionProtocolEventDetail detail = CpeTestData.getcpEventDetail();
		detail.setEventLabel("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(detail));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.LABEL_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithInvalidSite() {
		CollectionProtocolEventDetail detail = CpeTestData.getcpEventDetail();
		detail.setDefaultSite("invalid-site");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(detail));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("cp-test/events-test/add-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithEmptySite() {
		CollectionProtocolEventDetail input = CpeTestData.getcpEventDetail();
		input.setDefaultSite("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail.getId());
		Assert.assertNotNull(detail);
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("default-event-label", detail.getEventLabel());
		Assert.assertNull(detail.getDefaultSite());
		Assert.assertEquals(1, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("default-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("default-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Active", detail.getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithInvalidEventPoint() {
		CollectionProtocolEventDetail input = CpeTestData.getcpEventDetail();
		input.setEventPoint(-1.0);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.INVALID_POINT, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("cp-test/events-test/add-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/add-event-with-null-event-point-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addEventWithNullEventPoint() {
		CollectionProtocolEventDetail input = CpeTestData.getcpEventDetail();
		input.setEventPoint(null);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail.getId());
		Assert.assertNotNull(detail);
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("default-event-label", detail.getEventLabel());
		Assert.assertEquals("default-site", detail.getDefaultSite());
		Assert.assertEquals(0, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("default-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("default-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Active", detail.getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithInvalidCP() {
		CollectionProtocolEventDetail input = CpeTestData.getcpEventDetail();
		input.setCollectionProtocol("invalid-cp");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithoutCP() {
		CollectionProtocolEventDetail input = CpeTestData.getcpEventDetail();
		input.setCollectionProtocol("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/add-event-with-disabled-cp-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithDisabledCP() {
		CollectionProtocolEventDetail input = CpeTestData.getcpEventDetail();
		input.setCollectionProtocol("disabled-cp");

		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/add-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/add-event-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addEventWithEmptyActivityStatus() {
		CollectionProtocolEventDetail input = CpeTestData.getcpEventDetail();
		input.setActivityStatus("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.addEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail.getId());
		Assert.assertNotNull(detail);
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("default-event-label", detail.getEventLabel());
		Assert.assertEquals("default-site", detail.getDefaultSite());
		Assert.assertEquals(1, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("default-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("default-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Active", detail.getActivityStatus());
	}
	
	/* 
	 * Update CollectionProtocolEvents API Tests 
	 */
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/update-event-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateEvent() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
				
		Assert.assertNotNull(resp);
		Assert.assertEquals("Operation unsuccessful!", true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull("Payload is null!", detail);
		Assert.assertNotNull("Payload.getId() is null", detail.getId());
		Assert.assertEquals("cp-title mismatch", "default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("label mismatch!", "updated-event-label", detail.getEventLabel());
		Assert.assertEquals("sitename is invalid", "updated-site", detail.getDefaultSite());
		Assert.assertEquals("event-point mismatch",2, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("clinical diagnosis value mismatch","updated-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("clinical status value mismatch", "updated-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Activity status mismatch", "Active", detail.getActivityStatus()); 
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithDuplicateEventLabel() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setEventLabel("duplicate-event-label");

		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		//TODO: invalid errorcode below please fix this ASAP
		TestUtils.checkErrorCode(resp, CpeErrorCode.DUP_LABEL, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithoutEventLabel() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setEventLabel("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.LABEL_REQUIRED, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithInvalidSite() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setDefaultSite("invalid-site");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithEmptySite() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setDefaultSite("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail);
		Assert.assertNotNull(detail.getId());
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("updated-event-label", detail.getEventLabel());
		Assert.assertNull(detail.getDefaultSite());
		Assert.assertEquals(2, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("updated-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("updated-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Active", detail.getActivityStatus()); 
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithInvalidEventPoint() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setEventPoint(-1.0);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.INVALID_POINT, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/update-event-with-null-event-point-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void UpdateEventWithNullEventPoint() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setEventPoint(null);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail);
		Assert.assertNotNull(detail.getId());
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("updated-event-label", detail.getEventLabel());
		Assert.assertEquals("updated-site", detail.getDefaultSite());
		Assert.assertEquals(0, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("updated-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("updated-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Active", detail.getActivityStatus()); 
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithInvalidCP() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setCollectionProtocol("invalid-cp");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithoutCP() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setCollectionProtocol("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/add-event-with-disabled-cp-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithDisabledCP() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setCollectionProtocol("disabled-cp");

		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/update-event-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateEventWithEmptyActivityStatus() {
		CollectionProtocolEventDetail input = CpeTestData.getCpeUpdateDetail();
		input.setActivityStatus("");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.updateEvent(getRequest(input));
		TestUtils.recordResponse(resp);
				
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail);
		Assert.assertNotNull(detail.getId());
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("updated-event-label", detail.getEventLabel());
		Assert.assertEquals("updated-site", detail.getDefaultSite());
		Assert.assertEquals(2, detail.getEventPoint().doubleValue(), 0);
		Assert.assertEquals("updated-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("updated-clinical-status", detail.getClinicalStatus());
		Assert.assertEquals("Active", detail.getActivityStatus()); 
	}
	
	/* 
	 * Copy CollectionProtocolEvents API Tests 
	 */
	
	@Test
	@DatabaseSetup("cp-test/events-test/copy-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/copy-event-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void copyEvent() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		input.setCpe(CpeTestData.getCpeCopyDetail());
		input.setEventId(1L);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail.getId());
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("copy-event-label", detail.getEventLabel());
		Assert.assertEquals("default-clinical-status",detail.getClinicalStatus());
		Assert.assertEquals("default-clinical-diagnosis", detail.getClinicalDiagnosis());
		Assert.assertEquals("default-site", detail.getDefaultSite());
		Assert.assertEquals(1.0, detail.getEventPoint().doubleValue(),0);
		Assert.assertEquals("Active", detail.getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/copy-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithNullEventId() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		input.setCpe(CpeTestData.getCpeCopyDetail());
		input.setEventId(null);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/copy-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithNullEventIdAndEmptyLabel() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		CollectionProtocolEventDetail detail = CpeTestData.getCpeCopyDetail();
		input.setEventLabel("");
		input.setCollectionProtocol("default-cp");
		input.setCpe(detail);
		input.setEventId(null);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/copy-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithNullEventIdAndEmptyCpTitle() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		CollectionProtocolEventDetail detail = CpeTestData.getCpeCopyDetail();
		input.setCpe(detail);
		input.setEventId(null);
		input.setCollectionProtocol("");
		input.setEventLabel("default-event-label");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/copy-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithEventLabelAndCP() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		CollectionProtocolEventDetail detail = CpeTestData.getCpeCopyDetail();
		
		input.setCpe(detail);
		input.setEventId(null);
		input.setCollectionProtocol("default-cp");
		input.setEventLabel("default-event-label");
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/copy-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithDuplicateLabel() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		CollectionProtocolEventDetail detail = CpeTestData.getcpEventDetail();
		input.setCpe(detail);
		input.setEventId(1L);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.DUP_LABEL, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithEmptyEventLabel() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		CollectionProtocolEventDetail detail = CpeTestData.getCpeCopyDetail();
		detail.setEventLabel("");
		input.setEventId(1L);
		input.setCpe(detail);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.LABEL_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithInvalidEventPoint() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		CollectionProtocolEventDetail detail = CpeTestData.getCpeCopyDetail();
		detail.setEventPoint(-1.0);
		input.setEventId(1L);
		input.setCpe(detail);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.INVALID_POINT, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void copyEventWithInvalidSite() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		CollectionProtocolEventDetail detail = CpeTestData.getCpeCopyDetail();
		detail.setDefaultSite("invalid-site");
		input.setEventId(1L);
		input.setCpe(detail);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/copy-event-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	@ExpectedDatabase(value="cp-test/events-test/copy-event-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void copyEventWithNullAndEmptyCpeFields() {
		CopyCpeOpDetail input = new CopyCpeOpDetail();
		input.setCpe(CpeTestData.getCpeCopyDetailWithNullAndEmptyFields());
		input.setEventId(1L);
		
		ResponseEvent<CollectionProtocolEventDetail> resp = cpSvc.copyEvent(getRequest(input));
		TestUtils.recordResponse(resp);
		Assert.assertNotNull(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		CollectionProtocolEventDetail detail = resp.getPayload();
		Assert.assertNotNull(detail.getId());
		Assert.assertEquals("default-cp", detail.getCollectionProtocol());
		Assert.assertEquals("copy-event-label", detail.getEventLabel());
	}
}