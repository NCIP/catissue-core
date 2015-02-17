package com.krishagni.core.tests;

import javax.annotation.Resource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.krishagni.core.tests.testdata.CommonUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
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
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Create Distribution Protocol Test
	 */
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTest() {
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(
				getRequest(DpTestData.getDistributionProtocolDetail()));
		
		AssertCreateDpDetails(resp);
	}
	
	public void AssertCreateDpDetails (ResponseEvent<DistributionProtocolDetail> resp) {
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getPayload());
		
		DistributionProtocolDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Title mismatch", "dp-title", detail.getTitle());
		Assert.assertEquals("Error: Short title mismatch", "dp-shortTitle", detail.getShortTitle());
		Assert.assertEquals("Error: Irb id mismatch", "ASDF-001", detail.getIrbId());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertEquals("Error: Start date mismatch", CommonUtils.getDate(21,1,2012), detail.getStartDate());
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
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setShortTitle("");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.SHORT_TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistrubutionProtocolTestWithInvalidPi() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.getPrincipalInvestigator().setId(-1L);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));

		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.PI_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistrubutionProtocolTestWithBlankTitle() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setTitle("");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTestWithBlankActivityStatus() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setActivityStatus("");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		AssertCreateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTestWithNullActivityStatus() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setActivityStatus(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		AssertCreateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/create-dp-expected-with-null-values-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createDistributionProtocolTestWithNullIrbIdAndStartDate() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setStartDate(null);
		input.setIrbId(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getPayload());
		Assert.assertNull("Error: Irb id was not found null", resp.getPayload().getIrbId());
		Assert.assertNull("Error: Start date was not found null", resp.getPayload().getStartDate());
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithNullTitle() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setTitle(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getPayload());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithNullShortTitle() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setShortTitle(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getPayload());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.SHORT_TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithNullPi() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setPrincipalInvestigator(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.PI_NOT_FOUND, ErrorType.USER_ERROR);
	}

	@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistrubutionProtocolTestWithDuplicateTitleAndShortTitle() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setTitle("duplicate-title");
		input.setShortTitle("duplicate-shortTitle");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.DUP_TITLE, ErrorType.USER_ERROR);
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.DUP_SHORT_TITLE, ErrorType.USER_ERROR);
	}
	
	// TODO: Uncomment this after isValidPv() gets implemented.
	//@Test
	@DatabaseSetup("dp-test/create-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void createDistributionProtocolTestWithInvalidActivityStatus() {
		DistributionProtocolDetail input = DpTestData.getDistributionProtocolDetail();
		input.setActivityStatus("invalid-status");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.createDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, ActivityStatusErrorCode.INVALID, ErrorType.USER_ERROR);
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
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(
				getRequest(DpTestData.getUpdateDistributionProtocolDetail()));
		
		AssertUpdateDpDetails(resp);
	}
	
	public void AssertUpdateDpDetails (ResponseEvent<DistributionProtocolDetail> resp) {
		Assert.assertNotNull("Error: Response was found null", resp);
		
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getPayload());
		
		DistributionProtocolDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Title mismatch", "updated-title", detail.getTitle());
		Assert.assertEquals("Error: Short title mismatch", "updated-shortTitle", detail.getShortTitle());
		Assert.assertEquals("Error: Irb id mismatch", "NEW-ASDF-001", detail.getIrbId());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertEquals("Error: Start date mismatch", CommonUtils.getDate(21,1,2014), detail.getStartDate());
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
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setShortTitle("");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.SHORT_TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistrubutionProtocolTestWithInvalidPi() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.getPrincipalInvestigator().setId(-1L);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.PI_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistrubutionProtocolTestWithBlankTitle() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setTitle("");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistrubutionProtocolTestWithDuplicateTitleAndShortTitle() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setTitle("duplicate-title");
		input.setShortTitle("duplicate-shortTitle");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.DUP_TITLE, ErrorType.USER_ERROR);
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.DUP_SHORT_TITLE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/update-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateDistributionProtocolTestWithBlankActivityStatus() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setActivityStatus("");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		AssertUpdateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/update-dp-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateDistributionProtocolTestWithNullActivityStatus() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setActivityStatus(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		AssertUpdateDpDetails(resp);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestUsingInvalidId() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setId(-1L);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	@ExpectedDatabase(value="dp-test/update-dp-expected-with-null-values-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateDistributionProtocolTestWithNullIrbIdAndStartDate() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setStartDate(null);
		input.setIrbId(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getPayload());
		Assert.assertNull("Error: Irb id was not found null", resp.getPayload().getIrbId());
		Assert.assertNull("Error: Start date was not found null", resp.getPayload().getStartDate());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithNullTitle() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setTitle(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getPayload());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithNullShortTitle() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setShortTitle(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		Assert.assertNull("Error: Distribution protocol detail was not found null", resp.getPayload());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.SHORT_TITLE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithNullPi() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setPrincipalInvestigator(null);
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.PI_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	// TODO: Uncomment this after isValidPv() gets implemented.
	//@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void updateDistributionProtocolTestWithInvalidActivityStatus() {
		DistributionProtocolDetail input = DpTestData.getUpdateDistributionProtocolDetail();
		input.setActivityStatus("invalid-status");
		
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.updateDistributionProtocol(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, ActivityStatusErrorCode.INVALID, ErrorType.USER_ERROR);
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
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.deleteDistributionProtocol(getRequest(1L));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Distribution protocol detail was found null", resp.getPayload());
		Assert.assertEquals("Error: Activity status was not found Disabled", "Disabled", resp.getPayload().getActivityStatus());
	}
	
	@Test
	@DatabaseSetup("dp-test/update-dp-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void deleteDistributionProtocolTestWithInvalidId() {
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.deleteDistributionProtocol(getRequest(-1L));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.PI_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get Distribution Protocol List
	 */
	
	@Test
	@DatabaseSetup("dp-test/dp-list-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void getDistributionProtocolList() {
		int startAt = 1, maxResults = 3;
		DpListCriteria criteria = new DpListCriteria();
		criteria.startAt(startAt);
		criteria.maxResults(maxResults);
		
		ResponseEvent<List<DistributionProtocolDetail>> resp = dpSvc.getDistributionProtocols(getRequest(criteria));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Distribution protocol list was found null", resp.getPayload());
		Assert.assertEquals("Error: Distribution protocol list count mismatch",(int)3, resp.getPayload().size());
		for(DistributionProtocolDetail detail : resp.getPayload()) {
			AssertDp(detail, startAt+1);
			startAt++;
		}
	}
	
	@Test
	@DatabaseSetup("dp-test/dp-list-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void getDistributionProtocolListWithoutStartAtAndMaxResult() {
		DpListCriteria criteria = new DpListCriteria();
		int i=1;
		
		ResponseEvent<List<DistributionProtocolDetail>> resp = dpSvc.getDistributionProtocols(getRequest(criteria));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Distribution protocol list was found null", resp.getPayload());
		Assert.assertEquals("Error: Distribution protocol size mismatch", (int)5, resp.getPayload().size());
		for(DistributionProtocolDetail detail : resp.getPayload()) {
			AssertDp(detail, i);
			i++;
		}
	}
	
	public void AssertDp (DistributionProtocolDetail detail, int i) {
		Assert.assertEquals("Error: Title mismatch", "dp-title-" + i, detail.getTitle());
		Assert.assertEquals("Error: Short title mismatch", "dp-shortTitle-" + i, detail.getShortTitle());
		Assert.assertEquals("Error: Irb id mismatch", "ASDF-" + i, detail.getIrbId());
		Assert.assertEquals("Error: Start date mismatch", CommonUtils.getDate(21,1,2012), detail.getStartDate());
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
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.getDistributionProtocol(getRequest(2L));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Distribution protocol was found null", resp.getPayload());
		Assert.assertEquals("Error: Request and response dp id mismatch", new Long(2), resp.getPayload().getId());
	}
	
	@Test
	@DatabaseSetup("dp-test/dp-list-initial.xml")
	@DatabaseTearDown("dp-test/generic-teardown.xml")
	public void getDistributionProtocolWithInvalidId() {
		ResponseEvent<DistributionProtocolDetail> resp = dpSvc.getDistributionProtocol(getRequest(-2L));
		
		Assert.assertEquals("Error: Response was not found successfull", false, resp.isSuccessful());
		TestUtils.isErrorCodePresent(resp, DistributionProtocolErrorCode.PI_NOT_FOUND, ErrorType.USER_ERROR);
	}
}