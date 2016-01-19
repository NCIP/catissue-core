package com.krishagni.catissueplus.core.administrative.domain;

import java.math.BigDecimal;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class DistributionOrderItem extends BaseEntity {
	public enum Status {
		DISTRIBUTED,
		
		DISTRIBUTED_AND_CLOSED
	}
	
	private DistributionOrder order;
	
	private BigDecimal quantity;
	
	private Specimen specimen;
	
	private Status status;

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
	
	public boolean isDistributedAndClosed() {
		return getStatus() == Status.DISTRIBUTED_AND_CLOSED;
	}

	public void distribute() {
		order.addOnSaveProc(() -> specimen.distribute(this));
	}
}
