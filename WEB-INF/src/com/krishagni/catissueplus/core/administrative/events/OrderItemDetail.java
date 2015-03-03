package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.OrderItem;

public class OrderItemDetail {
	private Long id;
	
	private Long specimenId;
	
	private Double quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public static OrderItemDetail from(OrderItem orderItem) {
		OrderItemDetail detail = new OrderItemDetail();
		detail.setId(orderItem.getId());
		detail.setQuantity(orderItem.getQuantity());
		
		if (orderItem.getSpecimen() != null) {
			detail.setSpecimenId(orderItem.getSpecimen().getId());
		}
		
		return detail;
	}
	
	public static List<OrderItemDetail> from(Set<OrderItem> orderItems) {
		List<OrderItemDetail> items = new ArrayList<OrderItemDetail>();
		
		for (OrderItem item : orderItems) {
			items.add(from(item));
		}
		
		return items;
	}
}
