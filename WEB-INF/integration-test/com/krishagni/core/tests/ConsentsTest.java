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
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpRespEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTiersEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqConsentTiersEvent;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.ConsentsTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class ConsentsTest {

	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private CollectionProtocolService cpSvc;

	@Autowired
	private ApplicationContext ctx;
	

	/*
	 * Update Consents API Tests
	 */
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	@ExpectedDatabase(value="consents-test/add-consents-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addConsentsTest() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Consentier was not expected to be null!", req.getConsentTier());
		Assert.assertEquals(req.getConsentTier().getStatement(), resp.getConsentTier().getStatement());
		Assert.assertNotNull("Consentier id was not expected to be null" , resp.getConsentTier().getId());
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	@ExpectedDatabase(value="consents-test/update-consents-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateConsentsTest() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setOp(ConsentTierOpEvent.OP.UPDATE);
		req.getConsentTier().setId(2L);
		req.getConsentTier().setStatement("updated-consent");
		
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Consentier was not expected to be null!", req.getConsentTier());
		Assert.assertEquals(req.getConsentTier().getStatement(), resp.getConsentTier().getStatement());		
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	@ExpectedDatabase(value="consents-test/delete-consents-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteConsentsTest() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setOp(ConsentTierOpEvent.OP.REMOVE);
		req.getConsentTier().setId(2L);
		
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Consentier was not expected to be null!", req.getConsentTier());		
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void addConsentWithInvalidCpid() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setCpId(-1L);
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void updateConsentsWithInvalidConsentId() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setOp(ConsentTierOpEvent.OP.UPDATE);
		req.getConsentTier().setId(-1L);
		req.getConsentTier().setStatement("updated-consent");
		
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Non existing consent tier for update operation", resp.getException().getMessage());		
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void updateConsentsWithNullConsentId() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setOp(ConsentTierOpEvent.OP.UPDATE);
		req.getConsentTier().setId(null);
		req.getConsentTier().setStatement("updated-consent");
		
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Non existing consent tier for update operation", resp.getException().getMessage());		
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void deleteConsentsTestWithInvalidConsentId() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setOp(ConsentTierOpEvent.OP.REMOVE);
		req.getConsentTier().setId(-1L);
		
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNull("Delete Consent Response was expected to be null!", resp.getConsentTier());		
	}
	
	/*
	 * Get Consents API Tests
	 */
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void getConsentsTest() {
		ReqConsentTiersEvent req = ConsentsTestData.getReqConsentTiersEvent();
		ConsentTiersEvent resp = cpSvc.getConsentTiers(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertEquals(new Integer(3), new Integer(resp.getConsentTiers().size()));
		
		for (ConsentTierDetail consent : resp.getConsentTiers()) {
			String expectedStatement = "statement-" + consent.getId();
			Assert.assertNotNull("ConsentId was not expected to be null!");
			if (consent.getId() < 1L || consent.getId() > 3L) {
				Assert.fail("Unexpected consent id found: " + consent.getId());
			}
			
			Assert.assertEquals(expectedStatement, consent.getStatement());
		}
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void getConsentsTestForNonExistingCp() {
		ReqConsentTiersEvent req = ConsentsTestData.getReqConsentTiersEvent();
		req.setCpId(-1L);
		ConsentTiersEvent resp = cpSvc.getConsentTiers(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
	}
	
	@Test
	@DatabaseSetup("consents-test/get-consents-for-disabled-cp-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void getConsentsForDisabledCp() {
		ReqConsentTiersEvent req = ConsentsTestData.getReqConsentTiersEvent();
		ConsentTiersEvent resp = cpSvc.getConsentTiers(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
	}
}
