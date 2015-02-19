package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Order {
	public static String PENDING = "Pending";

	public static String DISTRIBUTED = "Distributed";
	
	public static String DISTRIBUTED_AND_CLOSED = "Distributed And Closed";
	
	protected Long id;
	
	protected String name;
	
	protected User requester;
	
	protected Date requestedDate;
	
	protected Set<OrderItem> orderItems = new HashSet<OrderItem>();

	protected String status;

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

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
