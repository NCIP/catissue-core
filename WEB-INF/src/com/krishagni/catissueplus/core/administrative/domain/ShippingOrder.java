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
	
	private User distributor;
	
	private Set<ShippingOrderItem> orderItems = new HashSet<ShippingOrderItem>();
	
	private Status status;
	
	private Date shippingDate;
	
	private String comments;
	
	private String activityStatus;
	
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
	
	public User getDistributor() {
		return distributor;
	}
	
	public void setDistributor(User distributor) {
		this.distributor = distributor;
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
	
	public void update(ShippingOrder newOrder) {
		setName(newOrder.getName());
		setSite(newOrder.getSite());
		setDistributor(newOrder.getDistributor());
		setShippingDate(newOrder.getShippingDate());
		setComments(newOrder.getComments());
		setActivityStatus(newOrder.getActivityStatus());
		
		updateOrderItems(newOrder);
		updateStatus(newOrder);
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
	
	private void updateOrderItems(ShippingOrder other) {
		Map<Specimen, ShippingOrderItem> existingItems = new HashMap<Specimen, ShippingOrderItem>();
		for (ShippingOrderItem item : getOrderItems()) {
			existingItems.put(item.getSpecimen(), item); 
		}
		
		for (ShippingOrderItem newItem : other.getOrderItems()) {
			ShippingOrderItem oldItem = existingItems.remove(newItem.getSpecimen());
			if (oldItem != null) {
				oldItem.update(newItem);
			} else {
				getOrderItems().add(newItem);
			}
		}
		
		getOrderItems().removeAll(existingItems.values());
	}
	
	private void updateStatus(ShippingOrder order) {
		if (getStatus() == order.getStatus()) {
			return;
		}
		
		if (getStatus() == Status.PENDING && order.isOrderShipped()) {
			ship();
		} else {
			throw OpenSpecimenException.userError(ShippingOrderErrorCode.STATUS_CHANGE_NOT_ALLOWED);
		}
	}
	
	private boolean isOrderShipped() {
		return Status.SHIPPED == getStatus();
	}
}
