package com.krishagni.catissueplus.core.administrative.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionOrderDetail extends DistributionOrderSummary implements Mergeable<String, DistributionOrderDetail>, Serializable {
	private UserSummary distributor;
	
	private String trackingUrl;
	
	private String comments;

	private SpecimenRequestSummary request;
	
	private List<DistributionOrderItemDetail> orderItems = new ArrayList<DistributionOrderItemDetail>();
	
	private String activityStatus;

	private Map<String, Object> extraAttrs;

	//
	// For BO template
	//
	private DistributionOrderItemDetail orderItem;

	public UserSummary getDistributor() {
		return distributor;
	}

	public void setDistributor(UserSummary distributor) {
		this.distributor = distributor;
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

	public SpecimenRequestSummary getRequest() {
		return request;
	}

	public void setRequest(SpecimenRequestSummary request) {
		this.request = request;
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

	public Map<String, Object> getExtraAttrs() {
		return extraAttrs;
	}

	public void setExtraAttrs(Map<String, Object> extraAttrs) {
		this.extraAttrs = extraAttrs;
	}

	public DistributionOrderItemDetail getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(DistributionOrderItemDetail orderItem) {
		this.orderItem = orderItem;
	}

	public static DistributionOrderDetail from(DistributionOrder order) {
		DistributionOrderDetail detail = new DistributionOrderDetail();
		fromTo(order, detail);

		if (order.getDistributor() != null ) {
			detail.setDistributor(UserSummary.from(order.getDistributor()));
		}

		if (order.getRequest() != null) {
			detail.setRequest(SpecimenRequestSummary.from(order.getRequest()));
		}
		
		detail.setTrackingUrl(order.getTrackingUrl());
		detail.setComments(order.getComments());
		detail.setOrderItems(DistributionOrderItemDetail.from(order.getOrderItems()));
		detail.setActivityStatus(order.getActivityStatus());
		return detail;
	}
	
	public static List<DistributionOrderDetail> from(List<DistributionOrder> orders) {
		return orders.stream().map(DistributionOrderDetail::from).collect(Collectors.toList());
	}

	@Override
	@JsonIgnore
	public String getMergeKey() {
		return getName();
	}

	@Override
	public void merge(DistributionOrderDetail other) {
		getOrderItems().add(other.getOrderItem());
	}
}
