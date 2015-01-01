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
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpRespEvent;
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
	

	@Test
	@DatabaseSetup("ConsentsTest.addConsentsTest.initial.xml")
	@DatabaseTearDown("ConsentsTest.generic.teardown.xml")
	@ExpectedDatabase(value="ConsentsTest.addConsentsTest.expected.xml", 
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
	@DatabaseSetup("ConsentsTest.addConsentsTest.initial.xml")
	@DatabaseTearDown("ConsentsTest.generic.teardown.xml")
	@ExpectedDatabase(value="ConsentsTest.updateConsentsTest.expected.xml", 
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
	@DatabaseSetup("ConsentsTest.addConsentsTest.initial.xml")
	@DatabaseTearDown("ConsentsTest.generic.teardown.xml")
	@ExpectedDatabase(value="ConsentsTest.deleteConsentsTest.expected.xml", 
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
	@DatabaseSetup("ConsentsTest.addConsentsTest.initial.xml")
	@DatabaseTearDown("ConsentsTest.generic.teardown.xml")
	public void addConsentWithInvalidCpid() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setCpId(-1L);
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.NOT_FOUND, resp.getStatus());
		Assert.assertEquals(req.getCpId(), resp.getCpId());
	}
	
	@Test
	@DatabaseSetup("ConsentsTest.addConsentsTest.initial.xml")
	@DatabaseTearDown("ConsentsTest.generic.teardown.xml")
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
	@DatabaseSetup("ConsentsTest.addConsentsTest.initial.xml")
	@DatabaseTearDown("ConsentsTest.generic.teardown.xml")
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
	@DatabaseSetup("ConsentsTest.addConsentsTest.initial.xml")
	@DatabaseTearDown("ConsentsTest.generic.teardown.xml")
	public void deleteConsentsTestWithInvalidConsentId() {
		ConsentTierOpEvent req = ConsentsTestData.getConsentTierOpEvent();
		req.setOp(ConsentTierOpEvent.OP.REMOVE);
		req.getConsentTier().setId(-1L);
		
		ConsentTierOpRespEvent resp = cpSvc.updateConsentTier(req);
		
		Assert.assertEquals(EventStatus.OK, resp.getStatus());
		Assert.assertNull("Delete Consent Response was expected to be null!", resp.getConsentTier());		
	}
}
