package com.krishagni.core.tests;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ListFolderQueriesCriteria;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetails;
import com.krishagni.catissueplus.core.de.events.QueryFolderSummary;
import com.krishagni.catissueplus.core.de.events.SavedQueriesList;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.events.ShareQueryFolderOp;
import com.krishagni.catissueplus.core.de.events.UpdateFolderQueriesOp;
import com.krishagni.catissueplus.core.de.events.UpdateFolderQueriesOp.Operation;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.QueryFoldersTestData;

import edu.wustl.common.beans.SessionDataBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class QueryFoldersTest {
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private QueryService querySvc;
	
	@Autowired
	private ApplicationContext ctx;

	private <T> RequestEvent<T> getRequest(T payload) {
		return CommonUtils.getRequest(payload);
	}
	
	private <T> RequestEvent<T> getUnathRequest(T payload) {
		return CommonUtils.getUnauthRequest(payload);
	}
	
	/*
	 * Create Query Folder API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/create-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/create-query-folder-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createQueryFolderTest() {
		ResponseEvent<QueryFolderDetails> resp = querySvc.createFolder(getRequest(QueryFoldersTestData.getQueryFolderDetails()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		QueryFolderDetails details = resp.getPayload();
		Assert.assertEquals("Error: Folder name mismatch", "query-folder", details.getName());
		assertUserSummary(details.getOwner(), details.getOwner().getId().intValue());

		for(SavedQuerySummary query : details.getQueries()) {
			assertQueryDetails(query, query.getId().intValue());
		}
	}
	
	public void assertUserSummary(UserSummary user, int i) {
		Assert.assertEquals("Error: User's first name mismatch", "ADMIN"+i, user.getFirstName());
		Assert.assertEquals("Error: User's last name mismatch", "ADMIN"+i, user.getLastName());
	}
	
	public void assertQueryDetails(SavedQuerySummary query, int i) {
		Assert.assertEquals("Error: Query name mismatch", "query-"+i, query.getTitle());
		assertUserSummary(query.getCreatedBy(), query.getCreatedBy().getId().intValue());
		assertUserSummary(query.getLastModifiedBy(), query.getLastModifiedBy().getId().intValue()); 
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/create-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void createQueryFolderTestWithNullName() {
		QueryFolderDetails input = QueryFoldersTestData.getQueryFolderDetails();
		input.setName(null);
		
		ResponseEvent<QueryFolderDetails> resp = querySvc.createFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NAME_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	public void createQueryFolderTestWithoutUsersAndQueries() {
		ResponseEvent<QueryFolderDetails> resp = querySvc.createFolder(getRequest(QueryFoldersTestData.getQueryFolderDetails()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, UserErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.QUERIES_NOT_ACCESSIBLE, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/create-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/create-query-folder-share-with-specific-users-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void createQueryFolderTestShareWithSpecificUsers() {
		QueryFolderDetails input = QueryFoldersTestData.getQueryFolderDetails();
		input.setSharedWithAll(false);
		input.setSharedWith(QueryFoldersTestData.getUsers(1,3));
		
		ResponseEvent<QueryFolderDetails> resp = querySvc.createFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		QueryFolderDetails details = resp.getPayload();
		Assert.assertEquals("Error: Folder name mismatch", "query-folder", details.getName());
		assertUserSummary(details.getOwner(), details.getOwner().getId().intValue());
		
		for(UserSummary user : details.getSharedWith()) {
			if(user.getId() == details.getOwner().getId()) {
				continue;
			}
			assertUserSummary(user, user.getId().intValue());
		}

		for(SavedQuerySummary query : details.getQueries()) {
			assertQueryDetails(query, query.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/create-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void createQueryFolderTestWithInvalidUsers() {
		QueryFolderDetails input = QueryFoldersTestData.getQueryFolderDetails();
		input.setSharedWithAll(false);
		input.setSharedWith(Arrays.asList(CommonUtils.getUser(-1L, "User1", "User1", "User1@login.com")));
		
		ResponseEvent<QueryFolderDetails> resp = querySvc.createFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.INVALID_SHARE_ACCESS_DETAILS, ErrorType.USER_ERROR);
	}
	
	/*
	 * Update Query Folder API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-query-folder-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateQueryFolderTest() {
		ResponseEvent<QueryFolderDetails> resp = querySvc.updateFolder(getRequest(QueryFoldersTestData.getUpdateQueryFolderDetails()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		QueryFolderDetails details = resp.getPayload();
		Assert.assertEquals("Error: Folder name mismatch", "updated-query-folder", details.getName());
		assertUserSummary(details.getOwner(), details.getOwner().getId().intValue());

		for(UserSummary user : details.getSharedWith()) {
			if(user.getId() == details.getOwner().getId()) {
				continue;
			}
			assertUserSummary(user, user.getId().intValue());
		}

		for(SavedQuerySummary query : details.getQueries()) {
			assertQueryDetails(query, query.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void updateQueryFolderTestWithNullFolderId() {
		QueryFolderDetails input = QueryFoldersTestData.getUpdateQueryFolderDetails();
		input.setId(null);
		ResponseEvent<QueryFolderDetails> resp = querySvc.updateFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void updateInvalidQueryFolderTest() {
		QueryFolderDetails input = QueryFoldersTestData.getUpdateQueryFolderDetails();
		input.setId(-1L);
		ResponseEvent<QueryFolderDetails> resp = querySvc.updateFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void updateQueryFolderTestWithNullName() {
		QueryFolderDetails input = QueryFoldersTestData.getUpdateQueryFolderDetails();
		input.setName(null);
		
		ResponseEvent<QueryFolderDetails> resp = querySvc.updateFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NAME_REQUIRED, ErrorType.USER_ERROR);
	}
	
	@Test
	public void updateQueryFolderTestWithoutInitialData() {
		ResponseEvent<QueryFolderDetails> resp = querySvc.updateFolder(getRequest(QueryFoldersTestData.getUpdateQueryFolderDetails()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-query-folder-for-share-with-all-users-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateQueryFolderTestForShareWithAll() {
		QueryFolderDetails input = QueryFoldersTestData.getUpdateQueryFolderDetails();
		input.setSharedWithAll(true);
		
		ResponseEvent<QueryFolderDetails> resp = querySvc.updateFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		QueryFolderDetails details = resp.getPayload();
		Assert.assertEquals("Error: Folder name mismatch", "updated-query-folder", details.getName());
		assertUserSummary(details.getOwner(), details.getOwner().getId().intValue());

		for(SavedQuerySummary query : details.getQueries()) {
			assertQueryDetails(query, query.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void updateQueryFolderTestWithInvalidUsers() {
		QueryFolderDetails input = QueryFoldersTestData.getUpdateQueryFolderDetails();
		input.setSharedWith(Arrays.asList(CommonUtils.getUser(-1L, "User1", "User1", "User1@login.com")));
		
		ResponseEvent<QueryFolderDetails> resp = querySvc.updateFolder(getRequest(input));
		
		TestUtils.recordResponse(resp);
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.INVALID_SHARE_ACCESS_DETAILS, ErrorType.USER_ERROR);
	}
	
	/*
	 *  Get User Folders API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getUserFoldersTest() {
		ResponseEvent<List<QueryFolderSummary>> resp = querySvc.getUserFolders(getRequest(null));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		
		for(QueryFolderSummary detail : resp.getPayload()) {
			Long ownerId = detail.getOwner().getId();
			if(detail.getId() == ownerId || detail.isSharedWithAll() == true) {
				Assert.assertEquals("Error: Folder name mismatch", "query-folder-" + detail.getId(), detail.getName());
				Assert.assertEquals("Error: Folder was not found shared", true, detail.isSharedWithAll());
				assertUserSummary(detail.getOwner(), detail.getOwner().getId().intValue());
			}
		}
	}
	
	/*
	 * Get Folder API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getFolderTest() {
		ResponseEvent<QueryFolderDetails> resp = querySvc.getFolder(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		QueryFolderDetails detail = resp.getPayload();
		Assert.assertEquals("Error: Folder was found shared", false, detail.isSharedWithAll());
		Assert.assertEquals("Error: Folder id mismatch", new Long(1), detail.getId());
		Assert.assertEquals("Error: Folder name mismatch", "query-folder-" + detail.getId(), detail.getName());
		
		assertUserSummary(detail.getOwner(), detail.getOwner().getId().intValue());
		
		for(UserSummary user : detail.getSharedWith()) {
			if(user.getId() == detail.getOwner().getId()) {
				continue;
			}
			assertUserSummary(user, user.getId().intValue());
		}

		for(SavedQuerySummary query : detail.getQueries()) {
			assertQueryDetails(query, query.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getInvalidFolderTest() {
		ResponseEvent<QueryFolderDetails> resp = querySvc.getFolder(getRequest(-1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}

	/*
	 * Delete folder API's Test
	 */
	 
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/user-folder-list-after-deletion.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void deleteFolderTest() {
		ResponseEvent<Long> resp = querySvc.deleteFolder(getRequest(4L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Deleted folder id mismatch", new Long(4), resp.getPayload());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void deleteFolderTestWithNullId() {
		Long id = null;
		ResponseEvent<Long> resp = querySvc.deleteFolder(getRequest(id));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	public void deleteFolderTestWhichIsNotPresent() {
		ResponseEvent<Long> resp = querySvc.deleteFolder(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get Folder Queries API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getFolderQueries() {
		ResponseEvent<SavedQueriesList> resp = querySvc.getFolderQueries(getRequest(QueryFoldersTestData.getCriteria()));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		SavedQueriesList list = resp.getPayload();
		Assert.assertEquals(new Long(4), list.getCount());
		for(SavedQuerySummary query : list.getQueries()) {
			assertQueryDetails(query, query.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getFolderQueriesWithQuery() {
		ListFolderQueriesCriteria input = QueryFoldersTestData.getCriteria();
		input.query("3");
		ResponseEvent<SavedQueriesList> resp = querySvc.getFolderQueries(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		SavedQueriesList list = resp.getPayload();
		Assert.assertEquals(new Long(1), list.getCount());
		for(SavedQuerySummary query : list.getQueries()) {
			assertQueryDetails(query, query.getId().intValue());
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/user-folder-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getFolderQueriesFromInvalidFolder() {
		ListFolderQueriesCriteria input = QueryFoldersTestData.getCriteria();
		input.folderId(-1L);
		ResponseEvent<SavedQueriesList> resp = querySvc.getFolderQueries(getRequest(input));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Update Folder Queries
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-folder-queries-expected.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateFolderQueries() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(Arrays.asList(3L, 4L, 5L, 6L));
		opDetail.setOp(Operation.UPDATE);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		
		List<SavedQuerySummary> queryList = resp.getPayload();
		Assert.assertEquals(new Long(3), queryList.get(0).getId());
		Assert.assertEquals(new Long(4), queryList.get(1).getId());
		Assert.assertEquals(new Long(5), queryList.get(2).getId());
		Assert.assertEquals(new Long(6), queryList.get(3).getId());
	}
	
	@Test
	public void updateFolderQueriesWithInvalidFolderId() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(Arrays.asList(3L, 4L, 5L, 6L));
		opDetail.setOp(Operation.UPDATE);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-folder-queries-with-null-query-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateFolderQueriesWithInvalidQueries() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(Arrays.asList(-33L, -44L, -55L, -66L));
		opDetail.setOp(Operation.UPDATE);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		
		List<SavedQuerySummary> queryList = resp.getPayload();
		Assert.assertEquals("Error: Queries count mismatch", 0, queryList.size());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-folder-queries-with-null-query-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateFolderQueriesWithNullQueryList() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(null);
		opDetail.setOp(Operation.UPDATE);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		
		List<SavedQuerySummary> queryList = resp.getPayload();
		Assert.assertEquals("Error: Queries count mismatch", 0, queryList.size());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-folder-queries-with-add-operation.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateFolderQueriesWithAddOperation() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(Arrays.asList(3L, 6L));
		opDetail.setOp(Operation.ADD);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		
		List<SavedQuerySummary> queryList = resp.getPayload();
		Assert.assertEquals(new Long(3), queryList.get(0).getId());
		Assert.assertEquals(new Long(1), queryList.get(1).getId());
		Assert.assertEquals(new Long(2), queryList.get(2).getId());
		Assert.assertEquals(new Long(6), queryList.get(3).getId());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void updateFolderQueriesWithAddOperationWithInvalidQueries() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(Arrays.asList(-3L, -6L));
		opDetail.setOp(Operation.ADD);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());

		List<SavedQuerySummary> queryList = resp.getPayload();
		Assert.assertEquals(new Long(3), queryList.get(0).getId());
		Assert.assertEquals(new Long(1), queryList.get(1).getId());
		Assert.assertEquals(new Long(2), queryList.get(2).getId());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-folder-queries-with-null-query-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateFolderQueriesWithRemoveOperation() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(Arrays.asList(1L, 2L, 3L));
		opDetail.setOp(Operation.REMOVE);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		
		List<SavedQuerySummary> queryList = resp.getPayload();
		Assert.assertEquals("Error: Queries list size mismatch", 0, queryList.size());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/update-folder-queries-with-remove-operation.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void updateFolderQueriesWithRemovingInvalidQueries() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(2L);
		opDetail.setQueries(Arrays.asList(2L, 3L, -1L));
		opDetail.setOp(Operation.REMOVE);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		
		List<SavedQuerySummary> queryList = resp.getPayload();
		Assert.assertEquals("Error: Queries list size mismatch", 1, queryList.size());
		Assert.assertEquals(new Long(1), queryList.get(0).getId());
	}
	
	/*
	 * Share Folder API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/share-folder-with-updating-user-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void shareFolderTestWithUpdateUserList() {
		ShareQueryFolderOp opDetail = new ShareQueryFolderOp();
		opDetail.setFolderId(2L);
		opDetail.setUserIds(Arrays.asList(2L, 3L, 4L, 5L));
		opDetail.setOp(ShareQueryFolderOp.Operation.UPDATE);
		
		ResponseEvent<List<UserSummary>> resp = querySvc.shareFolder(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals(new Long(3), resp.getPayload().get(0).getId());
		Assert.assertEquals(new Long(4), resp.getPayload().get(1).getId());
		Assert.assertEquals(new Long(2), resp.getPayload().get(2).getId());
		Assert.assertEquals(new Long(5), resp.getPayload().get(3).getId());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void shareFolderTestWithInvalidId() {
		ShareQueryFolderOp opDetail = new ShareQueryFolderOp();
		opDetail.setFolderId(-2L);
		opDetail.setUserIds(Arrays.asList(3L, 4L, 5L));
		opDetail.setOp(ShareQueryFolderOp.Operation.UPDATE);
		
		ResponseEvent<List<UserSummary>> resp = querySvc.shareFolder(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.FOLDER_NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void shareFolderTestWithNullUserList() {
		ShareQueryFolderOp opDetail = new ShareQueryFolderOp();
		opDetail.setFolderId(2L);
		opDetail.setUserIds(null);
		opDetail.setOp(ShareQueryFolderOp.Operation.UPDATE);
		
		ResponseEvent<List<UserSummary>> resp = querySvc.shareFolder(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Shared user list size mismatch", 0, resp.getPayload().size());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/share-folder-with-updating-user-list.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void shareFolderTestWithAddingNewUsers() {
		ShareQueryFolderOp opDetail = new ShareQueryFolderOp();
		opDetail.setFolderId(2L);
		opDetail.setUserIds(Arrays.asList(2L, 4L, 5L, 6L, 7L));
		opDetail.setOp(ShareQueryFolderOp.Operation.ADD);
		
		ResponseEvent<List<UserSummary>> resp = querySvc.shareFolder(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals(new Long(3), resp.getPayload().get(0).getId());
		Assert.assertEquals(new Long(4), resp.getPayload().get(1).getId());
		Assert.assertEquals(new Long(2), resp.getPayload().get(2).getId());
		Assert.assertEquals(new Long(5), resp.getPayload().get(3).getId());
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	@ExpectedDatabase(value="queries-test/query-folders-test/share-folder-with-revoke-permission.xml", 
		assertionMode=DatabaseAssertionMode.NON_STRICT_UNORDERED)
	public void shareFolderTestWithRemovingUsers() {
		ShareQueryFolderOp opDetail = new ShareQueryFolderOp();
		opDetail.setFolderId(2L);
		opDetail.setUserIds(Arrays.asList(2L, 4L, 5L, 6L, 7L));
		opDetail.setOp(ShareQueryFolderOp.Operation.REMOVE);
		
		ResponseEvent<List<UserSummary>> resp = querySvc.shareFolder(getRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals(new Long(3), resp.getPayload().get(0).getId());
	}
	
	// Checking Accessibility For Fetching Folder Queries By Person (Neither ADMIN nor OWNER)
	@Test
	@DatabaseSetup("queries-test/query-folders-test/query-folder-check-accessibility-test.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getFolderQueriesByPerson() {
		ListFolderQueriesCriteria crit = new ListFolderQueriesCriteria().folderId(6L).startAt(0).countReq(true);
		ResponseEvent<SavedQueriesList> resp = querySvc.getFolderQueries(getUnathRequest(crit));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		SavedQueriesList list = resp.getPayload();
		Assert.assertEquals(new Long(3), list.getCount());
		Assert.assertEquals("Error: Query id mismatch", new Long(3), list.getQueries().get(0).getId());
		Assert.assertEquals("Error: Query id mismatch", new Long(2), list.getQueries().get(1).getId());
		Assert.assertEquals("Error: Query id mismatch", new Long(1), list.getQueries().get(2).getId());
	}
	
	// Checking Accessibility Updating Folder Queries By Person (Neither ADMIN nor OWNER)
	@Test
	@DatabaseSetup("queries-test/query-folders-test/query-folder-check-accessibility-test.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getUnauthorizedFolderTest() {
		ResponseEvent<QueryFolderDetails> resp = querySvc.getFolder(getUnathRequest(7L));

		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.OP_NOT_ALLOWED, ErrorType.USER_ERROR);
	}
	
	// Checking Accessibility Updating Folder Queries By Person (Neither ADMIN nor OWNER)
	@Test
	@DatabaseSetup("queries-test/query-folders-test/query-folder-check-accessibility-test.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void updateFolderQueriesByPerson() {
		UpdateFolderQueriesOp opDetail = new UpdateFolderQueriesOp();
		opDetail.setFolderId(6L);
		opDetail.setQueries(Arrays.asList(3L, 4L, 5L, 6L));
		opDetail.setOp(Operation.UPDATE);
		
		ResponseEvent<List<SavedQuerySummary>> resp = querySvc.updateFolderQueries(getUnathRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.OP_NOT_ALLOWED, ErrorType.USER_ERROR);
	}
	
	// Checking Accessibility For Deleting Folder By Person (Neither ADMIN nor OWNER)
	@Test
	@DatabaseSetup("queries-test/query-folders-test/query-folder-check-accessibility-test.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void deleteFolderTestByPerson() {
		ResponseEvent<Long> resp = querySvc.deleteFolder(getUnathRequest(6L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.OP_NOT_ALLOWED, ErrorType.USER_ERROR);
	}
	
	// Checking Accessibility Accessible Folders By Person (Neither ADMIN nor OWNER)
	@Test
	@DatabaseSetup("queries-test/query-folders-test/query-folder-check-accessibility-test.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getUserFoldersTestByPerson() {
		ResponseEvent<List<QueryFolderSummary>> resp = querySvc.getUserFolders(getUnathRequest(null));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Folder id mismatch", new Long(8), resp.getPayload().get(0).getId());
		Assert.assertEquals("Error: Folder id mismatch", new Long(6), resp.getPayload().get(1).getId());
	}
	
	// Checking Accessibility Accessible Folders By Person (Neither ADMIN nor OWNER)
	@Test
	@DatabaseSetup("queries-test/query-folders-test/query-folder-check-accessibility-test.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void shareFolderTestByPerson() {
		ShareQueryFolderOp opDetail = new ShareQueryFolderOp();
		opDetail.setFolderId(6L);
		opDetail.setUserIds(Arrays.asList(2L, 3L, 4L, 5L));
		opDetail.setOp(ShareQueryFolderOp.Operation.UPDATE);
		
		ResponseEvent<List<UserSummary>> resp = querySvc.shareFolder(getUnathRequest(opDetail));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.OP_NOT_ALLOWED, ErrorType.USER_ERROR);
	}
}