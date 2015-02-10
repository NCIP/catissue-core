
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static DistributionProtocolDetail from(DistributionProtocol distributionProtocol) {

		DistributionProtocolDetail details = new DistributionProtocolDetail();
		details.setShortTitle(distributionProtocol.getShortTitle());
		details.setId(distributionProtocol.getId());
		details.setTitle(distributionProtocol.getTitle());
		details.setIrbId(distributionProtocol.getIrbId());
		details.setStartDate(distributionProtocol.getStartDate());
		details.setPrincipalInvestigator(getPrincipleInvestigatorInfo(distributionProtocol.getPrincipalInvestigator()));
		details.setActivityStatus(distributionProtocol.getActivityStatus());
		return details;
	}

	private static UserSummary getPrincipleInvestigatorInfo(User principleInvestigator) {
		return UserSummary.from(principleInvestigator);
	}
	
	public static List<DistributionProtocolDetail> from(List<DistributionProtocol> distributionProtocols) {
		List<DistributionProtocolDetail> list = new ArrayList<DistributionProtocolDetail>();
		
		for (DistributionProtocol dp : distributionProtocols) {
			list.add(from(dp));
		}
		
		return list;
	}

}
