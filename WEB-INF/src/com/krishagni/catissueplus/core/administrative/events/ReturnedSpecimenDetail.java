package com.krishagni.catissueplus.core.administrative.events;

import java.math.BigDecimal;
import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class ReturnedSpecimenDetail {
	private String orderName;

	private String specimenLabel;

	private Long itemId;

	private BigDecimal quantity;

	private StorageLocationSummary location;

	private UserSummary user;

	private Date time;

	private String comments;

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getSpecimenLabel() {
		return specimenLabel;
	}

	public void setSpecimenLabel(String specimenLabel) {
		this.specimenLabel = specimenLabel;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public StorageLocationSummary getLocation() {
		return location;
	}

	public void setLocation(StorageLocationSummary location) {
		this.location = location;
	}

	public UserSummary getUser() {
		return user;
	}

	public void setUser(UserSummary user) {
		this.user = user;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
