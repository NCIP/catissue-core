package com.krishagni.catissueplus.core.administrative.events;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DistributionOrderSpecificationListCriteria extends AbstractListCriteria<DistributionOrderSpecificationListCriteria> {
	private Long dpId;
	
	private boolean specimenType;
	
	private boolean anatomicSite;
	
	private boolean pathologyStatus;
	
	private Set<Long> siteIds;
	
	@Override
	public DistributionOrderSpecificationListCriteria self() {
		return this;
	}
	
	public Long dpId() {
		return dpId;
	}
	
	public DistributionOrderSpecificationListCriteria dpId(Long dpId) {
		this.dpId = dpId;
		return self();
	}
	
	public boolean specimenType() {
		return specimenType;
	}
	
	public DistributionOrderSpecificationListCriteria specimenType(boolean specimenType) {
		this.specimenType = specimenType;
		return self();
	}
	
	public boolean anatomicSite() {
		return anatomicSite;
	}
	
	public DistributionOrderSpecificationListCriteria anatomicSite(boolean anatomicSite) {
		this.anatomicSite = anatomicSite;
		return self();
	}
	
	public boolean pathologyStatus() {
		return pathologyStatus;
	}
	
	public DistributionOrderSpecificationListCriteria pathologyStatus(boolean pathologyStatus) {
		this.pathologyStatus = pathologyStatus;
		return self();
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public DistributionOrderSpecificationListCriteria siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
		return self();
	}
}
