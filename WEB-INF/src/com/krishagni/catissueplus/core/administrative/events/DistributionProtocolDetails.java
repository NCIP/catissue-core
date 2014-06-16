
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;

public class DistributionProtocolDetails {

	private Long id;

	private String principalInvestigator;

	private String title;

	private String shortTitle;

	private String irbId;

	private Date startDate;

	private Long anticipatedSpecimenCount;

	private String descriptionUrl;

	private String activityStatus;

	private List<String> modifiedAttributes = new ArrayList<String>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(String principalInvestigator) {
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

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public static DistributionProtocolDetails fromDomain(DistributionProtocol distributionProtocol) {

		DistributionProtocolDetails details = new DistributionProtocolDetails();
		details.setShortTitle(distributionProtocol.getShortTitle());
		details.setId(distributionProtocol.getId());
		details.setTitle(distributionProtocol.getTitle());
		details.setIrbId(distributionProtocol.getIrbId());
		details.setStartDate(distributionProtocol.getStartDate());
		details.setAnticipatedSpecimenCount(distributionProtocol.getAnticipatedSpecimenCount());
		details.setDescriptionUrl(distributionProtocol.getDescriptionUrl());
		details.setPrincipalInvestigator(distributionProtocol.getPrincipalInvestigator().getLoginName());
		details.setActivityStatus(distributionProtocol.getActivityStatus());
		return details;
	}

	public boolean isDistributionProtocolPrincipalInvestigatorModified() {
		return modifiedAttributes.contains("principalInvestigator");
	}

	public boolean isDistributionProtocolTitleModified() {
		return modifiedAttributes.contains("title");
	}

	public boolean isDistributionProtocolShortTitleModified() {
		return modifiedAttributes.contains("shortTitle");
	}

	public boolean isDistributionProtocolIrbIdModified() {
		return modifiedAttributes.contains("irbId");
	}

	public boolean isDistributionProtocolStartDateModified() {
		return modifiedAttributes.contains("startDate");
	}

	public boolean isDistributionProtocolAnticipatedSpecimenCountModified() {
		return modifiedAttributes.contains("anticipatedSpecimenCount");
	}

	public boolean isDistributionProtocolDescriptionUrlModified() {
		return modifiedAttributes.contains("descriptionUrl");
	}

	public boolean isDistributionProtocolActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}

}
