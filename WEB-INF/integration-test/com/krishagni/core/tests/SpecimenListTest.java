package com.krishagni.core.tests;

import java.util.Date;
import java.util.Arrays;
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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenListErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSummary;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenListService;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;

import com.krishagni.core.tests.testdata.SpecimenListTestData;
import com.krishagni.core.tests.testdata.CommonUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })

@WebAppConfiguration
public class SpecimenListTest {
	@Resource 
	private WebApplicationContext webApplicationContext;
	
	@Autowired 
	private SpecimenListService specimenListSvc;
	
	@Autowired 
	private ApplicationContext ctx;

	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	/*
	 *  createSpecimensList API test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/create-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/create-specimen-list-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createSpecimenList() {
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.createSpecimenList(getRequest(SpecimenListTestData.getSpecimenListDetail()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		SpecimenListDetails details = resp.getPayload();
		Assert.assertEquals("Error: List name mismatch", "list-name", details.getName());
		assertUserSummary(details.getOwner(), details.getOwner().getId().intValue());
		
		for(UserSummary user : details.getSharedWith()) {
			assertUserSummary(user, user.getId().intValue());
		}
		for(SpecimenDetail specimen : details.getSpecimens()) {
			assertSpecimenDetails(specimen, specimen.getId().intValue());
		}
	}
	
	public void assertUserSummary(UserSummary user, int i) {
		Assert.assertEquals("Error: User's first name mismatch", "ADMIN"+i, user.getFirstName());
		Assert.assertEquals("Error: User's last name mismatch", "ADMIN"+i, user.getLastName());
		Assert.assertEquals("Error: User's login name mismatch", "admin"+i+"@admin.com", user.getLoginName());
	}
	
	public void assertSpecimenDetails(SpecimenDetail specimen, int i) {
		Assert.assertEquals("Error: Anatomic site mismatch", "Head", specimen.getAnatomicSite());
		Assert.assertEquals("Error: Laterality mismatch", "Right", specimen.getLaterality());
		Assert.assertEquals("Error: Lineage mismatch", "New", specimen.getLineage());
		Assert.assertEquals("Error: Specimen label mismatch", "spm-"+i, specimen.getLabel());
		Assert.assertEquals("Error: Specimen barcode mismatch", "spm-barcode-"+i, specimen.getBarcode());
		Assert.assertEquals("Error: Pathology status mismatch", "Metastatic", specimen.getPathology());
		Assert.assertEquals("Error: Specimen class mismatch", "Molecular", specimen.getSpecimenClass());
		Assert.assertEquals("Error: Specimen type mismatch", "DNA", specimen.getType());
		Assert.assertEquals("Error: Initial quantity mismatch", new Double(0.5), specimen.getInitialQty());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/create-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void createSpecimenListWithNullOwner() {
		SpecimenListDetails input = SpecimenListTestData.getSpecimenListDetail();
		input.setOwner(null);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.createSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		assertUserSummary(resp.getPayload().getOwner(), resp.getPayload().getOwner().getId().intValue());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/create-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void createSpecimenListWithNullName() {
		SpecimenListDetails input = SpecimenListTestData.getSpecimenListDetail();
		input.setName(null);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.createSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/create-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void createSpecimenListWithNullSpecimenListAndUsers() {
		SpecimenListDetails input = SpecimenListTestData.getSpecimenListDetail();
		input.setSpecimens(null);
		input.setSharedWith(null);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.createSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Specimen list count mismatch", 0, resp.getPayload().getSpecimens().size());
		Assert.assertEquals("Error: Shared user count mismatch", 0, resp.getPayload().getSharedWith().size());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/create-specimen-list-with-invalid-specimen-and-users.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void createSpecimenListWithInvalidSpecimensAndUsers() {
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.createSpecimenList(getRequest(SpecimenListTestData.getSpecimenListDetail()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.INVALID_LABELS, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.INVALID_USERS_LIST, ErrorType.USER_ERROR);
	}
	
	/*
	 * Update Specimen List API's Test Cases
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/update-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/update-specimen-list-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateSpecimenList() {
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.updateSpecimenList(
				getRequest(SpecimenListTestData.getUpdateSpecimenListDetail()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		SpecimenListDetails details = resp.getPayload();
		Assert.assertEquals("Error: List name mismatch", "updated-name", details.getName());
		assertUserSummary(details.getOwner(), details.getOwner().getId().intValue());
		Assert.assertEquals("Error: Shared user count mismatch", 0, details.getSharedWith().size());
		for(SpecimenDetail specimen : details.getSpecimens()) {
			assertSpecimenDetails(specimen, specimen.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/update-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void updateSpecimenListWithNullOwner() {
		SpecimenListDetails input = SpecimenListTestData.getUpdateSpecimenListDetail();
		input.setOwner(null);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.updateSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		assertUserSummary(resp.getPayload().getOwner(), resp.getPayload().getOwner().getId().intValue());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/update-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void updateSpecimenListWithNullName() {
		SpecimenListDetails input = SpecimenListTestData.getUpdateSpecimenListDetail();
		input.setName(null);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.updateSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NAME_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/update-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void updateSpecimenListWithNullSpecimenListAndUsers() {
		SpecimenListDetails input = SpecimenListTestData.getUpdateSpecimenListDetail();
		input.setSpecimens(null);
		input.setSharedWith(null);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.updateSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Specimen list count mismatch", 0, resp.getPayload().getSpecimens().size());
		Assert.assertEquals("Error: Shared user count mismatch", 0, resp.getPayload().getSharedWith().size());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/update-specimen-list-with-invalid-specimens.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void updateSpecimenListWithInvalidSpecimens() {
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.updateSpecimenList(getRequest(SpecimenListTestData.getUpdateSpecimenListDetail()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.INVALID_LABELS, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/update-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void updateInvalidSpecimenList() {
		SpecimenListDetails input = SpecimenListTestData.getUpdateSpecimenListDetail();
		input.setId(-1L);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.updateSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/update-specimen-list-initial.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void updateSpecimenListWithNullId() {
		SpecimenListDetails input = SpecimenListTestData.getUpdateSpecimenListDetail();
		input.setId(null);
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.updateSpecimenList(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get Specimen List For User API test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void getUserSpecimenList() {
		ResponseEvent<List<SpecimenListSummary>> resp = specimenListSvc.getUserSpecimenLists(getRequest(null));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		List<SpecimenListSummary> summaryDetail = resp.getPayload();
		Assert.assertEquals("Error: Summary count mismatch", 2, summaryDetail.size());
		for(SpecimenListSummary summary : summaryDetail) {
			Assert.assertEquals("Error: List name mismatch", "list-"+summary.getId(), summary.getName());
			assertUserSummary(summary.getOwner(), summary.getOwner().getId().intValue());
		}
	}
	
	/*
	 * Get Specimen List by Id API test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void getUserSpecimenListById() {
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.getSpecimenList(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		SpecimenListDetails details = resp.getPayload();
		Assert.assertEquals("Error: List id mismatch", new Long(1), details.getId());
		Assert.assertEquals("Error: List name mismatch", "list-"+details.getId(), details.getName());
		assertUserSummary(details.getOwner(), details.getOwner().getId().intValue());
		
		for(UserSummary user : details.getSharedWith()) {
			assertUserSummary(user, user.getId().intValue());
		}
		for(SpecimenDetail specimen : details.getSpecimens()) {
			assertSpecimenDetails(specimen, specimen.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void getUserSpecimenListWithInvalidId() {
		ResponseEvent<SpecimenListDetails> resp = specimenListSvc.getSpecimenList(getRequest(-1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get List Specimens API Test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void getListSpecimens() {
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.getListSpecimens(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		for(SpecimenDetail specimen : resp.getPayload()) {
			assertSpecimenDetails(specimen, specimen.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void getListSpecimensUsingInvalidId() {
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.getListSpecimens(getRequest(-1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}

	/*
	 * Update List Specimens API Test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/updated-list-specimens.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateListSpecimens() {
		UpdateListSpecimensOp opDetail = new UpdateListSpecimensOp();
		opDetail.setListId(1L);
		opDetail.setSpecimens(Arrays.asList("spm-3","spm-4","spm-5"));
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp.Operation.valueOf("UPDATE"));
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.updateListSpecimens(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		int i=3;
		for(SpecimenDetail specimen : resp.getPayload()) {
			assertSpecimenDetails(specimen, i);
			i++;
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	public void updateInvalidListSpecimens() {
		UpdateListSpecimensOp opDetail = new UpdateListSpecimensOp();
		opDetail.setListId(-1L);
		opDetail.setSpecimens(Arrays.asList("spm-3","spm-4","spm-5"));
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp.Operation.valueOf("UPDATE"));
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.updateListSpecimens(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/updated-list-specimens-with-remove-operation.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateListSpecimensWithoutSpecimens() {
		UpdateListSpecimensOp opDetail = new UpdateListSpecimensOp();
		opDetail.setListId(1L);
		opDetail.setSpecimens(null);
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp.Operation.valueOf("UPDATE"));
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.updateListSpecimens(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Specimen List was not empty", 0, resp.getPayload().size());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/updated-list-specimens-with-adding-new-specimens.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateListSpecimensWithAddingNewSpecimens() {
		UpdateListSpecimensOp opDetail = new UpdateListSpecimensOp();
		opDetail.setListId(2L);
		opDetail.setSpecimens(Arrays.asList("spm-3","spm-4"));
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp.Operation.valueOf("ADD"));
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.updateListSpecimens(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Specimen List size mismatch", 4, resp.getPayload().size());
		Assert.assertEquals("Error: Specimen label mismatch", "spm-3", resp.getPayload().get(0).getLabel());
		Assert.assertEquals("Error: Specimen label mismatch", "spm-4", resp.getPayload().get(1).getLabel());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/generic-specimen-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateListSpecimensWithAddingExistingSpecimens() {
		UpdateListSpecimensOp opDetail = new UpdateListSpecimensOp();
		opDetail.setListId(2L);
		opDetail.setSpecimens(Arrays.asList("spm-1","spm-2"));
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp.Operation.valueOf("ADD"));
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.updateListSpecimens(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		int i=1;
		for(SpecimenDetail specimen : resp.getPayload()) {
			assertSpecimenDetails(specimen, i);
			i++;
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/updated-list-specimens-with-adding-new-specimens.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/generic-specimen-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateListSpecimensWithRemovingSpecimens() {
		UpdateListSpecimensOp opDetail = new UpdateListSpecimensOp();
		opDetail.setListId(2L);
		opDetail.setSpecimens(Arrays.asList("spm-3","spm-4"));
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp.Operation.valueOf("REMOVE"));
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.updateListSpecimens(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		int i=1;
		for(SpecimenDetail specimen : resp.getPayload()) {
			assertSpecimenDetails(specimen, i);
			i++;
		}
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/generic-specimen-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateListSpecimensWithRemovingUntrackedSpecimens() {
		UpdateListSpecimensOp opDetail = new UpdateListSpecimensOp();
		opDetail.setListId(2L);
		opDetail.setSpecimens(Arrays.asList("spm-5","spm-6"));
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp.Operation.valueOf("REMOVE"));
		ResponseEvent<List<SpecimenDetail>> resp = specimenListSvc.updateListSpecimens(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		int i=1;
		for(SpecimenDetail specimen : resp.getPayload()) {
			assertSpecimenDetails(specimen, i);
			i++;
		}
	}
	
	/*
	 * Share Specimen List API's Test
	 */
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/share-specimen-list-with-new-user.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void shareSpecimenListWithAddingNewUser() {
		ShareSpecimenListOp opDetail = new ShareSpecimenListOp();
		opDetail.setListId(1L);
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp.Operation.valueOf("ADD"));
		opDetail.setUserIds(Arrays.asList(3L, 4L));
		
		ResponseEvent<List<UserSummary>> resp = specimenListSvc.shareSpecimenList(getRequest(opDetail));
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: User id mismatch", new Long(3), resp.getPayload().get(0).getId());
		Assert.assertEquals("Error: User id mismatch", new Long(4), resp.getPayload().get(1).getId());
		Assert.assertEquals("Error: User id mismatch", new Long(2), resp.getPayload().get(2).getId());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/generic-specimen-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void shareSpecimenListWithAddingNonExistingUser() {
		ShareSpecimenListOp opDetail = new ShareSpecimenListOp();
		opDetail.setListId(1L);
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp.Operation.valueOf("ADD"));
		opDetail.setUserIds(Arrays.asList(5L, 6L));
		
		ResponseEvent<List<UserSummary>> resp = specimenListSvc.shareSpecimenList(getRequest(opDetail));
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: User id mismatch", new Long(2), resp.getPayload().get(0).getId());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/generic-specimen-list.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/share-specimen-list-with-new-user.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void shareSpecimenListWithUpdatedUserList() {
		ShareSpecimenListOp opDetail = new ShareSpecimenListOp();
		opDetail.setListId(1L);
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp.Operation.valueOf("UPDATE"));
		opDetail.setUserIds(Arrays.asList(2L, 3L, 4L));
		
		ResponseEvent<List<UserSummary>> resp = specimenListSvc.shareSpecimenList(getRequest(opDetail));
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: User id mismatch", new Long(3), resp.getPayload().get(0).getId());
		Assert.assertEquals("Error: User id mismatch", new Long(4), resp.getPayload().get(1).getId());
		Assert.assertEquals("Error: User id mismatch", new Long(2), resp.getPayload().get(2).getId());
	}
	
	@Test
	@DatabaseSetup("specimen-test/specimen-list/share-specimen-list-with-new-user.xml")
	@DatabaseTearDown("specimen-test/specimen-list/generic-teardown.xml")
	@ExpectedDatabase(value="specimen-test/specimen-list/generic-specimen-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void shareSpecimenListWithByRemovingUser() {
		ShareSpecimenListOp opDetail = new ShareSpecimenListOp();
		opDetail.setListId(1L);
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp.Operation.valueOf("REMOVE"));
		opDetail.setUserIds(Arrays.asList(3L, 4L));
		
		ResponseEvent<List<UserSummary>> resp = specimenListSvc.shareSpecimenList(getRequest(opDetail));
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: User id mismatch", new Long(2), resp.getPayload().get(0).getId());
	}
	
	@Test
	public void shareSpecimenListWhichIsNotPresent() {
		ShareSpecimenListOp opDetail = new ShareSpecimenListOp();
		opDetail.setListId(1L);
		opDetail.setOp(com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp.Operation.valueOf("ADD"));
		opDetail.setUserIds(Arrays.asList(3L, 4L));
		
		ResponseEvent<List<UserSummary>> resp = specimenListSvc.shareSpecimenList(getRequest(opDetail));
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SpecimenListErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
}	