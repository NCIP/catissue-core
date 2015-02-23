package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class DistributionOrder {
	private Long id;
	
	private String name;
	
	private DistributionProtocol distributionProtocol;
	
	private Site distributionSite;
	
	private User requester;
	
	private Date creationDate;
	
	private User distributor;
	
	private Date executionDate;

	private String orderType = "Distribution";
	
	private Set<DistributionOrderItem> orderItems = new HashSet<DistributionOrderItem>();
	
	private String status;
	
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

	public DistributionProtocol getDistributionProtocol() {
		return distributionProtocol;
	}

	public void setDistributionProtocol(DistributionProtocol distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}

	public Site getDistributionSite() {
		return distributionSite;
	}

	public void setDistributionSite(Site distributionSite) {
		this.distributionSite = distributionSite;
	}

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getDistributor() {
		return distributor;
	}

	public void setDistributor(User distributor) {
		this.distributor = distributor;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Set<DistributionOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<DistributionOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void update(DistributionOrder other) {
		setName(other.name);
		setRequester(other.requester);
		setDistributor(other.distributor);
		updateStatus(other);
		setDistributionProtocol(other.distributionProtocol);
		setCreationDate(other.creationDate);
		setDistributionSite(other.distributionSite);
		setExecutionDate(other.executionDate);
		updateOrderItems(other.orderItems);
	}
	
	public void updateStatus(DistributionOrder other) {
		if (isOrderDistributed() && other.isPending()) {
			/*
			 * Once an order is distributed it can't be set to pending state again
			 * as the specimens would have been already distributed.
			 */
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.INVALID_STATUS);
		}
		
		setStatus(other.status);
	}
	
	private void updateOrderItems(Set<DistributionOrderItem> newItems) {
		orderItems.clear();
		
		for (DistributionOrderItem item : newItems) {
			item.setOrder(this);
		}
		orderItems.addAll(newItems);
	}
	
	public boolean isPending() {
		return PENDING.equals(status);
	}
	
	public boolean isOrderDistributed() {
		if (DISTRIBUTED.equals(status) || DISTRIBUTED_AND_CLOSED.equals(status)) {
			return true;
		}
		
		return false;
	}
	
	public boolean closeAfterDistribution() {
		return DISTRIBUTED_AND_CLOSED.equals(status);
	}
	
	public static boolean isDistributionStatusValid(String status) {
		if (PENDING.equals(status) || 
				DISTRIBUTED.equals(status) || 
				DISTRIBUTED_AND_CLOSED.equals(status)) {
			return true;
		}
		
		return false;
	}
	
	public static String PENDING = "Pending";

	public static String DISTRIBUTED = "Distributed";
	
	public static String DISTRIBUTED_AND_CLOSED = "Distributed And Closed";
}
