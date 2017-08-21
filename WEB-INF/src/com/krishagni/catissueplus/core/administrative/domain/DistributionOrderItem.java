package com.krishagni.catissueplus.core.administrative.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

@Audited
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

	private BigDecimal returnedQuantity;

	private StorageContainer returningContainer;

	private String returningRow;

	private String returningColumn;

	private User returnedBy;

	private Date returnDate;

	private Integer freezeThawIncrOnReturn;

	private String returnComments;

	private transient SpecimenRequestItem requestItem;

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

	public BigDecimal getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(BigDecimal returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	public StorageContainer getReturningContainer() {
		return returningContainer;
	}

	public void setReturningContainer(StorageContainer returningContainer) {
		this.returningContainer = returningContainer;
	}

	public String getReturningRow() {
		return returningRow;
	}

	public void setReturningRow(String returningRow) {
		this.returningRow = returningRow;
	}

	public String getReturningColumn() {
		return returningColumn;
	}

	public void setReturningColumn(String returningColumn) {
		this.returningColumn = returningColumn;
	}

	public User getReturnedBy() {
		return returnedBy;
	}

	public void setReturnedBy(User returnedBy) {
		this.returnedBy = returnedBy;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Integer getFreezeThawIncrOnReturn() {
		return freezeThawIncrOnReturn;
	}

	public void setFreezeThawIncrOnReturn(Integer freezeThawIncrOnReturn) {
		this.freezeThawIncrOnReturn = freezeThawIncrOnReturn;
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

	public SpecimenRequestItem getRequestItem() {
		return requestItem;
	}

	public void setRequestItem(SpecimenRequestItem requestItem) {
		this.requestItem = requestItem;
	}

	public boolean isReturned() { return getStatus() == Status.RETURNED; }

	public void distribute() {
		if (requestItem != null) {
			requestItem.throwErrorIfFulfilled();
		}

		order.addOnSaveProc(() -> specimen.distribute(this));

		if (requestItem != null) {
			requestItem.distribute(getOrder());
		}
	}	

	public void returnSpecimen() {
		specimen.returnSpecimen(this);
		setStatus(Status.RETURNED);
	}

	public static boolean isValidDistributionStatus(String status) {
		return status.equals(Status.DISTRIBUTED.name()) ||
			status.equals(Status.DISTRIBUTED_AND_CLOSED.name());
	}
}
