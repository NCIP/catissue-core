package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Utility;

public class Shipment extends BaseEntity {
	public enum Status {
		PENDING,
		SHIPPED,
		RECEIVED
	}
	
	private String name;
	
	private Site site;
	
	private Date shippedDate;
	
	private User sender;
	
	private String senderComments;
	
	private Date receivedDate;
	
	private User receiver;
	
	private String receiverComments;
	
	private Status status;
	
	private String activityStatus;
	
	private Set<ShipmentItem> shipmentItems = new HashSet<ShipmentItem>();
	
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

	public Date getShippedDate() {
		return shippedDate;
	}

	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getSenderComments() {
		return senderComments;
	}

	public void setSenderComments(String senderComments) {
		this.senderComments = senderComments;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
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

	public Set<ShipmentItem> getShipmentItems() {
		return shipmentItems;
	}

	public void setShipmentItems(Set<ShipmentItem> shipmentItems) {
		this.shipmentItems = shipmentItems;
	}

	public void update(Shipment other) {
		setName(other.getName());
		setSite(other.getSite());
		setShippedDate(other.getShippedDate());
		setSender(other.getSender());
		setSenderComments(other.getSenderComments());
		setReceivedDate(other.getReceivedDate());
		setReceiver(other.getReceiver());
		setReceiverComments(other.getReceiverComments());
		setActivityStatus(other.getActivityStatus());
		
		if (getStatus() == Status.PENDING) {
			updateOrderItems(other);
		}
		updateStatus(other);
	}
	
	public void ship() {
		if (getId() != null && isShipped()) {
			throw OpenSpecimenException.userError(ShipmentErrorCode.ALREADY_SHIPPED);
		}
		
		if (CollectionUtils.isEmpty(getShipmentItems())) {
			throw OpenSpecimenException.userError(ShipmentErrorCode.NO_SPECIMENS_TO_SHIP);
		}
		
		for (ShipmentItem item : getShipmentItems()) {
			item.ship();
		}
		
		setStatus(Status.SHIPPED);
	}
	
	public void receive(Shipment other) {
		if (isReceived()) {
			throw OpenSpecimenException.userError(ShipmentErrorCode.ALREADY_RECEIVED);
		}
		
		ensureShippedSpecimens(other);
		
		Map<Specimen, ShipmentItem> existingItems = new HashMap<Specimen, ShipmentItem>();
		for (ShipmentItem item : getShipmentItems()) {
			existingItems.put(item.getSpecimen(), item); 
		}
		
		for (ShipmentItem newItem : other.getShipmentItems()) {
			ShipmentItem oldItem = existingItems.remove(newItem.getSpecimen());
			oldItem.receive(newItem);
		}
		
		setStatus(Status.RECEIVED);
 	}
	
	public boolean isPending() {
		return Status.PENDING == getStatus();
	}
	
	public boolean isShipped() {
		return Status.SHIPPED == getStatus();
	}
	
	public boolean isReceived() {
		return Status.RECEIVED == getStatus();
	}
	
	public void ensureShippedSpecimens(Shipment other) {
		List<String> existingSpecimens = Utility.<List<String>>collect(getShipmentItems(), "specimen.label");
		List<String> newSpecimens = Utility.<List<String>>collect(other.getShipmentItems(), "specimen.label");
		
		if (!CollectionUtils.isEqualCollection(existingSpecimens, newSpecimens)) {
			throw OpenSpecimenException.userError(ShipmentErrorCode.INVALID_SHIPPED_SPECIMENS);
		}
	}
	
	private void updateOrderItems(Shipment other) {
		Map<Specimen, ShipmentItem> existingItems = new HashMap<Specimen, ShipmentItem>();
		for (ShipmentItem item : getShipmentItems()) {
			existingItems.put(item.getSpecimen(), item); 
		}
		
		for (ShipmentItem newItem : other.getShipmentItems()) {
			ShipmentItem oldItem = existingItems.remove(newItem.getSpecimen());
			if (oldItem == null) {
				getShipmentItems().add(newItem);
			}
		}
		
		getShipmentItems().removeAll(existingItems.values());
	}
	
	private void updateStatus(Shipment other) {
		if (getStatus() == other.getStatus()) {
			return;
		}
		
		if (getStatus() == Status.PENDING && other.isShipped()) {
			ship();
		} else if (isShipped() && other.isReceived()) {
			receive(other);
		} else {
			throw OpenSpecimenException.userError(ShipmentErrorCode.STATUS_CHANGE_NOT_ALLOWED);
		}
	}
}
