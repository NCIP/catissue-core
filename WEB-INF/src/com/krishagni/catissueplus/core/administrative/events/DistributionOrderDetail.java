package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.DistributionStatus;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionOrderDetail {
	private Long id;
	
	private String name;
	
	private DistributionProtocolDetail distributionProtocol;
	
	private SiteDetail site;
	
	private UserSummary requester;
	
	private Date creationDate;
	
	private UserSummary distributor;
	
	private Date executionDate;
	
	private DistributionStatus status;
	
	private List<DistributionOrderItemDetail> orderItems = new ArrayList<DistributionOrderItemDetail>();
	
	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DistributionProtocolDetail getDistributionProtocol() {
		return distributionProtocol;
	}

	public void setDistributionProtocol(
			DistributionProtocolDetail distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}

	public SiteDetail getSite() {
		return site;
	}

	public void setSite(SiteDetail distributionSite) {
		this.site = distributionSite;
	}

	public UserSummary getRequester() {
		return requester;
	}

	public void setRequester(UserSummary requester) {
		this.requester = requester;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public UserSummary getDistributor() {
		return distributor;
	}

	public void setDistributor(UserSummary distributor) {
		this.distributor = distributor;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public DistributionStatus getStatus() {
		return status;
	}

	public void setStatus(DistributionStatus status) {
		this.status = status;
	}

	public List<DistributionOrderItemDetail> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<DistributionOrderItemDetail> orderItems) {
		this.orderItems = orderItems;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static DistributionOrderDetail from(DistributionOrder order) {
		DistributionOrderDetail detail = new DistributionOrderDetail();
		detail.setStatus(order.getStatus());
		detail.setId(order.getId());
		detail.setName(order.getName());
		detail.setCreationDate(order.getCreationDate());
		detail.setExecutionDate(order.getExecutionDate());
		detail.setOrderItems(DistributionOrderItemDetail.from(order.getOrderItems()));
		detail.setDistributionProtocol(DistributionProtocolDetail.from(order.getDistributionProtocol()));
		detail.setRequester(UserSummary.from(order.getRequester()));
		detail.setSite(SiteDetail.fromDomain(order.getSite()));
		detail.setActivityStatus(order.getActivityStatus());
		if (order.getDistributor() != null ) {
			detail.setDistributor(UserSummary.from(order.getDistributor()));
		}
		
		return detail;
	}
	
	public static List<DistributionOrderDetail> from(List<DistributionOrder> orders) {
		List <DistributionOrderDetail> list = new ArrayList<DistributionOrderDetail>();
		
		for (DistributionOrder order : orders) {
			list.add(from(order));
		}
		
		return list;
	}
}