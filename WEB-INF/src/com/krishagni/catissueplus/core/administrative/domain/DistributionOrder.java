package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class DistributionOrder extends BaseEntity {
	public enum DistributionStatus { 
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
	
	private DistributionStatus status;
	
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

	public DistributionStatus getStatus() {
		return status;
	}

	public void setStatus(DistributionStatus status) {
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
		updateStatus(other);
		setDistributionProtocol(other.distributionProtocol);
		setCreationDate(other.creationDate);
		setSite(other.site);
		setExecutionDate(other.executionDate);
	}
	
	public void distribute() {
		if (!DistributionStatus.PENDING.equals(status)) {
			for (DistributionOrderItem orderItem : getOrderItems()) {
				orderItem.distribute();
			}
		}
	}
	
	private void updateOrderItems(DistributionOrder other) {
		CollectionUpdater.update(orderItems, other.orderItems);

		for (DistributionOrderItem item : getOrderItems()) {
			item.setOrder(this);
		}
		
	}
	
	private void updateDistribution(DistributionOrder other) {
		if (DistributionStatus.PENDING.equals(status) && other.isOrderDistributed()) {
			
			for (DistributionOrderItem orderItem : getOrderItems()) {
				orderItem.distribute();
			}
		}
	}
	
	private void updateStatus(DistributionOrder other) {
		if (status.equals(other.status) || 
				(DistributionStatus.PENDING.equals(status) && other.isOrderDistributed())) {
			setStatus(other.status);
		} else { 
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.STATUS_CHANGE_NOT_ALLOWED);
		}
	}
	
	public boolean isOrderDistributed() {
		return (DistributionStatus.DISTRIBUTED.equals(status) || 
				DistributionStatus.DISTRIBUTED_AND_CLOSED.equals(status));
	}
}
