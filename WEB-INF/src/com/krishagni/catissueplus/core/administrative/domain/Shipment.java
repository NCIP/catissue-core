package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.factory.ShipmentErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SpecimenRequestErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Utility;

public class Shipment extends BaseEntity {
	public enum Status {
		PENDING("Pending"),

		SHIPPED("Shipped"),

		RECEIVED("Received");
		
		private final String name;
		
		private Status(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static Status fromName(String name) {
			if (StringUtils.isBlank(name)) {
				return null;
			}
			
			Status result = null;
			for (Status value : Status.values()) {
				if (value.name.equalsIgnoreCase(name)) {
					result = value;
					break;
				}
			}
			
			return result;
		}
	}
	
	private String name;
	
	private String  courierName;
	
	private String trackingNumber;
	
	private String trackingUrl;
	
	private Site sendingSite;
	
	private Site receivingSite;
	
	private Date shippedDate;
	
	private User sender;
	
	private String senderComments;
	
	private Date receivedDate;
	
	private User receiver;
	
	private String receiverComments;
	
	private Status status;
	
	private String activityStatus;
	
	private Set<ShipmentItem> shipmentItems = new HashSet<ShipmentItem>();
	
	private Set<User> notifyUsers = new HashSet<User>();

	private SpecimenRequest request;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	public Site getSendingSite() {
		return sendingSite;
	}
	
	public void setSendingSite(Site sendingSite) {
		this.sendingSite = sendingSite;
	}
	
	public Site getReceivingSite() {
		return receivingSite;
	}

	public void setReceivingSite(Site receivingSite) {
		this.receivingSite = receivingSite;
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

	public Set<User> getNotifyUsers() {
		return notifyUsers;
	}

	public void setNotifyUsers(Set<User> notifyUsers) {
		this.notifyUsers = notifyUsers;
	}

	public SpecimenRequest getRequest() {
		return request;
	}

	public void setRequest(SpecimenRequest request) {
		this.request = request;
	}

	public void update(Shipment other) {
		setName(other.getName());
		setCourierName(other.getCourierName());
		setTrackingNumber(other.getTrackingNumber());
		setTrackingUrl(other.getTrackingUrl());
		setSendingSite(other.getSendingSite());
		setReceivingSite(other.getReceivingSite());
		setShippedDate(other.getShippedDate());
		setSender(other.getSender());
		setSenderComments(other.getSenderComments());
		setReceivedDate(other.getReceivedDate());
		setReceiver(other.getReceiver());
		setReceiverComments(other.getReceiverComments());
		setActivityStatus(other.getActivityStatus());

		updateRequest(other);
		updateOrderItems(other);
		updateNotifyUsers(other);
		updateStatus(other);
	}

	public Set<ShipmentItem> getShipmentItemsWithReqDetail() {
		Map<Long, SpecimenRequestItem> reqItemsMap = Collections.emptyMap();
		if (getRequest() != null) {
			reqItemsMap = getRequest().getSpecimenIdRequestItemMap();
		}

		for (ShipmentItem item : getShipmentItems()) {
			item.setRequestItem(reqItemsMap.get(item.getSpecimen().getId()));
		}

		return getShipmentItems();
	}

	public void ship() {
		if (isShipped()) {
			throw OpenSpecimenException.userError(ShipmentErrorCode.ALREADY_SHIPPED);
		}

		Set<ShipmentItem> shipmentItems = getShipmentItemsWithReqDetail();
		if (CollectionUtils.isEmpty(shipmentItems)) {
			throw OpenSpecimenException.userError(ShipmentErrorCode.NO_SPECIMENS_TO_SHIP);
		}

		shipmentItems.forEach(ShipmentItem::ship);
		setStatus(Status.SHIPPED);

		if (getRequest() != null) {
			getRequest().closeIfFulfilled();
		}
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

	private void updateRequest(Shipment other) {
		if (getStatus() != Status.PENDING) {
			return;
		}

		SpecimenRequest request = other.getRequest();
		if (request != null && request.isClosed()) {
			throw OpenSpecimenException.userError(SpecimenRequestErrorCode.CLOSED, request.getId());
		}

		setRequest(request);
	}

	private void updateOrderItems(Shipment other) {
		if (getStatus() != Status.PENDING) {
			return;
		}

		Map<Specimen, ShipmentItem> existingItems = new HashMap<Specimen, ShipmentItem>();
		for (ShipmentItem item : getShipmentItems()) {
			existingItems.put(item.getSpecimen(), item); 
		}
		
		for (ShipmentItem newItem : other.getShipmentItems()) {
			ShipmentItem oldItem = existingItems.remove(newItem.getSpecimen());
			if (oldItem == null) {
				newItem.setShipment(this);
				getShipmentItems().add(newItem);
			}
		}
		
		getShipmentItems().removeAll(existingItems.values());
	}

	private void updateNotifyUsers(Shipment other) {
		if (getStatus() != Status.PENDING) {
			return;
		}

		CollectionUpdater.update(getNotifyUsers(), other.getNotifyUsers());
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
