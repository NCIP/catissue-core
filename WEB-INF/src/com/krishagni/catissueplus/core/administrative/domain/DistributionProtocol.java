
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import krishagni.catissueplus.util.CommonUtil;

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;


public class DistributionProtocol {
	private static final String ENTITY_NAME = "distribution_protocol";

	private Long id;

	private User principalInvestigator;

	private String title;

	private String shortTitle;

	private String irbId;

	private Date startDate;
	
	private Date endDate;

	private String activityStatus;
	
	private Set<DistributionOrder> distributionOrders = new HashSet<DistributionOrder>();
	
	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Set<DistributionOrder> getDistributionOrders() {
		return distributionOrders;
	}

	public void setDistributionOrders(Set<DistributionOrder> distributionOrders) {
		this.distributionOrders = distributionOrders;
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
		this.setPrincipalInvestigator(distributionProtocol.getPrincipalInvestigator());
		this.setStartDate(distributionProtocol.getStartDate());
		this.setEndDate(distributionProtocol.getEndDate());
		this.setActivityStatus(distributionProtocol.getActivityStatus());
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail
				.singletonList(DistributionOrder.getEntityName(), getDistributionOrders().size());
	}
	
	public void delete() {
		List<DependentEntityDetail> dependentEntities = getDependentEntities();
		if (!dependentEntities.isEmpty()) {
			throw OpenSpecimenException.userError(DistributionProtocolErrorCode.REF_ENTITY_FOUND);
		}
		
		setShortTitle(CommonUtil.appendTimestamp(getShortTitle()));
		setTitle(CommonUtil.appendTimestamp(getTitle()));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
	
}
