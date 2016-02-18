package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class DistributionOrder extends BaseEntity {
	public enum Status { 
		PENDING,
		EXECUTED
	}
	
	private static final String ENTITY_NAME = "distribution_order";
	
	private String name;
	
	private DistributionProtocol distributionProtocol;
	
	private Site site;
	
	private User requester;
	
	private Date creationDate;
	
	private User distributor;
	
	private Date executionDate;

	private Set<DistributionOrderItem> orderItems = new HashSet<DistributionOrderItem>();
	
	private Status status;
	
	private String activityStatus;
	
	private String trackingUrl;
	
	private String comments;
	
	public static String getEntityName() {
		return ENTITY_NAME;
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

	public Set<DistributionOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<DistributionOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Map<Long, DistributionOrderItem> getOrderItemsMap() {
		return getOrderItems().stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
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
	
	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Institute getInstitute() {
		return requester.getInstitute();
	}

	public void update(DistributionOrder newOrder) { // TODO: can't update executed order
		setName(newOrder.getName());
		setRequester(newOrder.getRequester());
		setDistributor(newOrder.getDistributor());
		setDistributionProtocol(newOrder.getDistributionProtocol());
		setCreationDate(newOrder.getCreationDate());
		setSite(newOrder.getSite());
		setExecutionDate(newOrder.getExecutionDate());
		setTrackingUrl(newOrder.getTrackingUrl());
		setComments(newOrder.getComments());
		
		updateOrderItems(newOrder);
		updateDistribution(newOrder);		
		updateStatus(newOrder);
	}
	
	public void distribute() {
		if (isOrderExecuted()) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.ALREADY_EXECUTED);
		}
		
		if (CollectionUtils.isEmpty(getOrderItems())) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.NO_SPECIMENS_TO_DIST);
		}
		
		for (DistributionOrderItem orderItem : getOrderItems()) {
			orderItem.distribute();
		}
		
		setStatus(Status.EXECUTED);
	}
	
	private void updateOrderItems(DistributionOrder other) {
		if (isOrderExecuted()) {
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
		if (getStatus() == Status.PENDING && other.getStatus() == Status.EXECUTED) {
			distribute();
		}
	}
	
	private void updateStatus(DistributionOrder other) {
		if (status == other.status) {
			return;
		}
		
		if (status == Status.PENDING && other.isOrderExecuted()) {
			setStatus(other.status);
		} else {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.STATUS_CHANGE_NOT_ALLOWED);
		}
	}
	
	public boolean isOrderExecuted() {
		return Status.EXECUTED == status;
	}
}
