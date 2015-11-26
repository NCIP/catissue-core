package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ShipmentDetail {
	private Long id;
	
	private String name;
	
	private String  courierName;
	
	private String trackingNumber;
	
	private String trackingUrl;
	
	private String sendSiteName;
	
	private String recInstName;
	
	private String recSiteName;
	
	private Date shippedDate;
	
	private UserSummary sender;
	
	private String senderComments;
	
	private Date receivedDate;
	
	private UserSummary receiver;
	
	private String receiverComments;
	
	private String status;
	
	private String activityStatus;
	
	private List<ShipmentItemDetail> shipmentItems = new ArrayList<ShipmentItemDetail>();
	
	private List<UserSummary> notifyUsers = new ArrayList<UserSummary>();
	
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

	public String getSendSiteName() {
		return sendSiteName;
	}

	public void setSendSiteName(String sendSiteName) {
		this.sendSiteName = sendSiteName;
	}
	
	public String getRecInstName() {
		return recInstName;
	}

	public void setRecInstName(String recInstName) {
		this.recInstName = recInstName;
	}
	
	public String getRecSiteName() {
		return recSiteName;
	}
	
	public void setRecSiteName(String recSiteName) {
		this.recSiteName = recSiteName;
	}

	public Date getShippedDate() {
		return shippedDate;
	}

	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}

	public UserSummary getSender() {
		return sender;
	}

	public void setSender(UserSummary sender) {
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

	public UserSummary getReceiver() {
		return receiver;
	}

	public void setReceiver(UserSummary receiver) {
		this.receiver = receiver;
	}

	public String getReceiverComments() {
		return receiverComments;
	}

	public void setReceiverComments(String receiverComments) {
		this.receiverComments = receiverComments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public List<ShipmentItemDetail> getShipmentItems() {
		return shipmentItems;
	}

	public void setShipmentItems(List<ShipmentItemDetail> shipmentItems) {
		this.shipmentItems = shipmentItems;
	}

	public List<UserSummary> getNotifyUsers() {
		return notifyUsers;
	}

	public void setNotifyUsers(List<UserSummary> notifyUsers) {
		this.notifyUsers = notifyUsers;
	}

	public static ShipmentDetail from(Shipment shipment) {
		ShipmentDetail detail = new ShipmentDetail();
		detail.setId(shipment.getId());
		detail.setName(shipment.getName());
		detail.setCourierName(shipment.getCourierName());
		detail.setTrackingNumber(shipment.getTrackingNumber());
		detail.setTrackingUrl(shipment.getTrackingUrl());
		detail.setSendSiteName(shipment.getSendingSite().getName());
		detail.setRecInstName(shipment.getReceivingSite().getInstitute().getName());
		detail.setRecSiteName(shipment.getReceivingSite().getName());
		detail.setShippedDate(shipment.getShippedDate());
		detail.setSender(UserSummary.from(shipment.getSender()));
		detail.setSenderComments(shipment.getSenderComments());
		detail.setReceivedDate(shipment.getReceivedDate());
		detail.setReceiver(shipment.getReceiver() == null ? null : UserSummary.from(shipment.getReceiver()));
		detail.setReceiverComments(shipment.getReceiverComments());
		detail.setStatus(shipment.getStatus().toString());
		detail.setActivityStatus(shipment.getActivityStatus());
		detail.setShipmentItems(ShipmentItemDetail.from(shipment.getShipmentItems()));
		
		if (shipment.isPending()) {
			detail.setNotifyUsers(UserSummary.from(shipment.getNotifyUsers()));
		}
		
		return detail;
	}
	
	public static List<ShipmentDetail> from(Collection<Shipment> orders) {
		List<ShipmentDetail> details = new ArrayList<ShipmentDetail>();
		for(Shipment order : orders) {
			details.add(from(order));
		}
		
		return details;
	}
}
