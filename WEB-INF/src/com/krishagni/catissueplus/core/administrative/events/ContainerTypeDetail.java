
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;

public class ContainerTypeDetail extends ContainerTypeSummary {
	private ContainerTypeSummary canHold;

	public ContainerTypeSummary getCanHold() {
		return canHold;
	}

	public void setCanHold(ContainerTypeSummary canHold) {
		this.canHold = canHold;
	}

	public static ContainerTypeDetail from(ContainerType containerType) {
		if (containerType == null) {
			return null;
		}
		
		ContainerTypeDetail detail = new ContainerTypeDetail();
		ContainerTypeDetail.copy(containerType, detail);
		detail.setCanHold(ContainerTypeSummary.from(containerType.getCanHold()));
		return detail;
	}
}
