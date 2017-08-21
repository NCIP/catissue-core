package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;


public class DpTestData {
	public static DistributionProtocolDetail getDistributionProtocolDetail() {
		DistributionProtocolDetail detail = new DistributionProtocolDetail();
		detail.setIrbId("ASDF-001");
		detail.setPrincipalInvestigator(getPrincipalInvestigator());
		detail.setShortTitle("dp-shortTitle");
		detail.setTitle("dp-title");
		detail.setStartDate(CommonUtils.getDate(21, 1, 2012));
		return detail;
	}
	
	public static UserSummary getPrincipalInvestigator() {
		UserSummary user = new UserSummary();
		user.setId(1L);
		user.setFirstName("ADMIN");
		user.setLastName("ADMIN");
		user.setLoginName("admin@admin.com");
		return user;
	}
	
	public static DistributionProtocolDetail getUpdateDistributionProtocolDetail() {
		DistributionProtocolDetail detail = new DistributionProtocolDetail();
		detail.setId(1L);
		detail.setIrbId("NEW-ASDF-001");
		detail.setPrincipalInvestigator(getUpdatePrincipalInvestigator());
		detail.setShortTitle("updated-shortTitle");
		detail.setTitle("updated-title");
		detail.setStartDate(CommonUtils.getDate(21, 1, 2014));
		return detail;
	}
	
	public static UserSummary getUpdatePrincipalInvestigator() {
		UserSummary user = new UserSummary();
		user.setId(2L);
		user.setFirstName("first-name");
		user.setLastName("last-name");
		user.setLoginName("user@user.com");
		return user;
	}
}