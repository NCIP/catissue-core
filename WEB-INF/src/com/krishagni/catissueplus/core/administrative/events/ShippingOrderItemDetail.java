package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.ShippingOrderItem;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;

public class ShippingOrderItemDetail {
	private Long id;
	
	private SpecimenInfo specimen;
	
	private String quality;
	
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
	
	public String getQuality() {
		return quality;
	}
	
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	public static ShippingOrderItemDetail from(ShippingOrderItem orderItem) {
		ShippingOrderItemDetail itemDetail = new ShippingOrderItemDetail();
		
		itemDetail.setId(orderItem.getId());
		itemDetail.setSpecimen(SpecimenInfo.from(orderItem.getSpecimen()));
		if (orderItem.getQuality() != null) {
			itemDetail.setQuality(orderItem.getQuality().toString());
		}
		
		return itemDetail;
	}
	
	public static Set<ShippingOrderItemDetail> from(Collection<ShippingOrderItem> orderItems) {
		Set<ShippingOrderItemDetail> itemDetails = new HashSet<ShippingOrderItemDetail>();
		
		for(ShippingOrderItem item : orderItems) {
			itemDetails.add(ShippingOrderItemDetail.from(item));
		}
		
		return itemDetails;
	}
}
