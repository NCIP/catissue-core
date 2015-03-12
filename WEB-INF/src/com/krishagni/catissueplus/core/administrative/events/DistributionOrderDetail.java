package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.DistributionAction;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionOrderDetail {
	private Long id;
	
	private String name;
	
	private Long distributionProtocolId;
	
	private UserSummary requester;
	
	private UserSummary distributor;
	
	private Date requestedDate;
	
	private DistributionAction distributionAction;
	
	private String activityStatus;

	private List<OrderItemDetail> orderItems = new ArrayList<OrderItemDetail>();

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

	public Long getDistributionProtocolId() {
		return distributionProtocolId;
	}

	public void setDistributionProtocolId(Long distributionProtocolId) {
		this.distributionProtocolId = distributionProtocolId;
	}

	public UserSummary getRequester() {
		return requester;
	}

	public void setRequester(UserSummary requester) {
		this.requester = requester;
	}

	public UserSummary getDistributor() {
		return distributor;
	}

	public void setDistributor(UserSummary distributor) {
		this.distributor = distributor;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public DistributionAction getDistributionAction() {
		return distributionAction;
	}

	public void setDistributionAction(DistributionAction distributionAction) {
		this.distributionAction = distributionAction;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public List<OrderItemDetail> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemDetail> orderItems) {
		this.orderItems = orderItems;
	}

	public static DistributionOrderDetail from(DistributionOrder distributionOrder) {
		DistributionOrderDetail detail = new DistributionOrderDetail();
		detail.setActivityStatus(distributionOrder.getActivityStatus());
		detail.setDistributionAction(distributionOrder.getDistributionAction());
		detail.setDistributionProtocolId(distributionOrder.getDistributionProtocol().getId());
		detail.setId(distributionOrder.getId());
		detail.setName(distributionOrder.getName());
		detail.setRequestedDate(distributionOrder.getRequestedDate());
		detail.setOrderItems(OrderItemDetail.from(distributionOrder.getOrderItems()));
		
		if (distributionOrder.getDistributor() != null ) {
			detail.setDistributor(UserSummary.from(distributionOrder.getDistributor()));
		}
		
		if (distributionOrder.getRequester() != null ) {
			detail.setRequester(UserSummary.from(distributionOrder.getRequester()));
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