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

	private StorageContainer returnContainer;

	private String returnRow;

	private String returnColumn;

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

	public StorageContainer getReturnContainer() {
		return returnContainer;
	}

	public void setReturnContainer(StorageContainer returnContainer) {
		this.returnContainer = returnContainer;
	}

	public String getReturnRow() {
		return returnRow;
	}

	public void setReturnRow(String returnRow) {
		this.returnRow = returnRow;
	}

	public String getReturnColumn() {
		return returnColumn;
	}

	public void setReturnColumn(String returnColumn) {
		this.returnColumn = returnColumn;
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

	public boolean isReturned() { return getStatus() == Status.RETURNED; }

	public void distribute() {
		order.addOnSaveProc(() -> specimen.distribute(this));
	}

	public void returnSpecimen() {
		specimen.returnSpecimen(this);
		setStatus(Status.RETURNED);
	}
}
