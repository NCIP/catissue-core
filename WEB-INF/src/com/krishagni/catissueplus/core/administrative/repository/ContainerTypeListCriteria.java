package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ContainerTypeListCriteria extends AbstractListCriteria<ContainerTypeListCriteria> {

	private String canHold;

	@Override
	public ContainerTypeListCriteria self() {
		return this;
	}

	public String canHold() {
		return canHold;
	}

	public ContainerTypeListCriteria canHold(String canHold) {
		this.canHold = canHold;
		return self();
	}
	
}
