package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;

public class DistributionOrderItemDetail {
	private Long id;
	
	private SpecimenInfo specimen;
	
	private Double quantity;

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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public static DistributionOrderItemDetail from(DistributionOrderItem orderItem) {
		DistributionOrderItemDetail detail = new DistributionOrderItemDetail();
		detail.setId(orderItem.getId());
		detail.setQuantity(orderItem.getQuantity());
		detail.setSpecimen(SpecimenInfo.from(orderItem.getSpecimen()));

		return detail;
	}
	
	public static List<DistributionOrderItemDetail> from(Set<DistributionOrderItem> orderItems) {
		List<DistributionOrderItemDetail> items = new ArrayList<DistributionOrderItemDetail>();
		for (DistributionOrderItem item : orderItems) {
			items.add(from(item));
		}
		
		return items;
	}
}
