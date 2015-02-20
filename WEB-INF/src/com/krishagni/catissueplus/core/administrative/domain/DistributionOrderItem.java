package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class DistributionOrderItem {
	private Long id;
	
	private DistributionOrder order;
	
	private Double quantity;
	
	private Specimen specimen;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
	
	public void distribute(boolean closeAfterDistribution) {
		specimen.distribute(quantity, closeAfterDistribution);
	}
}
