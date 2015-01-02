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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AddSpecimenRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenRequirementsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementAddedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementsEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CpTestData;
import com.krishagni.core.tests.testdata.SpecimenRequirementTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class SpecimenRequirementTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private CollectionProtocolService cpSvc;
	
	@Autowired
	private ApplicationContext ctx;
	

	/*
	 * Add Specimen Requirement Api Tests
	 */
	@Test
	@DatabaseSetup("SpecimenRequirementTest.addSpecimenRequirementTest.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	@ExpectedDatabase(value="SpecimenRequirementTest.addSpecimenRequirementTest.expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addSpecimenRequirementTest() {
		AddSpecimenRequirementEvent req = SpecimenRequirementTestData.getAddSpecimenRequirementEvent();
		SpecimenRequirementAddedEvent resp = cpSvc.addSpecimenRequirement(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		SpecimenRequirementDetail expected = req.getRequirement();
		SpecimenRequirementDetail actual = resp.getRequirement();
		
		Assert.assertNotNull("Specimen requirement detail was not expected to be null!",actual);
		Assert.assertNotNull(actual.getId());
		
		Assert.assertEquals(expected.getAnatomicSite(), actual.getAnatomicSite());
		Assert.assertEquals(expected.getCollectionContainer(), actual.getCollectionContainer());
		Assert.assertEquals(expected.getCollectionProcedure(), actual.getCollectionProcedure());
		Assert.assertEquals(expected.getLabelFmt(), actual.getLabelFmt());
		Assert.assertEquals(expected.getLaterality(), actual.getLaterality());
		Assert.assertEquals(expected.getLineage(), actual.getLineage());
		Assert.assertEquals(expected.getName(), actual.getName());
		Assert.assertEquals(expected.getPathologyStatus(), actual.getPathologyStatus());
		Assert.assertEquals(expected.getSpecimenClass(), actual.getSpecimenClass());
		Assert.assertEquals(expected.getStorageType(), actual.getStorageType());
		Assert.assertEquals(expected.getType(), actual.getType());
		Assert.assertEquals(expected.getConcentration(), actual.getConcentration());
		Assert.assertEquals(expected.getEventId(), actual.getEventId());
		Assert.assertEquals(expected.getInitialQty(), actual.getInitialQty());
		Assert.assertNotNull(actual.getCollector());
		Assert.assertNotNull(actual.getReceiver());
		Assert.assertEquals(new Long(2), actual.getCollector().getId());
		Assert.assertEquals(new Long(3), actual.getReceiver().getId());
		
		Long collectorId = actual.getCollector().getId();
		String firstName = "ADMIN" + collectorId;
		String lastName = "ADMIN" + collectorId;
		String loginName = "admin" + collectorId + "@admin.com";
		
		UserSummary collector = actual.getCollector();
		Assert.assertEquals(firstName, collector.getFirstName());
		Assert.assertEquals(lastName, collector.getLastName());
		Assert.assertEquals(loginName, collector.getLoginName());
		
		
		Long receiverId = actual.getReceiver().getId();
		firstName = "ADMIN" + collectorId;
		lastName = "ADMIN" + collectorId;
		loginName = "admin" + collectorId + "@admin.com";
		
		UserSummary receiver = actual.getCollector();
		Assert.assertEquals(firstName, receiver.getFirstName());
		Assert.assertEquals(lastName, receiver.getLastName());
		Assert.assertEquals(loginName, receiver.getLoginName());
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.addSpecimenRequirementTest.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void addSpecimenRequirementInvalidAttributes() {
		AddSpecimenRequirementEvent req = SpecimenRequirementTestData.getAddSpecimenRequirementEvent();
		SpecimenRequirementDetail detail = new SpecimenRequirementDetail();
		detail.setCollector(CpTestData.getUser(-1L, "", "",""));
		detail.setReceiver(CpTestData.getUser(-2L, "", "",""));
		detail.setEventId(-1L);
		detail.setInitialQty(-2.2D);
		req.setRequirement(detail);
		
		SpecimenRequirementAddedEvent resp = cpSvc.addSpecimenRequirement(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Expected error: missing specimen-class not found",
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "specimenClass"));
		Assert.assertEquals("Expected error: missing specimen-type not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "specimenType"));
		Assert.assertEquals("Expected error: missing anatomic-site not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "anatomicSite"));
		Assert.assertEquals("Expected error: missing laterality not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "laterality"));
		Assert.assertEquals("Expected error: missing pathology-status not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "pathologyStatus"));
		Assert.assertEquals("Expected error: missing storage type not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "storageType"));
		Assert.assertEquals("Expected error: missing collector details not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "collector"));
		Assert.assertEquals("Expected error: missing collection procedure not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "collectionProcedure"));
		Assert.assertEquals("Expected error: missing collection-container not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "collectionContainer"));
		Assert.assertEquals("Expected error: invalid receiver details not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "receiver"));
		Assert.assertEquals("Expected error: missing event-id not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "eventId"));
		Assert.assertEquals("Expected error: invalid initial quantity not found", 
				true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "initialQty"));
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.addSpecimenRequirementTest.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void addSpecimenRequirementMissingAttributes() {
		AddSpecimenRequirementEvent req = SpecimenRequirementTestData.getAddSpecimenRequirementEvent();
		SpecimenRequirementDetail detail = req.getRequirement();
		detail.setCollector(CpTestData.getUser(null, "", "",""));
		detail.setReceiver(null);
		detail.setEventId(null);
		detail.setConcentration(-1.3D);
		
		SpecimenRequirementAddedEvent resp = cpSvc.addSpecimenRequirement(req);
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "collector"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "receiver"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "eventId"));
		Assert.assertEquals(true, TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "concentration"));
	}
	
	/*
	 * Get Specimen Requirement API Tests
	 */
	@Test
	@DatabaseSetup("SpecimenRequirementTest.getSpecimenRequirementTest.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void getSpecimenRequirementTest() {
		ReqSpecimenRequirementsEvent req = SpecimenRequirementTestData.getReqSpecimenRequirementsEvent();
		SpecimenRequirementsEvent resp = cpSvc.getSpecimenRequirments(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(3), new Integer(resp.getSpecimenRequirements().size()));
		
		for (SpecimenRequirementDetail actual : resp.getSpecimenRequirements()) {
			Assert.assertEquals("Right", actual.getAnatomicSite());
			Assert.assertEquals("default-container", actual.getCollectionContainer());
			Assert.assertEquals("default-procedure", actual.getCollectionProcedure());
			Assert.assertEquals("default-label-format", actual.getLabelFmt());
			Assert.assertEquals("Head", actual.getLaterality());
			//TODO: currently lineage is hard coded as new
			Assert.assertEquals("New", actual.getLineage());
			Assert.assertEquals("default-label", actual.getName());
			Assert.assertEquals("Malignant", actual.getPathologyStatus());
			Assert.assertEquals("Molecular", actual.getSpecimenClass());
			Assert.assertEquals("default-storage", actual.getStorageType());
			Assert.assertEquals("Plasma", actual.getType());
			Assert.assertEquals(new Double(0.5), actual.getConcentration());
			Assert.assertEquals(new Long(1), actual.getEventId());
			Assert.assertEquals(new Double(1.1), actual.getInitialQty());
			Assert.assertNotNull(actual.getCollector());
			Assert.assertNotNull(actual.getReceiver());
			Assert.assertEquals(new Long(2), actual.getCollector().getId());
			Assert.assertEquals(new Long(3), actual.getReceiver().getId());
			
			Long collectorId = actual.getCollector().getId();
			String firstName = "ADMIN" + collectorId;
			String lastName = "ADMIN" + collectorId;
			String loginName = "admin" + collectorId + "@admin.com";
			
			UserSummary collector = actual.getCollector();
			Assert.assertEquals(firstName, collector.getFirstName());
			Assert.assertEquals(lastName, collector.getLastName());
			Assert.assertEquals(loginName, collector.getLoginName());
			
			
			Long receiverId = actual.getReceiver().getId();
			firstName = "ADMIN" + collectorId;
			lastName = "ADMIN" + collectorId;
			loginName = "admin" + collectorId + "@admin.com";
			
			UserSummary receiver = actual.getCollector();
			Assert.assertEquals(firstName, receiver.getFirstName());
			Assert.assertEquals(lastName, receiver.getLastName());
			Assert.assertEquals(loginName, receiver.getLoginName());
		}
	}
	
	@Test
	public void getSpecimenRequirementForNonExistingCpe() {
		ReqSpecimenRequirementsEvent req = SpecimenRequirementTestData.getReqSpecimenRequirementsEvent();
		SpecimenRequirementsEvent resp = cpSvc.getSpecimenRequirments(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpeId(), resp.getCpeId());
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.getSRForEventWithEmptySRList.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void getSRForEventWithEmptySRList() {
		ReqSpecimenRequirementsEvent req = SpecimenRequirementTestData.getReqSpecimenRequirementsEvent();
		SpecimenRequirementsEvent resp = cpSvc.getSpecimenRequirments(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(0), new Integer(resp.getSpecimenRequirements().size()));
	}
}
