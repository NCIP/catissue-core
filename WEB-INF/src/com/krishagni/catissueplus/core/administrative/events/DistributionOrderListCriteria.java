package com.krishagni.catissueplus.core.administrative.events;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;


public class DistributionOrderListCriteria extends AbstractListCriteria<DistributionOrderListCriteria> {

	private Set<Long> instituteIds;
		
	@Override
	public DistributionOrderListCriteria self() {
		return this;
	}
	
	public Set<Long> instituteIds() {
		return instituteIds;
	}
	
	public DistributionOrderListCriteria instituteIds(Set<Long> instituteIds) {
		this.instituteIds = instituteIds;
		return self();
	}
}
