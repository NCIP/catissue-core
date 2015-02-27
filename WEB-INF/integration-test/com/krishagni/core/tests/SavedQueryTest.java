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
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.SavedQueriesList;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetail;
import com.krishagni.catissueplus.core.de.events.ListSavedQueriesCriteria;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;
import com.krishagni.core.common.ApplicationContextConfigurer;
import com.krishagni.core.common.TestUtils;
import com.krishagni.core.common.WebContextLoader;
import com.krishagni.core.tests.testdata.CommonUtils;
import com.krishagni.core.tests.testdata.SavedQueryTestData;

import edu.wustl.common.beans.SessionDataBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ApplicationContextConfigurer.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@WebAppConfiguration
public class SavedQueryTest {
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
	 * Get Saved Query API's Test
	 */
	
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
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getSavedQueryTest() {
		ResponseEvent<SavedQueryDetail> resp = querySvc.getSavedQuery(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		SavedQueryDetail queryDetail = resp.getPayload();
		Assert.assertEquals("Error: Query title mismatch", "query-1", queryDetail.getTitle());
		Assert.assertEquals("Error: Query-Cpid mismatch", new Long(1), queryDetail.getCpId());
		Assert.assertEquals("Error: Created by user mismatch", new Long(3), queryDetail.getCreatedBy().getId());
		assertUserSummary(queryDetail.getCreatedBy(), queryDetail.getCreatedBy().getId().intValue());
		Assert.assertEquals("Error: Last modified by user mismatch", new Long(2), queryDetail.getLastModifiedBy().getId());
		assertUserSummary(queryDetail.getLastModifiedBy(), queryDetail.getLastModifiedBy().getId().intValue());
	}
	
	@Test
	public void getNonExistingSavedQuery() {
		ResponseEvent<SavedQueryDetail> resp = querySvc.getSavedQuery(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	/*
	 * Get Saved Queries API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getSavedQueriesTest() {
		ListSavedQueriesCriteria crit = SavedQueryTestData.getCriteria();
		ResponseEvent<SavedQueriesList> resp = querySvc.getSavedQueries(getRequest(crit));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Saved query list count mismatch", new Long(6), resp.getPayload().getCount());
		List<SavedQuerySummary> queryList = resp.getPayload().getQueries();
		int i = 6;
		for(SavedQuerySummary query : queryList) {
			Assert.assertEquals("Query id mismatch", new Long(i), query.getId());
			assertQueryDetails(query, i);
			i--;
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getSavedQueriesTestWithoutCountAndWithInvalidCriteria() {
		ListSavedQueriesCriteria crit = new ListSavedQueriesCriteria().startAt(-1).maxResults(-1);
		ResponseEvent<SavedQueriesList> resp = querySvc.getSavedQueries(getRequest(crit));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertNull("Saved query list count was not found null", resp.getPayload().getCount());
		List<SavedQuerySummary> queryList = resp.getPayload().getQueries();
		int i = 6;
		for(SavedQuerySummary query : queryList) {
			Assert.assertEquals("Query id mismatch", new Long(i), query.getId());
			assertQueryDetails(query, i);
			i--;
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/saved-queries-test/saved-query-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getSavedQueriesTestWithQueryFilterById() {
		ListSavedQueriesCriteria crit = new ListSavedQueriesCriteria().query("3");
		ResponseEvent<SavedQueriesList> resp = querySvc.getSavedQueries(getRequest(crit));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertNull("Saved query list count was not found null", resp.getPayload().getCount());
		List<SavedQuerySummary> queryList = resp.getPayload().getQueries();
		for(SavedQuerySummary query : queryList) {
			Assert.assertEquals("Query id mismatch", new Long(3), query.getId());
		}
	}
	
	@Test
	@DatabaseSetup("queries-test/saved-queries-test/saved-query-list.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void getSavedQueriesTestWithQueryFilterByTitle() {
		ListSavedQueriesCriteria crit = new ListSavedQueriesCriteria().query("tissue");
		ResponseEvent<SavedQueriesList> resp = querySvc.getSavedQueries(getRequest(crit));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertNull("Saved query list count was not found null", resp.getPayload().getCount());
		List<SavedQuerySummary> queryList = resp.getPayload().getQueries();
		for(SavedQuerySummary query : queryList) {
			Assert.assertEquals("Error: Query title mismatch", true, query.getTitle().toLowerCase().contains("tissue"));
		}
	}
	
	/*
	 * Delete Saved Query API's Test
	 */
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void deleteSavedQuery() {
		ResponseEvent<Long> resp = querySvc.deleteQuery(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was not found successful", true, resp.isSuccessful());
		Assert.assertNotNull("Error: Response payload data was found null", resp.getPayload());
		Assert.assertEquals("Error: Delete query id mismatch", new Long(1), resp.getPayload());
	}
	
	@Test
	public void deleteInvalidQuery() {
		ResponseEvent<Long> resp = querySvc.deleteQuery(getRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.NOT_FOUND, ErrorType.USER_ERROR);
	}
	
	@Test
	@DatabaseSetup("queries-test/query-folders-test/update-query-folder-initial.xml")
	@DatabaseTearDown("queries-test/query-folders-test/generic-teardown.xml")
	public void deleteUnauthorizedSavedQuery() {
		ResponseEvent<Long> resp = querySvc.deleteQuery(getUnathRequest(1L));
		
		TestUtils.recordResponse(resp);
		Assert.assertEquals("Error: Response was found successful", false, resp.isSuccessful());
		Assert.assertNull("Error: Response payload data was not found null", resp.getPayload());
		TestUtils.checkErrorCode(resp, SavedQueryErrorCode.OP_NOT_ALLOWED, ErrorType.USER_ERROR);
	}
}