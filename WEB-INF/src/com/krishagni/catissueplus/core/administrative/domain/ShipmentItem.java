package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class ShipmentItem extends BaseEntity {
	public enum ReceivedQuality {
		ACCEPTABLE,
		UNACCEPTABLE
	}
	
	private Shipment shipment;
	
	private Specimen specimen;
	
	private ReceivedQuality receivedQuality;
	
	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}

	public ReceivedQuality getReceivedQuality() {
		return receivedQuality;
	}

	public void setReceivedQuality(ReceivedQuality receivedQuality) {
		this.receivedQuality = receivedQuality;
	}

	public void ship() {
		specimen.updatePosition(null);
	}
	
	public void receive(ShipmentItem other) {
		setReceivedQuality(other.getReceivedQuality());
		
		if (getReceivedQuality() == ReceivedQuality.ACCEPTABLE) {
			specimen.updatePosition(other.getSpecimen().getPosition());
		}
	}
}
