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
import com.krishagni.catissueplus.core.biospecimen.events.AliquotsRequirementCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotsRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateDerivedSpecimenReqEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DerivedSpecimenReqCreatedEvent;
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
		Assert.assertEquals("Invalid number specimen requirements size", 
				new Integer(3), new Integer(resp.getSpecimenRequirements().size()));
		
		for (SpecimenRequirementDetail actual : resp.getSpecimenRequirements()) {
			Assert.assertEquals("Head", actual.getAnatomicSite());
			Assert.assertEquals("default-container", actual.getCollectionContainer());
			Assert.assertEquals("default-procedure", actual.getCollectionProcedure());
			Assert.assertEquals("default-label-format", actual.getLabelFmt());
			Assert.assertEquals("Right", actual.getLaterality());
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
	
	/*
	 * Create Aliquot API Tests
	 */
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	@ExpectedDatabase(value="SpecimenRequirementTest.createSRAliquot.expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSRAliquot() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals("Aliquot count mismatch!", new Integer(req.getRequirement().getNoOfAliquots()), 
				new Integer(resp.getAliquots().size()));
		
		for (SpecimenRequirementDetail actual : resp.getAliquots()) {
			Assert.assertEquals("Anatomic Site mismatch","Head", actual.getAnatomicSite());
			Assert.assertEquals("Collection container mismatch", "default-container", actual.getCollectionContainer());
			Assert.assertEquals("Collection procedure mismatch","default-procedure", actual.getCollectionProcedure());
			Assert.assertEquals("Label format mismatch", req.getRequirement().getLabelFmt(), actual.getLabelFmt());
			Assert.assertEquals("Laterality mismatch" , "Right", actual.getLaterality());
			Assert.assertEquals("Lineage mismatch", "Aliquot", actual.getLineage());
			Assert.assertEquals("label mismatch", "default-label", actual.getName());
			Assert.assertEquals("Pathology status mismatch" , "Malignant", actual.getPathologyStatus());
			Assert.assertEquals("Specimen class missmatch", "Molecular", actual.getSpecimenClass());
			Assert.assertEquals("Storage type mismatch","Manual", actual.getStorageType());
			Assert.assertEquals("type mismatch", "Plasma", actual.getType());
			Assert.assertEquals("Concentration mismatch", new Double(0.5), actual.getConcentration());
			Assert.assertEquals("Event id mismatch", new Long(1), actual.getEventId());
			Assert.assertEquals("initial quantity mismatch" , new Double(0.1), actual.getInitialQty());
			Assert.assertNotNull("Collector is null!", actual.getCollector());
			Assert.assertNotNull("Receiver is null", actual.getReceiver());
			Assert.assertEquals("Collector id mismatch", new Long(2), actual.getCollector().getId());
			Assert.assertEquals("Receiver id mismatch" , new Long(3), actual.getReceiver().getId());
			
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
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void createSRAliquotWithInSufficientInitialQuantity() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		req.getRequirement().setNoOfAliquots(200);
		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error insufficient initial quantity not found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INSUFFICIENT_SPECIMEN_QTY, "count"));
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquotWithDisabledParentSR.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void createSRAliquotWithDisabledParentSR() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement's activity status disabled not found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	public void createSRAliquotWithNonExistingParentSR() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		req.getRequirement().setParentSrId(111L);
		
		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);

		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement was found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	public void createSRAliquotWithInvalidParentSRId() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		req.getRequirement().setParentSrId(-1L);

		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement was not invalid!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	public void createSRAliquotWithoutParentSRId() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		req.getRequirement().setParentSrId(null);

		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);

		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement was not found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	@ExpectedDatabase(value="SpecimenRequirementTest.createSRAliquotInheritParentSpecimenLabel.expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSRAliquotInheritParentSpecimenLabel() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		req.getRequirement().setLabelFmt(null);
		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals("Aliquot count mismatch!", new Integer(req.getRequirement().getNoOfAliquots()), 
				new Integer(resp.getAliquots().size()));
		
		for (SpecimenRequirementDetail actual : resp.getAliquots()) {
			Assert.assertEquals("Anatomic Site mismatch","Head", actual.getAnatomicSite());
			Assert.assertEquals("Collection container mismatch", "default-container", actual.getCollectionContainer());
			Assert.assertEquals("Collection procedure mismatch","default-procedure", actual.getCollectionProcedure());
			Assert.assertEquals("Label format mismatch", "default-label-format", actual.getLabelFmt());
			Assert.assertEquals("Laterality mismatch" , "Right", actual.getLaterality());
			Assert.assertEquals("Lineage mismatch", "Aliquot", actual.getLineage());
			Assert.assertEquals("label mismatch", "default-label", actual.getName());
			Assert.assertEquals("Pathology status mismatch" , "Malignant", actual.getPathologyStatus());
			Assert.assertEquals("Specimen class missmatch", "Molecular", actual.getSpecimenClass());
			Assert.assertEquals("Storage type mismatch","Manual", actual.getStorageType());
			Assert.assertEquals("type mismatch", "Plasma", actual.getType());
			Assert.assertEquals("Concentration mismatch", new Double(0.5), actual.getConcentration());
			Assert.assertEquals("Event id mismatch", new Long(1), actual.getEventId());
			Assert.assertEquals("initial quantity mismatch" , new Double(0.1), actual.getInitialQty());
			Assert.assertNotNull("Collector is null!", actual.getCollector());
			Assert.assertNotNull("Receiver is null", actual.getReceiver());
			Assert.assertEquals("Collector id mismatch", new Long(2), actual.getCollector().getId());
			Assert.assertEquals("Receiver id mismatch" , new Long(3), actual.getReceiver().getId());
			
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
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void createSRAliquotWithInInvalidInitialQuantity() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		req.getRequirement().setQtyPerAliquot(-0.1D);
		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error invalid initial quantity for aliquots not found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "qtyPerAliquot"));
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void createSRAliquotWithInInvalidNumOfAliquots() {
		CreateAliquotsRequirementEvent req = SpecimenRequirementTestData.getCreateAliquotsRequirementEvent();
		req.getRequirement().setNoOfAliquots(0);
		AliquotsRequirementCreatedEvent resp = cpSvc.createAliquots(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error insufficient initial quantity not found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "noOfAliquots"));
	}
	
	/*
	 * Create Derived SR API Tests
	 */
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	@ExpectedDatabase(value="SpecimenRequirementTest.createDerived.expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDerived() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull(resp.getDerived());
		
		SpecimenRequirementDetail detail = resp.getDerived();

		Assert.assertEquals("Anatomic Site mismatch", "Head", detail.getAnatomicSite());
		Assert.assertEquals("Collection container mismatch", "default-container", detail.getCollectionContainer());
		Assert.assertEquals("Collection procedure mismatch", "default-procedure", detail.getCollectionProcedure());
		Assert.assertEquals("Label format mismatch", "derived-label-format", detail.getLabelFmt());
		Assert.assertEquals("Laterality mismatch" , "Right", detail.getLaterality());
		Assert.assertEquals("Lineage mismatch", "Derived", detail.getLineage());
		Assert.assertEquals("Label mismatch", "derived-label", detail.getName());
		Assert.assertEquals("Pathology status mismatch" , "Malignant", detail.getPathologyStatus());
		Assert.assertEquals("Specimen class mismatch", "Molecular", detail.getSpecimenClass());
		Assert.assertEquals("Storage type mismatch", "Manual", detail.getStorageType());
		Assert.assertEquals("Type mismatch", "Saliva", detail.getType());		
		Assert.assertEquals("Concentration mismatch", 0.1, detail.getConcentration(), 0);
		Assert.assertEquals("Event id mismatch", new Long(1), detail.getEventId());
		Assert.assertEquals("Initial quantity mismatch", 0.1, detail.getInitialQty(), 0);
		
		Assert.assertNotNull(detail.getCollector());
		Long collectorId = detail.getCollector().getId();
		String firstName = "ADMIN" + collectorId;
		String lastName = "ADMIN" + collectorId;
		String loginName = "admin" + collectorId + "@admin.com";
		Assert.assertEquals(firstName, detail.getCollector().getFirstName());
		Assert.assertEquals(lastName, detail.getCollector().getLastName());
		Assert.assertEquals(loginName, detail.getCollector().getLoginName());
		
		Assert.assertNotNull(detail.getReceiver());
		Long receiverId = detail.getReceiver().getId();
		firstName = "ADMIN" + receiverId;
		lastName = "ADMIN" + receiverId;
		loginName = "admin" + receiverId + "@admin.com";
		Assert.assertEquals(firstName, detail.getReceiver().getFirstName());
		Assert.assertEquals(lastName, detail.getReceiver().getLastName());
		Assert.assertEquals(loginName, detail.getReceiver().getLoginName());
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void createDerivedWithInvalidInitialQuantity() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		req.getRequirement().setQuantity(-0.5);

		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error initial quantity was valid!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "initialQty"));
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquotWithDisabledParentSR.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void createDerivedWithDisabledParentSR() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement was not disabled!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	public void createDerivedWithNonExistingParentSR() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		req.getRequirement().setParentSrId(111L);
		
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);

		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement was found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	public void createDerivedWithInvalidParentSRId() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		req.getRequirement().setParentSrId(-1L);
		
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement was valid!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	public void createDerivedWithoutParentSRId() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		req.getRequirement().setParentSrId(null);
		
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error parent specimen requirement was found!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId"));
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	@ExpectedDatabase(value="SpecimenRequirementTest.createDerivedWithInheritParentSpecimenLabel.expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDerivedWithoutParentSpecimenLabel() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		req.getRequirement().setLabelFmt("");
		
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull(resp.getDerived());
		
		SpecimenRequirementDetail detail = resp.getDerived();

		Assert.assertEquals("Anatomic Site mismatch", "Head", detail.getAnatomicSite());
		Assert.assertEquals("Collection container mismatch", "default-container", detail.getCollectionContainer());
		Assert.assertEquals("Collection procedure mismatch", "default-procedure", detail.getCollectionProcedure());
		Assert.assertEquals("Label format mismatch", "default-label-format", detail.getLabelFmt());
		Assert.assertEquals("Laterality mismatch" , "Right", detail.getLaterality());
		Assert.assertEquals("Lineage mismatch", "Derived", detail.getLineage());
		Assert.assertEquals("Label mismatch", "derived-label", detail.getName());
		Assert.assertEquals("Pathology status mismatch" , "Malignant", detail.getPathologyStatus());
		Assert.assertEquals("Specimen class mismatch", "Molecular", detail.getSpecimenClass());
		Assert.assertEquals("Storage type mismatch", "Manual", detail.getStorageType());
		Assert.assertEquals("Type mismatch", "Saliva", detail.getType());		
		Assert.assertEquals("Concentration mismatch", 0.1, detail.getConcentration(), 0);
		Assert.assertEquals("Event id mismatch", new Long(1), detail.getEventId());
		Assert.assertEquals("Initial quantity mismatch", 0.1, detail.getInitialQty(), 0);
		
		Assert.assertNotNull(detail.getCollector());
		Long collectorId = detail.getCollector().getId();
		String firstName = "ADMIN" + collectorId;
		String lastName = "ADMIN" + collectorId;
		String loginName = "admin" + collectorId + "@admin.com";
		Assert.assertEquals(firstName, detail.getCollector().getFirstName());
		Assert.assertEquals(lastName, detail.getCollector().getLastName());
		Assert.assertEquals(loginName, detail.getCollector().getLoginName());
		
		Assert.assertNotNull(detail.getReceiver());
		Long receiverId = detail.getReceiver().getId();
		firstName = "ADMIN" + receiverId;
		lastName = "ADMIN" + receiverId;
		loginName = "admin" + receiverId + "@admin.com";
		Assert.assertEquals(firstName, detail.getReceiver().getFirstName());
		Assert.assertEquals(lastName, detail.getReceiver().getLastName());
		Assert.assertEquals(loginName, detail.getReceiver().getLoginName());
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	@ExpectedDatabase(value="SpecimenRequirementTest.createDerivedWithInheritParentName.expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDerivedWithoutName() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		req.getRequirement().setName("");
		
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull(resp.getDerived());
		
		SpecimenRequirementDetail detail = resp.getDerived();

		Assert.assertEquals("Anatomic Site mismatch", "Head", detail.getAnatomicSite());
		Assert.assertEquals("Collection container mismatch", "default-container", detail.getCollectionContainer());
		Assert.assertEquals("Collection procedure mismatch", "default-procedure", detail.getCollectionProcedure());
		Assert.assertEquals("Label format mismatch", "derived-label-format", detail.getLabelFmt());
		Assert.assertEquals("Laterality mismatch" , "Right", detail.getLaterality());
		Assert.assertEquals("Lineage mismatch", "Derived", detail.getLineage());
		Assert.assertEquals("Label mismatch", "default-label", detail.getName());
		Assert.assertEquals("Pathology status mismatch" , "Malignant", detail.getPathologyStatus());
		Assert.assertEquals("Specimen class mismatch", "Molecular", detail.getSpecimenClass());
		Assert.assertEquals("Storage type mismatch", "Manual", detail.getStorageType());
		Assert.assertEquals("Type mismatch", "Saliva", detail.getType());		
		Assert.assertEquals("Concentration mismatch", 0.1, detail.getConcentration(), 0);
		Assert.assertEquals("Event id mismatch", new Long(1), detail.getEventId());
		Assert.assertEquals("Initial quantity mismatch", 0.1, detail.getInitialQty(), 0);
		
		Assert.assertNotNull(detail.getCollector());
		Long collectorId = detail.getCollector().getId();
		String firstName = "ADMIN" + collectorId;
		String lastName = "ADMIN" + collectorId;
		String loginName = "admin" + collectorId + "@admin.com";
		Assert.assertEquals(firstName, detail.getCollector().getFirstName());
		Assert.assertEquals(lastName, detail.getCollector().getLastName());
		Assert.assertEquals(loginName, detail.getCollector().getLoginName());
		
		Assert.assertNotNull(detail.getReceiver());
		Long receiverId = detail.getReceiver().getId();
		firstName = "ADMIN" + receiverId;
		lastName = "ADMIN" + receiverId;
		loginName = "admin" + receiverId + "@admin.com";
		Assert.assertEquals(firstName, detail.getReceiver().getFirstName());
		Assert.assertEquals(lastName, detail.getReceiver().getLastName());
		Assert.assertEquals(loginName, detail.getReceiver().getLoginName());
	}
	
	@Test
	@DatabaseSetup("SpecimenRequirementTest.createSRAliquot.initial.xml")
	@DatabaseTearDown("SpecimenRequirementTest.generic.teardown.xml")
	public void createDerivedWithMissingAttributeValues() {
		CreateDerivedSpecimenReqEvent req = SpecimenRequirementTestData.getCreateDerivedSpecimenReqEvent();
		req.getRequirement().setSpecimenClass("");
		req.getRequirement().setStorageType("");
		req.getRequirement().setQuantity(null);
		
		DerivedSpecimenReqCreatedEvent resp = cpSvc.createDerived(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("The error speciemen class value was not null or empty!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "specimenClass"));
		Assert.assertEquals("The error storage type value was not null or empty!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.MISSING_ATTR_VALUE, "storageType"));
		Assert.assertEquals("The error initial quantity value was not invalid!", true,
				TestUtils.isErrorCodePresent(resp, SpecimenErrorCode.INVALID_ATTR_VALUE, "initialQty"));
	}
}
