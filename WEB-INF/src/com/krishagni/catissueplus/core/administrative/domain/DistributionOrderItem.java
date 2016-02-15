package com.krishagni.catissueplus.core.administrative.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class DistributionOrderItem extends BaseEntity {
	public enum Status {
		DISTRIBUTED,
		
		DISTRIBUTED_AND_CLOSED,

		RETURNED
	}
	
	private DistributionOrder order;
	
	private BigDecimal quantity;
	
	private Specimen specimen;
	
	private Status status;

	private BigDecimal returnQuantity;

	private StorageContainer returnLocation;

	private String returnContainerRow;

	private String returnContainerColumn;

	private User returnUser;

	private Date returnDate;

	private String returnComments;

	public DistributionOrder getOrder() {
		return order;
	}

	public void setOrder(DistributionOrder order) {
		this.order = order;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(BigDecimal returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public StorageContainer getReturnLocation() {
		return returnLocation;
	}

	public void setReturnLocation(StorageContainer returnLocation) {
		this.returnLocation = returnLocation;
	}

	public String getReturnContainerRow() {
		return returnContainerRow;
	}

	public void setReturnContainerRow(String returnContainerRow) {
		this.returnContainerRow = returnContainerRow;
	}

	public String getReturnContainerColumn() {
		return returnContainerColumn;
	}

	public void setReturnContainerColumn(String returnContainerColumn) {
		this.returnContainerColumn = returnContainerColumn;
	}

	public User getReturnUser() {
		return returnUser;
	}

	public void setReturnUser(User returnUser) {
		this.returnUser = returnUser;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnComments() {
		return returnComments;
	}

	public void setReturnComments(String returnComments) {
		this.returnComments = returnComments;
	}

	public boolean isDistributedAndClosed() {
		return getStatus() == Status.DISTRIBUTED_AND_CLOSED;
	}

	public void distribute() {
		order.addOnSaveProc(() -> specimen.distribute(this));
	}

	public void returnSpecimen(StorageContainerPosition newLocation) {
		specimen.returnSpecimen(this, newLocation);
		this.setStatus(Status.RETURNED);
	}
}
