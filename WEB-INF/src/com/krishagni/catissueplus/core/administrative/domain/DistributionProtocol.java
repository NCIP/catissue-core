
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;

import krishagni.catissueplus.util.CommonUtil;

import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionProtocol {

	private Long id;

	private User principalInvestigator;

	private String title;

	private String shortTitle;

	private String irbId;

	private Date startDate;

	private String descriptionUrl;

	private Long anticipatedSpecimenCount;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(User principalInvestigator) {
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

	public Long getAnticipatedSpecimenCount() {
		return anticipatedSpecimenCount;
	}

	public void setAnticipatedSpecimenCount(Long anticipatedSpecimenCount) {
		this.anticipatedSpecimenCount = anticipatedSpecimenCount;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public void update(DistributionProtocol distributionProtocol) {
		if (distributionProtocol.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			this.setShortTitle(CommonUtil.appendTimestamp(distributionProtocol.getShortTitle()));
			this.setTitle(CommonUtil.appendTimestamp(distributionProtocol.getTitle()));
		}
		else {
			this.setShortTitle(distributionProtocol.getShortTitle());
			this.setTitle(distributionProtocol.getTitle());
		}
		this.setIrbId(distributionProtocol.getIrbId());
		this.setAnticipatedSpecimenCount(distributionProtocol.getAnticipatedSpecimenCount());
		this.setPrincipalInvestigator(distributionProtocol.getPrincipalInvestigator());
		this.setDescriptionUrl(distributionProtocol.getDescriptionUrl());
		this.setStartDate(distributionProtocol.getStartDate());
		this.setActivityStatus(distributionProtocol.getActivityStatus());
	}

	public void delete() {
		//need to check whether its referenced by any order
		//
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
}
