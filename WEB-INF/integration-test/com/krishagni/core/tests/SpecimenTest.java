package com.krishagni.core.tests;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

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
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.SpecimenTestData;
import com.krishagni.core.tests.testdata.CommonUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })

@WebAppConfiguration
public class SpecimenTest {
	@Resource 
	private WebApplicationContext webApplicationContext;
	
	@Autowired 
	private SpecimenService specimenSvc;
	
	@Autowired 
	private ApplicationContext ctx;

	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 *  collectSpecimens API test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimens() {
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(SpecimenTestData.getSpecimenList()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals(true, resp.isSuccessful());
		
		Assert.assertEquals("Invalid number: count of specimens", new Integer(2), new Integer(resp.getPayload().size()));
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithEmptyLabel() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLabel(null);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.LABEL_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidSR() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setReqId(-1L);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SrErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidVisit() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setVisitId(-1L);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, VisitErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-with-duplicate-label.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithDuplicateLabel() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLabel("duplicate-label");
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.DUP_LABEL, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidLineage() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLineage("Invalid-Lineage");
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.INVALID_LINEAGE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-with-duplicate-label.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithDuplicateBarcode() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setBarcode("duplicate-barcode");

		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.DUP_BARCODE, ErrorType.USER_ERROR);
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidInitialQuantity() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setInitialQty(-1.0);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.INVALID_QTY, ErrorType.USER_ERROR);
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutInitialQuantity() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setInitialQty(null);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithoutInitialQuantityAndReqId() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setReqId(null);
		input.get(0).setInitialQty(null);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.INVALID_QTY, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithEmptyActivityStatus() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setActivityStatus("");

		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-with-mismatch-visit-id.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithMismatchCpEventId() {
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(SpecimenTestData.getSpecimenList()));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.INVALID_VISIT, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithNullVisitId() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setVisitId(null);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.VISIT_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutLineage() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLineage(null);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}

	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithDerivedLineageAndParentId() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLineage("Derived");
		input.get(0).setParentId(1L);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		Assert.assertEquals("Derived", resp.getPayload().get(0).getLineage());
		Assert.assertEquals(new Long(1), resp.getPayload().get(0).getParentId());
		Assert.assertEquals("parent-spm", resp.getPayload().get(0).getParentLabel());
		
		Assert.assertEquals("New", resp.getPayload().get(1).getLineage());
		Assert.assertNull(resp.getPayload().get(1).getParentId());
		Assert.assertNull(resp.getPayload().get(1).getParentLabel());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}

	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithDerivedLineageAndParentLabel() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLineage("Derived");
		input.get(0).setParentLabel("parent-spm");
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		Assert.assertEquals("Derived", resp.getPayload().get(0).getLineage());
		Assert.assertEquals(new Long(1), resp.getPayload().get(0).getParentId());
		Assert.assertEquals("parent-spm", resp.getPayload().get(0).getParentLabel());
		
		Assert.assertEquals("New", resp.getPayload().get(1).getLineage());
		Assert.assertNull(resp.getPayload().get(1).getParentId());
		Assert.assertNull(resp.getPayload().get(1).getParentLabel());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectAdhocSpecimen() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setReqId(null);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		Assert.assertNull(resp.getPayload().get(0).getStorageType());
		Assert.assertEquals("default-storage", resp.getPayload().get(1).getStorageType());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithNullCollectionStatus() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setStatus(null);
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidCollectionStatus() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setStatus("invalid-status");
	
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.INVALID_COLL_STATUS, ErrorType.USER_ERROR);
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutAnatomicSite() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setAnatomicSite(null);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutLaterality() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLaterality(null);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutPathologyStatus() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setPathology(null);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutSpecimenClass() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setSpecimenClass(null);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutSpecimenType() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setType(null);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithoutCreatedDate() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setCreatedOn(null);
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull(resp.getPayload());
		Assert.assertEquals((int)2, resp.getPayload().size());
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals(formatter.format(new Date()), formatter.format(resp.getPayload().get(0).getCreatedOn()));
		
		for (SpecimenDetail detail : resp.getPayload()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getPayload().indexOf(detail));
		}
	}
	
	//TODO: fix this once the dev code is finalized wether specimen-creation date can come before visit date
	//@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidCreatedDate() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setCreatedOn(CommonUtils.getDate(21, 1, 2000));
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		Assert.assertNull(resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenErrorCode.INVALID_CREATION_DATE, ErrorType.USER_ERROR);
	}

	
	// TODO :: Implement after isValidPv implements.
	//@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidActivityStatus() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setActivityStatus("Invalid-Activity-Status");

		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
	}
	
	// TODO :: Implement after isValidPv implements.
	//@Test                       
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidAnatomicSite() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setAnatomicSite("invalid-anatomic-site");
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidLaterality() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setLaterality("invalid-laterality");
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidPathologicalStatus() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setPathology("invalid-pathology");
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidSpecimenClass() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setSpecimenClass("invalid=specimenClass");
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/generic-teardown.xml")
	public void collectSpecimensWithInvalidSpecimenType() {
		List<SpecimenDetail> input = SpecimenTestData.getSpecimenList();
		input.get(0).setType("invalid-type");
		
		ResponseEvent<List<SpecimenDetail>> resp = specimenSvc.collectSpecimens(getRequest(input));
		TestUtils.recordResponse(resp);
		
	}
	
	public void AssertSpecimenDetails(SpecimenDetail detail, int index) {
		Assert.assertEquals("Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertEquals("Anatomic site mismatch", "Head", detail.getAnatomicSite());
		Assert.assertEquals("Initial quantity mismatch", new Double(0.5), detail.getInitialQty());
		Assert.assertEquals("Specimen label mismatch", "spm" + (index+1), detail.getLabel());
		Assert.assertEquals("Barcode mismatch", "barcode-" + (index+1), detail.getBarcode());
		Assert.assertEquals("Laterality mismatch", "Right", detail.getLaterality());
		Assert.assertEquals("Pathology status mismatch", "Metastatic", detail.getPathology());
		Assert.assertEquals("Specimen class mismatch", "Molecular", detail.getSpecimenClass());
		Assert.assertEquals("Collection status mismatch", "Collected", detail.getStatus());
		Assert.assertEquals("Type mismatch", "DNA", detail.getType());
		Assert.assertNotNull("Child specimens are null", detail.getChildren());
		
		int children = detail.getChildren().size();
		Assert.assertEquals("Invalid number: count of children specimen", 
				new Integer(2), new Integer(children));
		
		for (SpecimenDetail child : detail.getChildren()) {
			String childSpecimenLabel = detail.getLabel() + "Child-" + children;
			String childSpecimenBarcode = detail.getLabel() + "-barcode-" + children;
			
			Assert.assertEquals("Activity status mismatch", "Active", child.getActivityStatus());
			Assert.assertEquals("Anatomic site mismatch", "Head", child.getAnatomicSite());
			Assert.assertEquals("Initial quantity mismatch", new Double(0.5), child.getInitialQty());
			Assert.assertEquals("Specimen label mismatch", childSpecimenLabel, child.getLabel());
			Assert.assertEquals("Laterality mismatch", "Right", child.getLaterality());
			Assert.assertEquals("Barcode mismatch", childSpecimenBarcode, child.getBarcode());
			Assert.assertEquals("Pathology status mismatch", "Metastatic", child.getPathology());
			Assert.assertEquals("Lineage mismatch", Specimen.DERIVED, child.getLineage());
			Assert.assertEquals("Specimen class mismatch", "Molecular", child.getSpecimenClass());
			Assert.assertEquals("Collection status mismatch", "Collected", child.getStatus());
			Assert.assertEquals("Storage type mismatch", "default-storage", child.getStorageType());
			Assert.assertEquals("Type mismatch", "DNA", child.getType());
			Assert.assertEquals("Parent id mismatch", detail.getId(), child.getParentId());
			Assert.assertEquals("Parent label mismatch", detail.getLabel(), child.getParentLabel());
			children--;
		}
	}
}	