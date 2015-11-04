package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class ShippingOrderItem extends BaseEntity {
	public enum Quality {
		ACCEPTABLE,
		UNACCEPTABLE
	}
	
	private ShippingOrder order;
	
	private Specimen specimen;
	
	private Quality quality;
	
	public ShippingOrder getOrder() {
		return order;
	}
	
	public void setOrder(ShippingOrder order) {
		this.order = order;
	}
	
	public Specimen getSpecimen() {
		return specimen;
	}
	
	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}
	
	public Quality getQuality() {
		return quality;
	}
	
	public void setQuality(Quality quality) {
		this.quality = quality;
	}
	
	public void ship() {
		specimen.updatePosition(null);
	}
	
	public void update(ShippingOrderItem other) {
		setOrder(other.getOrder());
		setSpecimen(other.specimen);
		setQuality(other.getQuality());
	}
}
