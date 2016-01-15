package com.krishagni.catissueplus.core.administrative.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequestItem;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;

public class SpecimenRequestItemDetail {
	private Long id;

	private SpecimenInfo specimen;

	private String status;

	private Long orderId;

//	private String orderName;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

//	public String getOrderName() {
//		return orderName;
//	}
//
//	public void setOrderName(String orderName) {
//		this.orderName = orderName;
//	}

	public static SpecimenRequestItemDetail from(SpecimenRequestItem item) {
		SpecimenRequestItemDetail result = new SpecimenRequestItemDetail();
		result.setId(item.getId());
		result.setSpecimen(SpecimenInfo.from(item.getSpecimen()));
		result.setStatus(item.getStatus().name());

		if (item.getDistribution() != null) {
			result.setOrderId(item.getDistribution().getId());
		} else if (item.getShipment() != null) {
			result.setOrderId(item.getShipment().getId());
		}

		return result;
	}

	public static List<SpecimenRequestItemDetail> from(Collection<SpecimenRequestItem> items) {
		return items.stream().map(SpecimenRequestItemDetail::from).collect(Collectors.toList());
	}
}
