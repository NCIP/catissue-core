package com.krishagni.core.tests.testdata;

import java.util.List;
import java.util.ArrayList;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ListFolderQueriesCriteria;
import com.krishagni.catissueplus.core.de.events.QueryFolderDetails;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;

public class QueryFoldersTestData {
	public static QueryFolderDetails getQueryFolderDetails() {
		QueryFolderDetails folderDetails = new QueryFolderDetails();
		folderDetails.setName("query-folder");
		folderDetails.setQueries(getSavedQueries(1,3));
		folderDetails.setSharedWithAll(true);
		return folderDetails;
	}
	
	public static QueryFolderDetails getUpdateQueryFolderDetails() {
		QueryFolderDetails folderDetails = new QueryFolderDetails();
		folderDetails.setId(2L);
		folderDetails.setName("updated-query-folder");
		folderDetails.setQueries(getSavedQueries(2,5));
		folderDetails.setSharedWithAll(false);
		folderDetails.setSharedWith(getUsers(3,5));
		return folderDetails;
	}
	
	public static ListFolderQueriesCriteria getCriteria() {
		ListFolderQueriesCriteria crit = new ListFolderQueriesCriteria()
										.folderId(2L)
										.startAt(0)
										.maxResults(3)
										.countReq(true);
		return crit;
	}
	
	public static List<UserSummary> getUsers(int m, int n) {
		List<UserSummary> users = new ArrayList<UserSummary>();
		for (int i=m; i<=n; i++) {
			String firstName = "ADMIN" + i;
			String lastName = "ADMIN" + i;
			String loginName = "admin" + i + "@login.com";
			users.add(CommonUtils.getUser(new Long(i),firstName, lastName, loginName));
		}
		return users;
	}
	
	public static List<SavedQuerySummary> getSavedQueries(int m, int n) {
		List<SavedQuerySummary> queries = new ArrayList<SavedQuerySummary>();
		for(int i=m; i<=n; i++) {
			UserSummary user = CommonUtils.getUser(new Long(2),"ADMIN2", "ADMIN2", "admin2@login.com"); 
			SavedQuerySummary sq = new SavedQuerySummary();
			sq.setId(new Long(i));
			sq.setTitle("query-" + i);
			sq.setCreatedBy(user);
			sq.setLastModifiedBy(user);
			queries.add(sq);
		}
		return queries;
	}
}