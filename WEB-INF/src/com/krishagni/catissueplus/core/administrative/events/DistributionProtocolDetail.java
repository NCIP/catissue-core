
package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionProtocolDetail {

	private Long id;

	private UserSummary principalInvestigator;

	private String title;

	private String shortTitle;

	private String irbId;

	private Date startDate;

	private Long anticipatedSpecimenCount;

	private String descriptionUrl;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserSummary getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(UserSummary principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public Long getAnticipatedSpecimenCount() {
		return anticipatedSpecimenCount;
	}

	public void setAnticipatedSpecimenCount(Long anticipatedSpecimenCount) {
		this.anticipatedSpecimenCount = anticipatedSpecimenCount;
	}

	public String getIrbId() {
		return irbId;
	}

	public void setIrbId(String irbId) {
		this.irbId = irbId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getDescriptionUrl() {
		return descriptionUrl;
	}

	public void setDescriptionUrl(String descriptionURL) {
		this.descriptionUrl = descriptionURL;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static DistributionProtocolDetail fromDomain(DistributionProtocol distributionProtocol) {

		DistributionProtocolDetail details = new DistributionProtocolDetail();
		details.setShortTitle(distributionProtocol.getShortTitle());
		details.setId(distributionProtocol.getId());
		details.setTitle(distributionProtocol.getTitle());
		details.setIrbId(distributionProtocol.getIrbId());
		details.setStartDate(distributionProtocol.getStartDate());
		details.setAnticipatedSpecimenCount(distributionProtocol.getAnticipatedSpecimenCount());
		details.setDescriptionUrl(distributionProtocol.getDescriptionUrl());
		details.setPrincipalInvestigator(getPrincipleInvestigatorInfo(distributionProtocol.getPrincipalInvestigator()));
		details.setActivityStatus(distributionProtocol.getActivityStatus());
		return details;
	}

	private static UserSummary getPrincipleInvestigatorInfo(User principleInvestigator) {
		UserSummary pi = new UserSummary();
		pi.setLoginName(principleInvestigator.getLoginName());
		if (principleInvestigator.getAuthDomain() != null) {
			pi = UserSummary.from(principleInvestigator);
		}

		return pi;
	}

}
