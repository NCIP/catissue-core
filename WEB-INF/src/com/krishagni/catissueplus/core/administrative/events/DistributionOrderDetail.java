package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionOrderDetail {
	private Long id;
	
	private String name;
	
	private DistributionProtocolDetail distributionProtocol;
	
	private UserSummary requester;
	
	private Date creationDate;
	
	private UserSummary distributor;
	
	private Date executionDate;
	
	private String status;
	
	private List<DistributionOrderItemDetail> orderItems = new ArrayList<DistributionOrderItemDetail>();

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DistributionOrderItemDetail> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<DistributionOrderItemDetail> orderItems) {
		this.orderItems = orderItems;
	}

	public static DistributionOrderDetail from(DistributionOrder distributionOrder) {
		DistributionOrderDetail detail = new DistributionOrderDetail();
		detail.setStatus(distributionOrder.getStatus());
		detail.setId(distributionOrder.getId());
		detail.setName(distributionOrder.getName());
		detail.setCreationDate(distributionOrder.getCreationDate());
		detail.setExecutionDate(distributionOrder.getExecutionDate());
		detail.setOrderItems(DistributionOrderItemDetail.from(distributionOrder.getOrderItems()));
		detail.setDistributionProtocol(DistributionProtocolDetail.from(distributionOrder.getDistributionProtocol()));
		detail.setRequester(UserSummary.from(distributionOrder.getRequester()));
		if (distributionOrder.getDistributor() != null ) {
			detail.setDistributor(UserSummary.from(distributionOrder.getDistributor()));
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