package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ShipmentItem;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;


public class ShipmentItemDetail implements Comparable<ShipmentItemDetail> {
	private Long id;
	
	private SpecimenInfo specimen;
	
	private String receivedQuality;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SpecimenInfo getSpecimen() {
		return specimen;
	}

	public void setSpecimen(SpecimenInfo specimen) {
		this.specimen = specimen;
	}

	public String getReceivedQuality() {
		return receivedQuality;
	}

	public void setReceivedQuality(String receivedQuality) {
		this.receivedQuality = receivedQuality;
	}

	public static ShipmentItemDetail from(ShipmentItem shipmentItem) {
		ShipmentItemDetail itemDetail = new ShipmentItemDetail();
		itemDetail.setId(shipmentItem.getId());
		itemDetail.setSpecimen(SpecimenInfo.from(shipmentItem.getSpecimen()));
		
		if (shipmentItem.getReceivedQuality() != null) {
			itemDetail.setReceivedQuality(shipmentItem.getReceivedQuality().toString());
		}
		
		return itemDetail;
	}
	
	public static List<ShipmentItemDetail> from(Collection<ShipmentItem> shipmentsItems) {
		List<ShipmentItemDetail> result = new ArrayList<ShipmentItemDetail>();
		for(ShipmentItem item : shipmentsItems) {
			result.add(ShipmentItemDetail.from(item));
		}
		
		Collections.sort(result);
		return result;
	}

	@Override
	public int compareTo(ShipmentItemDetail other) {
		return getId().compareTo(other.getId());
	}
}
