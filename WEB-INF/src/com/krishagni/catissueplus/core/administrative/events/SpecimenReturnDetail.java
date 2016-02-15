package com.krishagni.catissueplus.core.administrative.events;

import java.math.BigDecimal;
import java.util.Date;

public class SpecimenReturnDetail {
	private Long itemId;

	private BigDecimal quantity;

	private StorageContainerPositionDetail location;

	private Long userId;

	private Date time;

	private String comments;

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

	public StorageContainerPositionDetail getLocation() {
		return location;
	}

	public void setLocation(StorageContainerPositionDetail location) {
		this.location = location;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
