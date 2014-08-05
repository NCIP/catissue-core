
package com.krishagni.catissueplus.core.services.testdata;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.GetDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UserInfo;
import com.krishagni.catissueplus.core.auth.domain.factory.AuthenticationType;

import edu.wustl.common.beans.SessionDataBean;

public class DistributionProtocolTestData {

	private static final String PATCH_DISTRIBUTION_PROTOCOL = "patch distribution protocol";

	public static DistributionProtocol getDistributionProtocolToReturn() {
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setId(1L);
		protocol.setIrbId("IRB4444");
		protocol.setTitle("DPProtocol");
		protocol.setShortTitle("DPP");
		protocol.setStartDate(new Date());
		protocol.setDescriptionUrl("www.distributionprotocol.com");
		protocol.setAnticipatedSpecimenCount(50L);
		protocol.setPrincipalInvestigator(getUserDetails());
		protocol.setActivityStatus("Active");
		return protocol;
	}

	public static DistributionProtocol getDistributionProtocolWithNullAuthDomain() {
		DistributionProtocol protocol = new DistributionProtocol();
		protocol.setId(1L);
		protocol.setIrbId("IRB4444");
		protocol.setTitle("DPProtocol");
		protocol.setShortTitle("DPP");
		protocol.setStartDate(new Date());
		protocol.setDescriptionUrl("www.distributionprotocol.com");
		protocol.setAnticipatedSpecimenCount(50L);
		protocol.setPrincipalInvestigator(getUserDetailsWithNullAuthDomain());
		protocol.setActivityStatus("Active");
		return protocol;
	}

	public static User getUserDetailsWithNullAuthDomain() {
		User user = getUserDetails();
		user.setAuthDomain(null);
		return user;
	}

	public static User getUserDetails() {
		User user = new User();
		user.setId(1L);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setLoginName("admin@admin.com");
		user.setEmailAddress("sci@sci.com");
		user.setDepartment(new Department());
		user.setAddress(new Address());
		user.setActivityStatus("Active");
		return user;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEvent() {
		CreateDistributionProtocolEvent event = new CreateDistributionProtocolEvent();
		event.setSessionDataBean(getSessionDataBean());
		DistributionProtocolDetails details = getDistributionProtocolDetails();
		event.setDistributionProtocolDetails(details);
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithTitle() {
		UpdateDistributionProtocolEvent event = new UpdateDistributionProtocolEvent();
		event.setSessionDataBean(getSessionDataBean());
		DistributionProtocolDetails details = getDistributionProtocolDetails();
		event.setDetails(details);
		event.setTitle("asds");
		return event;
	}

	private static DistributionProtocolDetails getDistributionProtocolDetails() {
		DistributionProtocolDetails details = new DistributionProtocolDetails();
		details.setIrbId("IRB333");
		details.setPrincipalInvestigator(getPrincipalInvestigator());
		details.setDescriptionUrl("www.simpleurl.com");
		details.setAnticipatedSpecimenCount(56L);
		details.setShortTitle("CPP");
		details.setTitle("CPProtocol");
		details.setStartDate(new Date(10 - 06 - 2014));
		details.setActivityStatus("Active");
		return details;
	}

	private static DistributionProtocolPatchDetails getDistributionProtocolPatchDetails() {
		DistributionProtocolPatchDetails details = new DistributionProtocolPatchDetails();
		details.setIrbId("IRB333");
		details.setPrincipalInvestigator(getPrincipalInvestigator());
		details.setDescriptionUrl("www.simpleurl.com");
		details.setAnticipatedSpecimenCount(56L);
		details.setShortTitle("CPP");
		details.setTitle("CPProtocol");
		details.setStartDate(new Date(10 - 06 - 2014));
		details.setActivityStatus("Active");
		return details;
	}

	private static UserInfo getPrincipalInvestigator() {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginName("admin@admin.com");
		userInfo.setDomainName(AuthenticationType.CATISSUE.value());
		return userInfo;
	}

	public static SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventWithEmptyInvestigatorName() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		event.getDistributionProtocolDetails().setPrincipalInvestigator(getEmptyPrincipalInvestigator());
		return event;
	}

	private static UserInfo getEmptyPrincipalInvestigator() {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginName("");
		userInfo.setDomainName("");
		return userInfo;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventEmptyTitle() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		event.getDistributionProtocolDetails().setTitle("");
		return event;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventEmptyShortTitle() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		event.getDistributionProtocolDetails().setShortTitle("");
		return event;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventWithNullStartDate() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		event.getDistributionProtocolDetails().setStartDate(null);
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEvent() {
		UpdateDistributionProtocolEvent event = new UpdateDistributionProtocolEvent();
		DistributionProtocolDetails details = getDistributionProtocolDetails();
		event.setDetails(details);
		event.setId(1L);
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithInvalidInvestigator() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setPrincipalInvestigator(getInvalidPrincipalInvestigator());
		return event;
	}

	private static UserInfo getInvalidPrincipalInvestigator() {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginName("adam@adam.com");
		return userInfo;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithEmptyInvestigatorName() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setPrincipalInvestigator(getEmptyPrincipalInvestigator());
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventEmptyTitle() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setTitle("");
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventEmptyShortTitle() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setShortTitle("");
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchData() {
		PatchDistributionProtocolEvent event = new PatchDistributionProtocolEvent();
		event.setId(1L);
		DistributionProtocolPatchDetails details = new DistributionProtocolPatchDetails();
		try {
			BeanUtils.populate(details, getDistributionProtocolPatchAttributes());
		}
		catch (Exception e) {
			reportError(SiteErrorCode.BAD_REQUEST, PATCH_DISTRIBUTION_PROTOCOL);
		}
		details.setModifiedAttributes(new ArrayList<String>(getDistributionProtocolPatchAttributes().keySet()));
		event.setDetails(details);
		return event;
	}

	private static Map<String, Object> getDistributionProtocolPatchAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("title", "CPProtocol");
		attributes.put("shortTitle", "CPP");
		attributes.put("irbId", "IRB555");
		attributes.put("principalInvestigator", getPrincipalInvestigator());
		attributes.put("startDate", new Date());
		attributes.put("anticipatedSpecimenCount", 50L);
		attributes.put("descriptionUrl", "www.modifiedProtocol.com");
		attributes.put("activityStatus", "Active");
		return attributes;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEvent() {
		PatchDistributionProtocolEvent event = getPatchData();
		return event;

	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithEmptyInvestigatorName() {
		PatchDistributionProtocolEvent event = getPatchData();
		event.getDetails().setPrincipalInvestigator(getEmptyPrincipalInvestigator());
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithEmptyTitle() {
		PatchDistributionProtocolEvent event = getPatchData();
		event.getDetails().setTitle("");
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithEmptyShortTitle() {
		PatchDistributionProtocolEvent event = getPatchData();
		event.getDetails().setShortTitle("");
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventNullDate() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setTitle("DPProtocol");
		event.getDetails().setShortTitle("DPP");
		event.getDetails().setStartDate(null);
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventNullDate() {
		PatchDistributionProtocolEvent event = getPatchDistributionProtocolEvent();
		event.getDetails().setStartDate(null);
		return event;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventEmptyIrbId() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		event.getDistributionProtocolDetails().setIrbId("");
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithEmptyIrbId() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setIrbId("");
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithEmptyIrbId() {
		PatchDistributionProtocolEvent event = getPatchDistributionProtocolEvent();
		event.getDetails().setIrbId("");
		return event;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventWithInvalidActivityStatus() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		event.getDistributionProtocolDetails().setActivityStatus("unActive");
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithInvalidActivityStatus() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setActivityStatus("unActive");
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithDiffTitles() {
		PatchDistributionProtocolEvent event = getPatchDistributionProtocolEvent();
		event.getDetails().setShortTitle("XPP");
		event.getDetails().setTitle("XPProtocol");
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithInvalidActivityStatus() {
		PatchDistributionProtocolEvent event = getPatchDistributionProtocolEvent();
		event.getDetails().setActivityStatus("unActive");
		return event;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventWithNegativeAnticipatedSpecimenCountNumber() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		event.getDistributionProtocolDetails().setAnticipatedSpecimenCount(-2L);
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithNegativeAnticipatedSpecimenCountNumber() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setAnticipatedSpecimenCount(-2L);
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithNegativeAnticipatedSpecimenCountNumber() {
		PatchDistributionProtocolEvent event = getPatchDistributionProtocolEvent();
		event.getDetails().setAnticipatedSpecimenCount(-2L);
		return event;
	}

	public static PatchDistributionProtocolEvent getEmptyPatchData() {
		PatchDistributionProtocolEvent event = new PatchDistributionProtocolEvent();
		DistributionProtocolPatchDetails details = new DistributionProtocolPatchDetails();
		event.setDetails(details);
		return event;
	}

	public static CreateDistributionProtocolEvent getCreateDistributionProtocolEventWithNullAuthDomain() {
		CreateDistributionProtocolEvent event = getCreateDistributionProtocolEvent();
		User principalInvestigator = new User();
		principalInvestigator.setAuthDomain(null);

		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithTitle() {
		PatchDistributionProtocolEvent event = getPatchData();
		event.setId(null);
		DistributionProtocolPatchDetails details = getDistributionProtocolPatchDetails();
		event.setTitle("CPProtocol");
		event.setDetails(details);
		return event;
	}

	public static DeleteDistributionProtocolEvent getDeleteDistributionProtocolEvent() {
		DeleteDistributionProtocolEvent event = new DeleteDistributionProtocolEvent();
		event.setId(1L);

		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithDisableActivityStatus() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setActivityStatus("Disabled");
		return event;
	}

	public static UpdateDistributionProtocolEvent getUpdateDistributionProtocolEventWithoutDisableActivityStatus() {
		UpdateDistributionProtocolEvent event = getUpdateDistributionProtocolEvent();
		event.getDetails().setActivityStatus("Active");
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithDisableActivityStatus() {
		PatchDistributionProtocolEvent event = getPatchDistributionProtocolEvent();
		event.getDetails().setActivityStatus("Disabled");
		return event;
	}

	public static PatchDistributionProtocolEvent getPatchDistributionProtocolEventWithoutDisableActivityStatus() {
		PatchDistributionProtocolEvent event = getPatchDistributionProtocolEvent();
		event.getDetails().setActivityStatus("Active");
		return event;
	}

	public static List<DistributionProtocol> getDPs() {
		List<DistributionProtocol> distributionProtocols = new ArrayList<DistributionProtocol>();
		distributionProtocols.add(getDistributionProtocolToReturn());
		distributionProtocols.add(getDistributionProtocolToReturn());
		return distributionProtocols;
	}

	public static ReqAllDistributionProtocolEvent getAllDistributionProtocolEvent() {
		ReqAllDistributionProtocolEvent event = new ReqAllDistributionProtocolEvent();
		event.setMaxResults(1000);
		return event;
	}

	public static GetDistributionProtocolEvent getDistributionProtocolEvent() {
		GetDistributionProtocolEvent event = new GetDistributionProtocolEvent();
		event.setId(1L);
		return event;
	}

	public static GetDistributionProtocolEvent getDistributionProtocolEventForName() {
		GetDistributionProtocolEvent event = new GetDistributionProtocolEvent();
		event.setTitle("CP Title");
		return event;
	}

	public static DeleteDistributionProtocolEvent getDeleteDistributionProtocolEventForTitle() {
		DeleteDistributionProtocolEvent event = new DeleteDistributionProtocolEvent();
		event.setTitle("title");
		return event;
	}
}
