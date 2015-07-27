package com.krishagni.catissueplus.core.administrative.events;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;

public class DistributionOrderItemDetail {
	private Long id;
	
	private SpecimenInfo specimen;
	
	private BigDecimal quantity;
	
	private String status;

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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static DistributionOrderItemDetail from(DistributionOrderItem orderItem) {
		DistributionOrderItemDetail detail = new DistributionOrderItemDetail();
		detail.setId(orderItem.getId());
		detail.setQuantity(orderItem.getQuantity());
		detail.setSpecimen(SpecimenInfo.from(orderItem.getSpecimen()));
		detail.setStatus(orderItem.getStatus().name());

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
