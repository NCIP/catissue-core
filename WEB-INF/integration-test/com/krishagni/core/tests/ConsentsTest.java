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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
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
	

	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Update Consents API Tests
	 */
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	@ExpectedDatabase(value="consents-test/add-consents-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void addConsentsTest() {
		ConsentTierOp input = ConsentsTestData.getConsentTierOp();
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull("Consentier was not expected to be null!", resp.getPayload());
		Assert.assertEquals(input.getConsentTier().getStatement(), resp.getPayload().getStatement());
		Assert.assertNotNull("Consentier id was not expected to be null" , resp.getPayload().getId());
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	@ExpectedDatabase(value="consents-test/update-consents-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateConsentsTest() {
		ConsentTierOp input = ConsentsTestData.getConsentTierOp();
		input.setOp(OP.UPDATE);
		input.getConsentTier().setId(2L);
		input.getConsentTier().setStatement("updated-consent");
		
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull("Consentier was not expected to be null!", resp.getPayload());
		Assert.assertEquals(input.getConsentTier().getStatement(), resp.getPayload().getStatement());		
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	@ExpectedDatabase(value="consents-test/delete-consents-test-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteConsentsTest() {
		ConsentTierOp input = ConsentsTestData.getConsentTierOp();
		input.setOp(OP.REMOVE);
		input.getConsentTier().setId(2L);
		
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNotNull("Consentier was not expected to be null!", resp.getPayload());		
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void addConsentWithInvalidCpid() {
		ConsentTierOp input = ConsentsTestData.getConsentTierOp();
		input.setCpId(-1L);
		
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(input));
		
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void updateConsentsWithInvalidConsentId() {
		ConsentTierOp input = ConsentsTestData.getConsentTierOp();
		input.setOp(OP.UPDATE);
		input.getConsentTier().setId(-1L);
		input.getConsentTier().setStatement("updated-consent");
		
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(input));
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.CONSENT_TIER_NOT_FOUND, ErrorType.USER_ERROR);		
	}

	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void updateConsentsWithNullConsentId() {
		ConsentTierOp input = ConsentsTestData.getConsentTierOp();
		input.setOp(OP.UPDATE);
		input.getConsentTier().setId(null);
		input.getConsentTier().setStatement("updated-consent");
		
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(input));
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.CONSENT_TIER_NOT_FOUND, ErrorType.USER_ERROR);		
	}
	
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void deleteConsentsTestWithInvalidConsentId() {
		ConsentTierOp input = ConsentsTestData.getConsentTierOp();
		input.setOp(OP.REMOVE);
		input.getConsentTier().setId(-1L);
		
		ResponseEvent<ConsentTierDetail> resp = cpSvc.updateConsentTier(getRequest(input));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertNull("Delete Consent Response was expected to be null!", resp.getPayload());		
	}
	
	/*
	 * Get Consents API Tests
	 */
	@Test
	@DatabaseSetup("consents-test/add-consents-test-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void getConsentsTest() {
		ResponseEvent<List<ConsentTierDetail>> resp = cpSvc.getConsentTiers(getRequest(1L));
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(new Integer(3), new Integer(resp.getPayload().size()));
		
		for (ConsentTierDetail consent : resp.getPayload()) {
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
		ResponseEvent<List<ConsentTierDetail>> resp  = cpSvc.getConsentTiers(getRequest(-1L));
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("consents-test/get-consents-for-disabled-cp-initial.xml")
	@DatabaseTearDown("consents-test/generic-teardown.xml")
	public void getConsentsForDisabledCp() {
		ResponseEvent<List<ConsentTierDetail>> resp = cpSvc.getConsentTiers(getRequest(1L));
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, CpErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
}
