package com.krishagni.core.tests;

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
import com.krishagni.catissueplus.core.biospecimen.events.ReqCpeListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CpeAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AddCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpeListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CpeUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
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
	
	private final String EVENT_LABEL = "eventLabel";
	
	private final String DEFAULT_SITE = "defaultSite";
	
	private final String EVENT_POINT = "eventPoint";
	
	private final String COLLECTION_PROTOCOL = "collectionProtocol";
	
	/* 
	 * Get CollectionProtocolEvents API Tests 
	 */
	
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEvents() {
		ReqCpeListEvent req = CpeTestData.getCpeList();
		
		CpeListEvent resp = cpSvc.getProtocolEvents(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());		
		Assert.assertNotNull(resp.getEvents());
		Assert.assertEquals((int)4, resp.getEvents().size());
		
		for (CollectionProtocolEventDetail event : resp.getEvents()) {			
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
		ReqCpeListEvent req = CpeTestData.getCpeList();
		req.setCpId(11L);
		
		CpeListEvent resp = cpSvc.getProtocolEvents(req);
		TestUtils.recordResponse(resp);

		Assert.assertEquals("Events with given CP was not found in response.", EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEventsForDisabledCp() {
		ReqCpeListEvent req = CpeTestData.getCpeList();
		req.setCpId(2L);
		
		CpeListEvent resp = cpSvc.getProtocolEvents(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Events with disabled CP was not found in response.", EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertNotNull(resp.getCpId());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
		Assert.assertNull(resp.getEvents());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/get-event-list-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void getEventsForCpWithNoEvents() {
		ReqCpeListEvent req = CpeTestData.getCpeList();
		req.setCpId(3L);
		
		CpeListEvent resp = cpSvc.getProtocolEvents(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
		Assert.assertNotNull(resp);
		Assert.assertEquals((int)0, resp.getEvents().size());
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
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolEventDetail detail = resp.getCpe();
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
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setEventLabel("duplicate-event-label");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals("Duplicate event label was not found in response.", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Event label not unique", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithoutEventLabel() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setEventLabel("");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Event label was null in the response.", true, TestUtils.isErrorCodePresent(resp,CpErrorCode.MISSING_EVENT_LABEL, EVENT_LABEL));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithInvalidSite() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setDefaultSite("invalid-site");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid site was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_SITE, DEFAULT_SITE));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithInvalidEventPoint() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setEventPoint(-1.0);
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid event point was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_EVENT_POINT, EVENT_POINT));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithInvalidCP() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setCollectionProtocol("invalid-cp");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid collection protocol was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithoutCP() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setCollectionProtocol("");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Collection protocol was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/add-event-with-disabled-cp-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void addEventWithDisabledCP() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setCollectionProtocol("disabled-cp");

		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);

		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Collection protocol was disabled in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
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
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		
		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);
				
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolEventDetail detail = resp.getCpe();
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
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithDuplicateEventLabel() {
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		req.getCpe().setEventLabel("duplicate-event-label");

		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals("Duplicate event label was not found in response.", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Event label not unique", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithoutEventLabel() {
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		req.getCpe().setEventLabel("");
		
		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Event label was null in the response.", true, TestUtils.isErrorCodePresent(resp,CpErrorCode.MISSING_EVENT_LABEL, EVENT_LABEL));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithInvalidSite() {
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		req.getCpe().setDefaultSite("invalid-site");
		
		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid site was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_SITE, DEFAULT_SITE));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithInvalidEventPoint() {
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		req.getCpe().setEventPoint(-1.0);
		
		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid event point was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_EVENT_POINT, EVENT_POINT));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithInvalidCP() {
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		req.getCpe().setCollectionProtocol("invalid-cp");
		
		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid collection protocol was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/generic-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithoutCP() {
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		req.getCpe().setCollectionProtocol("");
		
		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Collection protocol was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("cp-test/events-test/add-event-with-disabled-cp-initial.xml")
	@DatabaseTearDown("cp-test/events-test/generic-teardown.xml")
	public void updateEventWithDisabledCP() {
		UpdateCpeEvent req = CpeTestData.getUpdateCpeEvent();
		req.getCpe().setCollectionProtocol("disabled-cp");

		CpeUpdatedEvent resp = cpSvc.updateEvent(req);
		TestUtils.recordResponse(resp);

		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Collection protocol was disabled in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}
}