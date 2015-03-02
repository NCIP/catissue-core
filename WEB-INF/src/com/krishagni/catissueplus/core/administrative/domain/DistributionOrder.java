package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class DistributionOrder extends BaseEntity {
	public enum Status { 
		PENDING,
		DISTRIBUTED,
		DISTRIBUTED_AND_CLOSED
	}
	
	private String name;
	
	private DistributionProtocol distributionProtocol;
	
	private Site site;
	
	private User requester;
	
	private Date creationDate;
	
	private User distributor;
	
	private Date executionDate;

	private String orderType = "Distribution";
	
	private Set<DistributionOrderItem> orderItems = new HashSet<DistributionOrderItem>();
	
	private Status status;
	
	private String activityStatus;
	
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

	public Site getSite() {
		return site;
	}

	public void setSite(Site distributionSite) {
		this.site = distributionSite;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public void update(DistributionOrder other) {
		
		setName(other.name);
		setRequester(other.requester);
		setDistributor(other.distributor);
		updateOrderItems(other);
		updateDistribution(other);
		setDistributionProtocol(other.distributionProtocol);
		updateStatus(other);
		setCreationDate(other.creationDate);
		setSite(other.site);
		setExecutionDate(other.executionDate);
	}
	
	public void distribute() {
		if (isOrderDistributed()) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.ALREADY_DISTRIBUTED);
		}
		
		for (DistributionOrderItem orderItem : getOrderItems()) {
			orderItem.distribute();
		}
		
		setStatus(Status.DISTRIBUTED);
	}
	
	public void distributeAndClose() {
		if (isOrderDistributed()) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.ALREADY_DISTRIBUTED);
		}
		
		for (DistributionOrderItem orderItem : getOrderItems()) {
			orderItem.distributeAndClose();
		}
		
		setStatus(Status.DISTRIBUTED_AND_CLOSED);
	}
	
	private void updateOrderItems(DistributionOrder other) {
		if (isOrderDistributed()) {
			/*
			 * Order items can't be modified once the order is distributed.
			 */
			return;
		}
		
		CollectionUpdater.update(orderItems, other.orderItems);
		
		for (DistributionOrderItem item : getOrderItems()) {
			item.setOrder(this);
		}
		
	}
	
	private void updateDistribution(DistributionOrder other) {
		if (status == Status.PENDING && other.status == Status.DISTRIBUTED) {
			distribute();
		} else if (status == Status.PENDING && other.status == Status.DISTRIBUTED_AND_CLOSED) {
			distributeAndClose();
		}
	}
	
	private void updateStatus(DistributionOrder other) {
		if (status == other.status) {
			return;
		}
		
		if (status == Status.PENDING && other.isOrderDistributed()) {
			setStatus(other.status);
		} else {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.STATUS_CHANGE_NOT_ALLOWED);
		}
	}
	
	public boolean isOrderDistributed() {
		return (Status.DISTRIBUTED == status || 
				Status.DISTRIBUTED_AND_CLOSED == status);
	}
}
