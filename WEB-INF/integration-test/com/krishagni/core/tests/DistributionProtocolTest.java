package com.krishagni.core.tests;

import javax.annotation.Resource;

import java.util.Calendar;
import java.util.Date;

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

import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.DpTestData;
import com.krishagni.core.tests.testdata.CprTestData;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetailEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqDistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class DistributionProtocolTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private DistributionProtocolService dpSvc;
	
	@Autowired
	private ApplicationContext ctx;
	
	/*
	 * Create Distribution Protocol Test
	 */
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTest() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		AssertCreateDpDetails(resp);
	}
	
	public void AssertCreateDpDetails (DistributionProtocolCreatedEvent resp) {
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getProtocol());
		
		DistributionProtocolDetail detail = resp.getProtocol();
		Assert.assertEquals("Error: Title mismatch", "dp-title", detail.getTitle());
		Assert.assertEquals("Error: Short title mismatch", "dp-shortTitle", detail.getShortTitle());
		Assert.assertEquals("Error: Irb id mismatch", "ASDF-001", detail.getIrbId());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertEquals("Error: Start date mismatch", CprTestData.getDate(21,1,2012), detail.getStartDate());
		Assert.assertNotNull("Error: Principal investigator was found null", detail.getPrincipalInvestigator());
		
		UserSummary pi = detail.getPrincipalInvestigator();
		Assert.assertEquals("Error: PI first name  mismatch", "ADMIN", pi.getFirstName());
		Assert.assertEquals("Error: PI last name  mismatch", "ADMIN", pi.getLastName());
		Assert.assertEquals("Error: PI login name  mismatch", "admin@admin.com", pi.getLoginName());
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistrubutionProtocolTestWithBlankShortTitle() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setShortTitle("");
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Missing short title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.MISSING_ATTR_VALUE, "short title"));
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistrubutionProtocolTestWithInvalidPi() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().getPrincipalInvestigator().setId(-1L);
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Invalid principle investigator error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.INVALID_PRINCIPAL_INVESTIGATOR, "principle investigator"));
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistrubutionProtocolTestWithBlankTitle() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setTitle("");
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Missing title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.MISSING_ATTR_VALUE, "title"));
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTestWithBlankActivityStatus() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setActivityStatus("");
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		AssertCreateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTestWithNullActivityStatus() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setActivityStatus(null);
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		AssertCreateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected-with-null-values-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTestWithNullIrbIdAndStartDate() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setStartDate(null);
		req.getProtocol().setIrbId(null);
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getProtocol());
		Assert.assertNull("Error: Irb id was not found null", resp.getProtocol().getIrbId());
		Assert.assertNull("Error: Start date was not found null", resp.getProtocol().getStartDate());
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithNullTitle() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setTitle(null);
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getProtocol());
		Assert.assertEquals("title:Required attribute is either empty or null\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithNullShortTitle() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setShortTitle(null);
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getProtocol());
		Assert.assertEquals("short title:Required attribute is either empty or null\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithNullPi() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setPrincipalInvestigator(null);
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Invalid principle investigator error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.INVALID_PRINCIPAL_INVESTIGATOR, "principle investigator"));
	}

	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistrubutionProtocolTestWithDuplicateTitleAndShortTitle() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setTitle("duplicate-title");
		req.getProtocol().setShortTitle("duplicate-shortTitle");
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Duplicate title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.NOT_UNIQUE, "title"));
		Assert.assertEquals("Error: Duplicate short title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.NOT_UNIQUE, "short-title"));
	}
	
	// TODO: Uncomment this after isValidPv() gets implemented.
	//@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithInvalidActivityStatus() {
		CreateDistributionProtocolEvent req = DpTestData.getCreateDistributionProtocolEvent();
		req.getProtocol().setActivityStatus("invalid-status");
		
		DistributionProtocolCreatedEvent resp = dpSvc.createDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Invalid activity status error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.INVALID_ATTR_VALUE, "activity status"));
	}

	/*
	 * Update Distribution Protocol Test
	 */
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/update-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateDistributionProtocolTest() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		AssertUpdateDpDetails(resp);
	}
	
	public void AssertUpdateDpDetails (DistributionProtocolUpdatedEvent resp) {
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getProtocol());
		
		DistributionProtocolDetail detail = resp.getProtocol();
		Assert.assertEquals("Error: Title mismatch", "updated-title", detail.getTitle());
		Assert.assertEquals("Error: Short title mismatch", "updated-shortTitle", detail.getShortTitle());
		Assert.assertEquals("Error: Irb id mismatch", "NEW-ASDF-001", detail.getIrbId());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertEquals("Error: Start date mismatch", CprTestData.getDate(21,1,2014), detail.getStartDate());
		Assert.assertNotNull("Error: Principal investigator was found null", detail.getPrincipalInvestigator());
		
		UserSummary pi = detail.getPrincipalInvestigator();
		Assert.assertEquals("Error: PI first name  mismatch", "first-name", pi.getFirstName());
		Assert.assertEquals("Error: PI last name  mismatch", "last-name", pi.getLastName());
		Assert.assertEquals("Error: PI login name  mismatch", "user@user.com", pi.getLoginName());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistrubutionProtocolTestWithBlankShortTitle() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setShortTitle("");
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Missing short title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.MISSING_ATTR_VALUE, "short title"));
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistrubutionProtocolTestWithInvalidPi() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().getPrincipalInvestigator().setId(-1L);
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Invalid principle investigator error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.INVALID_PRINCIPAL_INVESTIGATOR, "principle investigator"));
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistrubutionProtocolTestWithBlankTitle() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setTitle("");
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Missing title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.MISSING_ATTR_VALUE, "title"));
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistrubutionProtocolTestWithDuplicateTitleAndShortTitle() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setTitle("duplicate-title");
		req.getProtocol().setShortTitle("duplicate-shortTitle");
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Duplicate title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.NOT_UNIQUE, "title"));
		Assert.assertEquals("Error: Duplicate short title error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.NOT_UNIQUE, "short-title"));
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/update-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateDistributionProtocolTestWithBlankActivityStatus() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setActivityStatus("");
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		AssertUpdateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/update-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateDistributionProtocolTestWithNullActivityStatus() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setActivityStatus(null);
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		AssertUpdateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestUsingInvalidId() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.setId(-1L);
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Invalid distribution protocol was found", EventStatus.NOT_FOUND, resp.getStatus());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/update-dp-expected-with-null-values-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateDistributionProtocolTestWithNullIrbIdAndStartDate() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setStartDate(null);
		req.getProtocol().setIrbId(null);
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getProtocol());
		Assert.assertNull("Error: Irb id was not found null", resp.getProtocol().getIrbId());
		Assert.assertNull("Error: Start date was not found null", resp.getProtocol().getStartDate());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithNullTitle() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setTitle(null);
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getProtocol());
		Assert.assertEquals("title:Required attribute is either empty or null\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithNullShortTitle() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setShortTitle(null);
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getProtocol());
		Assert.assertEquals("short title:Required attribute is either empty or null\n", resp.getException().getMessage());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithNullPi() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setPrincipalInvestigator(null);
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Invalid principle investigator error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.INVALID_PRINCIPAL_INVESTIGATOR, "principle investigator"));
	}
	
	// TODO: Uncomment this after isValidPv() gets implemented.
	//@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithInvalidActivityStatus() {
		UpdateDistributionProtocolEvent req = DpTestData.getUpdateDistributionProtocolEvent();
		req.getProtocol().setActivityStatus("invalid-status");
		
		DistributionProtocolUpdatedEvent resp = dpSvc.updateDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", EventStatus.BAD_REQUEST, resp.getStatus());
		Assert.assertEquals("Error: Invalid activity status error was not found", true,
				TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.INVALID_ATTR_VALUE, "activity status"));
	}
	
	/*
	 * Delete Distribution Protocol Test
	 */
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/delete-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteDistributionProtocolTestWithId() {
		DeleteDistributionProtocolEvent req = DpTestData.getDeleteDistributionProtocolEvent();
		
		DistributionProtocolDeletedEvent resp = dpSvc.deleteDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getProtocol());
		Assert.assertEquals("Error: Activity status was not found Disabled", "Disabled", resp.getProtocol().getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void deleteDistributionProtocolTestWithInvalidId() {
		DeleteDistributionProtocolEvent req = DpTestData.getDeleteDistributionProtocolEvent();
		req.setId(-1L);
		
		DistributionProtocolDeletedEvent resp = dpSvc.deleteDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Invalid distribution protocol was found", EventStatus.NOT_FOUND, resp.getStatus());
	}
	
	/*
	 * Get Distribution Protocol List
	 */
	
	@Test
	@DatabaseSetup("dp-test/dp-list-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void getDistributionProtocolList() {
		ReqDistributionProtocolsEvent req =  new ReqDistributionProtocolsEvent();
		int startAt = 1, maxResult = 3;
		req.setStartAt(startAt);
		req.setMaxResults(maxResult);
		
		DistributionProtocolsEvent resp = dpSvc.getDistributionProtocols(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol list was found null", resp.getProtocols());
		Assert.assertEquals("Error: Distribution protocol list count mismatch",(int)3, resp.getProtocols().size());
		for(DistributionProtocolDetail detail : resp.getProtocols()) {
			AssertDp(detail, startAt+1);
			startAt++;
		}
	}
	
	@Test
	@DatabaseSetup("dp-test/dp-list-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void getDistributionProtocolListWithoutStartAtAndMaxResult() {
		ReqDistributionProtocolsEvent req =  new ReqDistributionProtocolsEvent();
		int i=1;
		DistributionProtocolsEvent resp = dpSvc.getDistributionProtocols(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol list was found null", resp.getProtocols());
		Assert.assertEquals("Error: Distribution protocol list was not empty",(int)5, resp.getProtocols().size());
		for(DistributionProtocolDetail detail : resp.getProtocols()) {
			AssertDp(detail, i);
			i++;
		}
	}
	
	public void AssertDp (DistributionProtocolDetail detail, int i) {
		Assert.assertEquals("Error: Title mismatch", "dp-title-" + i, detail.getTitle());
		Assert.assertEquals("Error: Short title mismatch", "dp-shortTitle-" + i, detail.getShortTitle());
		Assert.assertEquals("Error: Irb id mismatch", "ASDF-" + i, detail.getIrbId());
		Assert.assertEquals("Error: Start date mismatch", CprTestData.getDate(21,1,2012), detail.getStartDate());
		Assert.assertEquals("Error: Activity Status mismatch", "Active", detail.getActivityStatus());
		
		UserSummary pi = detail.getPrincipalInvestigator();
		Assert.assertEquals("Error: PI first name  mismatch", "ADMIN", pi.getFirstName());
		Assert.assertEquals("Error: PI last name  mismatch", "ADMIN", pi.getLastName());
		Assert.assertEquals("Error: PI login name  mismatch", "admin@admin.com", pi.getLoginName());
	}
	
	/*
	 * Get Distribution Protocol By Id Or Label
	 */
	
	@Test
	@DatabaseSetup("dp-test/dp-list-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void getDistributionProtocolById() {
		ReqDistributionProtocolEvent req = new ReqDistributionProtocolEvent();
		req.setId(2L);
		
		DistributionProtocolDetailEvent resp = dpSvc.getDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not successfull", EventStatus.OK, resp.getStatus());
		Assert.assertNotNull("Error: Distribution protocol was found null", resp.getProtocol());
		Assert.assertEquals("Error: Request and response dp id mismatch", req.getId(), resp.getProtocol().getId());
	}
	
	@Test
	@DatabaseSetup("dp-test/dp-list-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void getDistributionProtocolWithInvalidId() {
		ReqDistributionProtocolEvent req = new ReqDistributionProtocolEvent();
		req.setId(-2L);
		
		DistributionProtocolDetailEvent resp = dpSvc.getDistributionProtocol(req);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Invalid distribution protocol was found", EventStatus.NOT_FOUND, resp.getStatus());
	}
}