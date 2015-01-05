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
import com.krishagni.catissueplus.core.biospecimen.events.CpeAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AddCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
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
	
	@Test
	@DatabaseSetup("CollectionProtocolEventsTest.addEvent.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	@ExpectedDatabase(value="CollectionProtocolEventsTest.addEvent.expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addEvent() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		
		CollectionProtocolEventDetail detail = resp.getCpe();
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
	@DatabaseSetup("CollectionProtocolEventsTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	public void addEventWithDuplicateEventLabel() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setEventLabel("duplicate-event-label");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccess());
		Assert.assertEquals("Duplicate event label was not found in response.", EventStatus.BAD_REQUEST, resp.getStatus());
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolEventsTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	public void addEventWithoutLabel() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setEventLabel("");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Event label was null in the response.", true, TestUtils.isErrorCodePresent(resp,CpErrorCode.MISSING_EVENT_LABEL, EVENT_LABEL));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolEventsTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	public void addEventWithInvalidSite() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setDefaultSite("invalid-site");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid site was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_SITE, DEFAULT_SITE));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolEventsTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	public void addEventWithInvalidEventPoint() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setEventPoint(-1.0);
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid event point was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_EVENT_POINT, EVENT_POINT));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolEventsTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	public void addEventWithInvalidCP() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setCollectionProtocol("invalid-cp");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Invalid collection protocol was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolEventsTest.generic.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	public void addEventWithoutCP() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setCollectionProtocol("");
		
		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Collection protocol was not found in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}
	
	@Test
	@DatabaseSetup("CollectionProtocolEventsTest.addEventWithDisabledCP.initial.xml")
	@DatabaseTearDown("CollectionProtocolEventsTest.generic.teardown.xml")
	public void addEventWithDisabledCP() {
		AddCpeEvent req = CpeTestData.getAddCpeEvent();
		req.getCpe().setCollectionProtocol("disabled-cp");

		CpeAddedEvent resp = cpSvc.addEvent(req);
		TestUtils.recordResponse(resp);

		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Collection protocol was disabled in response.", true, TestUtils.isErrorCodePresent(resp, CpErrorCode.INVALID_CP_TITLE, COLLECTION_PROTOCOL));
	}	
}