package com.krishagni.catissueplus.core.administrative.events;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DistributionOrderSpecReqListCriteria extends AbstractListCriteria<DistributionOrderSpecReqListCriteria> {
	private Long dpId;
	
	private boolean specimenType;
	
	private boolean anatomicSite;
	
	private boolean pathologyStatus;
	
	private Set<Long> siteIds;
	
	@Override
	public DistributionOrderSpecReqListCriteria self() {
		return this;
	}
	
	public Long dpId() {
		return dpId;
	}
	
	public DistributionOrderSpecReqListCriteria dpId(Long dpId) {
		this.dpId = dpId;
		return self();
	}
	
	public boolean specimenType() {
		return specimenType;
	}
	
	public DistributionOrderSpecReqListCriteria specimenType(boolean specimenType) {
		this.specimenType = specimenType;
		return self();
	}
	
	public boolean anatomicSite() {
		return anatomicSite;
	}
	
	public DistributionOrderSpecReqListCriteria anatomicSite(boolean anatomicSite) {
		this.anatomicSite = anatomicSite;
		return self();
	}
	
	public boolean pathologyStatus() {
		return pathologyStatus;
	}
	
	public DistributionOrderSpecReqListCriteria pathologyStatus(boolean pathologyStatus) {
		this.pathologyStatus = pathologyStatus;
		return self();
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public DistributionOrderSpecReqListCriteria siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
		return self();
	}
}
