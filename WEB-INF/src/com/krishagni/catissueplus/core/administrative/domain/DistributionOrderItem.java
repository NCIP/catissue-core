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

	private Integer returnPosOne;

	private Integer returnPosTwo;

	private String returnPosOneStr;

	private String returnPosTwoStr;

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

	public Integer getReturnPosOne() {
		return returnPosOne;
	}

	public void setReturnPosOne(Integer returnPosOne) {
		this.returnPosOne = returnPosOne;
	}

	public Integer getReturnPosTwo() {
		return returnPosTwo;
	}

	public void setReturnPosTwo(Integer returnPosTwo) {
		this.returnPosTwo = returnPosTwo;
	}

	public String getReturnPosOneStr() {
		return returnPosOneStr;
	}

	public void setReturnPosOneStr(String returnPosOneStr) {
		this.returnPosOneStr = returnPosOneStr;
	}

	public String getReturnPosTwoStr() {
		return returnPosTwoStr;
	}

	public void setReturnPosTwoStr(String returnPosTwoStr) {
		this.returnPosTwoStr = returnPosTwoStr;
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

	public void returnSpecimen() {
		specimen.returnSpecimen(this);
		this.setStatus(Status.RETURNED);
	}
}
