package com.krishagni.core.tests;

import java.util.ArrayList; 
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.administrative.events.SiteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.SiteListCriteria;
import com.krishagni.catissueplus.core.administrative.services.SiteService;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.InstituteTestData;
import com.krishagni.core.tests.testdata.SiteTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class SiteTest {
	@Resource
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private SiteService siteSvc;
	
	@Autowired
	ApplicationContext ctx;
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Create Site Test Api's
	 */
	@Test
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/create-site-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSiteTest() {
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(SiteTestData.getSiteDetail()));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "default-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/create-site-with-null-activity-status-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSiteTestWithNullActivityStatus() {
		SiteDetail input = SiteTestData.getSiteDetail();
		input.setActivityStatus(null);
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "default-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}
		
	@Test
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/create-site-with-empty-activity-status-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSiteTestWithEmptyActivityStatus() {
		SiteDetail input = SiteTestData.getSiteDetail();
		input.setActivityStatus("");
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "default-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}
	@Test
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void createSiteTestWithEmptySiteName() {
		SiteDetail input = SiteTestData.getSiteDetail();
		input.setName("");
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void createSiteTestWithEmptySiteType() {
		SiteDetail input = SiteTestData.getSiteDetail();
		input.setType("");
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.TYPE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void createSiteTestWithDuplicateSiteCode() {
		SiteDetail input = SiteTestData.getSiteDetailWithDuplicateSiteCode();
		input.setCode("1");
		List<UserSummary> userList = input.getCoordinators();
		userList.remove(1);
		input.setCoordinators(userList);
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.DUP_CODE, ErrorType.USER_ERROR);
	}
	
	@Test 
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void createSiteTestWithDuplicateSiteName() {
		SiteDetail input = SiteTestData.getSiteDetail();
		input.setName("default-site1");
		List<UserSummary> coordinators = input.getCoordinators();
		coordinators.remove(1);
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.DUP_NAME, ErrorType.USER_ERROR);
	}
	
	//@Test TODO:remove comments when implement pvs
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void createSiteTestWithInvalidSiteType() {
		SiteDetail input = SiteTestData.getSiteDetail();
		input.setType("invalid-type");
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.INVALID_TYPE, ErrorType.USER_ERROR);
	}
	
	@Test 
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void createSiteTestWithInvalidCoordinator() {
		SiteDetail input = SiteTestData.getSiteDetail();
		input.setName("default-site2");
		input.setCode("Site-2");
		
		List<UserSummary> coordinatorCollection = new ArrayList<UserSummary>();
		UserSummary coordinator = input.getCoordinators().get(0);
		coordinator.setId(2L);
		coordinatorCollection.add(coordinator);
		input.setCoordinators(coordinatorCollection);
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, UserErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Update Site Test Api's
	 */
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTest() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "updated-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithEmptyActivityStatus() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setActivityStatus("");
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "updated-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}

	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithNullActivityStatus() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setActivityStatus(null);
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "updated-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithInvalidActivityStatus() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setActivityStatus("in-active");
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, ActivityStatusErrorCode.INVALID, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-with-activity-status-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-with-changed-activity-status-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithClosedActivityStatus() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setId(1L);
		input.setActivityStatus("Closed");
		input.setCoordinators(new ArrayList<UserSummary>());
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "updated-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Closed", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-with-activity-status-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-with-disabled-activity-status-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithDisabledActivityStatus() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setId(1L);
		input.setActivityStatus("Disabled");
		input.setCoordinators(new ArrayList<UserSummary>());
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "updated-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Disabled", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		
	}
	
	@Test
	@DatabaseSetup("site-test/delete-site-initial.xml")
	@DatabaseTearDown("site-test/generic-delete-teardown.xml")
	public void updateSiteTestWithDisabledActivityStatusWhichHaveDependency() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		List<UserSummary> userList = input.getCoordinators();
		userList.remove(1);
		input.setCoordinators(userList);
		input.setId(1L);
		input.setActivityStatus("Disabled");
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.REF_ENTITY_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-without-name-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithSameName() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setName("default-site1");
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "default-site1", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-empty-code-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithEmptySiteCode() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setCode("");
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "updated-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}	
		}
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-with-new-coordinator-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-with-new-coordinator-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithNewCoordinator() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		List<UserSummary> userList = input.getCoordinators();
		userList.remove(1);
		
		UserSummary summary = new UserSummary();
		summary.setDomain("default");
		summary.setId(3L);
		summary.setLoginName("super@super.com");
		summary.setFirstName("SUPER");
		summary.setFirstName("SUPER");
		userList.add(summary);
		input.setCoordinators(userList);
		
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		SiteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "updated-site", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", detail.getCoordinators());
		Assert.assertEquals(2,detail.getCoordinators().size());
		
		List<UserSummary> coordinators = detail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 3L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("SUPER",userSummary.getLastName());
				Assert.assertEquals("super@super.com", userSummary.getLoginName());
			}	
		}
	}

	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithInvalidIdTest() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setId(-1L);
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithEmptyNameTest() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setName("");
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithNullNameTest() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setName(null);
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithDuplicateNameTest() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setId(2L);
		input.setName("default-site1");
		
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.DUP_NAME, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithDuplicateSiteCode() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setId(2L);
		input.setCode("1");
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.DUP_CODE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/update-site-null-code-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSiteTestWithNullSiteCode() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setId(2L);
		input.setCode(null);
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		SiteDetail siteDetail = resp.getPayload();
		Assert.assertEquals(null,siteDetail.getCode());
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithInvalidCoordinator() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		
		List<UserSummary> coordinatorCollection = new ArrayList<UserSummary>();
		UserSummary coordinator = input.getCoordinators().get(0);
		coordinator.setId(3L);
		coordinatorCollection.add(coordinator);
		input.setCoordinators(coordinatorCollection);
		
		ResponseEvent<SiteDetail> resp = siteSvc.updateSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, UserErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithEmptySiteType() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setType("");
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.TYPE_REQUIRED, ErrorType.USER_ERROR);
	}
	
	//@Test TODO: need to write pv code
	@DatabaseSetup("site-test/create-site-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void updateSiteTestWithInvalidSiteType() {
		SiteDetail input = SiteTestData.getSiteDetailForUpdate();
		input.setType("invalid-type");
		
		ResponseEvent<SiteDetail> resp = siteSvc.createSite(getRequest(input));
		TestUtils.recordResponse(resp);
		
		TestUtils.checkErrorCode(resp, SiteErrorCode.INVALID_TYPE, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get Site list Api's
	 */
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteListTest() {
		SiteListCriteria crit = new SiteListCriteria();
		ResponseEvent<List<SiteDetail>> resp = siteSvc.getSites(getRequest(crit));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		List<SiteDetail> siteList = resp.getPayload();
		Assert.assertEquals(new Integer(3), new Integer(siteList.size()));
		
		for (SiteDetail detail : siteList) {
			String siteName = "default-site" + detail.getId();			
			Assert.assertEquals(siteName, detail.getName());
		}
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteListTestWithQuery() {
		SiteListCriteria crit = new SiteListCriteria();
		crit.query("default-site2");
		ResponseEvent<List<SiteDetail>> resp = siteSvc.getSites(getRequest(crit));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		List<SiteDetail> siteList = resp.getPayload();
		Assert.assertEquals(new Integer(1), new Integer(siteList.size()));
		
		for (SiteDetail detail : siteList) {
			String siteName = "default-site2";			
			Assert.assertEquals(siteName, detail.getName());
		}
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteListTestWithQueryForMultipleResult() {
		SiteListCriteria crit = new SiteListCriteria();
		crit.query("default-site");
		ResponseEvent<List<SiteDetail>> resp = siteSvc.getSites(getRequest(crit));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		List<SiteDetail> siteList = resp.getPayload();
		Assert.assertEquals(new Integer(3), new Integer(siteList.size()));
		
		for (SiteDetail detail : siteList) {
			String siteName = "default-site" + detail.getId();			
			Assert.assertEquals(siteName, detail.getName());
		}
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteListTestWithStartAt() {
		SiteListCriteria crit = new SiteListCriteria();
		crit.startAt(1);
		ResponseEvent<List<SiteDetail>> resp = siteSvc.getSites(getRequest(crit));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		List<SiteDetail> siteList = resp.getPayload();
		Assert.assertEquals(new Integer(2), new Integer(siteList.size()));
		
		for (SiteDetail detail : siteList) {
			String siteName = "default-site" + (detail.getId());			
			Assert.assertEquals(siteName, detail.getName());
		}
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteListTestWithMaxLength() {
		SiteListCriteria crit = new SiteListCriteria();
		crit.maxResults(2);
		ResponseEvent<List<SiteDetail>> resp = siteSvc.getSites(getRequest(crit));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		List<SiteDetail> siteList = resp.getPayload();
		Assert.assertEquals(new Integer(2), new Integer(siteList.size()));
		
		for (SiteDetail detail : siteList) {
			String siteName = "default-site" + (detail.getId());			
			Assert.assertEquals(siteName, detail.getName());
		}
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-empty-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteListTestWithEmptyData() {
		SiteListCriteria crit = new SiteListCriteria();
		ResponseEvent<List<SiteDetail>> resp = siteSvc.getSites(getRequest(crit));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		List<SiteDetail> siteList = resp.getPayload();
		Assert.assertEquals(0, siteList.size());
	}
	
	/*
	 * Get Site Test Api's
	 */
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteByIdTest() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setId(1L);
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		SiteDetail siteDetail = resp.getPayload();
		Assert.assertEquals(new Long(1), siteDetail.getId());
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		Assert.assertEquals("Error: Name mismatch", "default-site1", siteDetail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", siteDetail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", siteDetail.getCoordinators());
		Assert.assertEquals(1,siteDetail.getCoordinators().size());
		
		List<UserSummary> coordinators = siteDetail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("site-test/update-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	public void getSiteWithName() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setName("default-site1");
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		SiteDetail siteDetail = resp.getPayload();
		Assert.assertEquals(new Long(1), siteDetail.getId());
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Site detail was found null", resp.getPayload());
		
		Assert.assertEquals("Error: Name mismatch", "default-site1", siteDetail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", siteDetail.getActivityStatus());
		Assert.assertNotNull("Error: Coordinators was found null", siteDetail.getCoordinators());
		Assert.assertEquals(2,siteDetail.getCoordinators().size());
		
		List<UserSummary> coordinators = siteDetail.getCoordinators();
		
		for (UserSummary userSummary : coordinators) {
			if(userSummary.getId() == 1L) {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("ADMIN",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("admin@admin.com", userSummary.getLoginName());
			}
			else {
				Assert.assertEquals("default",userSummary.getDomain());
				Assert.assertEquals("SUPER",userSummary.getFirstName());
				Assert.assertEquals("ADMIN",userSummary.getLastName());
				Assert.assertEquals("super@admin.com", userSummary.getLoginName());
			}
		}
	}
	
	@Test
	public void getSiteWithInvalidId() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setId(null);
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void getSiteWithInvalidName() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setName(null);
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void getSiteTestWithNonExistingSiteById() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setId(5L);
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void getSiteTestWithNonExistingSiteByName() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setName("default-site5");
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/get-site-with-disabled-status-initial.xml")
	public void getSiteByIdTestWhichActivityStatusIsDisabled() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setName("default-site1");
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("site-test/get-site-with-disabled-status-initial.xml")
	public void getSiteByNameTestWhichActivityStatusIsDisabled() {
		SiteQueryCriteria crit = new SiteQueryCriteria(); 
		crit.setName("default-site1");
		
		RequestEvent<SiteQueryCriteria> req = getRequest(crit);
		ResponseEvent<SiteDetail> resp = siteSvc.getSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, SiteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Delete Site Test Api's
	 */
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/delete-site-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteSiteTest() {
		DeleteEntityOp crit = new DeleteEntityOp();
		RequestEvent<DeleteEntityOp> req = getRequest(crit);
		crit.setId(2L);
		ResponseEvent<Map<String, List>> resp = siteSvc.deleteSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(0, resp.getPayload().size());
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-teardown.xml")
	@ExpectedDatabase(value="site-test/delete-close-site-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteSiteAndCloseTest() {
		DeleteEntityOp crit = new DeleteEntityOp();
		RequestEvent<DeleteEntityOp> req = getRequest(crit);
		crit.setId(2L);
		crit.setClose(true);
		ResponseEvent<Map<String, List>> resp = siteSvc.deleteSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(0, resp.getPayload().size());
	}
	
	@Test
	@DatabaseSetup("site-test/delete-site-initial.xml")
	@DatabaseTearDown("site-test/generic-delete-teardown.xml")
	public void deleteSiteTestWithVisitDependencies() {
		DeleteEntityOp crit = new DeleteEntityOp();
		RequestEvent<DeleteEntityOp> req = getRequest(crit);
		crit.setId(1L);
		crit.setClose(false);
		ResponseEvent<Map<String, List>> resp = siteSvc.deleteSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(1, resp.getPayload().size());
	}

	//@Test
	@DatabaseSetup("site-test/delete-site-which-have-sc-dependency-initial.xml")
	@DatabaseTearDown("site-test/generic-delete-teardown.xml")
	public void deleteSiteTestWithSCDependencies() {
		DeleteEntityOp crit = new DeleteEntityOp();
		RequestEvent<DeleteEntityOp> req = getRequest(crit);
		crit.setId(1L);
		crit.setClose(false);
		ResponseEvent<Map<String, List>> resp = siteSvc.deleteSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(1, resp.getPayload().size());
	}
	
	@Test
	@DatabaseSetup("site-test/create-site-data-initial.xml")
	@DatabaseTearDown("site-test/generic-delete-teardown.xml")
	@ExpectedDatabase(value="site-test/delete-close-site-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteSiteTestWithDependenciesAndClose() {
		DeleteEntityOp crit = new DeleteEntityOp();
		RequestEvent<DeleteEntityOp> req = getRequest(crit);
		crit.setId(2L);
		crit.setClose(true);
		ResponseEvent<Map<String, List>> resp = siteSvc.deleteSite(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(0, resp.getPayload().size());
	}
}