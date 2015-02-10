package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;


public class DpTestData {
	//Create DPE
	
	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEvent() {
		CreateDistributionProtocolEvent req = new CreateDistributionProtocolEvent();
		req.setProtocol(getDistributionProtocolDetail());
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
	
	public static DistributionProtocolDetail getDistributionProtocolDetail() {
		DistributionProtocolDetail detail = new DistributionProtocolDetail();
		detail.setIrbId("ASDF-001");
		detail.setPrincipalInvestigator(getPrincipalInvestigator());
		detail.setShortTitle("dp-shortTitle");
		detail.setTitle("dp-title");
		detail.setStartDate(CprTestData.getDate(21, 1, 2012));
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
	
	// UpdateDPE
	
	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEvent() {
		UpdateDistributionProtocolEvent req = new UpdateDistributionProtocolEvent();
		req.setId(1L);
		req.setProtocol(getUpdateDistributionProtocolDetail());
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
	
	public static DistributionProtocolDetail getUpdateDistributionProtocolDetail() {
		DistributionProtocolDetail detail = new DistributionProtocolDetail();
		detail.setIrbId("NEW-ASDF-001");
		detail.setPrincipalInvestigator(getUpdatePrincipalInvestigator());
		detail.setShortTitle("updated-shortTitle");
		detail.setTitle("updated-title");
		detail.setStartDate(CprTestData.getDate(21, 1, 2014));
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
	
	//Delete DPE

	public static DeleteDistributionProtocolEvent getDeleteDistributionProtocolEvent() {
		DeleteDistributionProtocolEvent req = new DeleteDistributionProtocolEvent();
		req.setId(1L);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
}