package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class DistributionOrderItem extends BaseEntity {
	public enum Status {
		DISTRIBUTED,
		
		DISTRIBUTED_AND_CLOSED
	}
	
	private DistributionOrder order;
	
	private Double quantity;
	
	private Specimen specimen;
	
	private Status status;

	public DistributionOrder getOrder() {
		return order;
	}

	public void setOrder(DistributionOrder order) {
		this.order = order;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
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

	public void distribute() {		
		specimen.distribute(
				order.getDistributor(),
				order.getExecutionDate(),
				quantity, 
				status == Status.DISTRIBUTED_AND_CLOSED);
	}	
}
