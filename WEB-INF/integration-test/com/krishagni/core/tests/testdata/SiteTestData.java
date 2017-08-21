package com.krishagni.core.tests.testdata;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.SiteDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SiteTestData {

	public static SiteDetail getSiteDetail() {
		SiteDetail siteDetail = new SiteDetail();
		siteDetail.setName("default-site");
		siteDetail.setType("repository");
		siteDetail.setActivityStatus("Active");
		siteDetail.setCode("1");
		siteDetail.setCoordinators(getCoordinatorCollection());
		return siteDetail;
	}

	private static List<UserSummary> getCoordinatorCollection() {
		List<UserSummary> coordinatorCollection = new ArrayList<UserSummary>();
		UserSummary summary1 = new UserSummary();
		summary1.setDomain("default");
		summary1.setId(1L);
		summary1.setLoginName("admin@admin.com");
		summary1.setFirstName("ADMIN");
		summary1.setFirstName("ADMIN");
	
		coordinatorCollection.add(summary1);

		UserSummary summary2 = new UserSummary();
		summary2.setDomain("default");
		summary2.setId(2L);
		summary2.setLoginName("admin@admin.com");
		summary2.setFirstName("ADMIN");
		summary2.setFirstName("ADMIN");
		
		coordinatorCollection.add(summary2);
		return coordinatorCollection;
	}

	public static SiteDetail getSiteDetailWithDuplicateSiteCode() {
		SiteDetail siteDetail = new SiteDetail();
		siteDetail.setName("default1-site");
		siteDetail.setType("repository");
		siteDetail.setActivityStatus("Active");
		siteDetail.setCode("1");
		siteDetail.setCoordinators(getCoordinatorCollection());
		return siteDetail;
	}

	public static SiteDetail getSiteDetailForUpdate() {
		SiteDetail siteDetail = new SiteDetail();
		siteDetail.setId(1L);
		siteDetail.setName("updated-site");
		siteDetail.setType("repository");
		siteDetail.setActivityStatus("Active");
		siteDetail.setCode("1");
		siteDetail.setCoordinators(getCoordinatorCollection());
		return siteDetail;
	}
}
