package com.krishagni.core.tests;

import java.util.Date;
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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimensCollectedEvent;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;

import com.krishagni.core.tests.testdata.SpecimenServiceTestData;
import com.krishagni.core.tests.testdata.CprTestData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })

@WebAppConfiguration
public class SpecimenServiceTest {
	@Resource 
	private WebApplicationContext webApplicationContext;
	
	@Autowired 
	private SpecimenService specimenSvc;
	
	@Autowired 
	private ApplicationContext ctx;

	/*
	 *  collectSpecimens API test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimens() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		//TestUtils.recordResponse(resp);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals("Invalid number: count of specimens", 
				new Integer(2), new Integer(resp.getSpecimens().size()));
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithEmptyLabel() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setLabel(null);

		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("label:Required attribute is either empty or null\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidSR() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setReqId(-1L);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("reqId:Attribute value is invalid\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidVisit() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setVisitId(-1L);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("visitId:Attribute value is invalid\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-with-duplicate-values.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithDuplicateLabel() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setLabel("duplicate-label");
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("label:specimen collection group with same already exists.\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidLineage() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setLineage("Invalid-Lineage");
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("lineage:Attribute value is invalid\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-with-duplicate-values.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithDuplicateBarcode() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setBarcode("duplicate-barcode");

		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("barcode:Registration is already present with same barcode.\n", resp.getException().getMessage());
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidInitialQuantity() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setInitialQty(-1.0);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("initialQty:Attribute value is invalid\n", resp.getException().getMessage());
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutInitialQuantity() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setInitialQty(null);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithoutInitialQuantityAndReqId() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setReqId(null);
		req.getSpecimens().get(0).setInitialQty(null);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("initialQty:Required attribute is either empty or null\n", resp.getException().getMessage());
	}

	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithEmptyActivityStatus() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setActivityStatus("");

		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-with-mismatch-visit-id.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithMismatchCpEventId() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("visitId:Attribute value is invalid\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithEmptyVisitId() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setVisitId(null);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("visitId:Required attribute is either empty or null\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutLineage() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setLineage(null);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}

	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithDerivedLineageAndParentId() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setLineage("Derived");
		req.getSpecimens().get(0).setParentId(1L);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		Assert.assertEquals("Derived", resp.getSpecimens().get(0).getLineage());
		Assert.assertEquals(new Long(1), resp.getSpecimens().get(0).getParentId());
		Assert.assertEquals("parent-spm", resp.getSpecimens().get(0).getParentLabel());
		
		Assert.assertEquals("New", resp.getSpecimens().get(1).getLineage());
		Assert.assertNull(resp.getSpecimens().get(1).getParentId());
		Assert.assertNull(resp.getSpecimens().get(1).getParentLabel());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}

	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithDerivedLineageAndParentLabel() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setLineage("Derived");
		req.getSpecimens().get(0).setParentLabel("parent-spm");
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		Assert.assertEquals("Derived", resp.getSpecimens().get(0).getLineage());
		Assert.assertEquals(new Long(1), resp.getSpecimens().get(0).getParentId());
		Assert.assertEquals("parent-spm", resp.getSpecimens().get(0).getParentLabel());
		
		Assert.assertEquals("New", resp.getSpecimens().get(1).getLineage());
		Assert.assertNull(resp.getSpecimens().get(1).getParentId());
		Assert.assertNull(resp.getSpecimens().get(1).getParentLabel());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithEmptyReqId() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setReqId(null);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		Assert.assertNull(resp.getSpecimens().get(0).getStorageType());
		Assert.assertEquals("default-storage", resp.getSpecimens().get(1).getStorageType());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithNullCollectionStatus() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setStatus(null);
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals("Invalid number: count of specimens", 
				new Integer(2), new Integer(resp.getSpecimens().size()));
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidCollectionStatus() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setStatus("invalid-status");
	
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("status:Attribute value is invalid\n", resp.getException().getMessage());
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutAnatomicSite() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setAnatomicSite(null);
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutLaterality() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setLaterality(null);
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutPathologyStatus() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setPathology(null);
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutSpecimenClass() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setSpecimenClass(null);
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-service-test/collect-specimens-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void collectSpecimensWithoutSpecimenType() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setType(null);
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithoutCreatedDate() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setCreatedOn(null);
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals((int)2, resp.getSpecimens().size());
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Assert.assertEquals(formatter.format(new Date()), formatter.format(resp.getSpecimens().get(0).getCreatedOn()));
		
		for (SpecimenDetail detail : resp.getSpecimens()) {
			Assert.assertEquals("Storage type mismatch", "default-storage", detail.getStorageType());
			Assert.assertEquals("Lineage mismatch", "New", detail.getLineage());
			Assert.assertNull("Parent id mismatch", detail.getParentId());
			Assert.assertNull("Parent label mismatch", detail.getParentLabel());
			AssertSpecimenDetails(detail, resp.getSpecimens().indexOf(detail));
		}
	}
	
	@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidCreatedDate() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setCreatedOn(CprTestData.getDate(21, 1, 2000));
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
		Assert.assertEquals("createdOn:Attribute value is invalid\n", resp.getException().getMessage());
	}

	
	// TODO :: Implement after isValidPv implements.
	//@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidActivityStatus() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setActivityStatus("Invalid-Activity-Status");

		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
	}
	
	// TODO :: Implement after isValidPv implements.
	//@Test                       
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidAnatomicSite() {
		CollectSpecimensEvent req = SpecimenServiceTestData.collectSpecimenListEvent();
		req.getSpecimens().get(0).setAnatomicSite("invalid-anatomic-site");
		SpecimensCollectedEvent resp = specimenSvc.collectSpecimens(req);
		
		TestUtils.recordResponse(resp);
		System.out.println("Status :  " + resp.getStatus());
		System.out.println("Err Msg:  " + resp.getException().getMessage());
		System.out.println("Err Fld:  " + resp.getErroneousFields());

		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull(resp.getSpecimens());
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidLaterality() {
		
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidPathologicalStatus() {
		
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidSpecimenClass() {
		
	}
	
	// TODO :: Implement after isValidPv() implements.
	//@Test
	@DatabaseSetup("specimen-test/specimen-service-test/collect-specimens-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-service-test/collect-specimens-teardown.xml")
	public void collectSpecimensWithInvalidSpecimenType() {
			
	}
	
	public void AssertSpecimenDetails(SpecimenDetail detail, int index) {
		Assert.assertEquals("Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertEquals("Anatomic site mismatch", "Head", detail.getAnatomicSite());
		Assert.assertEquals("Initial quantity mismatch", new Double(0.5), detail.getInitialQty());
		Assert.assertEquals("Specimen label mismatch", "spm" + (index+1), detail.getLabel());
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
			Assert.assertEquals("Pathology status mismatch", "Metastatic", child.getPathology());
			Assert.assertEquals("Lineage mismatch", "New", child.getLineage());
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