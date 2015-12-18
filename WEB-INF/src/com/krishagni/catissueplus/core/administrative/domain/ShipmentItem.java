package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenShipmentReceivedEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenShipmentShippedEvent;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

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
		specimen.updatePosition(null, shipment.getShippedDate());
		SpecimenShipmentShippedEvent.createForShipmentItem(this).saveRecordEntry();
	}
	
	public void receive(ShipmentItem other) {
		setReceivedQuality(other.getReceivedQuality());
		
		StorageContainerPosition position = other.getSpecimen().getPosition();
		if (getReceivedQuality() == ReceivedQuality.ACCEPTABLE && position != null) {
			StorageContainer container = position.getContainer();
			if (container.isPositionOccupied(position.getPosOne(), position.getPosTwo())) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
			}
			
			specimen.updatePosition(position, shipment.getReceivedDate());
		}
		
		SpecimenShipmentReceivedEvent.createForShipmentItem(this).saveRecordEntry();
	}
}
