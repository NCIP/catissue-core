package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.factory.ShippingOrderErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class ShippingOrder extends BaseEntity {
	public enum Status {
		PENDING,
		SHIPPED,
		COLLECTED
	}
	
	private String name;
	
	private Site site;
	
	private User sender;
	
	private Set<ShippingOrderItem> orderItems = new HashSet<ShippingOrderItem>();
	
	private Status status;
	
	private Date shippingDate;
	
	private String comments;
	
	private String activityStatus;
	
	private User receiver;
	
	private String receiverComments;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Site getSite() {
		return site;
	}
	
	public void setSite(Site site) {
		this.site = site;
	}
	
	public User getSender() {
		return sender;
	}
	
	public void setSender(User sender) {
		this.sender = sender;
	}

	public Set<ShippingOrderItem> getOrderItems() {
		return orderItems;
	}
	
	public void setOrderItems(Set<ShippingOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
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
	
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getReceiverComments() {
		return receiverComments;
	}

	public void setReceiverComments(String receiverComments) {
		this.receiverComments = receiverComments;
	}

	public void update(ShippingOrder other) {
		setName(other.getName());
		setSite(other.getSite());
		setSender(other.getSender());
		setShippingDate(other.getShippingDate());
		setComments(other.getComments());
		setActivityStatus(other.getActivityStatus());
		setReceiver(other.getReceiver());
		setReceiverComments(other.getReceiverComments());
		
		updateOrderItems(other);
		updateStatus(other);
	}
	
	public void ship() {
		if (isOrderShipped()) {
			throw OpenSpecimenException.userError(ShippingOrderErrorCode.ORDER_ALREADY_SHIPPED);
		}
		
		if (CollectionUtils.isEmpty(getOrderItems())) {
			throw OpenSpecimenException.userError(ShippingOrderErrorCode.NO_SPECIMENS_TO_SHIP);
		}
		
		for (ShippingOrderItem item : getOrderItems()) {
			item.ship();
		}
		
		setStatus(Status.SHIPPED);
	}
	
	public void collect() {
		if (isOrderCollected()) {
			throw OpenSpecimenException.userError(ShippingOrderErrorCode.ORDER_ALREADY_COLLECTED);
		}
		
		setStatus(Status.COLLECTED);
 	}
	
	public boolean isOrderShipped() {
		return Status.SHIPPED == getStatus();
	}
	
	public boolean isOrderCollected() {
		return Status.COLLECTED == getStatus();
	}
	
	private void updateOrderItems(ShippingOrder other) {
		Map<Specimen, ShippingOrderItem> existingItems = new HashMap<Specimen, ShippingOrderItem>();
		for (ShippingOrderItem item : getOrderItems()) {
			existingItems.put(item.getSpecimen(), item); 
		}
		
		for (ShippingOrderItem newItem : other.getOrderItems()) {
			ShippingOrderItem oldItem = existingItems.remove(newItem.getSpecimen());
			if (oldItem != null && other.isOrderCollected()) {
				oldItem.collect(newItem);
			} else {
				getOrderItems().add(newItem);
			}
		}
		
		getOrderItems().removeAll(existingItems.values());
	}
	
	private void updateStatus(ShippingOrder other) {
		if (getStatus() == other.getStatus()) {
			return;
		}
		
		if (getStatus() == Status.PENDING && other.isOrderShipped()) {
			ship();
		} else if (isOrderShipped() && other.isOrderCollected()) {
			collect();
			updateOrderItems(other);
		} else {
			throw OpenSpecimenException.userError(ShippingOrderErrorCode.ORDER_NOT_SHIPPED);
		}
	}
}

