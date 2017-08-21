package com.krishagni.core.tests;

import java.util.List; 
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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.InstituteTestData;

import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class InstituteTest {
	@Resource
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private InstituteService instituteSvc;
	
	@Autowired
	ApplicationContext ctx;
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 * Create Institute Test Api's
	 */
	
	@Test
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/create-institute-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createInstituteTest() {
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(InstituteTestData.getInstituteDetail()));
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Name mismatch", "default-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Departments was found null", detail.getDepartments());
		
		List<DepartmentDetail> departments = detail.getDepartments();
		
		for (DepartmentDetail department : departments) {
			Assert.assertNotNull(department.getId());
			Assert.assertNotNull(department.getName());
			Assert.assertEquals("default-department", department.getName());
		}
	}
	
	@Test
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/create-institute-null-activity-status-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createInstituteTestWithNullActivityStatus() {
		InstituteDetail input = InstituteTestData.getInstituteDetail();
		input.setActivityStatus(null);
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(InstituteTestData.getInstituteDetail()));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Name mismatch", "default-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Departments was found null", detail.getDepartments());
		
		List<DepartmentDetail> departments = detail.getDepartments();
		
		for (DepartmentDetail department : departments) {
			Assert.assertNotNull(department.getId());
			Assert.assertNotNull(department.getName());
		}
	}
	
	@Test
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/create-institute-null-dept-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createInstituteTestWithNullDepartmentList() {
		InstituteDetail input = InstituteTestData.getInstituteDetail();
		input.setDepartments(null);
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(InstituteTestData.getInstituteDetail()));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Name mismatch", "default-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Departments was found null", detail.getDepartments());
		
		List<DepartmentDetail> departments = detail.getDepartments();
		
		for (DepartmentDetail department : departments) {
			Assert.assertNotNull(department.getId());
			Assert.assertNotNull(department.getName());
		}
	}
	
	@Test
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/create-institute-empty-activity-status-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createInstituteTestWithEmptyActivityStatus() {
		InstituteDetail input = InstituteTestData.getInstituteDetail();
		input.setActivityStatus("");
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(InstituteTestData.getInstituteDetail()));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Name mismatch", "default-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		Assert.assertNotNull("Error: Departments was found null", detail.getDepartments());
		
		List<DepartmentDetail> departments = detail.getDepartments();
		
		for (DepartmentDetail department : departments) {
			Assert.assertNotNull(department.getId());
			Assert.assertNotNull(department.getName());
		}
	}
	
	@Test
	public void createInstituteTestWithEmptyInstituteName() {
		InstituteDetail input = InstituteTestData.getInstituteDetail();
		input.setName("");
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);	
	}

	@Test
	public void createInstituteTestWithInvalidActivityStatus() {
		InstituteDetail input = InstituteTestData.getInstituteDetail();
		input.setActivityStatus("in-active");
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, ActivityStatusErrorCode.INVALID, ErrorType.USER_ERROR);	
	}
	
	@Test
	@DatabaseSetup("institute-test/create-institute-with-duplicate-name-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void createInstituteWithDuplicateInstituteName() {
		InstituteDetail input = InstituteTestData.getInstituteDetail();
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.DUP_NAME, ErrorType.USER_ERROR);
	}
	
	/*
	 * Update institute test api's
	 */
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/update-institute-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateInstituteTest() {
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(
				getRequest(InstituteTestData.getUpdateInstituteDetail()));
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals("Error: Institute name mismatch", "updated-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		
		List<DepartmentDetail> deptList = detail.getDepartments();
		
		for (DepartmentDetail departmentDetail : deptList) {
			if(departmentDetail.getId() == 1L) {
				Assert.assertEquals("updated-department",departmentDetail.getName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/update-institute-not-change-name-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateInstituteTestWithoutChangingInstituteName() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		input.setName("default-institute");
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(
				getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Institute name mismatch", "default-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		
		List<DepartmentDetail> deptList = detail.getDepartments();
		
		for (DepartmentDetail departmentDetail : deptList) {
			if(departmentDetail.getId() == 1L) {
				Assert.assertEquals("updated-department",departmentDetail.getName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/update-institute-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateInstituteTestWithNullActivityStatus() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		input.setActivityStatus(null);
		
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Institute name mismatch", "updated-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		
		List<DepartmentDetail> deptList = detail.getDepartments();
		
		for (DepartmentDetail departmentDetail : deptList) {
			if(departmentDetail.getId() == 1L) {
				Assert.assertEquals("updated-department",departmentDetail.getName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/update-institute-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateInstituteTestWithEmptyActivityStatus() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		input.setActivityStatus("");
		
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Institute name mismatch", "updated-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		
		List<DepartmentDetail> deptList = detail.getDepartments();
		
		for (DepartmentDetail departmentDetail : deptList) {
			if(departmentDetail.getId() == 1L) {
				Assert.assertEquals("updated-department",departmentDetail.getName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("institute-test/update-institute-with-dependencies-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/update-institute-with-dependencies-expected.xml",
			assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateInstituteTestWithUpdatedDepartmentWhichHaveDepenadacy() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Institute name mismatch", "updated-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		
		List<DepartmentDetail> deptList = detail.getDepartments();
		
		for (DepartmentDetail departmentDetail : deptList) {
			if(departmentDetail.getId() == 1L) {
				Assert.assertEquals("updated-department",departmentDetail.getName());
			}
		}
	}
	
	@Test 
	@DatabaseSetup("institute-test/update-institute-without-dependencies-for-remove-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/update-institute-without-dependencies-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateInstituteTestWithoutUpdatedDependenciesOfDepartment() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true,resp.isSuccessful());
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertNotNull("Error: Institute detail was found null", resp.getPayload());
		
		InstituteDetail detail = resp.getPayload();
		Assert.assertEquals("Error: Institute name mismatch", "updated-institute", detail.getName());
		Assert.assertEquals("Error: Activity status mismatch", "Active", detail.getActivityStatus());
		
		List<DepartmentDetail> deptList = detail.getDepartments();
		
		for (DepartmentDetail departmentDetail : deptList) {
			if(departmentDetail.getId() == 1L) {
				Assert.assertEquals("updated-department",departmentDetail.getName());
			}
		}
	}
	
	@Test 
	@DatabaseSetup("institute-test/update-institute-with-dependencies-for-remove-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void updateInstituteTestWithRomoveDepartmentsWhichHaveDependencies() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false,resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.DEPT_REF_ENTITY_FOUND, ErrorType.USER_ERROR);
	}
	
	
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void updateInstituteWithInavalidId() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		input.setId(-1L);
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void updateInstituteWithNonExistDepartment() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		List<DepartmentDetail> deptList = InstituteTestData.getInvalidDepartmentDetailList();
		input.setDepartments(deptList);
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.DEPT_NOT_FOUND, ErrorType.USER_ERROR);
	}
		
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void updateInstituteWithDuplicateInstituteName() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		input.setName("duplicate-institute");
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.DUP_NAME, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void updateInstituteTestWithEmptyInstituteName() {
		InstituteDetail input = InstituteTestData.getInstituteDetail();
		input.setName("");
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);	
	}
	
	@Test
	@DatabaseSetup("institute-test/update-institute-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void updateInstituteWithInvalidActivityStatus() {
		InstituteDetail input = InstituteTestData.getUpdateInstituteDetail();
		input.setActivityStatus("In-Active");
		ResponseEvent<InstituteDetail> resp = instituteSvc.updateInstitute(getRequest(input));
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, ActivityStatusErrorCode.INVALID, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get Institute list Test Api's
	 */
	
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstituteList() {
		InstituteListCriteria crit = new InstituteListCriteria();
		crit.startAt(1);
		crit.maxResults(3);
		RequestEvent<InstituteListCriteria> req = getRequest(crit);
		
		ResponseEvent<List<InstituteDetail>> resp = instituteSvc.getInstitutes(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute list was found null", resp.getPayload());
		Assert.assertEquals("Error: Institute list count mismatch",(int)3, resp.getPayload().size());
		
		List<InstituteDetail> instList = resp.getPayload();
		Assert.assertEquals(new Integer(3), new Integer(instList.size()));
		
		for (InstituteDetail detail : instList) {
			String instituteName = "default-institute" + detail.getId();
			
			Assert.assertEquals(instituteName, detail.getName());		
			
			List<DepartmentDetail> deptList = detail.getDepartments();
			for (DepartmentDetail deptDetail : deptList) {
				String deptName = "default-department" + deptDetail.getId();
				Assert.assertEquals(deptName, deptDetail.getName());
			}
		}
	}
	
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstituteListWithFilter() {
		InstituteListCriteria crit = new InstituteListCriteria();
		crit.query("default-institute1");
		RequestEvent<InstituteListCriteria> req = getRequest(crit);
		
		ResponseEvent<List<InstituteDetail>> resp = instituteSvc.getInstitutes(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute list was found null", resp.getPayload());
		Assert.assertEquals("Error: Institute list count mismatch",(int)1, resp.getPayload().size());
		
		List<InstituteDetail> instList = resp.getPayload();
		Assert.assertEquals(new Integer(1), new Integer(instList.size()));
	}
	
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstituteListWithoutStartAt_MaxResult_QueryFilter() {
		InstituteListCriteria crit = new InstituteListCriteria();
		
		RequestEvent<InstituteListCriteria> req = getRequest(crit);
		ResponseEvent<List<InstituteDetail>> resp = instituteSvc.getInstitutes(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute list was found null", resp.getPayload());
		Assert.assertEquals("Error: Institute list count mismatch",(int)5, resp.getPayload().size());
		
		List<InstituteDetail> instList = resp.getPayload();
		Assert.assertEquals(new Integer(5), new Integer(instList.size()));
		
		for (InstituteDetail detail : instList) {
			String instituteName = "default-institute" + detail.getId();
			Assert.assertEquals(instituteName, detail.getName());		
			
			List<DepartmentDetail> deptList = detail.getDepartments();
			for (DepartmentDetail deptDetail : deptList) {
				String deptName = "default-department" + deptDetail.getId();
				Assert.assertEquals(deptName, deptDetail.getName());
			}
		}
	}
	
	@Test
	public void getInstituteListWithEmptyData() {
		InstituteListCriteria crit = new InstituteListCriteria();
		crit.startAt(0);
		crit.maxResults(0);
		RequestEvent<InstituteListCriteria> req = getRequest(crit);
		ResponseEvent<List<InstituteDetail>> resp = instituteSvc.getInstitutes(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute list was found null", resp.getPayload());
		Assert.assertEquals("Error: Institute list count mismatch",(int)0, resp.getPayload().size());
	}
	
	/*
	 * Get Institute Test Api's
	 */
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstitute() {
		InstituteQueryCriteria crit = new InstituteQueryCriteria(); 
		crit.setId(1L);
		
		RequestEvent<InstituteQueryCriteria> req = getRequest(crit);		
		ResponseEvent<InstituteDetail> resp = instituteSvc.getInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute list was found null", resp.getPayload());
		Assert.assertEquals("Error: Institute id mismatch",(Long)1L, resp.getPayload().getId());
		InstituteDetail detail = resp.getPayload();
		
		List<DepartmentDetail> deptList = detail.getDepartments();
		for (DepartmentDetail deptDetail : deptList) {
			String deptName = "default-department1";
			Assert.assertEquals(deptName, deptDetail.getName());
		}
	}
	
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstituteWithInstituteName() {
		InstituteQueryCriteria crit = new InstituteQueryCriteria(); 
		crit.setName("default-institute1");
		
		RequestEvent<InstituteQueryCriteria> req = getRequest(crit);		
		ResponseEvent<InstituteDetail> resp = instituteSvc.getInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertNotNull("Error: Response was found null", resp);
		Assert.assertEquals("Error: Response was not found successfull", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Institute list was found null", resp.getPayload());
		Assert.assertEquals("Error: Institute id mismatch",(Long)1L, resp.getPayload().getId());
		
		InstituteDetail detail = resp.getPayload();
		List<DepartmentDetail> deptList = detail.getDepartments();
		for (DepartmentDetail deptDetail : deptList) {
			String deptName = "default-department1";
			Assert.assertEquals(deptName, deptDetail.getName());
		}
	}
	
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstituteWithNullIdAndNullName() {
		InstituteQueryCriteria crit = new InstituteQueryCriteria(); 
		crit.setId(null);
		crit.setName(null);
		
		RequestEvent<InstituteQueryCriteria> req = getRequest(crit);		
		ResponseEvent<InstituteDetail> resp = instituteSvc.getInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test 
	@DatabaseSetup("institute-test/institute-list-with-disabled-activity-status-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstituteWithIdAndDisabledActivityStatus() {
		InstituteQueryCriteria crit = new InstituteQueryCriteria(); 
		crit.setId(4L);
		RequestEvent<InstituteQueryCriteria> req = getRequest(crit);		
		ResponseEvent<InstituteDetail> resp = instituteSvc.getInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test 
	@DatabaseSetup("institute-test/institute-list-with-disabled-activity-status-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	public void getInstituteWithNameAndDisabledActivityStatus() {
		InstituteQueryCriteria crit = new InstituteQueryCriteria(); 
		crit.setId(4L);
		RequestEvent<InstituteQueryCriteria> req = getRequest(crit);
		ResponseEvent<InstituteDetail> resp = instituteSvc.getInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Delete Institute test Api's
	 */
	
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/delete-institute-expected.xml",
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteInstitute() {
		DeleteEntityOp deleteInstOp = new DeleteEntityOp();
		deleteInstOp.setId(2L);
		deleteInstOp.setClose(false);
		RequestEvent<DeleteEntityOp> req = getRequest(deleteInstOp);
		ResponseEvent<Map<String,List>> resp = instituteSvc.deleteInstitute(req);
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(0, resp.getPayload().size());
	}
	
	@Test
	@DatabaseSetup("institute-test/institute-with-dependencies-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/delete-institute-with-dependency-expected.xml",
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteInstituteWhichHaveDependencies() {
		DeleteEntityOp deleteInstOp = new DeleteEntityOp();
		deleteInstOp.setId(1L);
		deleteInstOp.setClose(false);
		RequestEvent<DeleteEntityOp> req = getRequest(deleteInstOp);
		
		ResponseEvent<Map<String,List>> resp = instituteSvc.deleteInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
	    Assert.assertEquals(1, resp.getPayload().size());
	}
	
	@Test 
	@DatabaseSetup("institute-test/institute-with-dependencies-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/delete-close-institute-with-dependency-expected.xml",
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteInstituteWhichHaveDependenciesButClose() {
		DeleteEntityOp deleteInstOp = new DeleteEntityOp();
		deleteInstOp.setId(1L);
		deleteInstOp.setClose(true);
		RequestEvent<DeleteEntityOp> req = getRequest(deleteInstOp);
		
		ResponseEvent<Map<String,List>> resp = instituteSvc.deleteInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
	    Assert.assertEquals(0, resp.getPayload().size());
	}
	
	@Test
	@DatabaseSetup("institute-test/institute-list-initial.xml")
	@DatabaseTearDown("institute-test/generic-teardown.xml")
	@ExpectedDatabase(value="institute-test/delete-close-institute-expected.xml",
	assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteInstituteAndClose() {
		DeleteEntityOp deleteInstOp = new DeleteEntityOp();
		deleteInstOp.setId(2L);
		deleteInstOp.setClose(true);
		RequestEvent<DeleteEntityOp> req = getRequest(deleteInstOp);
		
		ResponseEvent<Map<String,List>> resp = instituteSvc.deleteInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(true, resp.isSuccessful());
		Assert.assertEquals(true, resp.getPayload().isEmpty());
	}
	
	@Test
	public void deleteInstituteWithNullId() {
		DeleteEntityOp deleteInstOp = new DeleteEntityOp();
		deleteInstOp.setId(null);
		deleteInstOp.setClose(false);
		RequestEvent<DeleteEntityOp> req = getRequest(deleteInstOp);
		
		ResponseEvent<Map<String,List>> resp = instituteSvc.deleteInstitute(req);
		TestUtils.recordResponse(resp);
		
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	

	@Test
	public void deleteInstituteWithInvalidId() {
		DeleteEntityOp deleteInstOp = new DeleteEntityOp();
		deleteInstOp.setId(-1L);
		deleteInstOp.setClose(false);
		RequestEvent<DeleteEntityOp> req = getRequest(deleteInstOp);
		ResponseEvent<Map<String,List>> resp = instituteSvc.deleteInstitute(req);
		Assert.assertEquals(false, resp.isSuccessful());
		TestUtils.checkErrorCode(resp, InstituteErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
}
