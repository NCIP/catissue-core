package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ShippingOrderDetail {
	private Long id;
	
	private String name;
	
	private String instituteName;
	
	private String siteName;
	
	private UserSummary sender;
	
	private Set<ShippingOrderItemDetail> orderItems = new HashSet<ShippingOrderItemDetail>();
	
	private String status;
	
	private Date shippingDate;
	
	private String comments;
	
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
	
	public String getInstituteName() {
		return instituteName;
	}
	
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	
	public String getSiteName() {
		return siteName;
	}
	
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	public UserSummary getSender() {
		return sender;
	}

	public void setSender(UserSummary sender) {
		this.sender = sender;
	}

	public Set<ShippingOrderItemDetail> getOrderItems() {
		return orderItems;
	}
	
	public void setOrderItems(Set<ShippingOrderItemDetail> orderItems) {
		this.orderItems = orderItems;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getShippingDate() {
		return shippingDate;
	}
	
	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}
	
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public static ShippingOrderDetail from(ShippingOrder order) {
		ShippingOrderDetail detail = new ShippingOrderDetail();
		
		detail.setId(order.getId());
		detail.setName(order.getName());
		detail.setInstituteName(order.getSite().getInstitute().getName());
		detail.setSiteName(order.getSite().getName());
		detail.setSender(UserSummary.from(order.getSender()));
		detail.setOrderItems(ShippingOrderItemDetail.from(order.getOrderItems()));
		detail.setStatus(order.getStatus().toString());
		detail.setShippingDate(order.getShippingDate());
		detail.setComments(order.getComments());
		detail.setActivityStatus(order.getActivityStatus());
		
		return detail;
	}
	
	public static List<ShippingOrderDetail> from(Collection<ShippingOrder> orders) {
		List<ShippingOrderDetail> details = new ArrayList<ShippingOrderDetail>();
		
		for(ShippingOrder order : orders) {
			details.add(from(order));
		}
		
		return details;
	}
}
