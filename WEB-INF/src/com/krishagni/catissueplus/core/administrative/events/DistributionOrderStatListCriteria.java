package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DistributionOrderStatListCriteria extends AbstractListCriteria<DistributionOrderStatListCriteria> {
	private Long dpId;
	
	private List<String> groupByAttrs = new ArrayList<String>();
	
	private Set<Long> siteIds = new HashSet<Long>();
	
	@Override
	public DistributionOrderStatListCriteria self() {
		return this;
	}
	
	public Long dpId() {
		return dpId;
	}
	
	public DistributionOrderStatListCriteria dpId(Long dpId) {
		this.dpId = dpId;
		return self();
	}
	
	public List<String> groupByAttrs() {
		return groupByAttrs;
	}
	
	public DistributionOrderStatListCriteria groupByAttrs(List<String> groupByAttrs) {
		this.groupByAttrs = groupByAttrs;
		return self();
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public DistributionOrderStatListCriteria siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
		return self();
	}
}
