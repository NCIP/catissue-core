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
import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;

import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.CommonUtils;
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
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Add Specimen Requirement Api Tests
	 */
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/add-sr-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/add-sr-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addSpecimenRequirementTest() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.addSpecimenRequirement(
							getRequest(SpecimenRequirementTestData.getSpecimenRequirementDetail()));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		SpecimenRequirementDetail expected = SpecimenRequirementTestData.getSpecimenRequirementDetail();
		SpecimenRequirementDetail actual = resp.getPayload();
		
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
	@DatabaseSetup("specimen-test/specimen-requirement-test/add-sr-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void addSpecimenRequirementInvalidAttributes() {
		SpecimenRequirementDetail input = new SpecimenRequirementDetail();
		input.setCollector(CommonUtils.getUser(-1L, "", "",""));
		input.setReceiver(CommonUtils.getUser(-2L, "", "",""));
		input.setEventId(-1L);
		input.setInitialQty(-2.2D);
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.addSpecimenRequirement(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		
		TestUtils.checkErrorCode(resp, SrErrorCode.SPECIMEN_CLASS_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.SPECIMEN_TYPE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.ANATOMIC_SITE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.LATERALITY_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.PATHOLOGY_STATUS_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.STORAGE_TYPE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.COLLECTOR_NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.COLL_PROC_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.COLL_CONT_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.RECEIVER_NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.INVALID_QTY, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/add-sr-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void addSpecimenRequirementMissingAttributes() {
		SpecimenRequirementDetail input = SpecimenRequirementTestData.getSpecimenRequirementDetail();
		input.setCollector(CommonUtils.getUser(null, "", "",""));
		input.setReceiver(null);
		input.setEventId(null);
		input.setConcentration(-1.3D);
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.addSpecimenRequirement(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		
		TestUtils.checkErrorCode(resp, SrErrorCode.COLLECTOR_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.RECEIVER_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.CPE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.CONCENTRATION_REQUIRED, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get Specimen Requirements API Tests
	 */
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/get-sr-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void getSpecimenRequirementsTest() {
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.getSpecimenRequirments(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		
		Assert.assertEquals("Invalid number specimen requirements size", 
				new Integer(3), new Integer(resp.getPayload().size()));
		
		for (SpecimenRequirementDetail actual : resp.getPayload()) {
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
	public void getSpecimenRequirementsForNonExistingCpe() {
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.getSpecimenRequirments(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpeErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/get-sr-for-event-with-empty-sr-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void getSRForEventWithEmptySRList() {
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.getSpecimenRequirments(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(0), new Integer(resp.getPayload().size()));
	}

	/*
	 * Get Specimen Requirement Test API's
	 */
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/get-sr-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void getSpecimenRequirementTest() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.getSpecimenRequirement(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		
		SpecimenRequirementDetail actual = resp.getPayload();
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

	@Test
	public void getSpecimenRequirementForNonExistingSR() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.getSpecimenRequirement(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/get-sr-with-disable-activity-status-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void getSpecimenRequirementWithDisabledActivityStatus() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.getSpecimenRequirement(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Create Aliquot API Tests
	 */
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/create-sr-aliquot-expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSRAliquot() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("Aliquot count mismatch!", new Integer(4),	new Integer(resp.getPayload().size()));
		
		for (SpecimenRequirementDetail actual : resp.getPayload()) {
			Assert.assertEquals("Anatomic Site mismatch","Head", actual.getAnatomicSite());
			Assert.assertEquals("Collection container mismatch", "default-container", actual.getCollectionContainer());
			Assert.assertEquals("Collection procedure mismatch","default-procedure", actual.getCollectionProcedure());
			Assert.assertEquals("Label format mismatch", input.getLabelFmt(), actual.getLabelFmt());
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
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void createSRAliquotWithInSufficientInitialQuantity() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		input.setNoOfAliquots(200);
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.INSUFFICIENT_QTY, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-with-disabled-parent-sr-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void createSRAliquotWithDisabledParentSR() {
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(
				getRequest(SpecimenRequirementTestData.getAliquotSpecimensRequirement()));

		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void createSRAliquotWithNonExistingParentSR() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		input.setParentSrId(111L);
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void createSRAliquotWithInvalidParentSRId() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		input.setParentSrId(-1L);
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void createSRAliquotWithoutParentSRId() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		input.setParentSrId(null);
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/create-sr-aliquot-inherit-parent-specimen-label-expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSRAliquotInheritParentSpecimenLabel() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		input.setLabelFmt(null);
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("Aliquot count mismatch!", new Integer(input.getNoOfAliquots()), new Integer(resp.getPayload().size()));
		
		for (SpecimenRequirementDetail actual : resp.getPayload()) {
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
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void createSRAliquotWithInInvalidInitialQuantity() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		input.setQtyPerAliquot(-0.1D);
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.INVALID_QTY, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void createSRAliquotWithInInvalidNumOfAliquots() {
		AliquotSpecimensRequirement input = SpecimenRequirementTestData.getAliquotSpecimensRequirement();
		input.setNoOfAliquots(0);
		
		ResponseEvent<List<SpecimenRequirementDetail>> resp = cpSvc.createAliquots(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.INVALID_ALIQUOT_CNT, ErrorType.USER_ERROR);
	}
	
	/*
	 * Create Derived SR API Tests
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/create-derived-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDerived() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(
				getRequest(SpecimenRequirementTestData.getDerivedSpecimenRequirement()));
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		
		Assert.assertNotNull(resp.getPayload());
		
		SpecimenRequirementDetail detail = resp.getPayload();

		Assert.assertEquals("Label mismatch", "derived-label", detail.getName());
		Assert.assertEquals("Label format mismatch", "derived-label-format", detail.getLabelFmt());
		assertSRDetail(detail);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void createDerivedWithInvalidInitialQuantity() {
		DerivedSpecimenRequirement input = SpecimenRequirementTestData.getDerivedSpecimenRequirement();
		input.setQuantity(-0.5);
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(input));
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.INVALID_QTY, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-with-disabled-parent-sr-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void createDerivedWithDisabledParentSR() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(
				getRequest(SpecimenRequirementTestData.getDerivedSpecimenRequirement()));
		
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void createDerivedWithNonExistingParentSR() {
		DerivedSpecimenRequirement input = SpecimenRequirementTestData.getDerivedSpecimenRequirement();
		input.setParentSrId(111L);
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void createDerivedWithInvalidParentSRId() {
		DerivedSpecimenRequirement input = SpecimenRequirementTestData.getDerivedSpecimenRequirement();
		input.setParentSrId(-1L);
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(input));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void createDerivedWithoutParentSRId() {
		DerivedSpecimenRequirement input = SpecimenRequirementTestData.getDerivedSpecimenRequirement();
		input.setParentSrId(null);
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.PARENT_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/create-derived-with-inherit-parent-specimen-label-expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDerivedWithoutParentSpecimenLabel() {
		DerivedSpecimenRequirement input = SpecimenRequirementTestData.getDerivedSpecimenRequirement();
		input.setLabelFmt("");
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		
		SpecimenRequirementDetail detail = resp.getPayload();

		Assert.assertEquals("Label mismatch", "derived-label", detail.getName());
		Assert.assertEquals("Label format mismatch", "default-label-format", detail.getLabelFmt());
		assertSRDetail(detail);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/create-derived-with-inherit-parent-name-expected.xml", 
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDerivedWithoutName() {
		DerivedSpecimenRequirement input = SpecimenRequirementTestData.getDerivedSpecimenRequirement();
		input.setName("");
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		
		SpecimenRequirementDetail detail = resp.getPayload();

		Assert.assertEquals("Label mismatch", "default-label", detail.getName());
		Assert.assertEquals("Label format mismatch", "derived-label-format", detail.getLabelFmt());
		assertSRDetail(detail);		
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/create-sr-aliquot-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void createDerivedWithMissingAttributeValues() {
		DerivedSpecimenRequirement input = SpecimenRequirementTestData.getDerivedSpecimenRequirement();
		input.setSpecimenClass("");
		input.setStorageType("");
		input.setQuantity(null);
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.createDerived(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.SPECIMEN_CLASS_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.STORAGE_TYPE_REQUIRED, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SrErrorCode.INVALID_QTY, ErrorType.USER_ERROR);
	}
	
	public void assertSRDetail(SpecimenRequirementDetail detail) {
		Assert.assertEquals("Anatomic Site mismatch", "Head", detail.getAnatomicSite());
		Assert.assertEquals("Collection container mismatch", "default-container", detail.getCollectionContainer());
		Assert.assertEquals("Collection procedure mismatch", "default-procedure", detail.getCollectionProcedure());
		Assert.assertEquals("Laterality mismatch" , "Right", detail.getLaterality());
		Assert.assertEquals("Lineage mismatch", "Derived", detail.getLineage());
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
	
	/*
	 * Copy Specimen Requirement Test API's
	 */
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/copy-sr-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/copy-sr-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void copySpecimenRequirementTest() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.copySpecimenRequirement(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		SpecimenRequirementDetail expected = SpecimenRequirementTestData.getSpecimenRequirementDetail();
		SpecimenRequirementDetail actual = resp.getPayload();
		
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
	@DatabaseSetup("specimen-test/specimen-requirement-test/copy-sr-with-child-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/copy-sr-with-child-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void copySpecimenRequirementWithChildTest() {
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.copySpecimenRequirement(getRequest(1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		SpecimenRequirementDetail expected = SpecimenRequirementTestData.getSpecimenRequirementDetail();
		SpecimenRequirementDetail actual = resp.getPayload();
		
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
	public void copySRWithInvalidId() {
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.copySpecimenRequirement(getRequest(-1L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/copy-sr-aliquot-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-requirement-test/copy-sr-aliquot-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void copySRWithAliquotLineage() {
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.copySpecimenRequirement(getRequest(2L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals("Aliquot", resp.getPayload().getLineage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-requirement-test/copy-sr-aliquot-initial-quantity-low-test-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-requirement-test/generic-teardown.xml")
	public void copySRWithAliquotLineageAndInvalidInitialQuantity() {
		
		ResponseEvent<SpecimenRequirementDetail> resp = cpSvc.copySpecimenRequirement(getRequest(3L));
		TestUtils.recordResponse(resp);

		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SrErrorCode.INSUFFICIENT_QTY, ErrorType.USER_ERROR);
	}
}